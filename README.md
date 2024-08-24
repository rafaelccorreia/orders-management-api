# Aplicativo de Gerenciamento de Pedidos

## Visão Geral

Este projeto é um aplicativo de gerenciamento de pedidos desenvolvido com Spring Boot. Ele utiliza RabbitMQ para gerenciamento de mensagens e MySQL para persistência de dados. O projeto é containerizado com Docker para facilitar a configuração e a implantação.

## Funcionalidades

- **Gerenciamento de Pedidos**: Criação e gerenciamento de pedidos com diferentes status.
- **Gerenciamento de Produtos**: Adição e atualização de produtos com detalhes e preços.
- **Integração com RabbitMQ**: Comunicação assíncrona para processamento de pedidos.
- **Containerização com Docker**: Ambiente de desenvolvimento e produção facilitado.

## Pré-requisitos

- **Docker** e **Docker Compose** instalados em sua máquina.

## Configuração

### Clonar o Repositório

Clone o repositório e acesse o diretório do projeto:

```bash
git clone https://github.com/seu-repo/order-management-app.git
cd order-management-app
```

### Configuração do Ambiente

Crie um arquivo .env na raiz do projeto com as seguintes variáveis de ambiente:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/ordermanagementdb
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=userpassword
SPRING_RABBITMQ_HOST=rabbitmq
SPRING_RABBITMQ_USERNAME=rabbitmq
SPRING_RABBITMQ_PASSWORD=rabbitmq
```


### Construir e Execultar com Docker Compose

Para construir e iniciar os containers, use o comando:

```bash
docker-compose up --build
```

Este comando irá:

- Construir as imagens Docker.
- Iniciar os containers do MySQL, RabbitMQ e do aplicativo.


### Verificação dos Serviços

- MySQL: Disponível em localhost:3306.
- RabbitMQ: Console de gerenciamento em localhost:15672 (usuário: rabbitmq, senha: rabbitmq).
- Aplicativo: Disponível em localhost:8080.


## Verificações de Saúde
O Docker Compose garante que o MySQL e o RabbitMQ estejam prontos antes de iniciar o aplicativo. Verificações de saúde são realizadas para assegurar a disponibilidade dos serviços.


## Uso
- `Endpoints da API`: Consulte a Documentação da API para detalhes sobre os endpoints disponíveis: https://documenter.getpostman.com/view/21557009/2sAXjF8Evh
- `Esquema do Banco de Dados`: O esquema e os dados iniciais são configurados automaticamente pelo Docker Compose.


## Infraestrutura AWS
Para implantar a aplicação e suas dependências na AWS, siga os passos abaixo para configurar o Amazon ECS e o Amazon RDS:

### 1.  Configuração do Amazon RDS para MySQL
#### Criar uma Instância do RDS MySQL:

- Acesse o console do Amazon RDS.
- Clique em Create database (Criar banco de dados).
- Escolha Standard Create (Criação padrão).
- Selecione MySQL como o mecanismo de banco de dados.
-  Configure os detalhes da instância (versão, classe, armazenamento, etc.).
- Defina as credenciais de acesso (usuário e senha) e o nome do banco de dados como ordermanagementdb.
- Configure a VPC, sub-redes e grupos de segurança para permitir acesso ao banco de dados.
- Clique em Create database (Criar banco de dados).


#### Obter o Endpoint do RDS:

- Após a criação da instância, vá para a lista de instâncias RDS e selecione a instância criada.
- Na aba Connectivity & security (Conectividade e segurança), copie o Endpoint e a Port (Porta).


### 2. Configuração do Amazon ECS para a Aplicação
#### Criar um Repositório no Amazon ECR (Elastic Container Registry):

- Acesse o console do Amazon ECR.
- Clique em Create repository (Criar repositório).
- Dê um nome ao repositório, por exemplo, order-management-app.
- Clique em Create repository (Criar repositório).

#### Fazer o Upload da Imagem Docker para o ECR:

- Siga as instruções fornecidas pelo ECR para fazer login no repositório.
- Faça o push da imagem Docker do seu aplicativo para o repositório ECR.

#### Criar um Cluster no Amazon ECS:

- Acesse o console do Amazon ECS.
- Clique em Create cluster (Criar cluster).
- Escolha EC2 Linux + Networking ou Fargate (dependendo do tipo de serviço que você deseja usar).
- Configure o cluster e clique em Create (Criar).

#### Criar uma Tarefa e Definir o Serviço:

- No console do Amazon ECS, vá para a aba Task Definitions (Definições de tarefas).
- Clique em Create new Task Definition (Criar nova definição de tarefa).
- Escolha Fargate ou EC2, conforme o tipo de cluster.
- Configure a definição da tarefa, especificando o contêiner com a imagem do ECR e as variáveis de ambiente (incluindo detalhes de conexão do MySQL e RabbitMQ).
- Clique em Create (Criar).

#### Criar um Serviço no ECS:

- No console do ECS, vá para o cluster criado.
- Clique em Create (Criar) para criar um novo serviço.
- Escolha a definição de tarefa criada e configure o serviço (número de réplicas, tipo de balanceador de carga, etc.).
- Clique em Create (Criar).

### 3. Configuração de Segurança e Rede
####   Configurar Grupos de Segurança:

- No console do EC2, acesse Security Groups (Grupos de segurança).
- Configure regras de entrada e saída para permitir a comunicação entre o ECS e o RDS (por exemplo, permitindo tráfego na porta do MySQL).

#### Configurar Variáveis de Ambiente e Conexão:

- Atualize as variáveis de ambiente na definição da tarefa ECS para usar o endpoint e as credenciais do RDS.
- Certifique-se de que o ECS e o RDS estão na mesma VPC para garantir a conectividade.


### 4. Monitoramento e Escalabilidade
####   Configurar Monitoramento:

- Utilize o Amazon CloudWatch para monitorar logs e métricas da aplicação e dos serviços.
- Configure alarmes para notificações em caso de falhas ou problemas de desempenho.

#### Configurar Escalabilidade:

- Configure políticas de escalabilidade automática para o ECS, se necessário, para ajustar a capacidade conforme a demanda.


## Contato
Para dúvidas ou problemas, entre em contato com:
- Rafael Correia
- GitHub: https://github.com/rafaelccorreia
- Linkedin: https://www.linkedin.com/in/rafaelccorreia