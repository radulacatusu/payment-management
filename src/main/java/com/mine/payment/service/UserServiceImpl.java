package com.mine.payment.service;

import com.mine.payment.model.User;
import com.mine.payment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Optional;


/**
 * @stefanl
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private UserRepository userRepository;

    @Override
    public boolean createUser(@NotNull User user) {

        User applicationUser = userRepository.findByName(user.getName());
        boolean rNew = false;
        if (applicationUser == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            rNew = true;
        }
        return rNew;
    }

    @Override
    public Optional<User> findByName(String name) {
        return Optional.ofNullable(userRepository.findByName(name));
    }
}
