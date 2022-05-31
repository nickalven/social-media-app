package com.nikos.posts.service;

import org.springframework.stereotype.Service;

import com.nikos.posts.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;		
	}
	
	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByUsername(email);
		
	}
}
