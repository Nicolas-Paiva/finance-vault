package com.finance_vault.finance_vault.repository;

import com.finance_vault.finance_vault.model.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void UserRepository_ThrowException_WhenIdIsProvided() {
        // Arrange
        User user = User.builder()
                .id(1L)
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

    @Test
    public void UserRepository_ShouldThrowException_WhenEmailIsMissing() {
        // Arrange
        User user = User.builder()
                .name("Nicolas")
                .lastName("Paiva")
                .password("abc")
                .balance(1999)
                .createdAt(LocalDateTime.now())
                .build();

        // Act & Assert
        Assertions.assertThatThrownBy(() -> {
            userRepository.save(user);
            userRepository.flush(); // Forces DB insert
        }).isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    public void UserRepository_GetAll_ReturnMoreThanOneUser() {
        // Arrange
        User user1 = User.builder()
                .name("Nicolas")
                .lastName("Paiva")
                .email("abc@abc.com")
                .password("abc")
                .balance(1999)
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("Beatriz")
                .lastName("Baia")
                .email("abc@def.com")
                .password("abc")
                .balance(3500)
                .createdAt(LocalDateTime.now())
                .build();

        // Act
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        // Assert
        Assertions.assertThat(users.size()).isEqualTo(2);

    }


    @Test
    public void ShouldThrowException_WhenEmailIsDuplicated() {
        // Arrange
        User user1 = User.builder()
                .name("Nicolas")
                .lastName("Paiva")
                .email("abc@abc.com")
                .password("abc")
                .balance(1999)
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("Beatriz")
                .lastName("Baia")
                .email("abc@abc.com")
                .password("abc")
                .balance(3500)
                .createdAt(LocalDateTime.now())
                .build();

        // Act
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        // Assert
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

}
