package com.fundoo.user.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import org.springframework.web.multipart.MultipartFile;

import com.fundoo.elasticSearch.ElasticSearchDao;
import com.fundoo.response.Response;
import com.fundoo.user.dto.CollaboratorDto;
import com.fundoo.user.dto.ColorDto;
import com.fundoo.user.dto.NoteDto;
import com.fundoo.user.model.CollaboratorsEntity;
import com.fundoo.user.model.LabelEntity;
import com.fundoo.user.model.NoteEntity;
import com.fundoo.user.repository.NoteRepo;
import com.fundoo.user.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class NoteController 
{
	@Autowired
	NoteRepo noteRepo;
	
	@Autowired
	UserService service;
	
	@Autowired
	ElasticSearchDao elasticSearchDao;
	
	public NoteController(ElasticSearchDao elasticSearchDao) 
	{
		this.elasticSearchDao = elasticSearchDao;
	}
	
	/*
	 * Crud Operations 
	 * on Note 
	 */	
	@PostMapping("/newNote") 
	public ResponseEntity<Response> newNote(@RequestHeader String token, @RequestBody NoteDto noteDto) 
	{  
		System.out.println(noteDto.getNoteTitle());
		Response response = service.createNote(token, noteDto); 
		return new ResponseEntity<>(response, HttpStatus.OK); 
	}
	 
	
	/*
	 * @GetMapping("/displayAllNotes") public List<NoteEntity>
	 * displayAllNotes(@RequestHeader String token) { return
	 * service.displayNotes(token); }
	 */
	
	@DeleteMapping("/deleteNote")
	public ResponseEntity<Response> deleteNoteByNoteId(@RequestHeader String token, @RequestParam Long noteId)
	{
		Response response = service.deleteNote(token, noteId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/updateNote")
	public ResponseEntity<Response> updateNotebyNoteId(@RequestHeader String token, @RequestBody NoteDto  noteDto, 
			@RequestParam Long noteId) 
	{
		Response response = service.updateNote(token,noteDto,noteId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/find")
	public List<NoteEntity> getNoteByTitle(@RequestBody String searchField,@RequestHeader String token) throws IOException 
	{
		return elasticSearchDao.searchNote(token, searchField);
	}
	
	//PinNote
	
	@PutMapping("/pinOrUnpinNote")
	public ResponseEntity<Response> pinOrUnpinNote(@RequestHeader String token, @RequestParam Long noteId)
	{
		Response response = service.pinOrUnpinNoteByNoteId(token,noteId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/showPinnedNotes")
	public List<NoteEntity> showPinnedNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes = service.displayPinnedNotes(token);
		return notes;
	}
	
	@GetMapping("/showUnpinnedNotes")
	public List<NoteEntity> showUnpinnedNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes = service.displayUnpinnedNotes(token);
		return notes;
	}
	
	//ArchiveNote
	
	@PutMapping("/archiveNote")
	public ResponseEntity<Response> archiveNote(@RequestHeader String token, @RequestParam Long noteId)
	{
		Response response = service.archiveNoteByNoteId(token,noteId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/showArchivedNotes")
	public List<NoteEntity> showArchivedNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes = service.displayArchivedNotes(token);
		return notes;
	}
	
	@GetMapping("/showUnarchivedNotes")
	public List<NoteEntity> showUnarchivedNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes = service.displayUnarchivedNotes(token);
		return notes;
	}
	
	//TrashNote
	
	@PutMapping("/trashNote")
	public ResponseEntity<Response> trashNote(@RequestHeader String token, @RequestParam Long noteId)
	{
		Response response = service.trashNoteByNoteId(token,noteId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/checkTrashTime")
	public ResponseEntity<Response> checkTrashTime(@RequestHeader String token, @RequestParam Long noteId)
	{
		Response response  = service.checkTrashTimeOfNote(token, noteId);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/showTrashedNotes")
	public List<NoteEntity> showTrashedNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes = service.displayTrashedNotes(token);
		return notes;
	}
	
	@GetMapping("/showUntrashedNotes")
	public List<NoteEntity> showUntrashedNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes = service.displayUntrashedNotes(token);
		return notes;
	}
	
	//ColorNote
	
	@PutMapping("/colorNote")
	public ResponseEntity<Response> colorNote(@RequestHeader String token,@RequestParam String color, @RequestParam Long noteId)
	{
		Response response = service.colorNoteByNoteId(token,color,noteId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
//	
	
	@GetMapping("/showLabelsAttachedToNote")
	public List<LabelEntity> showLabelsAttachedToNote(@RequestHeader String token, @RequestParam Long noteId) 
	{
		List<LabelEntity> labels = service.showLabelsAttachedToNoteId(token,noteId);
		return labels;
	}
	
	
	//Upload-display-delete Image in note
	
	@PostMapping("/uploadNoteImage")
    public ResponseEntity<Response> uploadNoteImage(@RequestHeader String token,@RequestParam Long noteId ,@RequestParam MultipartFile file) 
	{
		Response response  = service.storeNoteImage(token,noteId,file);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/showNoteImage")
	public Resource showNoteImage(@RequestHeader String token, @RequestParam Long noteId) throws MalformedURLException 
	{
		Resource resource  = service.displayNoteImage(token, noteId);
        return resource;
	}
	
	@DeleteMapping("/deleteNoteImage")
	public ResponseEntity<Response> deleteNoteImage(@RequestHeader String token, @RequestParam Long noteId) 
	{
		Response response  = service.deleteNoteImage(token, noteId);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	//Collaborator Add-Display-Remove
	
	@PostMapping("/addCollaborator")
	public ResponseEntity<Response> addCollaborator(@RequestHeader String token,@RequestParam Long noteId,
			@RequestBody CollaboratorDto collaboratorDto)
	{
		Response response  = service.addCollaboratorByNoteId(token, noteId, collaboratorDto);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PutMapping("/removeCollaborator")
	public ResponseEntity<Response> removeCollaborator(@RequestHeader String token,@RequestParam Long noteId,
			@RequestParam String collabName)
	{
		Response response  = service.removeCollaboratorByNoteId(token, noteId, collabName);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/showCollaboratorsByNoteId")
	public List<CollaboratorsEntity> showCollaboratorsByNoteId(@RequestHeader String token, @RequestParam Long noteId)
	{
		List<CollaboratorsEntity> collabs = service.showCollaboratorsAttachedToNoteId(token,noteId);
		return collabs;
	}
	
	/*
	 * @GetMapping("/showAllCollaborators") public List<CollaboratorsEntity>
	 * showAllCollaborators(@RequestHeader String token) { List<CollaboratorsEntity>
	 * collabs = service.showCollaborators(token); return collabs; }
	 */
	
	//Reminder Add-Update-Remove-Display
	
	@PostMapping("/setReminder")
	public ResponseEntity<Response> setReminder(@RequestHeader String token, @RequestBody LocalDateTime dateTime, @RequestParam Long noteId)
	{
		Response response  = service.setReminderToNote(token, dateTime, noteId);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/checkReminder")
	public ResponseEntity<Response> checkReminder(@RequestHeader String token, @RequestParam Long noteId)
	{
		Response response  = service.checkReminderOfNote(token, noteId);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/showReminder")
	public LocalDateTime showReminder(@RequestHeader String token, @RequestParam Long noteId)
	{
		LocalDateTime dateTime  = service.showReminderOnNote(token, noteId);
        return dateTime;
	}
	
	@DeleteMapping("/deleteReminder")
	public ResponseEntity<Response> deleteReminder(@RequestHeader String token, @RequestParam Long noteId)
	{
		Response response  = service.deleteReminderFromNote(token, noteId);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
}
