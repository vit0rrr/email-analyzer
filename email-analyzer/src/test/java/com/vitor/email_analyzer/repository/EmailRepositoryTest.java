package com.vitor.email_analyzer.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import com.vitor.email_analyzer.model.EmailAnalise;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EmailRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmailRepository emailRepository;

    @Test
    void deveBuscarEmailsPorClassificacao() {
        EmailAnalise analise = new EmailAnalise();
        analise.setRemetente("banco@fake.com");
        analise.setAssunto("Conta bloqueada");
        analise.setCorpo("Clique aqui");
        analise.setClassificacao("PHISHING");
        analise.setExplicacao("Domínio suspeito");
        analise.setDataAnalise(LocalDateTime.now());
        entityManager.persistAndFlush(analise);

        List<EmailAnalise> resultado = 
            emailRepository.findByClassificacao("PHISHING");

        assertFalse(resultado.isEmpty());
        assertEquals("PHISHING", resultado.get(0).getClassificacao());
    }

    @Test
    void deveBuscarEmailsPorRemetente() {
        EmailAnalise analise = new EmailAnalise();
        analise.setRemetente("colega@empresa.com");
        analise.setAssunto("Reunião");
        analise.setCorpo("Podemos marcar?");
        analise.setClassificacao("NORMAL");
        analise.setExplicacao("E-mail comum");
        analise.setDataAnalise(LocalDateTime.now());
        entityManager.persistAndFlush(analise);

        List<EmailAnalise> resultado = 
            emailRepository.findByRemetente("colega@empresa.com");

        assertFalse(resultado.isEmpty());
        assertEquals("colega@empresa.com", resultado.get(0).getRemetente());
    }
}


