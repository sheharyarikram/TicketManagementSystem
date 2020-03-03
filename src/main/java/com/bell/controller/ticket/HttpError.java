package com.bell.controller.ticket;

public class HttpError {
	
	public String id;
	public int status; //httpStatusCode
	public String title; //httpStatusTitle
	public String detail;
	public int code; //codes from ErrorCodesEnum
	
	public HttpError(String id, int httpStatusCode, String httpStatusTitle, String detail, int internalCode) {
		this.id = id;
		this.status = httpStatusCode;
		this.title = httpStatusTitle;
		this.detail = detail;
		this.code = internalCode;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}

}
