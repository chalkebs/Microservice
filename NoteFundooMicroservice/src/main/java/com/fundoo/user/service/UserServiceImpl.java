package com.fundoo.user.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaFileManager.Location;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import com.fundoo.elasticSearch.ElasticSearchDao;
import com.fundoo.exception.NoteNotFoundException;
import com.fundoo.exception.UserException;
import com.fundoo.rabbitMQ.MessageProducer;
import com.fundoo.redis.RedisUtil;
import com.fundoo.response.Response;
import com.fundoo.response.ResponseOfToken;
import com.fundoo.user.dto.CollaboratorDto;
import com.fundoo.user.dto.Emaildto;
import com.fundoo.user.dto.LabelDto;
import com.fundoo.user.dto.NoteDto;
import com.fundoo.user.model.CollaboratorsEntity;
import com.fundoo.user.model.LabelEntity;
import com.fundoo.user.model.NoteEntity;
import com.fundoo.user.repository.CollaboratorsRepo;
import com.fundoo.user.repository.LabelRepo;
import com.fundoo.user.repository.NoteRepo;
import com.fundoo.utility.ResponseHelper;
import com.fundoo.utility.TokenGenerator;
import com.fundoo.utility.Utility;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Component
@SuppressWarnings("unused")
@Service
public  class UserServiceImpl implements UserService
{	
	/*
	 * @Autowired private UserRepository userRepo;
	 */
	
	@Autowired
	private NoteRepo noteRepo;
	
	@Autowired
	private LabelRepo labelRepo;
	
	@Autowired 
	private CollaboratorsRepo collabsRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenGenerator tokenUtil;

	@Autowired
	private Response statusResponse;
	
	@Autowired
	Utility utility;
	
	@Autowired
	private Environment environment;
	
	@Value("${profile.key}")
	private Path fileLocation;
	
	@Value("${noteImage.key}")
	private Path noteImageLocation;
	
	@Autowired
	ElasticSearchDao elasticSearchDao;
	
	@Autowired
	RedisUtil<List<String>> redisUtil;
	
	@Autowired
    private MessageProducer messageProducer;
	
	public UserServiceImpl(ElasticSearchDao elasticSearchDao) 
	{
		this.elasticSearchDao = elasticSearchDao;
	}
		
	/*
	 * // // //User // //
	 * 
	 * @Override public Response UserRegistration(Registerdto userDto) { UserEntity
	 * user = modelMapper.map(userDto, UserEntity.class);
	 * 
	 * Optional<UserEntity> alreadyPresent =
	 * userRepo.findByEmailId(user.getEmailId());
	 * 
	 * if (alreadyPresent.isPresent()) { statusResponse =
	 * ResponseHelper.statusResponse(401, "User already registered"); return
	 * statusResponse; } else { String password =
	 * passwordEncoder.encode(userDto.getPassword());
	 * 
	 * user.setPassword(password); user = userRepo.save(user);
	 * 
	 * String token = tokenUtil.createToken(user.getUserId());
	 * 
	 * //RabbitMQ implementation messageProducer.sendMessage(user.getEmailId()
	 * ,"Registered Successfully!!! Please verify the Email...",
	 * "Click on below link for verification \n http://localhost:8080/users/"+token)
	 * ;
	 * 
	 * statusResponse = ResponseHelper.statusResponse(200,
	 * "Registered Successfully"); return statusResponse; }
	 * 
	 * }
	 * 
	 * public ResponseOfToken UserLogin(Logindto loginDto) { clearRedisData();
	 * 
	 * Optional<UserEntity> user = userRepo.findByEmailId(loginDto.getEmailId());
	 * 
	 * ResponseOfToken response = new ResponseOfToken();
	 * 
	 * if (user.isPresent()) { return authentication(user, loginDto.getPassword());
	 * } else { response.setStatusCode(402);
	 * response.setStatusMessage("User Not Found"); response.setToken(null);
	 * 
	 * return response; }
	 * 
	 * }
	 * 
	 * @Override public ResponseOfToken authentication(Optional<UserEntity> user,
	 * String password) { ResponseOfToken response = new ResponseOfToken();
	 * 
	 * if(!user.get().isVerify()) { response.setStatusCode(401);
	 * response.setStatusMessage("Verification Required"); response.setToken(null);
	 * 
	 * return response; } else if (user.get().isVerify()) { boolean status =
	 * passwordEncoder.matches(password, user.get().getPassword());
	 * 
	 * if (status == true) { String token =
	 * tokenUtil.createToken(user.get().getUserId());
	 * 
	 * response.setStatusCode(200);
	 * response.setStatusMessage("Successfully Logged In");
	 * response.setToken(token);
	 * 
	 * return response; } else { response.setStatusCode(403);
	 * response.setStatusMessage("Invalid password"); response.setToken(null);
	 * 
	 * return response; } } return response; }
	 * 
	 * @Override public Response EmailValidation(String token) { Long id =
	 * tokenUtil.decodeToken(token);
	 * 
	 * UserEntity user = userRepo.findById(id) .orElseThrow(() -> new
	 * UserException(404, "User Not found"));
	 * 
	 * user.setVerify(true); userRepo.save(user);
	 * 
	 * statusResponse = ResponseHelper.statusResponse(200, "Successfully verified");
	 * 
	 * return statusResponse; }
	 * 
	 * 
	 * @Override public Response forgotPassword(Emaildto emailDto) throws
	 * UserException, UnsupportedEncodingException { Optional<UserEntity>
	 * alreadyPresent=userRepo.findByEmailId(emailDto.getEmailId());
	 * 
	 * if(!alreadyPresent.isPresent()) { return ResponseHelper.statusResponse(401,
	 * "User Not Found"); } else { Long id=alreadyPresent.get().getUserId(); String
	 * token = tokenUtil.createToken(id);
	 * Utility.send(emailDto.getEmailId(),"Password Reset Link...",
	 * "http://localhost:4200/resetpassword/"+token); return
	 * ResponseHelper.statusResponse(200,
	 * "Password Reset Link is Sent to your Email Id..."); }
	 * 
	 * }
	 * 
	 * @Override public Response resetPassword(String token, String password) throws
	 * UserException { Long id = tokenUtil.decodeToken(token); Optional<UserEntity>
	 * alreadyPresent=userRepo.findById(id); if(!alreadyPresent.isPresent()) {
	 * return ResponseHelper.statusResponse(401, "User is not present..."); } else {
	 * UserEntity user=userRepo.findById(id).orElseThrow(()-> new
	 * UserException(404,"User Not found")); String
	 * encodedPasword=passwordEncoder.encode(password);
	 * user.setPassword(encodedPasword); userRepo.save(user); return
	 * ResponseHelper.statusResponse(200, "Password Successfully Reset"); } }
	 * 
	 * @Override public Response storeFile(String token, MultipartFile files) { Long
	 * id = tokenUtil.decodeToken(token); UserEntity user =
	 * userRepo.findById(id).orElseThrow(null);
	 * 
	 * // Normalize file name String fileName =
	 * StringUtils.cleanPath(files.getOriginalFilename());
	 * 
	 * try { // Check if the file's name contains invalid characters
	 * if(fileName.contains("..")) { return ResponseHelper.statusResponse(401,
	 * "Sorry! Filename contains invalid path sequence " + fileName); }
	 * 
	 * Path newTargetLocation = this.fileLocation.resolve(fileName);
	 * 
	 * Files.copy(files.getInputStream(), newTargetLocation);
	 * 
	 * //Cloudinary Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
	 * "cloud_name", "bhagyashri", "api_key", "659895311711326", "api_secret",
	 * "rJm_niAxSPCsjde-SvMgKrwvnd8"));
	 * 
	 * File toUpload = new File(newTargetLocation.toString());
	 * 
	 * @SuppressWarnings("rawtypes") Map uploadResult =
	 * cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());
	 * 
	 * user.setImageUrl(uploadResult.get("secure_url").toString()); //Cloudinary
	 * 
	 * userRepo.save(user);
	 * 
	 * //Redis List<String> profile = new ArrayList<String>();
	 * 
	 * profile.add(user.getEmailId()); profile.add(user.getFirstName());
	 * profile.add(user.getLastName());
	 * profile.add(uploadResult.get("secure_url").toString());
	 * 
	 * redisUtil.putValue("profile", profile); //Redis
	 * 
	 * return ResponseHelper.statusResponse(200,
	 * "Profile Pic Uploaded Successfully"); } catch (Exception e) {
	 * e.printStackTrace(); return ResponseHelper.statusResponse(402,
	 * "Could not store file " + fileName + ". Please try again!"); }
	 * 
	 * }
	 * 
	 * @Override public Response removeProfilePic(String token) { Long id =
	 * tokenUtil.decodeToken(token); UserEntity user =
	 * userRepo.findById(id).orElseThrow(null);
	 * 
	 * user.setImageUrl(null); userRepo.save(user);
	 * 
	 * //Redis List<String> profile = new ArrayList<String>();
	 * 
	 * profile.add(null); profile.add(null); profile.add(null);
	 * profile.add(user.getImageUrl());
	 * 
	 * redisUtil.putValue("profile", profile); //Redis
	 * 
	 * return ResponseHelper.statusResponse(200,
	 * "Profile Pic Removed Successfully");
	 * 
	 * }
	 * 
	 * 
	 * @Override public List<String> getUserProfile(String token) { Long id =
	 * tokenUtil.decodeToken(token); Optional<UserEntity> user =
	 * userRepo.findById(id);
	 * 
	 * if(redisUtil.getValue("profile")==null||user.get().getImageUrl()==null) {
	 * List<String> profile = new ArrayList<String>();
	 * 
	 * profile.add(user.get().getEmailId()); profile.add(user.get().getFirstName());
	 * profile.add(user.get().getLastName());
	 * 
	 * if(user.get().getImageUrl()==null) { profile.add(
	 * "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxg_j0tsamW7HWzaVx_6qWORf96YE8576mjCdn9HS4iCTGul6p-Q"
	 * ); } else { profile.add(user.get().getImageUrl()); }
	 * 
	 * redisUtil.putValue("profile", profile);
	 * 
	 * return profile; }
	 * 
	 * return redisUtil.getValue("profile"); }
	 * 
	 * @Override public Response clearRedisData() { redisUtil.putValue("profile",
	 * null); return ResponseHelper.statusResponse(200,
	 * "Successfully Cleared the data of Redis"); }
	 * 
	 */
//
//
// Label
//
//	
	@Override
	public Response createLabel(@RequestHeader String token,@Valid LabelDto labelDto) 
	{
		LabelEntity labelEntity = modelMapper.map(labelDto, LabelEntity.class);
		
		Optional<LabelEntity> labelTest = labelRepo.findByLabelTitle(labelEntity.getLabelTitle());
		
		if(!labelTest.isPresent())
		{
			Long id = tokenUtil.decodeToken(token);
			
			labelEntity.setUserEntityId(id);	
			labelEntity.setColor("#EBEBEB");
			
			/*
			 * Optional<UserEntity> user = userRepo.findById(id);
			 * ((List<LabelEntity>)user.get().getLabels()).add(labelEntity);
			 */
			
			labelRepo.save(labelEntity);
			/* userRepo.save(user.get()); */
						
			statusResponse = ResponseHelper.statusResponse(200, "Label Created Successfully");
			return statusResponse;
		}
		else
		{
			return ResponseHelper.statusResponse(401, "Label is already present");
		}
		
	}

	/*
	 * @Override public List<LabelEntity> displayAllLabels(String token) { Long id =
	 * tokenUtil.decodeToken(token); //UserEntity user =
	 * userRepo.findById(id).get();
	 * 
	 * return (List<LabelEntity>)user.getLabels(); }
	 */
	
	@Override
	public Response deleteLabel(String token, Long labelId) 
	{
		Long id = tokenUtil.decodeToken(token);
		//Optional<UserEntity> user = userRepo.findById(id);
		
		LabelEntity labelEntity = labelRepo.findByLabelIdAndUserEntityId(labelId, id);
		//((List<LabelEntity>)user.get().getLabels()).remove(labelEntity);
		
		//userRepo.save(user.get());
		labelRepo.delete(labelEntity);
		
		statusResponse = ResponseHelper.statusResponse(200, "Label Deleted Successfully");
		return statusResponse;	
	}

	@Override
	public Response updateLabel(String token, LabelDto labelDto, Long labelId) 
	{
		Long id = tokenUtil.decodeToken(token);
		
		Optional<LabelEntity> labelTest = labelRepo.findByLabelTitle(labelDto.getLabelTitle());
		
		if(labelTest.isPresent())
		{
			return ResponseHelper.statusResponse(401, "Label is already present");
		}
		else
		{
			LabelEntity labelEntity = labelRepo.findByLabelIdAndUserEntityId(labelId, id);
			
			labelEntity.setLabelTitle(labelDto.getLabelTitle());
			
			LabelEntity label1 = labelRepo.save(labelEntity);
			
			statusResponse = ResponseHelper.statusResponse(200, "Label Updated Successfully");
			return statusResponse;
		}
		
	}
	
	@Override
	public Response newLabelOnNote(String token, LabelDto labelDto, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);

		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		LabelEntity labelEntity = modelMapper.map(labelDto, LabelEntity.class);
		
		Optional<LabelEntity> labelTest = labelRepo.findByLabelTitle(labelEntity.getLabelTitle());
		if(labelTest.isPresent())
		{
			return ResponseHelper.statusResponse(401, "Label is already present");
		}
		else
		{
			
			labelEntity.setUserEntityId(id);
			labelEntity.setNoteId(noteId);
			labelEntity.setColor("#EBEBEB");
			
			//Optional<UserEntity> user = userRepo.findById(id);
			//((List<LabelEntity>)user.get().getLabels()).add(labelEntity);
			
			Optional<NoteEntity> note = noteRepo.findById(noteId);
			((List<LabelEntity>)note.get().getLabels()).add(labelEntity);
			
			labelRepo.save(labelEntity); 
			//userRepo.save(user.get());
			noteRepo.save(note.get());
			
			Optional<LabelEntity> label = labelRepo.findByLabelTitle(labelDto.getLabelTitle());
			
			Optional<NoteEntity> notes = noteRepo.findById(noteId);
			List<NoteEntity> note1 = new ArrayList<NoteEntity>();
			note1.add(notes.get());
			
			labelEntity.setNotes(note1);	
			
			labelRepo.save(label.get());	
						
			statusResponse = ResponseHelper.statusResponse(200, "Label Created Successfully on Note");
						
			return statusResponse;
		}
			
	}

	@Override
	public Response attachLabelOnNote(String token, Long noteId, Long labelId) 
	{
		Long id = tokenUtil.decodeToken(token);
		
		LabelEntity labelEntity = labelRepo.findByLabelIdAndUserEntityId(labelId, id);
		
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		labelEntity.setNoteId(noteId);
		
		Optional<NoteEntity> note = noteRepo.findById(noteId);
		((List<LabelEntity>)note.get().getLabels()).add(labelEntity);
		
		Optional<LabelEntity> label = labelRepo.findById(labelId);
		((List<NoteEntity>)label.get().getNotes()).add(noteEntity);
				
		noteRepo.save(note.get());
		labelRepo.save(label.get());
		labelRepo.save(labelEntity);
		
		statusResponse = ResponseHelper.statusResponse(200, "Label Attached to Note Successfully");  
		return statusResponse;        
	}

	@Override
	public Response removeLabelOnNote(String token, Long noteId, Long labelId) 
	{
		
		Long id = tokenUtil.decodeToken(token);
		
		LabelEntity labelEntity = labelRepo.findByLabelIdAndNoteId(labelId, noteId);
		
		Optional<NoteEntity> note = noteRepo.findById(noteId);		
		((List<LabelEntity>)note.get().getLabels()).remove(labelEntity);
		
		NoteEntity note1 = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		Optional<LabelEntity> label = labelRepo.findById(labelId);
		((List<NoteEntity>)label.get().getNotes()).remove(note1);
		
		labelEntity.setNoteId(null);
		noteRepo.save(note.get());
		labelRepo.save(label.get());
		
		statusResponse = ResponseHelper.statusResponse(200, "Label Removed from Note Successfully");  
		return statusResponse;
	}

	@Override
	public List<NoteEntity> showNotesOnLabelId(String token, Long labelId) 
	{		
		Long id = tokenUtil.decodeToken(token);
		
		LabelEntity labelEntity = labelRepo.findByLabelIdAndUserEntityId(labelId, id);
		
		return (List<NoteEntity>)labelEntity.getNotes();
	}
	
//
//
//	Note
//
//
	
	@Override
	public Response createNote(String token, NoteDto noteDto) 
	{
		
		NoteEntity noteEntity = modelMapper.map(noteDto, NoteEntity.class);
		
		Long id = tokenUtil.decodeToken(token);
		
		noteEntity.setUserEntityId(id);
		noteEntity.setPinNote(false);
		noteEntity.setArchiveNote(false);
		noteEntity.setTrashNote(false);
		noteEntity.setColor("white");
		
		//Optional<UserEntity> user = userRepo.findById(id);
		//((List<NoteEntity>)user.get().getNotes()).add(noteEntity);
		
		noteRepo.save(noteEntity);
		//userRepo.save(user.get());	
		
		elasticSearchDao.insertNote(noteEntity, token);
				
		statusResponse = ResponseHelper.statusResponse(200, "Note Saved Successfully");
		return statusResponse;
	}

	/*
	 * @Override public List<NoteEntity> displayNotes(String token) { Long id =
	 * tokenUtil.decodeToken(token); UserEntity user = userRepo.findById(id).get();
	 * 
	 * return (List<NoteEntity>)user.getNotes(); }
	 */

	@Override
	public Response deleteNote(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		//Optional<UserEntity> user = userRepo.findById(id);
		NoteEntity noteEntity = noteRepo.findById(noteId).orElseThrow(null);
				
		//((List<NoteEntity>)user.get().getNotes()).remove(noteEntity);
		
		//userRepo.save(user.get());
		noteRepo.delete(noteEntity);
		
		elasticSearchDao.deleteNote(noteId, token);
				
		statusResponse = ResponseHelper.statusResponse(200, "Note Deleted Successfully");
		return statusResponse;
	}

	@Override
	public Response updateNote(String token, NoteDto noteDto, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
				
		if(noteDto.getNoteTitle()==""&&noteDto.getNoteData()=="")
		{
			System.out.println("Prev values stored");
			noteEntity.setNoteTitle(noteEntity.getNoteTitle());
			noteEntity.setNoteData(noteEntity.getNoteData());
		}
		else if(noteDto.getNoteData()==""&&noteDto.getNoteTitle()!="")
			{
				System.out.println("Title updated");
				noteEntity.setNoteData(noteEntity.getNoteData());
				noteEntity.setNoteTitle(noteDto.getNoteTitle());
			}
		else if(noteDto.getNoteTitle()==""&&noteDto.getNoteData()!="")
			{
				System.out.println("Data updated");
				noteEntity.setNoteTitle(noteEntity.getNoteTitle());
				noteEntity.setNoteData(noteDto.getNoteData());
			}
		else
		{
			noteEntity.setNoteTitle(noteDto.getNoteTitle());
			noteEntity.setNoteData(noteDto.getNoteData());
		}
		
		noteRepo.save(noteEntity);
		
		elasticSearchDao.updateNote(noteId, noteEntity, token);
				
		statusResponse = ResponseHelper.statusResponse(200, "Note Updated Successfully");
		return statusResponse;
	}
	
	
	
	/*
	 * Pin Note
	 */
	@Override
	public Response pinOrUnpinNoteByNoteId(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		if(!noteEntity.isPinNote())
		{
			if(noteEntity.isArchiveNote())
			{
				noteEntity.setArchiveNote(false);
			}
			
			if(!noteEntity.isTrashNote())
			{
				noteEntity.setPinNote(true);
				statusResponse = ResponseHelper.statusResponse(200, "Note Pinned Successfully");
			}
			else
			{
				noteEntity.setPinNote(false);
				statusResponse = ResponseHelper.statusResponse(201, "Note Unpinned");
			}
			
			elasticSearchDao.insertNote(noteEntity, token);
			noteRepo.save(noteEntity);
			return statusResponse;
			
		}
		else if(noteEntity.isPinNote())
		{
			noteEntity.setPinNote(false);
			elasticSearchDao.insertNote(noteEntity, token);
			noteRepo.save(noteEntity);
			statusResponse = ResponseHelper.statusResponse(201, "Note Unpinned Successfully");
			return statusResponse;
		}
		
		return statusResponse;
		
	}
	
	@Override
	public List<NoteEntity> displayPinnedNotes(String token) 
	{
		List<NoteEntity> pinNotes = new ArrayList<NoteEntity>();
		
		Long id = tokenUtil.decodeToken(token);
		
		//UserEntity user = userRepo.findById(id).get();
		//List<NoteEntity> notes = (List<NoteEntity>)user.getNotes();
		
		/*
		 * for(NoteEntity n : notes) { if(n.isPinNote()) { pinNotes.add(n); } }
		 */
		
		return pinNotes;
	}
	
	@Override
	public List<NoteEntity> displayUnpinnedNotes(String token) 
	{
		List<NoteEntity> UnpinNotes = new ArrayList<NoteEntity>();
		
		Long id = tokenUtil.decodeToken(token);
		
		//UserEntity user = userRepo.findById(id).get();
		//List<NoteEntity> notes = (List<NoteEntity>)user.getNotes();
		
		/*
		 * for(NoteEntity n : notes) { if(!n.isPinNote()) { UnpinNotes.add(n); } }
		 */
		
		return UnpinNotes;
	}
	
	/*
	 * Archive Note
	 */
	@Override
	public Response archiveNoteByNoteId(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		if(!noteEntity.isArchiveNote())
		{			
			if(noteEntity.isPinNote())
			{
				noteEntity.setPinNote(false);
			}
			
			if(!noteEntity.isTrashNote())
			{
				noteEntity.setArchiveNote(true);
				statusResponse = ResponseHelper.statusResponse(200, "Note Archived Successfully");
			}
			else
			{
				noteEntity.setArchiveNote(false);
				statusResponse = ResponseHelper.statusResponse(200, "Note Unarchived");
			}
			
			elasticSearchDao.insertNote(noteEntity, token);
			noteRepo.save(noteEntity);
			return statusResponse;
			
		}
		else if(noteEntity.isArchiveNote())
		{
			noteEntity.setArchiveNote(false);
			elasticSearchDao.insertNote(noteEntity, token);
			noteRepo.save(noteEntity);
			statusResponse = ResponseHelper.statusResponse(201, "Note Unarchived Successfully");
			return statusResponse;
		}
		
		return statusResponse;
	}
	
	@Override
	public List<NoteEntity> displayArchivedNotes(String token) 
	{
		List<NoteEntity> archiveNotes = new ArrayList<NoteEntity>();
		
		Long id = tokenUtil.decodeToken(token);
		
		//UserEntity user = userRepo.findById(id).get();
		//List<NoteEntity> notes = (List<NoteEntity>)user.getNotes();
		
		/*
		 * for(NoteEntity n : notes) { if(n.isArchiveNote()) { archiveNotes.add(n); } }
		 */
		
		return archiveNotes;
	}

	@Override
	public List<NoteEntity> displayUnarchivedNotes(String token) 
	{
		List<NoteEntity> unarchiveNotes = new ArrayList<NoteEntity>();
		
		Long id = tokenUtil.decodeToken(token);
		
		//UserEntity user = userRepo.findById(id).get();
		//List<NoteEntity> notes = (List<NoteEntity>)user.getNotes();
		
		/*
		 * for(NoteEntity n : notes) { if(!n.isArchiveNote()) { unarchiveNotes.add(n); }
		 * }
		 */
		
		return unarchiveNotes;
	}

	/*
	 * Trash Note
	 */
	@Override
	public Response trashNoteByNoteId(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		if(!noteEntity.isTrashNote())
		{
			noteEntity.setTrashNote(true);
			noteEntity.setTrashedTime(LocalDateTime.now());
			
			if(noteEntity.isPinNote())
			{
				noteEntity.setPinNote(false);
			}
			if(noteEntity.isArchiveNote())
			{
				noteEntity.setArchiveNote(false);
			}
			
			elasticSearchDao.insertNote(noteEntity, token);
			noteRepo.save(noteEntity);
			statusResponse = ResponseHelper.statusResponse(200, "Note Trashed Successfully");
			return statusResponse;
			
		}
		else if(noteEntity.isTrashNote())
		{
			noteEntity.setTrashNote(false);
			elasticSearchDao.insertNote(noteEntity, token);
			noteRepo.save(noteEntity);
			statusResponse = ResponseHelper.statusResponse(201, "Note Untrashed Successfully");
			return statusResponse;
		}
		
		return statusResponse;
	}
	
	@Override
	public Response checkTrashTimeOfNote(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		//Optional<UserEntity> user = userRepo.findById(id);
		NoteEntity note = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		LocalDateTime trashedTime = note.getTrashedTime();
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		long days = trashedTime.until( currentDateTime, ChronoUnit.DAYS);
		
		if(days == 7)
		{
			note.setTrashNote(false);
			
			//((List<NoteEntity>)user.get().getNotes()).remove(note);
			
			noteRepo.delete(note);
			elasticSearchDao.deleteNote(noteId, token);
			return ResponseHelper.statusResponse(200, "Trashed Note Deleted Successfully");
		}
		
		return ResponseHelper.statusResponse(401, "Still time for Delete");
	}
	
	@Override
	public List<NoteEntity> displayTrashedNotes(String token) 
	{
		List<NoteEntity> trashedNotes = new ArrayList<NoteEntity>();
		
		Long id = tokenUtil.decodeToken(token);
		
		/*
		 * UserEntity user = userRepo.findById(id).get(); List<NoteEntity> notes =
		 * (List<NoteEntity>)user.getNotes();
		 * 
		 * for(NoteEntity n : notes) { if(n.isTrashNote()) { trashedNotes.add(n); } }
		 */
		
		return trashedNotes;
	}

	@Override
	public List<NoteEntity> displayUntrashedNotes(String token) 
	{
		List<NoteEntity> untrashedNotes = new ArrayList<NoteEntity>();
		
		Long id = tokenUtil.decodeToken(token);
		
		/*
		 * UserEntity user = userRepo.findById(id).get(); List<NoteEntity> notes =
		 * (List<NoteEntity>)user.getNotes();
		 * 
		 * for(NoteEntity n : notes) { if(!n.isTrashNote()) { untrashedNotes.add(n); } }
		 */
		
		return untrashedNotes;
	}
	
	/*
	 * Color Note
	 */
	@Override
	public Response colorNoteByNoteId(String token, String color, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		noteEntity.setColor(color);
		noteRepo.save(noteEntity);
		elasticSearchDao.insertNote(noteEntity, token);
		
		statusResponse = ResponseHelper.statusResponse(200, "Note color set Successfully");
		return statusResponse;
	}
	
	@Override
	public List<LabelEntity> showLabelsAttachedToNoteId(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		return (List<LabelEntity>)noteEntity.getLabels();
	}

	
	/*
	 * Image for Note
	 */
	@Override
	public Response storeNoteImage(String token, Long noteId, MultipartFile file) 
	{
		 Long id = tokenUtil.decodeToken(token);
		 NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		 
		 // Normalize file name
		 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		 
	     try 
	     {
	    	 if(fileName.contains("..")) 
	         {
	        	 return ResponseHelper.statusResponse(401, "Sorry! Filename contains invalid path sequence " + fileName); 
	         }
	         
	         int index1 = fileName.indexOf(".");
	         String target = fileName.replace(fileName.substring(0,index1),noteId.toString());
	         
	         Path newTargetLocation = this.noteImageLocation.resolve(target);
	         
	         noteEntity.setFileName(target);
	         
	         noteRepo.save(noteEntity);
	         elasticSearchDao.insertNote(noteEntity, token);
	         
	         Files.copy(file.getInputStream(), newTargetLocation, StandardCopyOption.REPLACE_EXISTING);

	         return ResponseHelper.statusResponse(200, "Note Image Uploaded Successfully");
	      } 
	      catch (Exception ex)
	      {
	          return ResponseHelper.statusResponse(402, "Could not store file " + fileName + ". Please try again!"); 
	      }
	}

	@Override
	public Resource displayNoteImage(String token, Long noteId) throws MalformedURLException 
	{
		Long id = tokenUtil.decodeToken(token);
		
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		Path file = this.noteImageLocation.resolve(noteEntity.getFileName()).normalize();
        Resource resource = new UrlResource(noteImageLocation.toUri());
		
		return resource;
	}

	@Override
	public Response deleteNoteImage(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		try 
		{
			Path file = this.noteImageLocation.resolve(noteEntity.getFileName()).normalize();
			Files.deleteIfExists(file);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		noteEntity.setFileName(null);
		
		elasticSearchDao.insertNote(noteEntity, token);
		noteRepo.save(noteEntity);
		
        return ResponseHelper.statusResponse(200, "File Deleted Successfully"); 
		
	}

	
	/*
	 * Collaborator for Note
	 */
	@Override
	public Response addCollaboratorByNoteId(String token, Long noteId, CollaboratorDto collaboratorDto) 
	{
		
		CollaboratorsEntity collabsEntity = modelMapper.map(collaboratorDto, CollaboratorsEntity.class);
		
		Long id = tokenUtil.decodeToken(token);
		//Optional<UserEntity> user1 = userRepo.findById(id);
		
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		/*
		 * Optional<UserEntity> userTest = userRepo.findById(id);
		 * if(userTest.get().getEmailId()==collaboratorDto.getCollaboratorsName()) {
		 * return ResponseHelper.statusResponse(401, "Email Id Already Exist"); }
		 */
		
		Optional<CollaboratorsEntity> collab = collabsRepo.findByCollaboratorsNameAndNoteId
				(collaboratorDto.getCollaboratorsName(), noteId);
		if(collab.isPresent())
		{
			return ResponseHelper.statusResponse(402, "Email Id Already Exist");
		}
		
		/*
		 * Optional<UserEntity> user =
		 * userRepo.findByEmailId(collabsEntity.getCollaboratorsName());
		 * if(user.isPresent()) {
		 */
			collabsEntity.setNoteId(noteId);
			collabsEntity.setUserEntityId(id);
			
			//((List<CollaboratorsEntity>)user1.get().getCollabs()).add(collabsEntity);
			
			Optional<NoteEntity> note = noteRepo.findById(noteId);
			((List<CollaboratorsEntity>)note.get().getCollabs()).add(collabsEntity);
			
			collabsRepo.save(collabsEntity);
			//userRepo.save(user1.get());
			noteRepo.save(note.get());
			
			Utility.send(collabsEntity.getCollaboratorsName(), "Collaboration Mail...", 
					"Title : "+note.get().getNoteTitle()+"\n Note : "+note.get().getNoteData());
			
			return ResponseHelper.statusResponse(200, "Collaborator Added Successfully"); 
		//}
		/*
		 * else { return ResponseHelper.statusResponse(404,
		 * "Invalid EmailId of Collaborator"); }
		 */
		
	}

	@Override
	public Response removeCollaboratorByNoteId(String token, Long noteId, String collabName) 
	{		
		Long id = tokenUtil.decodeToken(token);
		
		//UserEntity user = userRepo.findById(id).get();
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
			
		CollaboratorsEntity collabEntity = collabsRepo.findByNoteIdAndCollaboratorsName(noteId, collabName).get();
		
		((List<CollaboratorsEntity>)noteEntity.getCollabs()).remove(collabEntity);
		//((List<CollaboratorsEntity>)user.getCollabs()).remove(collabEntity);
		
		noteRepo.save(noteEntity);
		//userRepo.save(user);
		
		collabEntity.setNoteId(null);
		collabEntity.setUserEntityId(null);
		
		collabsRepo.delete(collabEntity);
		
		return ResponseHelper.statusResponse(200, "Collaborator Removed from Note Successfully"); 
	}
	
	@Override
	public List<CollaboratorsEntity> showCollaboratorsAttachedToNoteId(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity noteEntity = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		return (List<CollaboratorsEntity>)noteEntity.getCollabs();
	} 

	/*
	 * @Override public List<CollaboratorsEntity> showCollaborators(String token) {
	 * Long id = tokenUtil.decodeToken(token); UserEntity user =
	 * userRepo.findById(id).get(); return
	 * (List<CollaboratorsEntity>)user.getCollabs(); }
	 */
	
	@Override
	public Response setReminderToNote(String token, LocalDateTime dateTime, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity note = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		if (dateTime.compareTo(currentDateTime) < 0) 
		{
			note.setDateTime(dateTime);
			noteRepo.save(note);
			elasticSearchDao.insertNote(note, token);
			return ResponseHelper.statusResponse(200, "Reminder is set to note");
		}
		return statusResponse;
	}
	
	@Override
	public Response checkReminderOfNote(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity note = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		LocalDateTime reminder = note.getDateTime();
		LocalDateTime currentDateTime = LocalDateTime.now();
		
		if(reminder.compareTo(currentDateTime)>0)
		{
			note.setDateTime(null);
			noteRepo.save(note);
			elasticSearchDao.insertNote(note, token);
			return ResponseHelper.statusResponse(200, "Reminder...");
		}
		
		return ResponseHelper.statusResponse(401, "Still time for Reminder");
	}
	
	@Override
	public Response deleteReminderFromNote(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity note = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		note.setDateTime(null);
		noteRepo.save(note);
		elasticSearchDao.insertNote(note, token);
		return ResponseHelper.statusResponse(200, "Reminder is deleted");
	}

	@Override
	public LocalDateTime showReminderOnNote(String token, Long noteId) 
	{
		Long id = tokenUtil.decodeToken(token);
		NoteEntity note = noteRepo.findByNoteIdAndUserEntityId(noteId, id);
		
		return note.getDateTime();
	}

}

