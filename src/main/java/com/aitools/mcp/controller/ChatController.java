package com.aitools.mcp.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {


    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private SyncMcpToolCallbackProvider syncMcpToolCallbackProvider;
    @Autowired
    @Qualifier("taskTools")
    private ToolCallbackProvider toolCallbackProvider;

    @GetMapping("/github")
    public String call(@RequestParam String input){
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultTools(syncMcpToolCallbackProvider.getToolCallbacks()).defaultFunctions(toolCallbackProvider.getToolCallbacks())
                .defaultAdvisors(new SimpleLoggerAdvisor(AdvisedRequest::userText, ChatResponse::toString, 0))
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
        return chatClient.prompt(input).call().content();
    }

    @GetMapping("/call")
    public String callByName(@RequestParam String name) {
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultTools(toolCallbackProvider.getToolCallbacks())
                .defaultAdvisors(new SimpleLoggerAdvisor(AdvisedRequest::userText, ChatResponse::toString, 0))
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
        return chatClient.prompt(name).call().content();
    }
}