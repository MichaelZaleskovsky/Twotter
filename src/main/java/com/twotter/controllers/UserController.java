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
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;

    @ApiOperation(value = "Gets list of all users whose name starts with {startWith}", 
    		tags = "All requests related to the user", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with the specified ID does not exist"), 
            @ApiResponse(code = 200, message = "Success")})
    @ApiImplicitParams(value = {
    		@ApiImplicitParam(name = "Authentication", value = "Public userId token", paramType = "header", required = true),
			@ApiImplicitParam(name = "startWith", value = "Beginning  of the user name", paramType = "path", required = true)})
	@GetMapping("/{startWith}")
	public ResponseEntity<List<UserResponse>> getAllUsers(
			@RequestHeader("Authentication") String userId, 
			@PathVariable String startWith) {
		
		UserEntity user = userService.findUserByUserId(userId);
		if (user == null) {
			return new ResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 
					  "User with the specified ID does not exist"), HttpStatus.NOT_FOUND);
		}
		List<UserResponse> result = userService.findUsersByNameStartWith(startWith);
		return new ResponseEntity<List<UserResponse>>(result, HttpStatus.OK);

	}

}
