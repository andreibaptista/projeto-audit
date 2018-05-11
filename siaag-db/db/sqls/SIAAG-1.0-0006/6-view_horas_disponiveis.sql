CREATE OR REPLACE VIEW "SIAAG"."VW_AGENDAS_DISPONIVEIS" ("ID", "DATA", "ID_USUARIO", "ATIVO", "TIPO_AGENDAMENTO") AS 
  select AG.ID, AG.DATA, AG.ID_USUARIO,AG.ATIVO, AGI.tipo_agendamento
from siaag.tab_agenda ag
join siaag.tab_agenda_item agi on ag.id = agi.ID_AGENDA
left join siaag.TAB_BLOQUEIO_AGENDA ba on ag.data between ba.data_inicio and ba.data_fim and ag.id_usuario = ba.id_usuario 
                                          and (agi.id_horario_inicio >= ba.id_horario_inicio and agi.id_horario_fim <= ba.id_horario_fim)
where ba.id is null and agi.ativo = 1 and ag.ativo = 1
order by ag.data
/

CREATE OR REPLACE VIEW "SIAAG"."VW_HORARIO_AGENDA" ("ID_AGENDA_ITEM", "ID_AGENDA", "DATA", "ID_HORARIO_INICIO_AGENDA", "ID_HORARIO_FIM_AGENDA", "ID_HORARIO_INICIO_BLOQUEIO", "ID_HORARIO_FIM_BLOQUEIO", "ID_ATENDENTE") AS 
  select 
       agi.ID as id_agenda_item,
       AG.ID as id_agenda, 
       AG.DATA as data,
       agi.id_horario_inicio as id_horario_inicio_agenda,
       agi.id_horario_fim as id_horario_fim_agenda,
       ba.id_horario_inicio as id_horario_inicio_bloqueio,
       ba.id_horario_fim as id_horario_fim_bloqueio,
       ag.id_usuario as id_atendente
from siaag.tab_agenda ag
join siaag.tab_agenda_item agi on ag.id = agi.ID_AGENDA
left join siaag.TAB_BLOQUEIO_AGENDA ba on ag.id_usuario = ba.id_usuario and 
                                          ag.data between ba.data_inicio and ba.data_fim 
where agi.ativo = 1 and ag.ativo = 1
order by ag.data
/


