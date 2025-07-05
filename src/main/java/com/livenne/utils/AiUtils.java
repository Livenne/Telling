package com.livenne.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AiUtils {
    private static final String API_URL = "https://api.suanli.cn/v1/chat/completions";
    private static final String API_KEY = "sk-W0rpStc95T7JVYVwDYc29IyirjtpPPby6SozFMQr17m8KWeo";

    // 更简洁的提示词模板
    private static final String PROMPT_TEMPLATE =
            "请将以下文本情感化丰富，只输出最终结果，不要包含任何解释、思考过程或选项列表。\n" +
                    "要求：保持原意，使用更生动形象的语言表达，但不要改变核心意思。\n" +
                    "只需输出改写后的文本。\n" +
                    "原文：\"%s\"";

    public static String enrichTextEmotionally(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return inputText;
        }

        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);  // 15秒连接超时
            connection.setReadTimeout(45000);     // 45秒读取超时

            // 2. 构建请求体
            String prompt = String.format(PROMPT_TEMPLATE, escapeJsonString(inputText));
            String jsonBody = String.format(
                    "{" +
                            "\"model\":\"free:QwQ-32B\"," +
                            "\"messages\":[{\"role\":\"user\"," +
                            "\"content\":\"%s\"}]}",
                    escapeJsonString(prompt)
            );


            // 3. 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 4. 获取响应状态
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                System.err.println("API错误: " + statusCode);
                return inputText;
            }

            // 5. 读取响应内容
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
            }

            System.out.println("API原始响应: " + response.toString());

            // 6. 提取AI回复内容
            String rawContent = extractContent(response.toString());
            System.out.println("提取的内容: " + rawContent);

            // 7. 清理响应内容
            return cleanResponse(rawContent);

        } catch (Exception e) {
            System.err.println("API异常: " + e.getMessage());
            return inputText;
        }
    }

    // 更健壮的JSON转义方法
    private static String escapeJsonString(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // 提取AI回复内容
    private static String extractContent(String jsonResponse) {
        // 查找content字段
        int contentStart = jsonResponse.indexOf("\"content\":\"");
        if (contentStart == -1) {
            return "未找到内容字段";
        }
        contentStart += 11; // 跳过"content":"

        // 查找内容结束位置
        int contentEnd = contentStart;
        int quoteCount = 0;
        while (contentEnd < jsonResponse.length()) {
            char c = jsonResponse.charAt(contentEnd);

            // 处理转义引号
            if (c == '\\' && contentEnd + 1 < jsonResponse.length() &&
                    jsonResponse.charAt(contentEnd + 1) == '"') {
                contentEnd += 2; // 跳过转义字符和引号
                continue;
            }

            // 找到非转义的结束引号
            if (c == '"') {
                break;
            }

            contentEnd++;
        }

        if (contentEnd > contentStart && contentEnd < jsonResponse.length()) {
            return jsonResponse.substring(contentStart, contentEnd);
        }

        return "无法提取内容";
    }

    // 清理响应内容
    private static String cleanResponse(String response) {
        // 1. 移除XML标签
        response = response.replaceAll("<[^>]+>", "");

        // 2. 查找最终结果位置
        int resultStart = response.indexOf("### 改写结果");
        if (resultStart != -1) {
            response = response.substring(resultStart + "### 改写结果".length());
        }

        // 3. 移除所有换行符和多余空格
        response = response.replaceAll("\\s+", " ").trim();

        // 4. 移除可能的开头引号
        if (response.startsWith("\"")) {
            response = response.substring(1);
        }

        // 5. 移除可能的结尾引号
        if (response.endsWith("\"")) {
            response = response.substring(0, response.length() - 1);
        }

        // 6. 如果包含思考过程，只取最后一部分
        if (response.contains("思考过程") || response.contains("think")) {
            int lastPartIndex = Math.max(
                    response.lastIndexOf("。"),
                    response.lastIndexOf("！")
            );
            if (lastPartIndex != -1) {
                response = response.substring(lastPartIndex + 1).trim();
            }
        }

        // 7. 如果仍然很长，取第一句
        if (response.length() > 100) {
            int endIndex = Math.max(
                    response.indexOf("。"),
                    response.indexOf("！")
            );
            if (endIndex != -1) {
                response = response.substring(0, endIndex + 1);
            } else {
                // 找不到句号，取前100个字符
                response = response.substring(0, Math.min(100, response.length()));
            }
        }

        return response.trim();
    }
}