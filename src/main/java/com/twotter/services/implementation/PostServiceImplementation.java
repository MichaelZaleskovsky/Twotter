package com.twotter.services.implementation;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.twotter.entities.PostEntity;
import com.twotter.entities.UserEntity;
import com.twotter.models.PostRequest;
import com.twotter.models.PostResponse;
import com.twotter.repository.PostRepository;
import com.twotter.services.PostService;

@Service
public class PostServiceImplementation implements PostService {
	
	@Autowired
	PostRepository repository;
	ThreadLocal<Boolean> last = new ThreadLocal<>();

	@Override
	public List<PostResponse> getAllPostsByPage(UserEntity user, int page, int size) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.Direction.DESC, "time");
		Slice<PostEntity> slice = repository.findByUser(user, pageable);
		
		last.set(slice == null ? true : slice.isLast());
		List<PostResponse> result = slice.get()
				.map(post -> new PostResponse(post))
				.collect(Collectors.toList());
		return result;
	}

	@Override
	public boolean isLastPage() {
		return last.get();
	}

	@Override
	public void createPost(UserEntity user, PostRequest post) {
		PostEntity postModel = new PostEntity(user, post.getText(), new Date().getTime());
		repository.save(postModel);
	}

	@Override
	public List<PostResponse> getAllPostsOfLeadersByPage(UserEntity user, int page, int size) {
		
		Set<UserEntity> leadersList = user.getLeaders();
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.Direction.DESC, "time");
		Slice<PostEntity> slice = repository.findByUserIn(leadersList, pageable);
		
		last.set(slice == null ? true : slice.isLast());
		List<PostResponse> result = slice.get()
				.map(post -> new PostResponse(post))
				.collect(Collectors.toList());
		return result;
	}

	@Override
	public List<PostResponse> getAllPostsOfOneLeaderByPage(UserEntity user, int page, int size, UserEntity leader) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.Direction.DESC, "time");
		Slice<PostEntity> slice = repository.findByUser(leader, pageable);
		
		last.set(slice == null ? true : slice.isLast());
		List<PostResponse> result = slice.get()
				.map(post -> new PostResponse(post))
				.collect(Collectors.toList());
		return result;
	}

	@Override
	public PostEntity findPostById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
