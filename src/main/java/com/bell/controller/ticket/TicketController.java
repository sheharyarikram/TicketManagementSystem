package com.bell.controller.ticket;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bell.ticket.TicketVO;


@RestController
public class TicketController {
		
	@Autowired
	private TicketRestService ticketRestService;
	
	private Logger logger = LoggerFactory.getLogger(TicketController.class);
	
	//URL: http://localhost:8080/
	@RequestMapping("/")
	public String healthCheck() {
		return "Hello!";
	}
	
	//URL: http://localhost:8080/tickets
    @RequestMapping(value = "/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private List<TicketVO> getAllTickets(HttpServletRequest request) {
    	String requestId = ticketRestService.generateNewId();
    	logger.info("server name = {}", request.getServerName());
    	logger.info("Received GET request to get list of all tickets - Request ID: {}", requestId);
        return ticketRestService.getAllTickets();
    }

    @RequestMapping(value = "/tickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> saveTicket(@RequestBody TicketVO ticket) {
    	String requestId = ticketRestService.generateNewId();
    	logger.info("Received POST request to save ticket with id: {} - Request ID: {}", ticket.getId(), requestId);
    	return ticketRestService.saveTicket(ticket, requestId);
    }
    
	//URL: http://localhost:8080/tickets/1
    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> getTicket(@PathVariable(required = true) int id) {
    	String requestId = ticketRestService.generateNewId();
    	logger.info("Received GET request to retrieve ticket with id: {} - Request ID: {}", id, requestId);
    	return ticketRestService.getTicket(id, requestId);
    }
    
	//URL: http://localhost:8080/tickets/assignee/{name}
//    @RequestMapping(value = "/tickets/assignee/{assingeeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    private List<TicketVO>  getTicket(@PathVariable(required = true) String assingeeName) {
//    	String requestId = ticketRestService.generateNewId();
//    	logger.info("Received GET request to retrieve all tickets for assignee: {} - Request ID: {}", assingeeName, requestId);
//    	return ticketRestService.getAllTicketsForAssignee(assingeeName);
//    }
    
    @RequestMapping(value = "/tickets/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> deleteTicket(@PathVariable("id") int id) {
    	String requestId = ticketRestService.generateNewId();
    	logger.info("Received DELETE request to delete ticket with id: {} - Request ID: {}", id + requestId);
    	return ticketRestService.deleteTicket(id, requestId);
    }
	
}
