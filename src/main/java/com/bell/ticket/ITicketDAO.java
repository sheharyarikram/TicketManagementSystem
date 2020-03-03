package com.bell.ticket;

import java.util.List;

public interface ITicketDAO {
	public List<TicketVO> getAllTickets();
	public boolean saveTicket(TicketVO patient);
	public TicketVO getTicketById(int id);
	public List<TicketVO> getAllTicketsByAssingee(String assingee);
}
