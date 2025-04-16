package com.aitools.mcp.config;

import com.aitools.mcp.service.TaskMcpCallServerServices;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    @Bean
//    @Primary // 标记为优先注入
    public MethodToolCallbackProvider taskTools(TaskMcpCallServerServices taskMcpCallServerServices) {
        return MethodToolCallbackProvider.builder().toolObjects(taskMcpCallServerServices).build();
    }
//    @Bean
//    ChatClient chatClient(ChatModel chatModel, SyncMcpToolCallbackProvider toolCallbackProvider) {
//        return ChatClient
//                .builder(chatModel)
//                .defaultTools(toolCallbackProvider.getToolCallbacks())
//                .build();
//    }
}