package com.mine.payment.service;

import com.mine.payment.model.Account;
import com.mine.payment.model.User;
import com.mine.payment.repository.UserRepository;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User user = createNewUser("Test", "Password");
        User userCreated = createNewUser("Test3", "Password");

        Mockito.when(userRepository.findByName(user.getName()))
                .thenReturn(user);

        Mockito.when(userRepository.save(userCreated))
                .thenReturn(userCreated);
    }

    @Test
    public void createUser() {
        User user = createNewUser("Test3", "Password");
        boolean created = userService.createUser(user);
        Assert.assertTrue(created);
    }

    @Test
    public void createExistingUser() {
        User user = createNewUser("Test", "Password");
        boolean created = userService.createUser(user);
        Assert.assertFalse(created);
    }

    @Test
    public void findExistingName() {
        String name = "Test";
        Optional<User> result = userService.findByName(name);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getName(), name);
    }

    @Test
    public void findNotExistingName() {
        String name = "Test2";
        Optional<User> result = userService.findByName(name);
        Assert.assertFalse(result.isPresent());
    }

    private User createNewUser(String name, String password) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        return newUser;
    }

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}