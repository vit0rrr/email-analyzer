package com.vitor.email_analyzer.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String remetente;
    private String assunto;
    private String corpo;
}
