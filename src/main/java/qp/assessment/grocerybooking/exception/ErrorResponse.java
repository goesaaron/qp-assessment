package qp.assessment.grocerybooking.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse {
	
	private Date date;
	private String message;
	private String details;

	public ErrorResponse(Date date, String message, String details) {
		this.date=date;
		this.message=message;
		this.details=details;
	}
}
