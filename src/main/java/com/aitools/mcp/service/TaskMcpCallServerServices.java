package com.aitools.mcp.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TaskMcpCallServerServices {


    @Tool(description = "根据名称返回年龄")
    public Integer owner_getByUserName(@ToolParam(description = "名称") String name) {
        Map<String, Integer> map = Map.of("张三", 23, "李四", 25, "王五", 26);
        return map.getOrDefault(name,0);
    }

}
