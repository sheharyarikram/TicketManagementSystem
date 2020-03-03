package com.bell.controller.ticket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bell.ticket.TicketDAOImpl;
import com.bell.ticket.TicketVO;

/*
 * REST service class to dispatch calls to data layer
 */
@Component
public class TicketRestService {
	
	@Autowired
	private TicketDAOImpl ticketDAO;
	private Logger logger = LoggerFactory.getLogger(TicketRestService.class);
	
	public List<TicketVO> getAllTickets() {
		//TODO - error handling
		return ticketDAO.getAllTickets();
	}
	
	public List<TicketVO> getAllTicketsForAssignee(String assigneeName) {
		return ticketDAO.getAllTicketsByAssingee(assigneeName);
	}
	
	public ResponseEntity<?> getTicket(int ticketId, String requestId) {
		TicketVO ticket = ticketDAO.getTicketById(ticketId);
		if (ticket == null) {
			return generateErrorResponseEntity(requestId, HttpStatus.NOT_FOUND, ErrorCodesEnum.TICKET_NOT_FOUND, "Ticket with ID " + ticketId + " not found!");
		} else {
			logger.info("Ticket with id {} found", ticket.getId());
			return new ResponseEntity<TicketVO>(ticket, HttpStatus.OK);
		}
	}
	
	public ResponseEntity<?> saveTicket(TicketVO ticket, String requestId) {	
		//Check if all required fields are provided
		if (ticket == null || ticket.getId() == 0 ||
				ticket.getDescription() == null || ticket.getDescription().isEmpty() ||
				ticket.getAssignee() == null || ticket.getAssignee().isEmpty() ||
				ticket.getStatus() == null || ticket.getStatus().toString().isEmpty()) {
			return generateErrorResponseEntity(requestId, HttpStatus.BAD_REQUEST, ErrorCodesEnum.FIELD_MISSING, "A required field missing or empty");
		}
		
		//Check if assignee is letters only
		if (!(ticket.getAssignee().chars().allMatch(Character::isLetter))) {
			return generateErrorResponseEntity(requestId, HttpStatus.BAD_REQUEST, ErrorCodesEnum.BAD_FORMAT, "Ticket assignee is incorrect format: " + ticket.getAssignee());
		}
		
	    if (!ticket.getStatus().toString().equalsIgnoreCase("open") && 
	    		!ticket.getStatus().toString().equalsIgnoreCase("closed") && 
	    		!ticket.getStatus().toString().equalsIgnoreCase("inprogress")) {
			logger.info("Status field is not a valid value (i.e. Open/Closed/InProgress) - status = {}", ticket.getStatus());
			return generateErrorResponseEntity(requestId, HttpStatus.BAD_REQUEST, ErrorCodesEnum.BAD_FORMAT, "Status attribute is invalid: " + ticket.getStatus());
		}

		//Check if this is an existing ticket and if the state is already closed (can't modify in that case)
		if (ticketDAO.doesTicketExist(ticket.getId())) {
			TicketVO existingTicket = ticketDAO.getTicketById(ticket.getId());
			if (existingTicket.getStatus().toString().equalsIgnoreCase(StatusEnum.closed.toString())) {
				logger.info("Cannot modify a closed ticket - id = {}, closed date = {}", ticket.getId(), ticket.getClosedDate());
				return generateErrorResponseEntity(requestId, HttpStatus.NOT_ACCEPTABLE, ErrorCodesEnum.TICKET_CLOSED, "Cannot update as ticket is already closed!");
			}
		}
		
		try {
			//Perform various operations related to the status of the ticket
			performStatusRelatedModifications(ticket, requestId);
		} catch (ParseException e) {
			logger.info("Failed to set created or closed date attribute");
			return generateErrorResponseEntity(requestId, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodesEnum.SERVER_ERROR, "Failed to set created or closed date attribute");
		}
		
		logger.info("Attempting to save ticket with id = {}, status = {}, description = {}, created date = {}, closed date = {}, assignee = {}", 
				ticket.getId(),
				ticket.getStatus(),
				ticket.getDescription(),
				ticket.getCreatedDate(),
				ticket.getClosedDate(),
				ticket.getAssignee());

		//Save the ticket (handles both insert and update)
		if (ticketDAO.saveTicket(ticket)) {
			return new ResponseEntity<TicketVO>(ticket, HttpStatus.CREATED);
		} else {
			return generateErrorResponseEntity(requestId, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodesEnum.SERVER_ERROR, "Failed to save ticket - something went wrong!");
		}
	}
	
	public ResponseEntity<?> deleteTicket(int ticketId, String requestId) {
		logger.info("Attempting to delete ticket with id {}", ticketId);

		if (ticketDAO.deleteTicket(ticketId) < 1) {
			return generateErrorResponseEntity(requestId, HttpStatus.NOT_FOUND, ErrorCodesEnum.TICKET_NOT_FOUND, "Ticket does not exist");
		} else {
			return new ResponseEntity<>("Deleted ticket successfully", HttpStatus.OK);
		}
	}
	
	public String generateNewId() {
		return UUID.randomUUID().toString().toUpperCase().replace("-", "");
	}
	
	public ResponseEntity<?> generateErrorResponseEntity(String requestId, HttpStatus httpStatus, ErrorCodesEnum internalCode, String description) {
		return new ResponseEntity<HttpError>(
			new HttpError(
				requestId, 
				httpStatus.value(), 
				httpStatus.getReasonPhrase(), 
				description, 
				internalCode.code
			),
			httpStatus
		);
	}
	
	/*
	 * Method to perform operations based on status:
	 * 	If status is being changed to closed for an existing ticket, set closed date to today
	 *  If status is closed for a new ticket, set closed date to today
	 *  If new ticket is being created, set created date to today
	 */
	public void performStatusRelatedModifications(TicketVO ticket, String requestId) throws ParseException {
		//Check if ticket is already existing and perform state related functionality
		if (ticketDAO.doesTicketExist(ticket.getId())) {
			TicketVO existingTicket = ticketDAO.getTicketById(ticket.getId());
			//set the created date from the existing ticket
			ticket.setCreatedDate(existingTicket.getCreatedDate());
			
			//If status is being changed to closed set the closed date to today
			if (!existingTicket.getStatus().toString().equalsIgnoreCase(StatusEnum.closed.toString())) {
				if (ticket.getStatus().toString().contentEquals(StatusEnum.closed.toString())) {
					setClosedDateToToday(ticket);
				}
			}
		} else {
			//ticket is new - set created date to today
			setCreatedDateToToday(ticket);
			//is state 'closed', set closed date to today as well
			if (ticket.getStatus().toString().equalsIgnoreCase(StatusEnum.closed.toString())) {
				setClosedDateToToday(ticket);
			}
		}
	}
	
	public void setCreatedDateToToday(TicketVO ticket) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		ticket.setCreatedDate(format.parse(format.format(new Date())));
	}
	
	public void setClosedDateToToday(TicketVO ticket) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		ticket.setClosedDate(format.parse(format.format(new Date())));
	}

}
