package com.twotter.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twotter.entities.UserEntity;
import com.twotter.models.UserResponse;
import com.twotter.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;

	@GetMapping("/{startWith}")
	public ResponseEntity<List<UserResponse>> getAllUsers(
			@RequestHeader("Authentication") String userId, 
			@PathVariable String startWith) {
		
		UserEntity user = userService.findUserByUserId(userId);
		if (user == null) {
			return new ResponseEntity<List<UserResponse>>(HttpStatus.NOT_FOUND);
		}
		List<UserResponse> result = userService.findUsersByNameStartWith(startWith);
		return new ResponseEntity<List<UserResponse>>(result, HttpStatus.OK);

	}

}
