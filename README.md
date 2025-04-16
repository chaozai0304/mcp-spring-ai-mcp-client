# 代码介绍和启动方式说明
##1. springboot ai创建mcp client

   `````
   1.调用AI查询使用那个call
   入参为：
   入口为：org.springframework.ai.openai.OpenAiChatModel#internalCall
   -调用->>org.springframework.ai.openai.api.OpenAiApi#chatCompletionEntity(org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest, org.springframework.util.MultiValueMap<java.lang.String,java.lang.String>)
   返回使用mcp的入参
   2.调用org.springframework.ai.mcp.SyncMcpToolCallback#call 所有的call mcpserver的入口
   日志打印io.modelcontextprotocol.spec.McpSchema#deserializeJsonRpcMessage
   
   3.最后在调用AI进行信息汇总
   入参为：
   入口为：org.springframework.ai.openai.OpenAiChatModel#internalCall
   -调用->>org.springframework.ai.openai.api.OpenAiApi#chatCompletionEntity(org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest, org.springframework.util.MultiValueMap<java.lang.String,java.lang.String>)
   `````

  ### 请求大模型的入参示例

   `````
   {
       "messages": [
           {
               "content": "查询chaozai0304所有项目",
               "role": "user"
           }
       ],
       "model": "gpt-4o",
       "stream": false,
       "temperature": 0.7,
       "tools": [
           {
               "type": "function",
               "function": {
                   "description": "Create or update a single file in a GitHub repository",
                   "name": "create_or_update_file",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "path": {
                               "type": "string",
                               "description": "Path where to create/update the file"
                           },
                           "content": {
                               "type": "string",
                               "description": "Content of the file"
                           },
                           "message": {
                               "type": "string",
                               "description": "Commit message"
                           },
                           "branch": {
                               "type": "string",
                               "description": "Branch to create/update the file in"
                           },
                           "sha": {
                               "type": "string",
                               "description": "SHA of the file being replaced (required when updating existing files)"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "path",
                           "content",
                           "message",
                           "branch"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Search for GitHub repositories",
                   "name": "search_repositories",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "query": {
                               "type": "string",
                               "description": "Search query (see GitHub search syntax)"
                           },
                           "page": {
                               "type": "number",
                               "description": "Page number for pagination (default: 1)"
                           },
                           "perPage": {
                               "type": "number",
                               "description": "Number of results per page (default: 30, max: 100)"
                           }
                       },
                       "required": [
                           "query"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Create a new GitHub repository in your account",
                   "name": "create_repository",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "name": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "description": {
                               "type": "string",
                               "description": "Repository description"
                           },
                           "private": {
                               "type": "boolean",
                               "description": "Whether the repository should be private"
                           },
                           "autoInit": {
                               "type": "boolean",
                               "description": "Initialize with README.md"
                           }
                       },
                       "required": [
                           "name"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Get the contents of a file or directory from a GitHub repository",
                   "name": "get_file_contents",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "path": {
                               "type": "string",
                               "description": "Path to the file or directory"
                           },
                           "branch": {
                               "type": "string",
                               "description": "Branch to get contents from"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "path"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Push multiple files to a GitHub repository in a single commit",
                   "name": "push_files",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "branch": {
                               "type": "string",
                               "description": "Branch to push to (e.g., 'main' or 'master')"
                           },
                           "files": {
                               "type": "array",
                               "items": {
                                   "type": "object",
                                   "properties": {
                                       "path": {
                                           "type": "string"
                                       },
                                       "content": {
                                           "type": "string"
                                       }
                                   },
                                   "required": [
                                       "path",
                                       "content"
                                   ],
                                   "additionalProperties": false
                               },
                               "description": "Array of files to push"
                           },
                           "message": {
                               "type": "string",
                               "description": "Commit message"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "branch",
                           "files",
                           "message"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Create a new issue in a GitHub repository",
                   "name": "create_issue",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string"
                           },
                           "repo": {
                               "type": "string"
                           },
                           "title": {
                               "type": "string"
                           },
                           "body": {
                               "type": "string"
                           },
                           "assignees": {
                               "type": "array",
                               "items": {
                                   "type": "string"
                               }
                           },
                           "milestone": {
                               "type": "number"
                           },
                           "labels": {
                               "type": "array",
                               "items": {
                                   "type": "string"
                               }
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "title"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Create a new pull request in a GitHub repository",
                   "name": "create_pull_request",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "title": {
                               "type": "string",
                               "description": "Pull request title"
                           },
                           "body": {
                               "type": "string",
                               "description": "Pull request body/description"
                           },
                           "head": {
                               "type": "string",
                               "description": "The name of the branch where your changes are implemented"
                           },
                           "base": {
                               "type": "string",
                               "description": "The name of the branch you want the changes pulled into"
                           },
                           "draft": {
                               "type": "boolean",
                               "description": "Whether to create the pull request as a draft"
                           },
                           "maintainer_can_modify": {
                               "type": "boolean",
                               "description": "Whether maintainers can modify the pull request"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "title",
                           "head",
                           "base"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Fork a GitHub repository to your account or specified organization",
                   "name": "fork_repository",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "organization": {
                               "type": "string",
                               "description": "Optional: organization to fork to (defaults to your personal account)"
                           }
                       },
                       "required": [
                           "owner",
                           "repo"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Create a new branch in a GitHub repository",
                   "name": "create_branch",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "branch": {
                               "type": "string",
                               "description": "Name for the new branch"
                           },
                           "from_branch": {
                               "type": "string",
                               "description": "Optional: source branch to create from (defaults to the repository's default branch)"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "branch"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Get list of commits of a branch in a GitHub repository",
                   "name": "list_commits",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string"
                           },
                           "repo": {
                               "type": "string"
                           },
                           "sha": {
                               "type": "string"
                           },
                           "page": {
                               "type": "number"
                           },
                           "perPage": {
                               "type": "number"
                           }
                       },
                       "required": [
                           "owner",
                           "repo"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "List issues in a GitHub repository with filtering options",
                   "name": "list_issues",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string"
                           },
                           "repo": {
                               "type": "string"
                           },
                           "direction": {
                               "type": "string",
                               "enum": [
                                   "asc",
                                   "desc"
                               ]
                           },
                           "labels": {
                               "type": "array",
                               "items": {
                                   "type": "string"
                               }
                           },
                           "page": {
                               "type": "number"
                           },
                           "per_page": {
                               "type": "number"
                           },
                           "since": {
                               "type": "string"
                           },
                           "sort": {
                               "type": "string",
                               "enum": [
                                   "created",
                                   "updated",
                                   "comments"
                               ]
                           },
                           "state": {
                               "type": "string",
                               "enum": [
                                   "open",
                                   "closed",
                                   "all"
                               ]
                           }
                       },
                       "required": [
                           "owner",
                           "repo"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Update an existing issue in a GitHub repository",
                   "name": "update_issue",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string"
                           },
                           "repo": {
                               "type": "string"
                           },
                           "issue_number": {
                               "type": "number"
                           },
                           "title": {
                               "type": "string"
                           },
                           "body": {
                               "type": "string"
                           },
                           "assignees": {
                               "type": "array",
                               "items": {
                                   "type": "string"
                               }
                           },
                           "milestone": {
                               "type": "number"
                           },
                           "labels": {
                               "type": "array",
                               "items": {
                                   "type": "string"
                               }
                           },
                           "state": {
                               "type": "string",
                               "enum": [
                                   "open",
                                   "closed"
                               ]
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "issue_number"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Add a comment to an existing issue",
                   "name": "add_issue_comment",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string"
                           },
                           "repo": {
                               "type": "string"
                           },
                           "issue_number": {
                               "type": "number"
                           },
                           "body": {
                               "type": "string"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "issue_number",
                           "body"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Search for code across GitHub repositories",
                   "name": "search_code",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "q": {
                               "type": "string"
                           },
                           "order": {
                               "type": "string",
                               "enum": [
                                   "asc",
                                   "desc"
                               ]
                           },
                           "page": {
                               "type": "number",
                               "minimum": 1
                           },
                           "per_page": {
                               "type": "number",
                               "minimum": 1,
                               "maximum": 100
                           }
                       },
                       "required": [
                           "q"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Search for issues and pull requests across GitHub repositories",
                   "name": "search_issues",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "q": {
                               "type": "string"
                           },
                           "order": {
                               "type": "string",
                               "enum": [
                                   "asc",
                                   "desc"
                               ]
                           },
                           "page": {
                               "type": "number",
                               "minimum": 1
                           },
                           "per_page": {
                               "type": "number",
                               "minimum": 1,
                               "maximum": 100
                           },
                           "sort": {
                               "type": "string",
                               "enum": [
                                   "comments",
                                   "reactions",
                                   "reactions-+1",
                                   "reactions--1",
                                   "reactions-smile",
                                   "reactions-thinking_face",
                                   "reactions-heart",
                                   "reactions-tada",
                                   "interactions",
                                   "created",
                                   "updated"
                               ]
                           }
                       },
                       "required": [
                           "q"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Search for users on GitHub",
                   "name": "search_users",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "q": {
                               "type": "string"
                           },
                           "order": {
                               "type": "string",
                               "enum": [
                                   "asc",
                                   "desc"
                               ]
                           },
                           "page": {
                               "type": "number",
                               "minimum": 1
                           },
                           "per_page": {
                               "type": "number",
                               "minimum": 1,
                               "maximum": 100
                           },
                           "sort": {
                               "type": "string",
                               "enum": [
                                   "followers",
                                   "repositories",
                                   "joined"
                               ]
                           }
                       },
                       "required": [
                           "q"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Get details of a specific issue in a GitHub repository.",
                   "name": "get_issue",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string"
                           },
                           "repo": {
                               "type": "string"
                           },
                           "issue_number": {
                               "type": "number"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "issue_number"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Get details of a specific pull request",
                   "name": "get_pull_request",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "pull_number": {
                               "type": "number",
                               "description": "Pull request number"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "pull_number"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "List and filter repository pull requests",
                   "name": "list_pull_requests",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "state": {
                               "type": "string",
                               "enum": [
                                   "open",
                                   "closed",
                                   "all"
                               ],
                               "description": "State of the pull requests to return"
                           },
                           "head": {
                               "type": "string",
                               "description": "Filter by head user or head organization and branch name"
                           },
                           "base": {
                               "type": "string",
                               "description": "Filter by base branch name"
                           },
                           "sort": {
                               "type": "string",
                               "enum": [
                                   "created",
                                   "updated",
                                   "popularity",
                                   "long-running"
                               ],
                               "description": "What to sort results by"
                           },
                           "direction": {
                               "type": "string",
                               "enum": [
                                   "asc",
                                   "desc"
                               ],
                               "description": "The direction of the sort"
                           },
                           "per_page": {
                               "type": "number",
                               "description": "Results per page (max 100)"
                           },
                           "page": {
                               "type": "number",
                               "description": "Page number of the results"
                           }
                       },
                       "required": [
                           "owner",
                           "repo"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Create a review on a pull request",
                   "name": "create_pull_request_review",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "pull_number": {
                               "type": "number",
                               "description": "Pull request number"
                           },
                           "commit_id": {
                               "type": "string",
                               "description": "The SHA of the commit that needs a review"
                           },
                           "body": {
                               "type": "string",
                               "description": "The body text of the review"
                           },
                           "event": {
                               "type": "string",
                               "enum": [
                                   "APPROVE",
                                   "REQUEST_CHANGES",
                                   "COMMENT"
                               ],
                               "description": "The review action to perform"
                           },
                           "comments": {
                               "type": "array",
                               "items": {
                                   "anyOf": [
                                       {
                                           "type": "object",
                                           "properties": {
                                               "path": {
                                                   "type": "string",
                                                   "description": "The relative path to the file being commented on"
                                               },
                                               "position": {
                                                   "type": "number",
                                                   "description": "The position in the diff where you want to add a review comment"
                                               },
                                               "body": {
                                                   "type": "string",
                                                   "description": "Text of the review comment"
                                               }
                                           },
                                           "required": [
                                               "path",
                                               "position",
                                               "body"
                                           ],
                                           "additionalProperties": false
                                       },
                                       {
                                           "type": "object",
                                           "properties": {
                                               "path": {
                                                   "type": "string",
                                                   "description": "The relative path to the file being commented on"
                                               },
                                               "line": {
                                                   "type": "number",
                                                   "description": "The line number in the file where you want to add a review comment"
                                               },
                                               "body": {
                                                   "type": "string",
                                                   "description": "Text of the review comment"
                                               }
                                           },
                                           "required": [
                                               "path",
                                               "line",
                                               "body"
                                           ],
                                           "additionalProperties": false
                                       }
                                   ]
                               },
                               "description": "Comments to post as part of the review (specify either position or line, not both)"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "pull_number",
                           "body",
                           "event"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Merge a pull request",
                   "name": "merge_pull_request",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "pull_number": {
                               "type": "number",
                               "description": "Pull request number"
                           },
                           "commit_title": {
                               "type": "string",
                               "description": "Title for the automatic commit message"
                           },
                           "commit_message": {
                               "type": "string",
                               "description": "Extra detail to append to automatic commit message"
                           },
                           "merge_method": {
                               "type": "string",
                               "enum": [
                                   "merge",
                                   "squash",
                                   "rebase"
                               ],
                               "description": "Merge method to use"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "pull_number"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Get the list of files changed in a pull request",
                   "name": "get_pull_request_files",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "pull_number": {
                               "type": "number",
                               "description": "Pull request number"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "pull_number"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Get the combined status of all status checks for a pull request",
                   "name": "get_pull_request_status",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "pull_number": {
                               "type": "number",
                               "description": "Pull request number"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "pull_number"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Update a pull request branch with the latest changes from the base branch",
                   "name": "update_pull_request_branch",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "pull_number": {
                               "type": "number",
                               "description": "Pull request number"
                           },
                           "expected_head_sha": {
                               "type": "string",
                               "description": "The expected SHA of the pull request's HEAD ref"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "pull_number"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Get the review comments on a pull request",
                   "name": "get_pull_request_comments",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "pull_number": {
                               "type": "number",
                               "description": "Pull request number"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "pull_number"
                       ]
                   }
               }
           },
           {
               "type": "function",
               "function": {
                   "description": "Get the reviews on a pull request",
                   "name": "get_pull_request_reviews",
                   "parameters": {
                       "additionalProperties": false,
                       "type": "object",
                       "properties": {
                           "owner": {
                               "type": "string",
                               "description": "Repository owner (username or organization)"
                           },
                           "repo": {
                               "type": "string",
                               "description": "Repository name"
                           },
                           "pull_number": {
                               "type": "number",
                               "description": "Pull request number"
                           }
                       },
                       "required": [
                           "owner",
                           "repo",
                           "pull_number"
                       ]
                   }
               }
           }
       ]
   }
   `````

 ###出参示例

   ``````
   {
       "choices": [
           {
               "content_filter_results": {},
               "finish_reason": "tool_calls",
               "index": 0,
               "logprobs": null,
               "message": {
                   "content": null,
                   "refusal": null,
                   "role": "assistant",
                   "tool_calls": [
                       {
                           "function": {
                               "arguments": "{\"query\":\"user:chaozai0304\"}",
                               "name": "search_repositories"
                           },
                           "id": "call_2EgYitXdIMMykij9S5M0gRz6",
                           "type": "function"
                       }
                   ]
               }
           }
       ],
       "created": 1744696413,
       "id": "chatcmpl-BMTWfxTMiqoJFie4RuxDfx0c7eXOw",
       "model": "gpt-4o-2024-11-20",
       "object": "chat.completion",
       "prompt_filter_results": [
           {
               "prompt_index": 0,
               "content_filter_results": {
                   "hate": {
                       "filtered": false,
                       "severity": "safe"
                   },
                   "jailbreak": {
                       "filtered": false,
                       "detected": false
                   },
                   "self_harm": {
                       "filtered": false,
                       "severity": "safe"
                   },
                   "sexual": {
                       "filtered": false,
                       "severity": "safe"
                   },
                   "violence": {
                       "filtered": false,
                       "severity": "safe"
                   }
               }
           }
       ],
       "system_fingerprint": "fp_ee1d74bde0",
       "usage": {
           "completion_tokens": 23,
           "completion_tokens_details": {
               "accepted_prediction_tokens": 0,
               "audio_tokens": 0,
               "reasoning_tokens": 0,
               "rejected_prediction_tokens": 0
           },
           "prompt_tokens": 2012,
           "prompt_tokens_details": {
               "audio_tokens": 0,
               "cached_tokens": 1920
           },
           "total_tokens": 2035
       }
   }
   ``````



##2.本地启动

``````
 sudo java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005  mcp-0.0.1-SNAPSHOT.jar
``````

##3.调用

```````
http://localhost:8080/chat/github?input=%E6%9F%A5%E8%AF%A2chaozai0304%E6%89%80%E6%9C%89%E9%A1%B9
```````

