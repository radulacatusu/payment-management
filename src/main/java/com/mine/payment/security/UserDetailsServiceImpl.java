package com.mine.payment.security;

import com.mine.payment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

;

/**
 * @stefanl
 */


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<com.mine.payment.model.User> applicationUser = userService.findByName(name);

        if (!applicationUser.isPresent()) {
            throw new UsernameNotFoundException(name);
        }
        return new User(applicationUser.get().getName(), applicationUser.get().getPassword(), Collections.emptyList());
    }
}
