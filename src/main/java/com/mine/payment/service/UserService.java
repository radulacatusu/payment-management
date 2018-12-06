package com.mine.payment.service;

import com.mine.payment.model.User;

import java.util.Optional;

/**
 * @stefanl
 */
public interface UserService {

    /**
     * Method used for user creation
     */
    boolean createUser(User user);

    /**
     * Retrieve the user with the specified id
     *
     * @param name
     * @return
     */
    Optional<User> findByName(String name);
}
