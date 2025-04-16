package com.aitools.mcp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class McpClientRun {
    public static void main(String[] args) {
        Map<String,String> map1 = new HashMap();
        map1.put("GITHUB_PERSONAL_ACCESS_TOKEN","");

        StdioClientTransport transport = new StdioClientTransport(
                ServerParameters.builder("/usr/local/bin/npx")
                        .args("-y","@modelcontextprotocol/server-github")
                        .env(map1)
                        .build()
        );
//        StdioClientTransport transport = new StdioClientTransport(
//                ServerParameters.builder("/usr/local/bin/npx")
//                        .args("-y", "@modelcontextprotocol/server-filesystem", ".")
//                        .build()
//        );
        try (McpSyncClient client = McpClient.sync(transport)
                .build()) {

            client.listTools();

            McpSchema.InitializeResult initialize = client.initialize();
            System.out.println("client initialized: " + initialize);


            tools(client); // 打印 MCP 工具列表

            createDirectory(client); // 创建目录
            createFile(client); // 创建文件

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void tools (McpSyncClient client) {
        McpSchema.ListToolsResult listToolsResult = client.listTools();
        listToolsResult.tools().forEach(System.out::println);
    }

    public static void createDirectory(McpSyncClient client) {
        McpSchema.CallToolRequest callToolRequest = new McpSchema.CallToolRequest(
                "create_directory",
                Map.of("path", "mcp")
        );
        McpSchema.CallToolResult callToolResult = client.callTool(callToolRequest);
        System.out.println(callToolResult.content());
    }

    public static void createFile(McpSyncClient client) {
        McpSchema.CallToolRequest callToolRequest = new McpSchema.CallToolRequest(
                "write_file",
                Map.of("path", "mcp/test.txt", "content", "hello world")
        );
        McpSchema.CallToolResult callToolResult = client.callTool(callToolRequest);
        System.out.println(callToolResult.content());
    }
}
