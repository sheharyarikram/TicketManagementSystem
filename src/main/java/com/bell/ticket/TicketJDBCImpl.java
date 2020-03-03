package com.bell.ticket;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bell.controller.ticket.StatusEnum;;


/*
 * JDBC functionality for Ticket
 */
@Component
public class TicketJDBCImpl implements ITicketJDBC {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private Logger logger = LoggerFactory.getLogger(TicketJDBCImpl.class);


	//Get list of all Tickets
    public List<TicketVO> findAll() {
        return jdbcTemplate.query("select * from ticket order by id asc", 
        	(rs, rowNum) -> new TicketVO(
                rs.getInt("id"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getString("assignee"),
                rs.getDate("created_date"),
                rs.getDate("closed_date")
                ));
    }
    
    //Save Ticket object
    public int save(TicketVO ticket) {
        return jdbcTemplate.update("insert into ticket (id, assignee, description, status, created_date, closed_date) values(?, ?, ?, ?, ?, ?)",
        		ticket.getId(),
        		ticket.getAssignee(),
        		ticket.getDescription(),
        		ticket.getStatus(),
        		ticket.getCreatedDate(), 
        		ticket.getClosedDate());
    }
    
    //Update ticket object
    public int update(TicketVO ticket) {
        return jdbcTemplate.update("update ticket set status = ?, assignee = ?, description = ?, created_date = ?, closed_date = ? where id = ?",
        		ticket.getStatus(), 
        		ticket.getAssignee(),
        		ticket.getDescription(),
        		ticket.getCreatedDate(), 
        		ticket.getClosedDate(), 
        		ticket.getId()); //lookup key
    }
    	
    public List<TicketVO> getTicketsByAssignee(String assingee) { 
    	return jdbcTemplate.query("select * from ticket where assginee = ?", 
    			new String[] {assingee}, 
    			(rs, rowNum) -> new TicketVO(
    				rs.getInt("id"),
    				rs.getString("description"),
    				rs.getString("status"),
    				rs.getString("assignee"),
    				rs.getDate("created_date"),
    				rs.getDate("closed_date")
    				));  
    }
    
    //Get ticket that matches the given unique Id
    public TicketVO getTicketById(int id) {     	
    	try {
        return jdbcTemplate.queryForObject("select * from ticket where id = ?", 
        		new Integer[] {id}, 
        		(rs, rowNum) -> new TicketVO(
    					rs.getInt("id"),
    	                rs.getString("description"),
    	                rs.getString("status"),
    					rs.getString("assignee"),
    					rs.getDate("created_date"),
    					rs.getDate("closed_date")));
    	} catch (EmptyResultDataAccessException ex) {
        	logger.info("Ticket with id {} does not exist!", id);
    		return null;
    	}
    }
    
    //delete ticket
    public int deleteById(int ticketId) {
       return jdbcTemplate.update("delete from ticket where id = ?", ticketId);
    }
    
    public Integer getMaxId() {
        return jdbcTemplate.queryForObject("select max(id) from ticket", Integer.class);
    }
    
}
