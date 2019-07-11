package com.twotter.services;

import java.util.List;

import com.twotter.entities.PostEntity;
import com.twotter.entities.UserEntity;
import com.twotter.models.PostRequest;
import com.twotter.models.PostResponse;

public interface PostService {
	
	public static final int MAX_POST_SIZE = 140;

	public List<PostResponse> getAllPostsByPage(UserEntity user, int page, int size);

	public boolean isLastPage();

	public void createPost(UserEntity user, PostRequest post);

	public List<PostResponse> getAllPostsOfLeadersByPage(UserEntity user, int page, int size);

	public List<PostResponse> getAllPostsOfOneLeaderByPage(UserEntity user, int page, int size, UserEntity lider);

	public PostEntity findPostById(long id);

}
