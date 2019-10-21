package com.fundoo.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundoo.user.model.LabelEntity;

@Repository
public interface LabelRepo extends JpaRepository<LabelEntity, Long>
{
	public LabelEntity findByLabelIdAndUserEntityId(Long labelId, Long userId);
	public Optional<LabelEntity> findByLabelTitle(String labelTitle);
	public Optional<LabelEntity> findByLabelId(Long labelId);
	public LabelEntity findByLabelIdAndNoteId(Long labelId, Long noteId);
	
}
