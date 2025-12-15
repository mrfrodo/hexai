package com.frodo.hexai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HexAIApp {

	public static void main(String[] args) {
		SpringApplication.run(HexAIApp.class, args);
	}

	@Bean
	ApplicationRunner demo(ChatClient chatClient) {
		return args -> {

			String output = chatClient.prompt()
					.user("Can you give me today's quote?")
					.call()
					.content();

			System.out.println("**** quote of the day: " + output);
		};
	}

}
