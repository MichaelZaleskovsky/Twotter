package com.twotter.repository;

import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.twotter.entities.PostEntity;
import com.twotter.entities.UserEntity;

@Repository
public interface PostRepository extends PagingAndSortingRepository<PostEntity, Long> {
	
	Slice<PostEntity> findByUser(UserEntity user, Pageable pagable);

	Slice<PostEntity> findByUserIn(Set<UserEntity> leadersList, Pageable pageable);
}
