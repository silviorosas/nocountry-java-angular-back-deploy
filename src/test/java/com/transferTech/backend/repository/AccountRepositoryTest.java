package com.transferTech.backend.repository;

import com.transferTech.backend.entity.Account;
import com.transferTech.backend.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    void setUp() {
        userRepository.saveAndFlush(
                User.builder()
                        .email("test@repository.com")
                        .password("example")
                        .name("John Doe")
                        .build()
        );
    }

    @Test
    void addAccountToUser() {
        accountRepository.saveAndFlush(
                Account.builder()
                        .user(userRepository.findByEmail("test@repository.com").get())
                        .accountNumber(BigInteger.valueOf(12345678))
                        .alias("hello.world")
                        .balance(0.0)
                        .build()
        );

        Optional<Account> newAccount = accountRepository.findByAccountNumber(BigInteger.valueOf(12345678));
        Optional<User> user = userRepository.findByEmail("test@repository.com");

        user.get().setAccount(newAccount.get());

        Assertions.assertEquals(user.get().getAccount(),newAccount.get());
        Assertions.assertEquals("hello.world",user.get().getAccount().getAlias());
    }

}
