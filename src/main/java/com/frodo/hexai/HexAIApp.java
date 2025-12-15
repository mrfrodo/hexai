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
			System.out.println("   (AI starts thinking...)");
			long start = System.currentTimeMillis();
			String output = chatClient.prompt()
					.user("HELLO HELLO IM FRODO FROM THE SHIRE")
					.call()
					.content();


			System.out.println("__________________________________________________________________________________________________________________________________________________________________________________________________________");
			System.out.println("   " + output + " ");
			System.out.println("__________________________________________________________________________________________________________________________________________________________________________________________________________");
			long timestamp = System.currentTimeMillis();
			long time = timestamp - start;
			System.out.println("   (AI took " + time + " ms. to execute)\n\n");
		};
	}

}
