package com.loginAPI.loginAPI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class LoginApiApplicationTests {

	@MockBean
	private JavaMailSender mailSender;

	@Test
	void contextLoads() {
	}

}
