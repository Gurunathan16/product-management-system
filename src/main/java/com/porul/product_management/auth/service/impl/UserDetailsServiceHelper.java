package com.porul.product_management.auth.service.impl;

import com.porul.product_management.auth.entity.Users;
import com.porul.product_management.auth.entity.UserPrincipal;
import com.porul.product_management.auth.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceHelper implements UserDetailsService{

    UsersRepository userRepository;

    public UserDetailsServiceHelper(UsersRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        Users user = userRepository.findByUsername(username);

        if(user == null)
            throw new UsernameNotFoundException("User not found!");

        return new UserPrincipal(user);
    }

}
