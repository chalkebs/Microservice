package com.fundoo.user.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundoo.response.Response;
import com.fundoo.user.dto.LabelDto;
import com.fundoo.user.model.LabelEntity;
import com.fundoo.user.model.NoteEntity;
import com.fundoo.user.repository.LabelRepo;
import com.fundoo.user.repository.NoteRepo;
import com.fundoo.user.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class LabelController 
{
	/*
	 * @Autowired UserRepository userRepo;
	 */
	
	@Autowired
	LabelRepo labelRepo;
	
	@Autowired
	NoteRepo noteRepo;
	
	@Autowired
	UserService service;
	
	/*
	 * 
	 * Crud Operations 
	 * on Label
	 * 
	 */
	
	@PostMapping("/newLabel") 
	public ResponseEntity<Response> newLabel(@RequestHeader String token, @Valid @RequestBody LabelDto labelDto ) 
	{
		Response response = service.createLabel(token,labelDto); 
		return new ResponseEntity<>(response, HttpStatus.OK); 
	}
	 
	/*
	 * @GetMapping("/displayAllLabels") public List<LabelEntity>
	 * displayLabels(@RequestHeader String token) { List<LabelEntity> labels =
	 * service.displayAllLabels(token); return labels; }
	 */
	
	@DeleteMapping("/deleteLabel")
	public ResponseEntity<Response> deleteLabelByLabelId(@RequestHeader String token, @RequestParam Long labelId)
	{
		Response response = service.deleteLabel(token, labelId); 
		return new ResponseEntity<>(response, HttpStatus.OK); 
	}
	
	@PutMapping("/updateLabel")
	public ResponseEntity<Response> updateLabelbyLabelId(@RequestHeader String token, @RequestBody LabelDto labelDto, 
			@RequestParam Long labelId) 
	{
		Response response = service.updateLabel(token,labelDto,labelId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/newLabelOnNote")
	public ResponseEntity<Response> newLabelOnNote(@RequestHeader String token, @RequestBody LabelDto labelDto,
			@RequestParam Long noteId)
	{
		Response response = service.newLabelOnNote(token,labelDto,noteId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/attachLabelToNote")
	public ResponseEntity<Response> attachLabelToNote(@RequestHeader String token, @RequestParam Long noteId, 
			@RequestParam Long labelId)
	{
		Response response = service.attachLabelOnNote(token, noteId, labelId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/removeLabelFromNote")
	public ResponseEntity<Response> removeLabelFromNote(@RequestHeader String token, @RequestParam Long noteId, 
			@RequestParam Long labelId)
	{
		Response response = service.removeLabelOnNote(token, noteId, labelId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/showNotesAttachedToLabel")
	public List<NoteEntity> showNotesAttachedToLabel(@RequestHeader String token, @RequestParam Long labelId )
	{
		List<NoteEntity> notes = service.showNotesOnLabelId(token, labelId);
		return notes;
	}
	
}
