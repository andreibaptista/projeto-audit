begin BEGIN EXECUTE IMMEDIATE 'drop table SIAAG.TAB_PONTO_ATENDIMENTO_SETOR  cascade constraints';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
CREATE TABLE "SIAAG"."TAB_PONTO_ATENDIMENTO_SETOR" 
   (	"ID" NUMBER(10,0) GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOT NULL ENABLE,
	"ID_PONTO_ATENDIMENTO" NUMBER(4,0) NOT NULL ENABLE, 
	"ID_SETOR" NUMBER(4,0) NOT NULL ENABLE, 
	 CONSTRAINT "PK_TAB_PONTO_ATENDIMENTO_SETOR" PRIMARY KEY ("ID"), 
	 CONSTRAINT "FK_PONTO_ATENDIMENTO_SETOR_PAT" FOREIGN KEY ("ID_PONTO_ATENDIMENTO")
	  REFERENCES "SIAAG"."TAB_PONTO_ATENDIMENTO" ("ID") ENABLE, 
	 CONSTRAINT "FK_PONTO_ATENDIMENTO_SETOR_SET" FOREIGN KEY ("ID_SETOR")
	  REFERENCES "SIAAG"."TAB_SETOR" ("ID") ENABLE
   )
	 
	 

/
DELETE FROM SIAAG.TAB_PONTO_ATENDIMENTO_SETOR
/
INSERT INTO SIAAG.TAB_PONTO_ATENDIMENTO_SETOR(ID_PONTO_ATENDIMENTO,ID_SETOR) VALUES ((SELECT ID FROM SIAAG.TAB_PONTO_ATENDIMENTO WHERE UPPER(NOME) = UPPER('IGEPREV BELÉM')),(SELECT ID FROM SIAAG.TAB_SETOR WHERE UPPER(DESCRICAO) = UPPER('Setor de Atendimento')))
/
begin BEGIN EXECUTE IMMEDIATE 'ALTER TABLE SIAAG.TAB_USUARIO DROP CONSTRAINT FK_USUARIO_PONTO_ATEND_SETOR ';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
begin BEGIN EXECUTE IMMEDIATE 'ALTER TABLE SIAAG.TAB_USUARIO DROP COLUMN ID_PONTO_ATENDIMENTO_SETOR ';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
ALTER TABLE SIAAG.TAB_USUARIO 
ADD (ID_PONTO_ATENDIMENTO_SETOR NUMBER(10) )
/
ALTER TABLE SIAAG.TAB_USUARIO
ADD CONSTRAINT FK_USUARIO_PONTO_ATEN_SETOR FOREIGN KEY
(
  ID_PONTO_ATENDIMENTO_SETOR 
)
REFERENCES SIAAG.TAB_PONTO_ATENDIMENTO_SETOR
(
  ID 
)
/
begin BEGIN EXECUTE IMMEDIATE 'ALTER TABLE SIAAG.TAB_USUARIO DROP COLUMN MATRICULA ';EXCEPTION WHEN OTHERS THEN DBMS_OUTPUT.PUT_LINE(SQLCODE||' -ERROR- '||SQLERRM);  END;end;
/
ALTER TABLE SIAAG.TAB_USUARIO 
ADD (MATRICULA VARCHAR2(20) )
/