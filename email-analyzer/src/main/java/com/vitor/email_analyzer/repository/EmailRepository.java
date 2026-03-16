package com.vitor.email_analyzer.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vitor.email_analyzer.model.EmailAnalise;


@Repository
public interface EmailRepository extends JpaRepository<EmailAnalise, Long> {

    List<EmailAnalise> findByRemetente(String remetente);

    List<EmailAnalise> findByClassificacao(String classificacao);

}