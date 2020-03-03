# TicketManagementSystem
Basic ticket/issue management system

This is a simple SpringBoot based Java application that performs various operations for a ticket management system.

A ticket consists of a unique identifier, assignee, created and closed dates and status. Status can only be one of open/closed/inprogress.

The app comes with an in-memory database with a few ticket records already created.

To Run this app:

	- Checkout from GIT
	
	- Import as Maven project in Eclipse
	
	- Right click on BellTicketApplication.java class and select 'Run as Java Application'
	
REST api's have been created to test the CRUD functionality:

	1. Retrieve list of all tickets:
		Send GET request: http://localhost:8080/tickets
		
	2. Retrieve ticket by Id:
		Send GET request: http://localhost:8080/tickets/{id}
		
	3. Update/modify an existing ticket as well as save new ticket:
		Send POST request: http://localhost:8080/tickets
		Body of request must contain ticket attributes in JSON format. Eg: 
		{"id":"1","assignee":"Sheharyar","description":"Fix some bugs","status":"closed"}
		
	4. Delete a ticket by Id:
		send DELETE request: http://localhost:80
