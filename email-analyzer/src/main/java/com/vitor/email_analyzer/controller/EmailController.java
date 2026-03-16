package com.vitor.email_analyzer.controller;


import com.vitor.email_analyzer.dto.EmailRequest;
import com.vitor.email_analyzer.model.EmailAnalise;
import com.vitor.email_analyzer.repository.EmailRepository;
import com.vitor.email_analyzer.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {
    
    private final EmailService emailService;
    private final EmailRepository emailRepository;

    @PostMapping("/analyze")
    public ResponseEntity<EmailAnalise> analisarEmail(@RequestBody EmailRequest request) {
        EmailAnalise analise = emailService.analisarEmail(request);
        return ResponseEntity.ok(analise);
    }
    

    @GetMapping
    public ResponseEntity<List<EmailAnalise>> listatodos() {
        return ResponseEntity.ok(emailRepository.findAll());
    }
    

    @GetMapping("/classificacao/{classificacao}")
    public ResponseEntity<List<EmailAnalise>> listarPorClassificacao(@PathVariable String classificacao) {
        return ResponseEntity.ok(emailRepository.findByClassificacao(classificacao.toUpperCase()));
    }


     @GetMapping("/{id}")
    public ResponseEntity<EmailAnalise> buscarPorId(@PathVariable Long id) {
        return emailRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
