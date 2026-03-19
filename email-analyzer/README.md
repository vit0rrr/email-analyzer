# Email Analyzer 📧

API REST para análise e classificação automática de e-mails usando Inteligência Artificial.

## Sobre o projeto

O Email Analyzer recebe um e-mail, analisa o conteúdo usando IA via AWS Bedrock e classifica automaticamente em quatro categorias, além de gerar uma resposta sugerida quando aplicável.

## Classificações

| Classificação | Descrição |
|---|---|
| 🔴 PHISHING | Tentativa de golpe ou fraude |
| 🟠 URGENTE | Requer atenção imediata |
| 🟡 SPAM | Propaganda indesejada |
| 🟢 NORMAL | E-mail comum de trabalho |

## Tecnologias

- Java 21
- Spring Boot 3.5.11
- PostgreSQL 16
- AWS Bedrock (Amazon Nova Lite)
- Docker
- Maven

## Pré-requisitos

- Java 21
- Docker Desktop
- Conta AWS com acesso ao Bedrock
- AWS CLI configurada

## Como rodar

**1. Clone o repositório**
```bash
git clone https://github.com/vit0rrr/email-analyzer.git
cd email-analyzer
```

**2. Suba o banco de dados**
```bash
docker-compose up -d
```

**3. Crie o banco**
```bash
docker exec -it email-analyzer-db psql -U postgres -c "CREATE DATABASE email_analyzer;"
```

**4. Configure as credenciais AWS**
```bash
aws configure
```

**5. Rode o projeto**
```bash
./mvnw spring-boot:run
```

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/emails/analyze` | Analisa um e-mail |
| GET | `/api/emails` | Lista todas as análises |
| GET | `/api/emails/classificacao/{classificacao}` | Filtra por classificação |
| GET | `/api/emails/buscar/{id}` | Busca por ID |

## Exemplo de uso

**Requisição:**
```json
{
    "remetente": "banco@bradesc0.com",
    "assunto": "Sua conta foi bloqueada",
    "corpo": "Clique aqui para desbloquear sua conta"
}
```

**Resposta:**
```json
{
    "id": 1,
    "remetente": "banco@bradesc0.com",
    "assunto": "Sua conta foi bloqueada",
    "classificacao": "PHISHING",
    "explicacao": "Domínio suspeito com zero no lugar do o",
    "respostaSugerida": null,
    "dataAnalise": "2026-03-19T18:00:00"
}
```

## Testes
```bash
./mvnw test
```