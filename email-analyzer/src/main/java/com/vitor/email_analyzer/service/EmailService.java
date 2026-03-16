package com.vitor.email_analyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitor.email_analyzer.dto.EmailRequest;
import com.vitor.email_analyzer.model.EmailAnalise;
import com.vitor.email_analyzer.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final BedrockService bedrockService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmailAnalise analisarEmail(EmailRequest request) {
        try {
            String respostaIA = bedrockService.analisarEmail(
                    request.getRemetente(),
                    request.getAssunto(),
                    request.getCorpo()
            );

            String jsonLimpo = respostaIA
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            Map<?, ?> resultado = objectMapper.readValue(jsonLimpo, Map.class);

            EmailAnalise analise = new EmailAnalise();
            analise.setRemetente(request.getRemetente());
            analise.setAssunto(request.getAssunto());
            analise.setCorpo(request.getCorpo());
            analise.setClassificacao((String) resultado.get("classificacao"));
            analise.setRespostaSugerida((String) resultado.get("respostaSugerida"));
            analise.setExplicacao((String) resultado.get("explicacao"));
            analise.setDataAnalise(LocalDateTime.now());

            return emailRepository.save(analise);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao analisar e-mail: " + e.getMessage(), e);
        }
    }
}
