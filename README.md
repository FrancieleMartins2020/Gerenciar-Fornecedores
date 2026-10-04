# 📦 Gerenciar Fornecedores e Contratos
> **Projeto desenvolvido para o Exame de Suficiência**  
> **Disciplina:** Desenvolvimento Web Back End 2 (TSIN34B)  
> **Curso:** Superior de Tecnologia em Sistemas para Internet — UTFPR Campus Toledo

<p align="center">
  <img src="https://shields.io" alt="Repo Size">
  <img src="https://shields.io" alt="Languages Count">
  <img src="https://shields.io" alt="Last Commit">
</p>

<p align="center">
  <a href="#-sobre-o-projeto">Sobre</a> •
  <a href="#-requisitos-atendidos">Requisitos</a> •
  <a href="#-tecnologias">Tecnologias</a> •
  <a href="#-arquitetura-mvc">Arquitetura</a> •
  <a href="#-modelagem-do-banco">Banco de Dados</a> •
  <a href="#-como-executar">Como Executar</a>
</p>

---

## 💻 Sobre o Projeto

Este sistema foi desenvolvido como parte do processo de avaliação de suficiência da UTFPR. O objetivo principal é fornecer a uma empresa local uma solução web eficiente para gerenciar seu ecossistema de parceiros comerciais, unificando o cadastro de **Fornecedores** e o controle de seus respectivos **Contratos Firmados** (produtos/serviços, valores e prazos).

---

## ⚙️ Requisitos Atendidos (Escopo do Exame)

A API foi projetada e implementada para cobrir 100% das regras de negócio solicitadas:

- [x] **Cadastro Completo (CRUD)**: Permite a inserção, alteração, remoção e listagem de fornecedores e contratos.
- [x] **Vínculo de Contratos**: Registro de informações detalhadas dos acordos comerciais (serviços/produtos negociados, valores e datas) associados a um fornecedor.
- [x] **Busca Customizada**: Endpoint específico para a localização imediata de um registro através do número do **CNPJ** do fornecedor.
- [x] **Listagem Consolidada**: Exibição transparente de fornecedores vinculados aos seus respectivos contratos.

---

## 🛠️ Tecnologias Utilizadas

Cumprindo as diretrizes de adoção de frameworks de mercado e persistência robusta:

- **Linguagem:** Java
- **Framework Web:** Spring Boot (Spring Web)
- **Persistência de Dados:** Spring Data JPA / Hibernate
- **Banco de Dados Relacional:** MySQL
- **Gerenciador de Dependências:** Maven

---

## 📐 Arquitetura do Projeto (Padrão MVC)

O projeto adota estritamente os princípios de **separação em camadas lógicas (MVC)** para garantir manutenibilidade e alta qualidade de software:

* **`Controller`**: Camada que expõe os endpoints REST e manipula as requisições HTTP da aplicação Web.
* **`Service`**: Contém as regras de negócio do sistema (validações de CNPJ, cálculos ou restrições de remoção).
* **`Repository`**: Interfaces que estendem o `JpaRepository`, responsáveis pelas operações de persistência direta no MySQL.
* **`Model / Entity`**: Classes Java que representam as tabelas e o mapeamento objeto-relacional (ORM).

---

## 🗄️ Modelagem do Banco de Dados

O banco de dados relacional conta com o mapeamento entre duas entidades principais com relacionamento **1:N (Um para Muitos)**:
1. **Fornecedor**: `id`, `nome`, `cnpj`, `telefone`, `email`.
2. **Contrato**: `id`, `produto_servico`, `valor`, `data_inicio`, `data_fim`, `fornecedor_id` (Chave Estrangeira).

---

## 🚀 Como Executar e Apresentar

### Pré-requisitos
- Java JDK 17 ou superior instalado.
- MySQL Server ativo localmente.

### 1. Preparação do Ambiente
Crie o schema no seu MySQL:
```sql
CREATE DATABASE gerenciar_fornecedores;
```
*(Certifique-se de validar as credenciais de `username` e `password` do banco dentro do arquivo `src/main/resources/application.properties` ou `.yml` antes de subir o app).*

### 2. Execução da API
No diretório raiz da aplicação, utilize os comandos do Maven Wrapper inclusos no repositório:

```bash
# No Windows:
\$ mvnw.cmd spring-boot:run

# No Linux/Mac:
\$ ./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 👤 Autor

Desenvolvido por **Franciele Martins**.  
*Acadêmica de Sistemas para Internet — UTFPR Toledo.*

<a href="https://github.com/FrancieleMartins2020">
  <img src="https://shields.io" alt="GitHub Profile">
</a>
