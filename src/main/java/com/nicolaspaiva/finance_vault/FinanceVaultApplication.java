package com.nicolaspaiva.finance_vault;

import com.nicolaspaiva.finance_vault.user.entity.Role;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


@SpringBootApplication
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
					.build();

			userRepository.save(adminAcc);
		}
	}
}
