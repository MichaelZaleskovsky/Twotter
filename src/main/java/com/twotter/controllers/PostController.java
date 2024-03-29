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
import com.twotter.models.ErrorResponse;
import com.twotter.models.PostRequest;
import com.twotter.models.PostResponse;
import com.twotter.services.PostService;
import com.twotter.services.UserService;

import io.swagger.annotations.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	PostService postService;

	@Autowired
	UserService userService;

    @ApiOperation(value = "Gets list of all posts of registred user. Response by pages. "
    		+ "Returns page with page number {page} and specified page size {size} ", 
    		tags = "All requests related to the post", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with the specified ID does not exist"), 
            @ApiResponse(code = 200, message = "Suceess, received page is the last page in the list"),
            @ApiResponse(code = 206, message = "Suceess, user can receive the next page")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true),
    		@ApiImplicitParam(name = "page", value = "Page number", paramType = "path", required = true),
    		@ApiImplicitParam(name = "size", value = "Size of page", paramType = "path", required = true)})
	@GetMapping("/{page}/{size}")
	public ResponseEntity<List<PostResponse>> getAllPostsByPage(
			@RequestHeader("Authentication") String userId,
			@PathVariable int page, 
			@PathVariable int size) {

		HttpStatus status;

		UserEntity user = userService.findUserByUserId(userId);
		if (user == null) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 
					  "User with the specified ID does not exist"), HttpStatus.NOT_FOUND);
		}
		List<PostResponse> responseValue = postService.getAllPostsByPage(user, page, size);
		if (postService.isLastPage()) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.PARTIAL_CONTENT;
		}
		return new ResponseEntity<List<PostResponse>>(responseValue, status);
	}

	@ApiOperation(value = "Creates new post. Returns public userId token of user as String."
    		+ "This token is to be included as 'Authentication' header to any other request."
    		+ "If user with name post.username does not exist - such user will be created.  ", 
    		tags = "All requests related to the post", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created for existing user"), 
            @ApiResponse(code = 201, message = "Successfully created, new user is created"),
            @ApiResponse(code = 413, message = "Post body length is more than 140")})
	@PostMapping
	public ResponseEntity<String> createNewPost(@RequestBody PostRequest post) {
		
		String text = post.getText();
		if (text.length() > PostService.MAX_POST_SIZE) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE.value(), 
					  "Post body length is more than 140"), HttpStatus.PAYLOAD_TOO_LARGE);
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

	@ApiOperation(value = "Deletes existing post by unique id. "
    		+ "Returns deleted post ", 
    		tags = "All requests related to the post", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted successfully"), 
            @ApiResponse(code = 403, message = "Forbidden to delete. Post created by other user"),
            @ApiResponse(code = 404, message = "User with the specified ID does not exist")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true),
    		@ApiImplicitParam(name = "id", value = "Unic id of post", paramType = "path", required = true)})
	@DeleteMapping("/{id}")
	public ResponseEntity<PostResponse> deletePost(
			@RequestHeader("Authentication") String userId,
			@PathVariable long id) {

		UserEntity user = userService.findUserByUserId(userId);
		PostEntity post = postService.findPostById(id);
		if (post == null) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 
					  "User with the specified ID does not exist"), HttpStatus.NOT_FOUND);
		}
		if (user == null || !user.getUserId().equals(post.getUser().getUserId())) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.FORBIDDEN.value(), 
									  "Forbidden to delete. Post created by other user"), HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<PostResponse>(new PostResponse(post), HttpStatus.OK);
	}

    @ApiOperation(value = "Gets list of all posts of those users who are the specified user follows. Response by pages. "
    		+ "Returns page with page number {page} and specified page size {size} ", 
    		tags = "All requests related to the post", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with the specified userId does not exist"), 
            @ApiResponse(code = 200, message = "Success, received page is the last page in the list"),
            @ApiResponse(code = 206, message = "Success, user can receive the next page")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true),
    		@ApiImplicitParam(name = "page", value = "Page number", paramType = "path", required = true),
    		@ApiImplicitParam(name = "size", value = "Size of page", paramType = "path", required = true)})
	@GetMapping("/leaders/{page}/{size}")
	public ResponseEntity<List<PostResponse>> getAllPostsOfLeadersByPage(
			@RequestHeader("Authentication") String userId,
			@PathVariable int page, 
			@PathVariable int size) {

		HttpStatus status;

		UserEntity user = userService.findUserByUserId(userId);
		if (user == null) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 
					  "User with the specified ID does not exist"), HttpStatus.NOT_FOUND);
		}
		
		List<PostResponse> responseValue = postService.getAllPostsOfLeadersByPage(user, page, size);
		if (postService.isLastPage()) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.PARTIAL_CONTENT;
		}
		return new ResponseEntity<List<PostResponse>>(responseValue, status);
	}

    @ApiOperation(value = "Gets list of all posts of one of those users who are the specified user follows. Response by pages. "
    		+ "Returns page with page number {page} and specified page size {size} ", 
    		tags = "All requests related to the post", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with the specified userId does not exist"), 
            @ApiResponse(code = 403, message = "Forbidden access. User must  be the leader's follower to get posts"), 
            @ApiResponse(code = 200, message = "Suceess, received page is the last page in the list"),
            @ApiResponse(code = 206, message = "Suceess, user can receive the next page")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true),
    		@ApiImplicitParam(name = "leaderId", value = "Public token of leader", paramType = "path", required = true),
    		@ApiImplicitParam(name = "page", value = "Page number", paramType = "path", required = true),
    		@ApiImplicitParam(name = "size", value = "Size of page", paramType = "path", required = true)})
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
			return new ResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 
					  "User with the specified ID does not exist"), HttpStatus.NOT_FOUND);
		}
		
		if (!user.getLeaders().contains(leader)) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.FORBIDDEN.value(), 
					  "Forbidden access. User must be the leader's follower to get posts "), HttpStatus.FORBIDDEN);
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
