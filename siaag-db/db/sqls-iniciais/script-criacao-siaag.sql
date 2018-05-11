CREATE TABLE SIAAG.TAB_PERFIL
(
  ID        NUMBER(10)        NOT NULL
    CONSTRAINT PK_PERFIL
    PRIMARY KEY,
  DESCRICAO VARCHAR2(20 CHAR) NOT NULL
)
/

CREATE TABLE SIAAG.TAB_TIPO_BENEFICIARIO
(
  ID        NUMBER(10)        NOT NULL
    CONSTRAINT PK_TIPO_BENEFICIARIO
    PRIMARY KEY,
  DESCRICAO VARCHAR2(30 CHAR) NOT NULL
)
/

CREATE TABLE SIAAG.TAB_TIPO_REQUERENTE
(
  ID        NUMBER(10)        NOT NULL
    CONSTRAINT PK_TIPO_REQUERENTE
    PRIMARY KEY,
  DESCRICAO VARCHAR2(30 CHAR) NOT NULL
)
/

CREATE TABLE SIAAG.TAB_ENDERECO
(
  ID          NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_ENDERECO
    PRIMARY KEY,
  BAIRRO      VARCHAR2(50 CHAR)  DEFAULT NULL,
  CEP         VARCHAR2(8 CHAR)   DEFAULT NULL,
  CIDADE      VARCHAR2(100 CHAR) DEFAULT NULL,
  COMPLEMENTO VARCHAR2(200 CHAR),
  LOGRADOURO  VARCHAR2(200 CHAR) DEFAULT NULL,
  NUMERO      VARCHAR2(10 CHAR)  DEFAULT NULL,
  UF          VARCHAR2(2 CHAR)   DEFAULT NULL
)
/

CREATE TABLE SIAAG.TAB_PESSOA
(
  ID              NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_PESSOA
    PRIMARY KEY,
  CELULAR         VARCHAR2(11 CHAR) DEFAULT NULL,
  CPF             VARCHAR2(11 CHAR)  NOT NULL,
  DATA_NASCIMENTO DATE              DEFAULT NULL,
  EMAIL           VARCHAR2(150 CHAR),
  NOME            VARCHAR2(150 CHAR) NOT NULL,
  TELEFONE1       VARCHAR2(11),
  TELEFONE2       VARCHAR2(11),
  ID_ENDERECO     NUMBER(10)        DEFAULT NULL
)
/

CREATE UNIQUE INDEX SIAAG.UNQ_CPF
  ON SIAAG.TAB_PESSOA (CPF)
/

CREATE TABLE SIAAG.TAB_USUARIO
(
  ID                         NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_USUARIO
    PRIMARY KEY,
  ATIVO                      NUMBER(1) DEFAULT 1 NOT NULL,
  LOGIN                      VARCHAR2(11 CHAR)   NOT NULL,
  SENHA                      VARCHAR2(255 CHAR)  NOT NULL,
  ID_PERFIL                  NUMBER(10)          NOT NULL
    CONSTRAINT FK_USUARIO_PERFIL_ID_PERFIL
    REFERENCES TAB_PERFIL,
  ID_PESSOA                  NUMBER(10)          NOT NULL
    CONSTRAINT FK_USUARIO_PESSOA_ID_PESSOA
    REFERENCES TAB_PESSOA,
  PRIMEIRO_ACESSO            NUMBER(1) DEFAULT 0 NOT NULL,
  ID_PONTO_ATENDIMENTO_SETOR NUMBER(10),
  MATRICULA                  VARCHAR2(20)
)
/

CREATE UNIQUE INDEX SIAAG.UNQ_LOGIN
  ON SIAAG.TAB_USUARIO (LOGIN)
/

CREATE TABLE SIAAG.TAB_AGENDA
(
  ID                       NUMBER(10) generated as identity ( maxvalue 9999999999)
    constraint PK_AGENDA
    primary key,
  ID_USUARIO               NUMBER(10)           not null
    constraint FK_AGENDA_USUARIO_ID_USUARIO
    references TAB_USUARIO
    on delete cascade,
  DATA                     DATE                 not null,
  ATIVO                    NUMBER(1) default 1  not null,
  ATENDIMENTO_NAO_AGENDADO NUMBER(1) default 0  not null
)
/

CREATE TABLE SIAAG.TAB_AGENDA_ITEM
(
  ID                NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_AGENDA_ITEM
    PRIMARY KEY,
  ID_AGENDA         NUMBER(10)          NOT NULL
    CONSTRAINT FK_AGENDA_ITEM_AGENDA_ID
    REFERENCES TAB_AGENDA
    ON DELETE CASCADE,
  TIPO_AGENDAMENTO  VARCHAR2(15)        NOT NULL,
  ID_HORARIO_INICIO NUMBER(4)           NOT NULL,
  ID_HORARIO_FIM    NUMBER(4)           NOT NULL,
  ATIVO             NUMBER(1) DEFAULT 1 NOT NULL
)
/

CREATE TABLE SIAAG.TAB_HORARIO
(
  ID      NUMBER(4)           NOT NULL
    CONSTRAINT PK_HORARIO
    PRIMARY KEY,
  HORARIO TIMESTAMP(6)        NOT NULL,
  ATIVO   NUMBER(1) DEFAULT 1 NOT NULL
)
/

ALTER TABLE TAB_AGENDA_ITEM
  ADD CONSTRAINT SIAAG.FK_AGENDA_ITEM_HORARIO_INICIO
FOREIGN KEY (ID_HORARIO_INICIO) REFERENCES TAB_HORARIO
/

ALTER TABLE TAB_AGENDA_ITEM
  ADD CONSTRAINT SIAAG.FK_AGENDA_ITEM_HORARIO_FIM
FOREIGN KEY (ID_HORARIO_FIM) REFERENCES TAB_HORARIO
/

CREATE TABLE SIAAG.TAB_BLOQUEIO_AGENDA
(
  ID                NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_BLOQUEIO_AGENDA
    PRIMARY KEY,
  DATA_INICIO       TIMESTAMP(6) NOT NULL,
  DATA_FIM          TIMESTAMP(6) NOT NULL,
  ID_HORARIO_INICIO NUMBER(4)    NOT NULL
    CONSTRAINT FK_BLOQUEIO_AGENDA_HORARIO_INI
    REFERENCES TAB_HORARIO,
  ID_HORARIO_FIM    NUMBER(4)    NOT NULL
    CONSTRAINT FK_BLOQUEIO_AGENDA_HORARIO_FIM
    REFERENCES TAB_HORARIO,
  ID_USUARIO        VARCHAR2(20) NOT NULL
)
/

CREATE TABLE SIAAG.TAB_ATENDIMENTO
(
  ID                      NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_ATENDIMENTO
    PRIMARY KEY,
  ID_REQUERENTE           NUMBER(10)             NOT NULL
    CONSTRAINT FK_ATENDIMENTO_PESSOA_REQU
    REFERENCES TAB_PESSOA,
  ID_BENEFICIARIO         NUMBER                 NOT NULL
    CONSTRAINT FK_ATENDIMENTO_PESSOA_BENEF
    REFERENCES TAB_PESSOA,
  ID_TIPO_BENEFICIARIO    NUMBER(4)              NOT NULL
    CONSTRAINT FK_ATENDIMENTO_TIPO_BENEF
    REFERENCES TAB_TIPO_BENEFICIARIO,
  ID_PONTO_ATENDIMENTO    NUMBER(10)             NOT NULL,
  ID_TIPO_ATENDIMENTO     NUMBER(4)              NOT NULL,
  FORMA_ATENDIMENTO       VARCHAR2(15)           NOT NULL,
  PRIORIDADE_CONTATO      VARCHAR2(20)           NOT NULL,
  DATA_NAO_AGENDADO       TIMESTAMP(6) DEFAULT NULL,
  ID_SITUACAO_ATENDIMENTO NUMBER(2) DEFAULT NULL NOT NULL,
  ATIVO                   NUMBER(1) DEFAULT 1    NOT NULL,
  ID_TIPO_REQUERENTE      NUMBER(4)              NOT NULL
    CONSTRAINT FK_ATENDIMENTO_TIPO_REQUERENTE
    REFERENCES TAB_TIPO_REQUERENTE,
  ID_HORARIO_INICIO       NUMBER(4)    DEFAULT NULL

    CONSTRAINT FK_ATENDIMENTO_HORARIO
    REFERENCES TAB_HORARIO,
  ID_ATENDENTE            NUMBER(10)             NOT NULL
    CONSTRAINT TAB_ATENDIMENTO_FK1
    REFERENCES TAB_PESSOA,
  DATA                    DATE DEFAULT NULL      NOT NULL,
  ID_HORARIO_FIM          NUMBER(10)   DEFAULT NULL

    CONSTRAINT TAB_ATENDIMENTO_FK2
    REFERENCES TAB_HORARIO,
  ID_USUARIO_CADASTROU    NUMBER(10)             NOT NULL
    CONSTRAINT TAB_ATENDIMENTO_FK3
    REFERENCES TAB_USUARIO,
  ID_RESPONSAVEL          NUMBER(10)             NOT NULL
    CONSTRAINT FK_ATENDIMENTO_PESSOA_RESP
    REFERENCES TAB_PESSOA,
  SENHA                   NUMBER       DEFAULT NULL,
  OBSERVACAO              VARCHAR2(500),
  TEMPO_ATENDIMENTO       NUMBER(10),
  INFORMACOES_ATENDIMENTO VARCHAR2(500),
  PRIORITARIO             NUMBER(1) DEFAULT 0    NOT NULL,
  ALTERAR_DADOS           NUMBER(1) DEFAULT 0    NOT NULL
)
/

COMMENT ON COLUMN SIAAG.TAB_ATENDIMENTO.PRIORIDADE_CONTATO IS 'SMS, EMAIL, CORRESPONDENCIA'
/

COMMENT ON COLUMN SIAAG.TAB_ATENDIMENTO.SENHA IS 'Senha do atendimento.'
/

CREATE TABLE SIAAG.TAB_TIPO_ATENDIMENTO
(
  ID                NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_TIPO_ATENDIMENTO
    PRIMARY KEY,
  DESCRICAO         VARCHAR2(50)        NOT NULL,
  TEMPO_ATENDIMENTO NUMBER(2)           NOT NULL,
  TEMPO_ANALISE     NUMBER(2)           NOT NULL,
  REDIRECIONAMENTO  NUMBER(1) DEFAULT 0 NOT NULL,
  ATIVO             NUMBER(1) DEFAULT 1 NOT NULL
)
/

CREATE UNIQUE INDEX SIAAG.UNQ_DESCRICAO
  ON SIAAG.TAB_TIPO_ATENDIMENTO (DESCRICAO)
/

COMMENT ON COLUMN SIAAG.TAB_TIPO_ATENDIMENTO.TEMPO_ATENDIMENTO IS 'Em minutos'
/

COMMENT ON COLUMN SIAAG.TAB_TIPO_ATENDIMENTO.TEMPO_ANALISE IS 'Em minutos'
/

ALTER TABLE TAB_ATENDIMENTO
  ADD CONSTRAINT SIAAG.FK_ATENDIMENTO_TP_ATEND
FOREIGN KEY (ID_TIPO_ATENDIMENTO) REFERENCES TAB_TIPO_ATENDIMENTO
/

CREATE TABLE SIAAG.TAB_SITUACAO_ATENDIMENTO
(
  ID        NUMBER(2)    NOT NULL
    CONSTRAINT TAB_SITUACAO_ATENDIMENTO_PK
    PRIMARY KEY,
  DESCRICAO VARCHAR2(20) NOT NULL
)
/

ALTER TABLE TAB_ATENDIMENTO
  ADD CONSTRAINT SIAAG.FK_ATENDIMENTO_SITUACAO
FOREIGN KEY (ID_SITUACAO_ATENDIMENTO) REFERENCES TAB_SITUACAO_ATENDIMENTO
/

CREATE TABLE SIAAG.TAB_PONTO_ATENDIMENTO
(
  ID          NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_PONTO_ATENDIMENTO
    PRIMARY KEY,
  NOME        VARCHAR2(100) NOT NULL,
  ID_ENDERECO NUMBER(10)
    CONSTRAINT FK_PONTO_ATENDIMENTO_ENDERECO
    REFERENCES TAB_ENDERECO
)
/

CREATE UNIQUE INDEX SIAAG.UNQ_PONTO_ATENDIMENTO_NOME
  ON SIAAG.TAB_PONTO_ATENDIMENTO (NOME)
/

ALTER TABLE TAB_ATENDIMENTO
  ADD CONSTRAINT SIAAG.FK_ATENDIMENTO_PONTO_ATEND
FOREIGN KEY (ID_PONTO_ATENDIMENTO) REFERENCES TAB_PONTO_ATENDIMENTO
/

CREATE TABLE SIAAG.TAB_SETOR
(
  ID        NUMBER(4)    NOT NULL
    CONSTRAINT PK_TAB_SETOR
    PRIMARY KEY,
  DESCRICAO VARCHAR2(50) NOT NULL,
  SIGLA     VARCHAR2(15)
)
/

CREATE UNIQUE INDEX SIAAG.UNQ_SETOR_DESCRICAO
  ON SIAAG.TAB_SETOR (DESCRICAO)
/

CREATE TABLE SIAAG.TAB_PONTO_ATENDIMENTO_SETOR
(
  ID                   NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_TAB_PONTO_ATENDIMENTO_SETOR
    PRIMARY KEY,
  ID_PONTO_ATENDIMENTO NUMBER(4) NOT NULL
    CONSTRAINT FK_PONTO_ATENDIMENTO_SETOR_PAT
    REFERENCES TAB_PONTO_ATENDIMENTO,
  ID_SETOR             NUMBER(4) NOT NULL
    CONSTRAINT FK_PONTO_ATENDIMENTO_SETOR_SET
    REFERENCES TAB_SETOR
)
/

ALTER TABLE TAB_USUARIO
  ADD CONSTRAINT SIAAG.FK_USUARIO_PONTO_ATEN_SETOR
FOREIGN KEY (ID_PONTO_ATENDIMENTO_SETOR) REFERENCES TAB_PONTO_ATENDIMENTO_SETOR
/

CREATE TABLE SIAAG.TAB_ARQUIVO
(
  ID             NUMBER(10) GENERATED AS IDENTITY
    CONSTRAINT PK_ARQUIVO
    PRIMARY KEY,
  DESCRICAO      VARCHAR2(100) NOT NULL,
  ARQUIVO        BLOB          NOT NULL,
  ID_ATENDIMENTO NUMBER(10)    NOT NULL
    CONSTRAINT FK_ARQUIVO_ATENDIMENTO
    REFERENCES TAB_ATENDIMENTO
    ON DELETE CASCADE
)
/

CREATE OR REPLACE VIEW SIAAG.VW_DATAS_DISPONIVEIS AS
  SELECT
    AG.DATA,
    AGI.tipo_agendamento
  FROM siaag.tab_agenda ag
    JOIN siaag.tab_agenda_item agi ON ag.id = agi.ID_AGENDA
    LEFT JOIN siaag.TAB_BLOQUEIO_AGENDA ba
      ON ag.data BETWEEN ba.data_inicio AND ba.data_fim AND ag.id_usuario = ba.id_usuario
         AND (agi.id_horario_inicio >= ba.id_horario_inicio AND agi.id_horario_fim <= ba.id_horario_fim)
  WHERE ba.id IS NULL AND agi.ativo = 1 AND ag.ativo = 1 AND ag.data > (sysdate + 14)
  GROUP BY ag.data, agi.tipo_agendamento
  ORDER BY ag.data
/

CREATE OR REPLACE VIEW SIAAG.VW_AGENDAS_DISPONIVEIS AS
  SELECT
    AG.ID,
    AG.DATA,
    AG.ID_USUARIO,
    AG.ATIVO,
    AGI.tipo_agendamento
  FROM siaag.tab_agenda ag
    JOIN siaag.tab_agenda_item agi ON ag.id = agi.ID_AGENDA
    LEFT JOIN siaag.TAB_BLOQUEIO_AGENDA ba
      ON ag.data BETWEEN ba.data_inicio AND ba.data_fim AND ag.id_usuario = ba.id_usuario
         AND (agi.id_horario_inicio >= ba.id_horario_inicio AND agi.id_horario_fim <= ba.id_horario_fim)
  WHERE ba.id IS NULL AND agi.ativo = 1 AND ag.ativo = 1
  ORDER BY ag.data
/

CREATE OR REPLACE VIEW SIAAG.VW_HORARIO_AGENDA AS
  SELECT
    agi.ID                AS id_agenda_item,
    AG.ID                 AS id_agenda,
    AG.DATA               AS data,
    agi.id_horario_inicio AS id_horario_inicio_agenda,
    agi.id_horario_fim    AS id_horario_fim_agenda,
    ba.id_horario_inicio  AS id_horario_inicio_bloqueio,
    ba.id_horario_fim     AS id_horario_fim_bloqueio,
    ag.id_usuario         AS id_atendente
  FROM siaag.tab_agenda ag
    JOIN siaag.tab_agenda_item agi ON ag.id = agi.ID_AGENDA
    LEFT JOIN siaag.TAB_BLOQUEIO_AGENDA ba ON ag.id_usuario = ba.id_usuario AND
                                              ag.data BETWEEN ba.data_inicio AND ba.data_fim
  WHERE agi.ativo = 1 AND ag.ativo = 1
  ORDER BY ag.data
/

CREATE OR REPLACE VIEW SIAAG.VW_PAINEL_ATENDIMENTO AS
  SELECT
    "ID",
    "ID_REQUERENTE",
    "ID_BENEFICIARIO",
    "ID_TIPO_BENEFICIARIO",
    "ID_PONTO_ATENDIMENTO",
    "ID_TIPO_ATENDIMENTO",
    "FORMA_ATENDIMENTO",
    "PRIORIDADE_CONTATO",
    "DATA_NAO_AGENDADO",
    "ID_SITUACAO_ATENDIMENTO",
    "ATIVO",
    "ID_TIPO_REQUERENTE",
    "ID_HORARIO_INICIO",
    "ID_ATENDENTE",
    "DATA",
    "ID_HORARIO_FIM",
    "ID_USUARIO_CADASTROU",
    "SENHA",
    "OBSERVACAO",
    "TEMPO_ATENDIMENTO",
    "INFORMACOES_ATENDIMENTO"
  FROM siaag.TAB_ATENDIMENTO AT
  WHERE AT.ATIVO = 1 AND TO_DATE(AT.DATA) = TO_DATE(sysdate) AND AT.ID_SITUACAO_ATENDIMENTO <> 4
  ORDER BY AT.DATA DESC, AT.ID_HORARIO_INICIO
/

CREATE SYNONYM SIAAG.VW_CADASTRO_SIAAG FOR EPREV.VW_CADASTRO_SIAAG
/


