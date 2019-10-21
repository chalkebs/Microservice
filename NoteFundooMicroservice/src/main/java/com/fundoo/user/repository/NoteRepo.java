package com.fundoo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundoo.user.model.NoteEntity;

@Repository
public interface NoteRepo extends JpaRepository<NoteEntity, Long>
{
	public NoteEntity findByNoteIdAndUserEntityId(Long noteId, Long userId);
	
	/* save(), 
	 * findOne(), 
	 * findAll(), 
	 * count(), 
	 * delete()
	 * all methods of PagingAndSortingRepository
	 * all methods of CrudRepository */
}
