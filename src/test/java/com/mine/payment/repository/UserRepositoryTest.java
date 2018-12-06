package com.mine.payment.repository;

import com.mine.payment.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByName() {
        User user = createNewUser("Test", "Password");
        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findByName(user.getName());
        Assert.assertEquals(found.getName(), user.getName());
    }

    private User createNewUser(String name, String password) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        return newUser;
    }
}