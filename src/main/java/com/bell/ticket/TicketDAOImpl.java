package com.bell.ticket;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/*
 * Ticket Data Access Object (REST -> DAO -> JDBC -> H2)
 */
@Component
public class TicketDAOImpl implements ITicketDAO {
		
	@Autowired
	private TicketJDBCImpl ticketJDBC;
	private Logger logger = LoggerFactory.getLogger(TicketDAOImpl.class);

	@Override
	public List<TicketVO> getAllTickets() {
		List<TicketVO> allTickets = ticketJDBC.findAll();
		return allTickets;
	}
	
	@Override
	public List<TicketVO> getAllTicketsByAssingee(String assingee) {
		List<TicketVO> allTickets = ticketJDBC.getTicketsByAssignee(assingee);
		return allTickets;
	}
	
	public TicketVO getTicketById(int id) {
		return ticketJDBC.getTicketById(id);
	}
	
//	public boolean addTicket(TicketVO ticket) {
//		if (ticketJDBC.save(ticket) < 1) {
//			return false;
//		} else {
//			return true;
//		}
//	}
	
	public boolean doesTicketExist(int id) {
		if (ticketJDBC.getTicketById(id) == null) {
			return false;
		} else {
			return true;
		}
	}

	//Handles both insert and update cases
	@Override
	public boolean saveTicket(TicketVO ticket) {
		try { 
			//Check if ticket already exists
			if (doesTicketExist(ticket.getId())) {
				//update case
				ticketJDBC.update(ticket);
			} else {
				//insert case
				ticketJDBC.save(ticket);
			}
		} catch (DataAccessException e) {
        	logger.info("Could not save ticket as ran into exception: ", e);
			return false;
		}
		
		return true;
	}
	
	public int deleteTicket(int ticketId) {
		return ticketJDBC.deleteById(ticketId);
	}
	
	public int getNextAvailableId() {
		return (ticketJDBC.getMaxId().intValue() + 1);
	}

}
