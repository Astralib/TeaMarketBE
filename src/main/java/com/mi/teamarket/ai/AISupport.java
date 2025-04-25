package com.mi.teamarket.ai;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

import java.io.IOException;
import java.util.Arrays;

public class AISupport {
    private static String getApiKey() {
        // 从项目路径 ./src/main/java/com/mi/teamarket/apikey.txt 读取所有内容并返回
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("./src/main/java/com/mi/teamarket/apikey.txt");
            return java.nio.file.Files.readString(path);
        } catch (IOException e) {
            return "API Key 未配置，请在项目路径./src/main/java/com/mi/teamarket/apikey.txt 中配置";
        }
    }

    private static GenerationResult callWithMessage(String content) throws ApiException, NoApiKeyException, InputRequiredException, IOException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(content)
                .build();
        String apiKey = getApiKey();
        if (apiKey.startsWith("sk-")) {
            System.out.println("apiKey is " + apiKey);
        }
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用阿里云百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(apiKey)
                // 模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }

    public static String summarize(String txt) {
        String returnStr = "AI 服务遇到错误，请稍后再试。";
        String endWith = """
                
                请为我详细总结上文，话语需要简洁，不要说废话，需要进行分段。不超过200字。
                """;
        try {
            GenerationResult result = callWithMessage(txt + endWith);
            returnStr = result.getOutput().getChoices().getFirst().getMessage().getContent();
        } catch (ApiException | NoApiKeyException | InputRequiredException | IOException e) {
            System.err.println("错误信息：" + e.getMessage());
        }
        return returnStr;
    }

}

