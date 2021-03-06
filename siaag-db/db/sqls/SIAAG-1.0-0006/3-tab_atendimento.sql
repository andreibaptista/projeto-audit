  begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_SITUACAO_ATENDIMENTO  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
  
  CREATE TABLE "SIAAG"."TAB_SITUACAO_ATENDIMENTO" 
   (	"ID" NUMBER(2,0) NOT NULL ENABLE, 
	"DESCRICAO" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	 CONSTRAINT "TAB_SITUACAO_ATENDIMENTO_PK" PRIMARY KEY ("ID"))
 / 
  DELETE FROM SIAAG.TAB_SITUACAO_ATENDIMENTO
  /
  INSERT INTO SIAAG.TAB_SITUACAO_ATENDIMENTO(ID,DESCRICAO) VALUES(1,'Ativo')
  /
	INSERT INTO SIAAG.TAB_SITUACAO_ATENDIMENTO(ID,DESCRICAO) VALUES(2,'Reagendado')
	/
INSERT INTO SIAAG.TAB_SITUACAO_ATENDIMENTO(ID,DESCRICAO) VALUES(3,'Expirado')
/
INSERT INTO SIAAG.TAB_SITUACAO_ATENDIMENTO(ID,DESCRICAO) VALUES(4,'Cancelado')
/
INSERT INTO SIAAG.TAB_SITUACAO_ATENDIMENTO(ID,DESCRICAO) VALUES(5,'Pré-atendimento')
/
INSERT INTO SIAAG.TAB_SITUACAO_ATENDIMENTO(ID,DESCRICAO) VALUES(6,'Pré-atendido')
/
INSERT INTO SIAAG.TAB_SITUACAO_ATENDIMENTO(ID,DESCRICAO) VALUES(7,'Atendido')


  /
  CREATE TABLE "SIAAG"."TAB_ATENDIMENTO" 
   (	"ID" NUMBER(10,0) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 ORDER  NOCYCLE  NOT NULL ENABLE, 
	"ID_REQUERENTE" NUMBER(10,0) NOT NULL ENABLE, 
	"ID_BENEFICIARIO" NUMBER NOT NULL ENABLE, 
	"ID_TIPO_BENEFICIARIO" NUMBER(4,0) NOT NULL ENABLE, 
	"ID_PONTO_ATENDIMENTO" NUMBER(10,0) NOT NULL ENABLE, 
	"ID_TIPO_ATENDIMENTO" NUMBER(4,0) NOT NULL ENABLE, 
	"FORMA_ATENDIMENTO" VARCHAR2(15 BYTE) NOT NULL ENABLE, 
	"PRIORIDADE_CONTATO" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"DATA_INCLUSAO" TIMESTAMP (6) NOT NULL ENABLE, 
	"ID_SITUACAO_ATENDIMENTO" NUMBER(2,0) DEFAULT NULL NOT NULL ENABLE, 
	"ATIVO" NUMBER(1,0) DEFAULT 1 NOT NULL ENABLE, 
	"ID_TIPO_REQUERENTE" NUMBER(4,0) NOT NULL ENABLE, 
	"ID_HORARIO_INICIO" NUMBER(4,0) NOT NULL ENABLE, 
	"ID_ATENDENTE" NUMBER(10,0) NOT NULL ENABLE, 
	"DATA" TIMESTAMP (6) NOT NULL ENABLE, 
	"ID_HORARIO_FIM" NUMBER NOT NULL ENABLE, 
	 CONSTRAINT "PK_ATENDIMENTO" PRIMARY KEY ("ID"), 
	 CONSTRAINT "FK_ATENDIMENTO_PESSOA_REQU" FOREIGN KEY ("ID_REQUERENTE")
	  REFERENCES "SIAAG"."TAB_PESSOA" ("ID") ENABLE, 
	 CONSTRAINT "FK_ATENDIMENTO_PESSOA_BENEF" FOREIGN KEY ("ID_BENEFICIARIO")
	  REFERENCES "SIAAG"."TAB_PESSOA" ("ID") ENABLE, 
	 CONSTRAINT "FK_ATENDIMENTO_TIPO_BENEF" FOREIGN KEY ("ID_TIPO_BENEFICIARIO")
	  REFERENCES "SIAAG"."TAB_TIPO_BENEFICIARIO" ("ID") ENABLE, 
	 CONSTRAINT "FK_ATENDIMENTO_TIPO_REQUERENTE" FOREIGN KEY ("ID_TIPO_REQUERENTE")
	  REFERENCES "SIAAG"."TAB_TIPO_REQUERENTE" ("ID") ENABLE, 
	 CONSTRAINT "FK_ATENDIMENTO_SITUACAO" FOREIGN KEY ("ID_SITUACAO_ATENDIMENTO")
	  REFERENCES "SIAAG"."TAB_SITUACAO_ATENDIMENTO" ("ID") ENABLE, 
	 CONSTRAINT "FK_ATENDIMENTO_PONTO_ATEND" FOREIGN KEY ("ID_PONTO_ATENDIMENTO")
	  REFERENCES "SIAAG"."TAB_PONTO_ATENDIMENTO" ("ID") ENABLE, 
	 CONSTRAINT "FK_ATENDIMENTO_HORARIO" FOREIGN KEY ("ID_HORARIO_INICIO")
	  REFERENCES "SIAAG"."TAB_HORARIO" ("ID") ENABLE, 
	 CONSTRAINT "TAB_ATENDIMENTO_FK1" FOREIGN KEY ("ID_ATENDENTE")
	  REFERENCES "SIAAG"."TAB_PESSOA" ("ID") ENABLE, 
	 CONSTRAINT "TAB_ATENDIMENTO_FK2" FOREIGN KEY ("ID_HORARIO_FIM")
	  REFERENCES "SIAAG"."TAB_HORARIO" ("ID") ENABLE
   )
   /
