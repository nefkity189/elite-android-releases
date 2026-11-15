#!/usr/bin/env python3
"""
Script de teste rapido para o jev-local (porta 8000).
Envia 3 mensagens simuladas: normal, urgente e suspeita de golpe.
"""
import json
import requests

BASE_URL = "http://127.0.0.1:8000/v1/systemone"

# 3 mensagens de teste
TEST_MESSAGES = [
    {
        "nome": "NORMAL",
        "texto": "Oi, tudo bem? Gostaria de saber o horario de funcionamento da loja no sabado."
    },
    {
        "nome": "URGENTE",
        "texto": "SOCORRO! Minha mae esta passando mal, precisa de ambulancia AGORA! Endeco: Rua das Flores 123, Centro."
    },
    {
        "nome": "SUSPEITA_GOLPE",
        "texto": "Parabens! Voce foi selecionado para ganhar R$ 50.000,00! Clique no link agora: http://bit.ly/golpe-falso e confirme seus dados bancarios para receber o premio hoje mesmo!"
    }
]

# Perguntas de decisao (conforme formato obrigatorio do CLAUDE.md)
QUESTIONS = {
    "is_urgent": {
        "type": "noul",
        "instructions": "Esta mensagem indica uma situacao de emergencia ou urgencia medica/seguranca que requer acao imediata?"
    },
    "is_scam": {
        "type": "noul",
        "instructions": "Esta mensagem parece ser uma tentativa de golpe, fraude, phishing ou engenharia social?"
    },
    "priority": {
        "type": "choice",
        "instructions": "Qual a prioridade de atendimento para esta mensagem?",
        "choices": ["baixa", "media", "alta", "critica"],
        "criteria": {
            "baixa": "Mensagem rotineira, pode aguardar",
            "media": "Precisa de resposta mas nao e emergencia",
            "alta": "Urgente, precisa de atencao rapida",
            "critica": "Emergencia real, risco de vida ou seguranca"
        }
    },
    "risk_score": {
        "type": "score",
        "instructions": "Qual o nivel de risco desta mensagem (0=seguro, 10=muito perigoso)?",
        "criteria": ["probabilidade de golpe", "urgencia real", "solicitacao de dados sensaveis", "links suspeitos"],
        "min": 0,
        "max": 10
    }
}

def send_request(state_text):
    payload = {
        "state": state_text,
        "model": "jev-latest",
        "questions": QUESTIONS
    }
    try:
        resp = requests.post(BASE_URL, json=payload, timeout=180)
        if resp.status_code == 200:
            return resp.json()
        else:
            return {"error": f"HTTP {resp.status_code}", "body": resp.text}
    except Exception as e:
        return {"error": str(e)}

def format_answer(key, value):
    if isinstance(value, dict):
        ans_type = value.get("type", "?")
        if ans_type == "noul":
            val = value.get("noul", "?")
            return f"  {key}: [{ans_type}] {val} (1.0=Sim, 0.0=Nao)"
        elif ans_type == "score":
            score_val = value.get("score", "?")
            conf = value.get("confidence", "?")
            return f"  {key}: [{ans_type}] {score_val}/10 (confianca={conf})"
        elif ans_type == "choice":
            choice_val = value.get("choice", "?")
            conf = value.get("confidence", "?")
            return f"  {key}: [{ans_type}] {choice_val} (confianca={conf})"
        else:
            return f"  {key}: {value}"
    else:
        return f"  {key}: {value}"

def main():
    print("=" * 70)
    print("TESTE JEV-LOCAL - 3 Mensagens Simuladas")
    print("=" * 70)
    print(f"Endpoint: {BASE_URL}")
    print()

    for msg in TEST_MESSAGES:
        print(f"\n{'='*70}")
        print(f"MENSAGEM: {msg['nome']}")
        print(f"{'='*70}")
        print(f"Texto: {msg['texto'][:100]}{'...' if len(msg['texto']) > 100 else ''}")
        print("-" * 70)

        result = send_request(msg["texto"])

        if "error" in result:
            print(f"[ERRO] {result['error']}")
            if "body" in result:
                print(f"   Detalhes: {result['body']}")
        else:
            answers = result.get("answers", {})
            for key, value in answers.items():
                print(format_answer(key, value))
            usage = result.get("usage", {})
            if usage:
                print(f"  [tokens] input={usage.get('input_tokens', '?')}, output={usage.get('output_tokens', '?')}")

    print("\n" + "=" * 70)
    print("TESTE CONCLUIDO")
    print("=" * 70)

if __name__ == "__main__":
    main()