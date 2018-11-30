package com.mine.payment.repository;

import com.mine.payment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @stefanl
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * @param name
     * @return
     */
    User findByName(String name);
}
