  CREATE TABLE "SIAAG"."TAB_USUARIO" 
   (	"ID" NUMBER(10,0) GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
	"ATIVO" NUMBER(1,0) NOT NULL ENABLE, 
	"LOGIN" VARCHAR2(11 CHAR) NOT NULL ENABLE, 
	"SENHA" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"ID_PERFIL" NUMBER(10,0) NOT NULL ENABLE, 
	"ID_PESSOA" NUMBER(10,0) NOT NULL ENABLE, 
	"PRIMEIRO_ACESSO" NUMBER(1) DEFAULT 0 NOT NULL ENABLE,
	 CONSTRAINT "PK_USUARIO" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBS_SIAAG"  ENABLE, 
	 CONSTRAINT "FKA8QEAQ4RFW5UOYB4T03OS4W7W" FOREIGN KEY ("ID_PERFIL")
	  REFERENCES "SIAAG"."TAB_PERFIL" ("ID") ENABLE, 
	 CONSTRAINT "FKGPEQRIL4790KJ1SAR84RLEXEG" FOREIGN KEY ("ID_PESSOA")
	  REFERENCES "SIAAG"."TAB_PESSOA" ("ID") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBS_SIAAG" ;
  
  CREATE UNIQUE INDEX "SIAAG"."UNQ_LOGIN" ON "SIAAG"."TAB_USUARIO" ("LOGIN") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "TBS_SIAAG" ;
