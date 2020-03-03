package com.bell.ticket;

import java.util.Date;

import com.bell.controller.ticket.StatusEnum;

/*
 * Ticket Object
 */
public class TicketVO {
	public int id;				//unique ticket id
	public String description;  //description of ticket
	public String status;	//current status of ticket
	public String assignee; 	//name of assigned person
	public Date createdDate;	//ticket creation date
	public Date closedDate;		//ticket close date
	
	public TicketVO(int id, String description, String status, String assignee, Date createdDate, Date closedDate) {
		this.id = id;
		this.description = description;
		this.status = status;
		this.assignee = assignee;
		this.createdDate = createdDate;
		this.closedDate = closedDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}
	
}
