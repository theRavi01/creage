package com.creage.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.creage.model.Users;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UsersServiceImpl  implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

	private final UsersRepository userRepository;


	public UsersServiceImpl(UsersRepository usersRepository) {
		this.userRepository = usersRepository;
	}


	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<Users> user = userRepository.findByEmail(username) ;
       if (user == null) {
           LOG.error("User not found with email: " + username);
           throw new UsernameNotFoundException("User not found with email: " + username);
       }

       LOG.info("Request Received to user for login: " + user.get().getEmail());
       List<GrantedAuthority> authorities = new ArrayList<>();
		return new org.springframework.security.core.userdetails.User(user.get().getEmail(),user.get().getPassword(),authorities);
//		return new org.springframework.security.core.userdetails.User("admin@gmail.com","Admin@123",authorities); 

	}
	

}
