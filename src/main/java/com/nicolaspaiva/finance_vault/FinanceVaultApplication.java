package com.nicolaspaiva.finance_vault;

import com.nicolaspaiva.finance_vault.user.entity.Role;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;


@SpringBootApplication
@EnableScheduling
public class FinanceVaultApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(FinanceVaultApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Optional<UserEntity> admin = userRepository.findByRole(Role.ADMIN);

		if(admin.isEmpty()){
			UserEntity adminAcc = UserEntity.builder()
					.firstName("admin")
					.lastName("admin")
					.email("admin@admin.com")
					.password(new BCryptPasswordEncoder().encode("a"))
					.role(Role.ADMIN)
					.isActive(true)
					.build();

			userRepository.save(adminAcc);
		}

		for(int i = 0; i < 50; i++){
			UserEntity user = UserEntity.builder()
					.email("u" + i + "@outlook.com")
					.password(new BCryptPasswordEncoder().encode("u"))
					.role(Role.USER)
					.createdAt(LocalDateTime.now())
					.isActive(false)
					.build();
			userRepository.save(user);
		}
	}
}
