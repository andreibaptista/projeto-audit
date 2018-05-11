package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.Horario;
import br.gov.pa.igeprev.siaag.model.Usuario;
import br.gov.pa.igeprev.siaag.model.view.VwHorarioAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;

public interface VwHorarioAgendaRepository extends JpaRepository<VwHorarioAgenda, Integer> {

    Collection<VwHorarioAgenda> findByData(Date data);

    @Query(value = "select * from vw_horario_agenda ha "+
            "where ha.data = :data and :horario between ha.ID_HORARIO_INICIO_AGENDA and ha.ID_HORARIO_FIM_AGENDA",nativeQuery = true)
    Collection<Usuario> findAtendentesByDataAndHorario(@Param("data") Date data,@Param("horario") Integer idHorario);
}
