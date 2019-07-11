package com.twotter.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twotter.entities.PostEntity;
import com.twotter.entities.UserEntity;
import com.twotter.models.PostRequest;
import com.twotter.models.PostResponse;
import com.twotter.services.PostService;
import com.twotter.services.UserService;

import io.swagger.annotations.*;

@Api(value = "PostController", description = "API methods to manage posts")
@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	PostService postService;

	@Autowired
	UserService userService;

    @ApiOperation(value = "Get list of all posts of registred user. Response by pages. "
    		+ "Return page with page number {page} and specified page size {size} ", 
    		tags = "getAllPostsByPage", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with specified userId not exist"), 
            @ApiResponse(code = 200, message = "Suceess, received page is the last page in the list"),
            @ApiResponse(code = 206, message = "Suceess, user cam get next page")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true),
    		@ApiImplicitParam(name = "page", value = "Number of page", paramType = "path", required = true),
    		@ApiImplicitParam(name = "size", value = "Size of page", paramType = "path", required = true)})
	@GetMapping("/{page}/{size}")
	public ResponseEntity<List<PostResponse>> getAllPostsByPage(
			@RequestHeader("Authentication") String userId,
			@PathVariable int page, 
			@PathVariable int size) {

		HttpStatus status;

		UserEntity user = userService.findUserByUserId(userId);
		if (user == null) {
			return new ResponseEntity<List<PostResponse>>(HttpStatus.NOT_FOUND);
		}
		List<PostResponse> responseValue = postService.getAllPostsByPage(user, page, size);
		if (postService.isLastPage()) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.PARTIAL_CONTENT;
		}
		return new ResponseEntity<List<PostResponse>>(responseValue, status);
	}

    @ApiOperation(value = "Create new post. Return public userId token of user as String."
    		+ "This token must be included as 'Authentication' header to any other request."
    		+ "If user with name post.username not exist - it will be created.  ", 
    		tags = "createNewPost", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfuly created for existing user"), 
            @ApiResponse(code = 201, message = "Successfuly created, new user created"),
            @ApiResponse(code = 413, message = "Post body length more then 140")})
	@PostMapping
	public ResponseEntity<String> createNewPost(@RequestBody PostRequest post) {
		
		String text = post.getText();
		if (text.length() > PostService.MAX_POST_SIZE) {
			return new ResponseEntity<String>(HttpStatus.PAYLOAD_TOO_LARGE);
		}

		UserEntity user = userService.findUser(post.getUsername());
		String userId;
		HttpStatus status = HttpStatus.OK;

		if (user == null) {
			user = userService.createUser(post.getUsername());
			status = HttpStatus.CREATED;
		}
		userId = user.getUserId();
		
		postService.createPost(user, post);
		return new ResponseEntity<String>(userId, status);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<PostResponse> deletePost(
			@RequestHeader("Authentication") String userId,
			@PathVariable long id) {

		UserEntity user = userService.findUserByUserId(userId);
		PostEntity post = postService.findPostById(id);
		if (post == null) {
			return new ResponseEntity<PostResponse>(HttpStatus.NOT_FOUND);
		}
		if (user == null || !user.getUserId().equals(post.getUser().getUserId())) {
			return new ResponseEntity<PostResponse>(HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<PostResponse>(new PostResponse(post), HttpStatus.OK);
	}

	@GetMapping("/leaders/{page}/{size}")
	public ResponseEntity<List<PostResponse>> getAllPostsOfLeadersByPage(
			@RequestHeader("Authentication") String userId,
			@PathVariable int page, 
			@PathVariable int size) {

		HttpStatus status;

		UserEntity user = userService.findUserByUserId(userId);
		if (user == null) {
			return new ResponseEntity<List<PostResponse>>(HttpStatus.NOT_FOUND);
		}
		
		List<PostResponse> responseValue = postService.getAllPostsOfLeadersByPage(user, page, size);
		if (postService.isLastPage()) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.PARTIAL_CONTENT;
		}
		return new ResponseEntity<List<PostResponse>>(responseValue, status);
	}

	@GetMapping("/leader/{liderId}/{page}/{size}")
	public ResponseEntity<List<PostResponse>> getAllPostsOfOneLeaderByPage(
			@RequestHeader("Authentication") String userId,
			@PathVariable String leaderId,
			@PathVariable int page, 
			@PathVariable int size) {

		HttpStatus status;

		UserEntity user = userService.findUserByUserId(userId);
		UserEntity leader = userService.findUserByUserId(leaderId);
		if (user == null || leader == null) {
			return new ResponseEntity<List<PostResponse>>(HttpStatus.NOT_FOUND);
		}
		
		if (!user.getLeaders().contains(leader)) {
			return new ResponseEntity<List<PostResponse>>(HttpStatus.FORBIDDEN);
		}
		
		List<PostResponse> responseValue = postService.getAllPostsOfOneLeaderByPage(user, page, size, leader);
		if (postService.isLastPage()) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.PARTIAL_CONTENT;
		}
		return new ResponseEntity<List<PostResponse>>(responseValue, status);
	}

}
