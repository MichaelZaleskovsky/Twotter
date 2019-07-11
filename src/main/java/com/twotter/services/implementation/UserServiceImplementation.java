package com.twotter.services.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twotter.entities.UserEntity;
import com.twotter.models.UserResponse;
import com.twotter.repository.UserRepository;
import com.twotter.services.UserService;

@Service
public class UserServiceImplementation implements UserService {
	
	@Autowired
	UserRepository repository;

	@Override
	public UserEntity findUser(String username) {
		
		return repository.findOneByUsername(username);
	}

	@Override
	public UserEntity createUser(String username) {
		String userId;
		do {
			userId = UUID.randomUUID().toString();
		} while (repository.existsByUserId(userId));
		
		UserEntity userModel = new UserEntity(username, userId);
		
		UserEntity user = repository.save(userModel);
		return user;
	}

	@Override
	public UserEntity findUserByUserId(String userId) {
		return repository.findOneByUserId(userId);
	}

	@Override
	public List<UserResponse> getLeaders(UserEntity user) {
		List<UserResponse> result = user.getLeaders().stream()
				.map(usr -> new UserResponse(usr))
				.collect(Collectors.toList());
		return result;
	}

	@Override
	public void addLeaderToUser(UserEntity leader, UserEntity user) {
		Set<UserEntity> leaders = user.getLeaders();
		if (leaders == null) {
			leaders = new HashSet<UserEntity>();
		}
		leaders.add(leader);
		repository.save(user);
		
	}

	@Override
	public void removeLeaderFromUser(UserEntity leader, UserEntity user) {
		Set<UserEntity> leaders = user.getLeaders();
		if (leaders == null) return;
		leaders.remove(leader);
	}

	@Override
	public List<UserResponse> findUsersByNameStartWith(String startWith) {
		List<UserEntity> usersList = repository.findByUsernameStartsWith(startWith);
		return null;
	}

}
