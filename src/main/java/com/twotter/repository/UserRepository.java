package com.twotter.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.twotter.entities.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findOneByUsername(String username);
	boolean existsByUserId(String userId);
	UserEntity findOneByUserId(String userId);
	List<UserEntity> findByUsernameStartsWith(String startWith);
}
