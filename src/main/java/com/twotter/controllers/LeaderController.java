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
import com.twotter.models.UserResponse;
import com.twotter.services.UserService;

@RestController
@RequestMapping("/leaders")
public class LeaderController {
	
	@Autowired
	UserService userService;

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllLeadersOfUser(@RequestHeader("Authentication") String userId) {
		UserEntity user = userService.findUserByUserId(userId);
		if (user == null) {
			return new ResponseEntity<List<UserResponse>>(HttpStatus.NOT_FOUND);
		}
		List<UserResponse> result = userService.getLeaders(user);
		return new ResponseEntity<List<UserResponse>>(result, HttpStatus.OK);
	}
	
	@PostMapping("/{leaderId}")
	public ResponseEntity<Void> followLeader(
			@RequestHeader("Authentication") String userId, 
			@PathVariable String leaderId) {
		
		UserEntity user = userService.findUserByUserId(userId);
		UserEntity leader = userService.findUserByUserId(leaderId);
		if (user == null || leader == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		userService.addLeaderToUser(leader, user);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{leaderId}")
	public ResponseEntity<Void> unFollowLeader(
			@RequestHeader("Authentication") String userId, 
			@PathVariable String leaderId) {
		
		UserEntity user = userService.findUserByUserId(userId);
		UserEntity leader = userService.findUserByUserId(leaderId);
		if (user == null || leader == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		userService.removeLeaderFromUser(leader, user);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
