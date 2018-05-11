  CREATE TABLE "SIAAG"."TAB_PESSOA" 
   (	"ID" NUMBER(10,0) GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1), 
	"CELULAR" VARCHAR2(11 CHAR) NOT NULL ENABLE, 
	"CPF" VARCHAR2(11 CHAR) NOT NULL ENABLE, 
	"DATA_NASCIMENTO" DATE NOT NULL ENABLE, 
	"EMAIL" VARCHAR2(150 CHAR), 
	"NOME" VARCHAR2(150 CHAR) NOT NULL ENABLE, 
	"PRIORIDADE_CONTATO" VARCHAR2(15 CHAR) NOT NULL ENABLE, 
	"TELEFONE1" VARCHAR2(10 CHAR), 
	"TELEFONE2" VARCHAR2(10 CHAR), 
	"ID_ENDERECO" NUMBER(10,0) NOT NULL ENABLE, 
	 CONSTRAINT "PK_PESSOA" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBS_SIAAG"  ENABLE, 
	 CONSTRAINT "FK_PESSOA_ENDERECO_ID_ENDERECO" FOREIGN KEY ("ID_ENDERECO")
	  REFERENCES "SIAAG"."TAB_ENDERECO" ("ID") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBS_SIAAG" ;
  
CREATE UNIQUE INDEX "SIAAG"."UNQ_CPF" ON "SIAAG"."TAB_PESSOA" ("CPF") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBS_SIAAG" ;