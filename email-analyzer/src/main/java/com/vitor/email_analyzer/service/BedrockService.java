package com.vitor.email_analyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.List;
import java.util.Map;

@Service
public class BedrockService {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.bedrock.model-id}")
    private String modelId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analisarEmail(String remetente, String assunto, String corpo) {
        try {
            BedrockRuntimeClient client = BedrockRuntimeClient.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();

            String prompt = construirPrompt(remetente, assunto, corpo);

            Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of("role", "user", "content", List.of(
                                Map.of("text", prompt)
                        ))
                ),
                "inferenceConfig", Map.of(
                        "maxTokens", 1024,
                        "temperature", 0.3
                )
        );
            String requestJson = objectMapper.writeValueAsString(requestBody);

            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId(modelId)
                    .body(SdkBytes.fromUtf8String(requestJson))
                    .build();

            InvokeModelResponse response = client.invokeModel(request);
            String responseBody = response.body().asUtf8String();

            Map<?, ?> responseMap = objectMapper.readValue(responseBody, Map.class);
            Map<?, ?> output = (Map<?, ?>) responseMap.get("output");
            Map<?, ?> message = (Map<?, ?>) output.get("message");
            List<?> contentList = (List<?>) message.get("content");
            Map<?, ?> contentMap = (Map<?, ?>) contentList.get(0);

            return (String) contentMap.get("text");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar o Bedrock: " + e.getMessage(), e);
        }
    }

    private String construirPrompt(String remetente, String assunto, String corpo) {
        return """
                Você é um especialista em segurança de e-mails.
                Analise o e-mail abaixo e responda EXATAMENTE neste formato JSON:
                
                {
                    "classificacao": "URGENTE ou NORMAL ou SPAM ou PHISHING",
                    "respostaSugerida": "sua resposta sugerida aqui ou null se for SPAM/PHISHING",
                    "explicacao": "explique brevemente o motivo da classificação"
                }
                
                E-mail para análise:
                Remetente: %s
                Assunto: %s
                Corpo: %s
                
                Responda APENAS com o JSON, sem texto adicional.
                """.formatted(remetente, assunto, corpo);
    }
}