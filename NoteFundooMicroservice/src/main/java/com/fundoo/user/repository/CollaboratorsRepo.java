package com.fundoo.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundoo.user.model.CollaboratorsEntity;

@Repository
public interface CollaboratorsRepo extends JpaRepository<CollaboratorsEntity, Long> 
{
	Optional<CollaboratorsEntity> findByCollaboratorsNameAndNoteId(String collabsName, Long noteId);

	Optional<CollaboratorsEntity> findByCollaboratorsName(String collaboratorsName);
	
	CollaboratorsEntity findByCollaboratorsIdAndUserEntityId(Long collabId, Long userId);
	
	Optional<CollaboratorsEntity> findByCollaboratorsNameAndCollaboratorsId(String collaboratorsName, Long collabId);

	Optional<CollaboratorsEntity> findByNoteIdAndCollaboratorsName(Long noteId, String collabName);

	CollaboratorsEntity findByCollaboratorsIdAndNoteId(Long collabId, Long noteId);

}
