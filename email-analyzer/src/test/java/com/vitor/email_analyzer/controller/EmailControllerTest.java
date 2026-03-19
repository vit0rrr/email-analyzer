package com.vitor.email_analyzer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitor.email_analyzer.dto.EmailRequest;
import com.vitor.email_analyzer.model.EmailAnalise;
import com.vitor.email_analyzer.repository.EmailRepository;
import com.vitor.email_analyzer.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private EmailRepository emailRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornar200QuandoAnalisarEmail() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setRemetente("banco@bradesc0.com");
        request.setAssunto("Sua conta foi bloqueada");
        request.setCorpo("Clique aqui para desbloquear");

        EmailAnalise analise = new EmailAnalise();
        analise.setId(1L);
        analise.setRemetente("banco@bradesc0.com");
        analise.setAssunto("Sua conta foi bloqueada");
        analise.setCorpo("Clique aqui para desbloquear");
        analise.setClassificacao("PHISHING");
        analise.setExplicacao("Domínio suspeito");
        analise.setRespostaSugerida(null);
        analise.setDataAnalise(LocalDateTime.now());

        when(emailService.analisarEmail(any()))
                .thenReturn(analise);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emails/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classificacao").value("PHISHING"))
                .andExpect(jsonPath("$.remetente").value("banco@bradesc0.com"));
    }

    @Test
    void deveRetornar200QuandoListarTodos() throws Exception {
        mockMvc.perform(get("/api/emails"))
                .andExpect(status().isOk());
    }
}