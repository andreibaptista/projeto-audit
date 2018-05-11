CREATE OR REPLACE VIEW "SIAAG"."VW_DATAS_DISPONIVEIS" ("DATA", "TIPO_AGENDAMENTO") AS 
  select AG.DATA, AGI.tipo_agendamento
from siaag.tab_agenda ag
join siaag.tab_agenda_item agi on ag.id = agi.ID_AGENDA
left join siaag.TAB_BLOQUEIO_AGENDA ba on ag.data between ba.data_inicio and ba.data_fim and ag.id_usuario = ba.id_usuario 
                                          and (agi.id_horario_inicio >= ba.id_horario_inicio and agi.id_horario_fim <= ba.id_horario_fim)
where ba.id is null and agi.ativo = 1 and ag.ativo = 1 and ag.data > SYSDATE
group by ag.data, agi.tipo_agendamento
order by ag.data

/
DELETE FROM SIAAG.TAB_TIPO_REQUERENTE
/
INSERT INTO SIAAG.TAB_TIPO_REQUERENTE(ID,DESCRICAO) VALUES(1,'O próprio')
/
INSERT INTO SIAAG.TAB_TIPO_REQUERENTE(ID,DESCRICAO) VALUES(2,'Curador')
/
INSERT INTO SIAAG.TAB_TIPO_REQUERENTE(ID,DESCRICAO) VALUES(3,'Guardião')
/
INSERT INTO SIAAG.TAB_TIPO_REQUERENTE(ID,DESCRICAO) VALUES(4,'Procurador')
/
INSERT INTO SIAAG.TAB_TIPO_REQUERENTE(ID,DESCRICAO) VALUES(5,'Representante legal')
/
INSERT INTO SIAAG.TAB_TIPO_REQUERENTE(ID,DESCRICAO) VALUES(6,'Tuto')
/