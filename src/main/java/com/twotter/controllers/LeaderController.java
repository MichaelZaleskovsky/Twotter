package com.twotter.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twotter.entities.UserEntity;
import com.twotter.models.ErrorResponse;
import com.twotter.models.UserResponse;
import com.twotter.services.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@RequestMapping("/leaders")
public class LeaderController {
	
	@Autowired
	UserService userService;

    @ApiOperation(value = "Gets list of all users followed by this user", 
    		tags = "All requests related to the following", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with the specified ID does not exist"), 
            @ApiResponse(code = 200, message = "Suceess")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true)})
	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllLeadersOfUser(@RequestHeader("Authentication") String userId) {
		UserEntity user = userService.findUserByUserId(userId);
		if (user == null) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 
					  "User with the specified ID does not exist"), HttpStatus.NOT_FOUND);
		}
		List<UserResponse> result = userService.getLeaders(user);
		return new ResponseEntity<List<UserResponse>>(result, HttpStatus.OK);
	}
	
    @ApiOperation(value = "Allows the specified user to start following the leader with {leaderId}", 
    		tags = "All requests related to the following", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User or leader with the specified ID does not exist"), 
            @ApiResponse(code = 204, message = "Suceess, no response content")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true),
			@ApiImplicitParam(name = "leaderId", value = "Public token of leader", paramType = "path", required = true)})
	@PostMapping("/{leaderId}")
	public ResponseEntity<Void> followLeader(
			@RequestHeader("Authentication") String userId, 
			@PathVariable String leaderId) {
		
		UserEntity user = userService.findUserByUserId(userId);
		UserEntity leader = userService.findUserByUserId(leaderId);
		if (user == null || leader == null) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 
					  "User or leader with the specified ID does not exist"), HttpStatus.NOT_FOUND);
		}
		userService.addLeaderToUser(leader, user);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

    @ApiOperation(value = "Cancel following the leader with {leaderId} for the specified user ", 
    		tags = "All requests related to the following", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User or leader with the specified ID does not exist"), 
            @ApiResponse(code = 204, message = "Success, no any response content")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true),
			@ApiImplicitParam(name = "leaderId", value = "Public token of leader", paramType = "path", required = true)})
	@DeleteMapping("/{leaderId}")
	public ResponseEntity<Void> unFollowLeader(
			@RequestHeader("Authentication") String userId, 
			@PathVariable String leaderId) {
		
		UserEntity user = userService.findUserByUserId(userId);
		UserEntity leader = userService.findUserByUserId(leaderId);
		if (user == null || leader == null) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 
					  "User or leader with the specified ID does not exist"), HttpStatus.NOT_FOUND);
		}
		userService.removeLeaderFromUser(leader, user);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
