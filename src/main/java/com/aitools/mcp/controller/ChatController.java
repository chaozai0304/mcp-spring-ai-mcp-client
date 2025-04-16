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
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/chat")
public class ChatController {


    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private SyncMcpToolCallbackProvider syncMcpToolCallbackProvider;
    @Autowired
    @Qualifier("taskTools")
    private MethodToolCallbackProvider toolCallbackProvider;

    @GetMapping("/github")
    public String call(@RequestParam String input){
//        ToolCallback[] a = syncMcpToolCallbackProvider.getToolCallbacks();
//        ToolCallback[] b = toolCallbackProvider.getToolCallbacks();
//        ToolCallback[] merged = Stream.of(a, b)
//                .flatMap(Arrays::stream)
//                .collect(Collectors.toMap(
//                        ToolCallback::getToolDefinition,
//                        Function.identity(),
//                        (existing, replacement) -> existing // 保留先出现的工具
//                ))
//                .values()
//                .toArray(ToolCallback[]::new);
        //
        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultTools(syncMcpToolCallbackProvider,toolCallbackProvider)
                /**增强顾问（Advisors）SimpleLoggerAdvisor​​：
                 功能：记录用户请求内容和模型响应日志。
                 参数说明：
                 AdvisedRequest::userText：提取用户输入的原始文本。
                 ChatResponse::toString：将模型响应转换为日志字符串。
                 0：日志输出级别
                 MessageChatMemoryAdvisor：
                 功能：维护对话记忆上下文，实现多轮对话能力。
                 InMemoryChatMemory：使用内存存储对话历史（网页6）
                 **/
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