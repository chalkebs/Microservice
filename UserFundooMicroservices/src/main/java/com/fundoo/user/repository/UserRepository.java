package com.fundoo.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundoo.user.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> 
{
	public Optional<UserEntity> findByEmailId(String emailId);
}
