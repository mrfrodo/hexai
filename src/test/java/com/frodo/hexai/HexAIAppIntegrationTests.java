package com.frodo.hexai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class HexAIAppIntegrationTests {

    @Autowired
    private ChatModel chatModel;

    @Test
    void testChatClient() {
        ChatClient chatClient = ChatClient.create(chatModel);

        String output = chatClient.prompt()
                .user("Hello, world. My name is frodo baggins")
                .call()
                .content();

        assertNotNull(output);
        System.out.println("**** RESPONSE FROM AI: " + output);
    }
}
