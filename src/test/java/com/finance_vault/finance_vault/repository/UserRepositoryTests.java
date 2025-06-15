package com.finance_vault.finance_vault.repository;

import com.finance_vault.finance_vault.model.user.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith; // ⬅️ Add this
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner; // ⬅️ And this

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnSavedUser() {
        // Arrange
        User user = User.builder()
                .name("Nicolas")
                .lastName("Paiva")
                .email("abc@abc.com")
                .password("abc")
                .balance(1999)
                .createdAt(LocalDateTime.now())
                .build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }
}
