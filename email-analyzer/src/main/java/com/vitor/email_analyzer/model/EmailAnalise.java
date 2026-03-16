package com.vitor.email_analyzer.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "email_analise")
public class EmailAnalise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String remetente;

    @Column(nullable = false)
    private String assunto;

    @Column(columnDefinition = "TEXT")
    private String corpo;

    @Column(nullable = false)
    private String classificacao;

    @Column(columnDefinition = "TEXT")
    private String RespostaSugerida;

    @Column(columnDefinition = "TEXT")
    private String explicacao;

    @Column(nullable = false)
    private LocalDateTime dataAnalise;




}
