ALTER TABLE SIAAG.TAB_SETOR ADD SIGLA VARCHAR2(15) NULL;

UPDATE SIAAG.TAB_USUARIO SET ID_PONTO_ATENDIMENTO_SETOR = null;
DELETE FROM SIAAG.TAB_PONTO_ATENDIMENTO_SETOR;
DELETE FROM SIAAG.TAB_SETOR;
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO) VALUES (1, 'Gabinete da Presidência');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (2, 'Núcleo Gestor de Investimento', 'Nugin');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (3, 'Assessoria de Comunicação', 'Ascom');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (4, 'Controle Interno', 'CI');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (5, 'Núcleo de Planejamento', 'Nuplan');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (6, 'Núcleo de Arrecadação Financeira', 'Nuaf');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (7, 'Procuradoria Jurídica', 'Projur');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (8, 'Diretoria de Administração e Finanças', 'Dafin');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (9, 'Gestão de Desenvolvimento de Pessoas', 'GDP');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (10, 'Gerência de Orçamento e Finanças', 'Gerof');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (11, 'Gerência de Administração e Serviços', 'Geras');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (12, 'Núcleo de Tecnologia de Informação', 'Nuti');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (13, 'Diretoria de Previdência', 'Dipre');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (14, 'Gerência de Cadastro e Habilitação', 'Gecah');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (15, 'Gerência de Concessão de Benefício', 'Gecob');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (16, 'Núcleo de Registro de Contribuições', 'Nurc');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (17, 'Central de Atendimento', 'Caten');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO) VALUES (18, 'Serviço Social');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO, SIGLA) VALUES (19, 'Núcleo de Diligências', 'Ndil');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO) VALUES (20, 'Arquivo Geral');
INSERT INTO SIAAG.TAB_SETOR(ID, DESCRICAO) VALUES (21, 'Protocolo');

INSERT INTO SIAAG.TAB_PONTO_ATENDIMENTO_SETOR(ID_PONTO_ATENDIMENTO,ID_SETOR) VALUES ((SELECT ID FROM SIAAG.TAB_PONTO_ATENDIMENTO WHERE UPPER(NOME) = UPPER('IGEPREV BELÉM')),(SELECT ID FROM SIAAG.TAB_SETOR WHERE UPPER(DESCRICAO) = UPPER('Central de Atendimento')));

UPDATE SIAAG.TAB_USUARIO SET ID_PONTO_ATENDIMENTO_SETOR = (SELECT PAS.ID FROM SIAAG.TAB_PONTO_ATENDIMENTO_SETOR PAS WHERE PAS.ID_SETOR = 17) WHERE not ID_PERFIL = 4;