package com.Mini.Mini.service;


import com.Mini.Mini.Entity.CustomUserDetail;
import com.Mini.Mini.Entity.User;
import com.Mini.Mini.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user=userRepository.findUserByEmail(email);
        user.orElseThrow(()->new UsernameNotFoundException("username not found"));

        if (!user.get().isVerified()) {
            throw new RuntimeException("User not found");
        }
            return user.map(CustomUserDetail::new).get();
    }
}
