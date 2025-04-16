package com.aitools.mcp.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskMcpCallServerServices {


    @Tool(description = "测试")
    public List<String> getById(@ToolParam(description = "id值") String id){

        return new ArrayList<String>();
    }

}
