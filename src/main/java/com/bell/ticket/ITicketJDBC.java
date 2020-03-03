package com.bell.ticket;

import java.util.List;

public interface ITicketJDBC {
	public List<TicketVO> findAll();
	public int save(TicketVO patient);
	public List<TicketVO> getTicketsByAssignee(String email);

}
