package com.fundoo.user.service;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import com.fundoo.response.Response;
import com.fundoo.user.dto.CollaboratorDto;
import com.fundoo.user.dto.LabelDto;
import com.fundoo.user.dto.NoteDto;
import com.fundoo.user.model.CollaboratorsEntity;
import com.fundoo.user.model.LabelEntity;
import com.fundoo.user.model.NoteEntity;

@Service
public interface UserService {

	/*
	 * LoginRegistration
	 * 
	 * 
	 * Response UserRegistration(Registerdto userDto) throws UserException,
	 * UnsupportedEncodingException;
	 * 
	 * ResponseOfToken UserLogin(Logindto loginDto) throws UserException,
	 * UnsupportedEncodingException;
	 * 
	 * Response EmailValidation(String token) throws UserException;
	 * 
	 * Response forgotPassword(Emaildto emailDto) throws UserException,
	 * UnsupportedEncodingException;
	 * 
	 * Response resetPassword(String emailid, String password) throws UserException;
	 * 
	 * ResponseOfToken authentication(Optional<UserEntity> user, String password)
	 * throws UnsupportedEncodingException, UserException;
	 * 
	 * Response storeFile(String token,MultipartFile file);
	 * 
	 * Response removeProfilePic(String token);
	 * 
	 * List<String> getUserProfile(String token);
	 * 
	 * Response clearRedisData();
	 */

	/*
	 * Label
	 */

	Response createLabel(@RequestHeader String token, @Valid LabelDto labelDto);

	//List<LabelEntity> displayAllLabels(String token);

	Response deleteLabel(String token, Long labelId);

	Response updateLabel(String token, LabelDto labelDto, Long labelId);

	Response newLabelOnNote(String token, LabelDto labelDto, Long noteId);

	Response attachLabelOnNote(String token, Long noteId, Long labelId);

	Response removeLabelOnNote(String token, Long noteId, Long labelId);

	List<NoteEntity> showNotesOnLabelId(String token, Long labelId);

	/*
	 * Note
	 */

	Response createNote(String token, NoteDto noteDto);

	//List<NoteEntity> displayNotes(String token);

	Response deleteNote(String token, Long noteId);

	Response updateNote(String token, @Valid NoteDto noteDto, Long noteId);

	List<LabelEntity> showLabelsAttachedToNoteId(String token, Long noteId);

	// PinNotes

	Response pinOrUnpinNoteByNoteId(String token, Long noteId);

	List<NoteEntity> displayPinnedNotes(String token);

	List<NoteEntity> displayUnpinnedNotes(String token);

	// ArchiveNotes

	Response archiveNoteByNoteId(String token, Long noteId);

	List<NoteEntity> displayArchivedNotes(String token);

	List<NoteEntity> displayUnarchivedNotes(String token);

	// TrashNotes

	Response trashNoteByNoteId(String token, Long noteId);

	List<NoteEntity> displayTrashedNotes(String token);

	List<NoteEntity> displayUntrashedNotes(String token);

	// ColorNotes

	Response colorNoteByNoteId(String token, String color, Long noteId);

	// Upload--Display-Delete Image

	Response storeNoteImage(String token, Long noteId, MultipartFile file);

	Resource displayNoteImage(String token, Long noteId) throws MalformedURLException;

	Response deleteNoteImage(String token, Long noteId);

	// Collaborator Add-Display-Remove

	Response addCollaboratorByNoteId(String token, Long noteId, CollaboratorDto collaboratorDto);

	Response removeCollaboratorByNoteId(String token, Long noteId, String collabName);

	List<CollaboratorsEntity> showCollaboratorsAttachedToNoteId(String token, Long noteId);

	//List<CollaboratorsEntity> showCollaborators(String token);

	// Reminder Add-Update-Remove-Display

	Response setReminderToNote(String token, LocalDateTime dateTime, Long noteId);

	LocalDateTime showReminderOnNote(String token, Long noteId);

	Response checkReminderOfNote(String token, Long noteId);

	Response deleteReminderFromNote(String token, Long noteId);

	Response checkTrashTimeOfNote(String token, Long noteId);

}
