package com.demo.book.springtdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SpringTddBookApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void passwordEncoder_빈이_올바르게_설정된다(
			@Autowired PasswordEncoder passwordEncoder
	) {
		assertThat(passwordEncoder).isNotNull();
		assertThat(passwordEncoder).isInstanceOf(Pbkdf2PasswordEncoder.class);
	}

}
