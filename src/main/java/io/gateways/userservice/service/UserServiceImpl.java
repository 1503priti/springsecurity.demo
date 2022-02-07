package io.gateways.userservice.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.gateways.userservice.domain.Role;
import io.gateways.userservice.domain.User;
import io.gateways.userservice.repo.RoleRepo;
import io.gateways.userservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepo userRepo;
	private final RoleRepo roleRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if(user == null) {
		log.error("User not found in the database");	
		throw new UsernameNotFoundException("User not found in the database");
		}
		else {
			log.info("User found in the database: {}", username);
		}
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(), authorities);
	}
	
	@Override
	public User saveUser(User user) {
		log.info("Saving new user {} to the database",user.getUsername());
		return userRepo.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		log.info("Saving new role {} to the database",role.getName());
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		log.info("Adding role {} to user {}",roleName,username);
		User user = userRepo.findByUsername(username);
		Role role = roleRepo.findByName(roleName);
		user.getRoles().add(role);

	}

	@Override
	public User getUser(String username) {
		log.info("Fetching user {}",username);
		return userRepo.findByUsername(username);
	}

	@Override
	public List<User> getUsers() {
		log.info("Fetching all users");
		return userRepo.findAll();
	}

	

}
