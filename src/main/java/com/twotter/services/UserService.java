package com.twotter.services;

import java.util.List;

import com.twotter.entities.UserEntity;
import com.twotter.models.UserResponse;

public interface UserService {

	UserEntity findUser(String username);

	List<UserResponse> findUsersByNameStartWith(String username);

	UserEntity createUser(String username);

	UserEntity findUserByUserId(String userId);

	List<UserResponse> getLeaders(UserEntity user);

	void addLeaderToUser(UserEntity leader, UserEntity user);

	void removeLeaderFromUser(UserEntity leader, UserEntity user);

}
