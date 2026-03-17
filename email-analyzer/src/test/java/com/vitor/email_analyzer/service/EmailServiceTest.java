package com.vitor.email_analyzer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vitor.email_analyzer.dto.EmailRequest;
import com.vitor.email_analyzer.model.EmailAnalise;
import com.vitor.email_analyzer.repository.EmailRepository;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private BedrockService bedrockService;

    @InjectMocks
    private EmailService emailService;

    @Test
    void deveClassificarEmailComoPhishing() {
        EmailRequest request = new EmailRequest();
        request.setRemetente("banco@bradesc0.com");
        request.setAssunto("Sua conta foi bloqueada");
        request.setCorpo("Clique aqui para desbloquear");

        String respostaIA = """
                {
                    "classificacao": "PHISHING",
                    "respostaSugerida": null,
                    "explicacao": "Domínio suspeito"
                }
                """;

        EmailAnalise analiseEsperada = new EmailAnalise();
        analiseEsperada.setClassificacao("PHISHING");

        when(bedrockService.analisarEmail(any(), any(), any()))
                .thenReturn(respostaIA);
        when(emailRepository.save(any()))
                .thenReturn(analiseEsperada);

        EmailAnalise resultado = emailService.analisarEmail(request);

        assertEquals("PHISHING", resultado.getClassificacao());
    }

    @Test
    void deveClassificarEmailComoNormal() {
        EmailRequest request = new EmailRequest();
        request.setRemetente("colega@empresa.com");
        request.setAssunto("Reunião amanhã");
        request.setCorpo("Podemos marcar para as 10h?");

        String respostaIA = """
                {
                    "classificacao": "NORMAL",
                    "respostaSugerida": "Claro, estarei disponível às 10h.",
                    "explicacao": "E-mail comum de trabalho"
                }
                """;

        EmailAnalise analiseEsperada = new EmailAnalise();
        analiseEsperada.setClassificacao("NORMAL");
        analiseEsperada.setRespostaSugerida("Claro, estarei disponível às 10h.");

        when(bedrockService.analisarEmail(any(), any(), any()))
                .thenReturn(respostaIA);
        when(emailRepository.save(any()))
                .thenReturn(analiseEsperada);

        EmailAnalise resultado = emailService.analisarEmail(request);

        assertEquals("NORMAL", resultado.getClassificacao());
        assertNotNull(resultado.getRespostaSugerida());
    }
}