package com.armtimes.armtimes.security;


import com.armtimes.armtimes.entity.User;
import com.armtimes.armtimes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(username);
        if(!byEmail.isPresent()){
            throw new UsernameNotFoundException("username does not exist");
        }
        return new CurrentUser(byEmail.get());
    }
}
