package com.fundoo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException
{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String NoteName;

	public NoteNotFoundException(String NoteName) 
	{
		super(String.format("%s Not Found", NoteName));
	    this.NoteName = NoteName;
	}

	public String getNoteName() 
	{
	    return NoteName;
	}

}
