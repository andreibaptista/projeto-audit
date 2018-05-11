package br.gov.pa.igeprev.siaag.bean;

import br.gov.pa.igeprev.siaag.enumeration.PerfilEnum;
import br.gov.pa.igeprev.siaag.enumeration.PeriodoEnum;
import br.gov.pa.igeprev.siaag.enumeration.TipoAgendamentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.*;
import br.gov.pa.igeprev.siaag.service.*;
import br.gov.pa.igeprev.siaag.utils.DataUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.util.*;

@ManagedBean
@ViewScoped
@Controller
public class AgendaBean extends ManagerBeanDefault {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private AgendaItemService agendaItemService;
    @Autowired
    private HorarioService horarioService;
    @Autowired
    private BloqueioAgendaService bloqueioAgendaService;
    @Autowired
    private AtendimentoService atendimentoService;

    private Usuario atendente = new Usuario();
    private Agenda agenda = new Agenda();
    private AgendaItem agendaItem = new AgendaItem();

    private Collection<Usuario> atendentes = new ArrayList<>();
    private List<AgendaItem> agendaItems = new ArrayList<>();
    private List<Horario> horarios = new ArrayList<>();
    private List<Horario> horariosInicio = new ArrayList<>();
    private List<Horario> horariosFim = new ArrayList<>();
    private AgendaItem agendaItemExclusaoSelecionada = new AgendaItem();

    private Collection<BloqueioAgenda> bloqueioAgendas = new ArrayList<>();

    private List<Usuario> atendentesDisponiveis = new ArrayList<>();
    private Set<Usuario> atendentesSelecionados;
    private List<AgendaItem> agendaItemsExclusao = new ArrayList<>();

    private PeriodoEnum periodoEnum;

    private String nomePesq;

    private Boolean diaPermitido = true;
    private Boolean atendimentoNaoAgendado = false;
    private Boolean confirmarSalvar = false;
    private Boolean exclusaoNaoPermitida = false;
    private String mensagemRetorno = "";

    private BloqueioAgenda bloqueioAgenda = new BloqueioAgenda();

    private Horario primeiroHorarioAtivo;
    private Horario ultimoHorarioAtivo;

    // Componentes Primefaces
    private ScheduleModel eventModel;
    private ScheduleEvent eventoSelecionado = new DefaultScheduleEvent();

    @Override
    public void inicializacao() throws ServiceException {
        eventModel = new DefaultScheduleModel();
        atendente = new Usuario();
        agendaItemExclusaoSelecionada = new AgendaItem();
        agendaItemsExclusao = new ArrayList<>();
        primeiroHorarioAtivo = horarioService.findPrimeiroHorarioAtivo();
        ultimoHorarioAtivo = horarioService.findUltimoHorarioAtivo();
        horarios = new ArrayList<>(horarioService.findByAtivo());
        horariosInicio = new ArrayList<>();
        horariosInicio.addAll(horarios);
        horariosInicio.remove(ultimoHorarioAtivo);
        horariosFim = new ArrayList<>();
        horariosFim.addAll(horarios);
        horariosFim.remove(primeiroHorarioAtivo);
        nomePesq = "";

        exclusaoNaoPermitida = false;
        buscarListaEntidades();
    }

    public void pesquisaAtendentes() {
        try {
            atendentes = usuarioService.findByPerfilIdAndPessoaNome(PerfilEnum.ATENDENTE.getId(), nomePesq);
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    protected void novoRegistro() {
        agenda = new Agenda();
    }

    @Override
    protected void editarRegistro(Object obj) throws ServiceException {
        atendente = (Usuario) obj;
        agendaItems = new ArrayList<>();
        agenda.setUsuario(atendente);
        agendaItem.setAgenda(agenda);
        atendentesDisponiveis = new ArrayList<>(usuarioService.findByPerfilId(PerfilEnum.ATENDENTE.getId()));
        atendentesDisponiveis.remove(atendente);
        agendaItemsExclusao = new ArrayList<>();
        agendaItemExclusaoSelecionada = new AgendaItem();
        carregaAgenda();
    }

    private void carregaAgenda() {
        try {
            eventModel = new DefaultScheduleModel();
            Collection<Agenda> agendas = agendaService.findByUsuarioId(atendente.getId());
            for (Agenda ag : agendas) {
                ag.setAgendaItems(agendaItemService.findByAgendaId(ag.getId()));
                // Atendimento agendado
                for (AgendaItem ai : ag.getAgendaItems()) {
                    DefaultScheduleEvent defaultScheduleEvent = new DefaultScheduleEvent(
                            ai.getHorarioInicio().horarioFormatado() + " - " + ai.getHorarioFim().horarioFormatado(),
                            DataUtils.DataCompleta(ag.getData(), ai.getHorarioInicio().getHorario()),
                            DataUtils.DataCompleta(ag.getData(), ai.getHorarioFim().getHorario()));
                    defaultScheduleEvent.setId(ai.getId() + "");
                    ai.setEvento(defaultScheduleEvent);
                    if (ai.getTipoAgendamento().equals(TipoAgendamentoEnum.AGENDADO)) {
                        defaultScheduleEvent.setStyleClass("atendimentoAgendado");
                    }
                    defaultScheduleEvent.setAllDay(true);
                    eventModel.addEvent(defaultScheduleEvent);
                }
                // Atendimento não agendado
                if (ag.getAtendimentoNaoAgendado()) {
                    DefaultScheduleEvent defaultScheduleEventNaoAgendado = new DefaultScheduleEvent(getMessage("AtendimentoNaoAgendado"),
                            DataUtils.DataCompleta(ag.getData(), primeiroHorarioAtivo.getHorario()),
                            DataUtils.DataCompleta(ag.getData(), ultimoHorarioAtivo.getHorario()));
                    defaultScheduleEventNaoAgendado.setStyleClass("atendimentoNaoAgendado");
                    defaultScheduleEventNaoAgendado.setAllDay(true);
                    eventModel.addEvent(defaultScheduleEventNaoAgendado);
                }
            }
            verificaDatasBloqueadas(eventModel);
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
        }
    }

    private void verificaDatasBloqueadas(ScheduleModel eventModel) {
        try {
            bloqueioAgendas = bloqueioAgendaService.findByUsuarioId(atendente.getId());

            for (BloqueioAgenda ba : bloqueioAgendas) {
                List<Date> datas = DataUtils.DatasEntre(ba.getDataInicio(), ba.getDataFim());
                for (Date data : datas) {
                    DefaultScheduleEvent defaultScheduleEvent = new DefaultScheduleEvent(
                            getMessage("DataBloqueada"),
                            DataUtils.DataCompleta(data, ba.getHorarioInicio().getHorario()),
                            DataUtils.DataCompleta(data, ba.getHorarioFim().getHorario()));
                    defaultScheduleEvent.setId(ba.getId() + "");
                    defaultScheduleEvent.setStyleClass("dataBloqueada");
                    defaultScheduleEvent.setAllDay(true);
                    eventModel.addEvent(defaultScheduleEvent);
                }
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    protected void visualizarRegistro(Object obj) {
        agenda.setUsuario((Usuario) obj);
    }

    @Override
    protected void cancelarOperacao() throws ServiceException {
        buscarListaEntidades();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('eventDialog').hide();");
        nomePesq = "";
    }

    @Override
    protected void buscarListaEntidades() throws ServiceException {
        atendentes = usuarioService.findByPerfilId(PerfilEnum.ATENDENTE.getId());
    }

    @Override
    protected void remover() throws ServiceException {
    }

    @Override
    protected void salvar() throws ServiceException {

    }

    public void salvarAgendaItem(ActionEvent actionEvent) {
        try {
            agenda.setAgendaItems(agendaItems);
            Collection<Agenda> agendas = new ArrayList<>();
            Collection<Agenda> agendasAtendentes = new ArrayList<>();
            Collection<Agenda> agendasPeriodo = new ArrayList<>();
            if (periodoEnum != null) {
                agendasPeriodo = setAgendasPorPeriodo(periodoEnum, agenda.getData(), atendimentoNaoAgendado);
            } else {
                agendasAtendentes.add(agenda);
            }
            atendentesSelecionados.add(atendente);
            if (atendentesSelecionados != null && atendentesSelecionados.size() > 0) {
                for (Usuario atend : atendentesSelecionados) {
                    if (agendasPeriodo != null && !agendasPeriodo.isEmpty()) {
                        setAgendasReplicadasEscolhidas(agendas, agendasPeriodo, atend, atendimentoNaoAgendado);
                    } else {
                        setAgendasReplicadasEscolhidas(agendas, agendasAtendentes, atend, atendimentoNaoAgendado);
                    }
                }
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (confirmarSalvar || !existemDatasComAtendimento(agendas)) {
                verificaAdicionaAgendaItemsExclusao();
                String mensagemRetorno = agendaService.salvar(agendas, agendaItemsExclusao);
                eventoSelecionado.setId(agendaItem.getId() + "");
                agendaItem = new AgendaItem();
                agendaItem.setAgenda(agenda);
                eventoSelecionado = new DefaultScheduleEvent();
                agendaItemsExclusao = new ArrayList<>();
                agendaItemExclusaoSelecionada = new AgendaItem();
                carregaAgenda();
                context.execute("PF('eventDialog').hide();");
                confirmarSalvar = false;
                if(agendas.size() > 1) showSuccessMessage();
                if(exclusaoNaoPermitida) showErrorMessage(getMessage("mensagemErroNaoPossivelExcluirAtendimentoExistente"));
            } else {
                context.execute("PF('confirmarSalvarDialog').show();");
                RequestContext.getCurrentInstance().update("form:confirmarSalvarDlg");
                confirmarSalvar = true;
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
        }
    }

    private boolean existemDatasComAtendimento(Collection<Agenda> agendas) throws Exception {
        if(periodoEnum != null || atendentesSelecionados != null && atendentesSelecionados.size() > 1) {
            StringBuilder mensagem = new StringBuilder();
            for (Agenda agenda : agendas) {
                Collection<Atendimento> atendimentosExistentes = atendimentoService.findByAtendenteIdAndDataAndSituacaoAtendimentoNotAgendado(agenda.getUsuario().getPessoa().getId(), agenda.getData());
                if (atendimentosExistentes != null && !atendimentosExistentes.isEmpty()) {
                    for (Atendimento atendExistente : atendimentosExistentes) {
                        if (!mensagem.toString().contains(atendExistente.getAtendente().getNome())) {
                            mensagem.append(atendExistente.getAtendente().getNome()).append(", ");
                        }
                    }
                }
            }
            if (!mensagem.toString().isEmpty()) {
                mensagemRetorno = getMessage("mensagemErroAtendentesNaoPossivelReplicarAtendimento", mensagem.toString());
                confirmarSalvar = false;
                return true;
            } else {
                confirmarSalvar = true;
                return false;
            }
        }else{
            return false;
        }
    }

    private void setAgendasReplicadasEscolhidas(Collection<Agenda> agendas, Collection<Agenda> agendasPeriodo, Usuario atend, Boolean atendimentoNaoAgendado) {
        for (Agenda agenda : agendasPeriodo) {
            Agenda ag = new Agenda();
            ag.setUsuario(atend);
            ag.setData(agenda.getData());
            ag.setAtendimentoNaoAgendado(atendimentoNaoAgendado);
            ag.setAgendaItems(this.agendaItems);
            agendas.add(ag);
        }
    }

    private Collection<Agenda> setAgendasPorPeriodo(PeriodoEnum periodoEnum, Date data, Boolean atendimentoNaoAgendado) {
        List<Date> datas = new ArrayList<>();
        Collection<Agenda> agendas = new ArrayList<>();
        if (periodoEnum.equals(PeriodoEnum.SEMANA)) {
            datas = DataUtils.DatasSemana(data);
        } else if (periodoEnum.equals(PeriodoEnum.MES)) {
            datas = DataUtils.DatasMes(data);
        } else if (periodoEnum.equals(PeriodoEnum.ANO)) {
            datas = DataUtils.DatasAno(data);
        }
        for (Date dt : datas) {
            Agenda ag = new Agenda();
            ag.setUsuario(atendente);
            ag.setAtivo(true);
            ag.setData(dt);
            ag.setAtendimentoNaoAgendado(atendimentoNaoAgendado);
            agendas.add(ag);
        }
        return agendas;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        try {
            exclusaoNaoPermitida = false;
            diaPermitido = true;
            periodoEnum = null;
            atendentesSelecionados = new HashSet<>();
            eventoSelecionado = (DefaultScheduleEvent) selectEvent.getObject();
            Calendar calEventoSelecionado = Calendar.getInstance();
            calEventoSelecionado.setTime(DataUtils.SomenteData(eventoSelecionado.getStartDate()));
            if (diaBloqueado(atendente.getId(), calEventoSelecionado)) {
                //TODO: Homologacao
                //if (calEventoSelecionado.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || calEventoSelecionado.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                Agenda agenda = agendaService.findbyUsuarioIdAndData(atendente.getId(), DataUtils.SomenteData(eventoSelecionado.getStartDate()));
                if (agenda == null || agenda.getId() == null) {
                    agenda = new Agenda();
                    agenda.setData(DataUtils.SomenteData(eventoSelecionado.getStartDate()));
                }
                atendimentoNaoAgendado = agenda.getAtendimentoNaoAgendado();
                this.agenda = agenda;
                this.agenda.setUsuario(atendente);
                agendaItems = new ArrayList<>(agendaItemService.findByAgendaId(this.getAgenda().getId()));
                if (agendaItems.isEmpty()) {
                    AgendaItem agendaItem = new AgendaItem();
                    //agendaItems.add(agendaItem);
                    setHorariosAgendaItem();
                }
                setEventoSelecionado(eventoSelecionado);
                /*} else {
                    diaPermitido = false;
                }*/
                //TODO: Homologacao
            } else {
                diaPermitido = false;
                agenda.setData(DataUtils.SomenteData(calEventoSelecionado.getTime()));
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
        }
    }

    public void onDateSelect(SelectEvent selectEvent) {
        try {
            diaPermitido = true;
            exclusaoNaoPermitida = false;
            periodoEnum = null;
            atendentesSelecionados = new HashSet<>();
            eventoSelecionado = new DefaultScheduleEvent("", (Date) selectEvent.getObject(),
                    (Date) selectEvent.getObject());
            Calendar calEventoSelecionado = Calendar.getInstance();
            calEventoSelecionado.setTime(DataUtils.SomenteData(eventoSelecionado.getStartDate()));
            if (diaBloqueado(atendente.getId(), calEventoSelecionado)) {
                //TODO: Homologacao
                /*if (calEventoSelecionado.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || calEventoSelecionado.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {*/
                Agenda agenda = agendaService.findbyUsuarioIdAndData(atendente.getId(), DataUtils.SomenteData(eventoSelecionado.getStartDate()));
                if (agenda == null || agenda.getId() == null) {
                    agenda = new Agenda();
                    agenda.setData(DataUtils.SomenteData(eventoSelecionado.getStartDate()));
                }
                atendimentoNaoAgendado = agenda.getAtendimentoNaoAgendado();
                this.agenda = agenda;
                this.agenda.setUsuario(atendente);
                agendaItems = new ArrayList<>(agendaItemService.findByAgendaId(this.getAgenda().getId()));
                if (agendaItems.isEmpty()) {
                    AgendaItem agendaItem = new AgendaItem();
                    //agendaItems.add(agendaItem);
                    setHorariosAgendaItem();
                }
                setEventoSelecionado(eventoSelecionado);
            } else {
                diaPermitido = false;
                agenda.setData(DataUtils.SomenteData(calEventoSelecionado.getTime()));
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
        }
    }

    /* CRUD AGENDA ITEM */
    public void novoAgendaItem() {
        agendaItem = new AgendaItem();
        agendaItem.setTipoAgendamento(TipoAgendamentoEnum.AGENDADO);
        agendaItems.add(agendaItem);
        setHorariosAgendaItem();
    }

    public boolean novoHorarioBloqueado() {
        return agendaItems != null && agendaItems.size() > 0 && agendaItems.get(agendaItems.size() - 1).getHorarioFim() != null &&
                agendaItems.get(agendaItems.size() - 1).getHorarioFim().equals(horarios.get(horarios.size() - 1));
    }

    public void setAgendaItemExclusao(){
        if (agendaItem != null && agendaItem.getId() != null) {
            agendaItemExclusaoSelecionada = agendaItem;
            agendaItems.remove(agendaItemExclusaoSelecionada);
        }
        agendaItems.remove(agendaItem);
    }

    private void verificaAdicionaAgendaItemsExclusao() {
        List<AgendaItem> agendasItemsSelecionadasExclusao = new ArrayList<>();
        try {
            if (agendaItemExclusaoSelecionada != null) {
                if (agendaItemExclusaoSelecionada.getId() != null) {
                    Date dataInicio;
                    Date dataFim;
                    if (periodoEnum == PeriodoEnum.ANO) {
                        dataInicio = DataUtils.PrimeiraDataAno(agendaItemExclusaoSelecionada.getAgenda().getData());
                        dataFim = DataUtils.UltimaDataAno(agendaItemExclusaoSelecionada.getAgenda().getData());
                    } else if (periodoEnum == PeriodoEnum.MES) {
                        dataInicio = DataUtils.PrimeiraDataMes(agendaItemExclusaoSelecionada.getAgenda().getData());
                        dataFim = DataUtils.UltimaDataMes(agendaItemExclusaoSelecionada.getAgenda().getData());
                    } else if (periodoEnum == PeriodoEnum.SEMANA) {
                        dataInicio = DataUtils.PrimeiraDataSemana(agendaItemExclusaoSelecionada.getAgenda().getData());
                        dataFim = DataUtils.UltimaDataSemana(agendaItemExclusaoSelecionada.getAgenda().getData());
                    } else {
                        dataInicio = agendaItemExclusaoSelecionada.getAgenda().getData();
                        dataFim = agendaItemExclusaoSelecionada.getAgenda().getData();
                    }

                    if (atendentesSelecionados != null && atendentesSelecionados.size() > 0) {
                        for (Usuario atendente : atendentesSelecionados) {
                            agendasItemsSelecionadasExclusao.addAll(agendaItemService.findByAgendaAtendenteIdHorarioInicioIdAndHorarioFimIdAndAgendaDataBetween(atendente.getId(), agendaItemExclusaoSelecionada.getHorarioInicio().getId(), agendaItemExclusaoSelecionada.getHorarioFim().getId(), dataInicio, dataFim));
                        }
                    }
                    for (AgendaItem agendaItemExcluir : agendasItemsSelecionadasExclusao) {
                        Collection<Atendimento> atendimentosExistentes = atendimentoService.findByAtendenteIdAndDataAndHorarioInicioIdBetween
                                (agendaItemExcluir.getAgenda().getUsuario().getPessoa().getId(), agendaItemExcluir.getAgenda().getData(), agendaItemExcluir.getHorarioInicio().getId(), agendaItemExcluir.getHorarioFim().getId());
                        if (atendimentosExistentes == null || atendimentosExistentes.isEmpty()) {
                            agendaItemsExclusao.add(agendaItemExcluir);
                            //agendaItemService.excluir(agendaItemExcluir.getId());
                            agendaItems.remove(agendaItemExcluir);
                        } else {
                            exclusaoNaoPermitida = true;
                            //showErrorMessage(getMessage("mensagemErroNaoPossivelExcluirAtendimentoExistente"));
                        }
                    }
                    carregaAgenda();
                } else {
                    agendaItems.remove(agendaItemExclusaoSelecionada);
                    carregaAgenda();
                }
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
        }

    }

    /* FIM CRUD AGENDA ITEM */

    private boolean diaBloqueado(Integer atendenteId, Calendar calEventoSelecionado) {
        try {
            Collection<BloqueioAgenda> bloqueioAgendas = bloqueioAgendaService.findByUsuarioIdAndData(atendenteId, DataUtils.SomenteData(calEventoSelecionado.getTime()));
            return bloqueioAgendas == null || bloqueioAgendas.size() == 0;
        } catch (Exception e) {
            showErrorMessage();
            return false;
        }
    }

    public Collection<Horario> getHorarios() {
        return horarios;
    }

    public void setHorariosAgendaItem() {
        for (int i = 0; i < agendaItems.size(); i++) {
            if (i == 0) {
                if (agendaItems.get(i).getHorarioInicio() != null || agendaItems.get(i).getHorarioFim() != null) {
                    if (agendaItems.get(i).getHorarioInicio() == null || agendaItems.get(i).getHorarioFim() == null
                            || agendaItems.get(i).getHorarioInicio().getId() == null
                            || agendaItems.get(i).getHorarioFim().getId() == null || agendaItems.get(i)
                            .getHorarioInicio().getId() > agendaItems.get(i).getHorarioFim().getId()) {
                        agendaItems.get(i).setHorarioFim(getProximoHorario(agendaItems.get(i).getHorarioInicio()));
                    }
                } else {
                    agendaItems.get(i).setHorarioInicio(primeiroHorarioAtivo);
                    agendaItems.get(i).setHorarioFim(ultimoHorarioAtivo);
                }
            } else {
                if (agendaItems.get(i).getHorarioInicio() == null || agendaItems.get(i - 1).getHorarioFim()
                        .getId() > agendaItems.get(i).getHorarioInicio().getId()) {
                    agendaItems.get(i).setHorarioInicio(getProximoHorario(agendaItems.get(i - 1).getHorarioFim()));
                }
                if (agendaItems.get(i).getHorarioFim() == null
                        || agendaItems.get(i).getHorarioInicio().getId() > agendaItems.get(i).getHorarioFim().getId()) {
                    agendaItems.get(i).setHorarioFim(getProximoHorario(agendaItems.get(i).getHorarioInicio()));
                }
            }
        }
    }

    // INICIO BLOQUEIO AGENDA
    public void novoBloqueioAgenda(Usuario atendente) {
        try {
            bloqueioAgenda = new BloqueioAgenda();
            bloqueioAgenda.setHorarioInicio(horarios.get(0));
            bloqueioAgenda.setHorarioFim(horarios.get(1));
            this.atendente = atendente;
            bloqueioAgenda.setUsuario(atendente);
            bloqueioAgendas = bloqueioAgendaService.findByUsuarioId(atendente.getId());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('bloquearData').show();");
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public void salvarBloqueioAgenda() {
        try {
            StringBuilder datasNaoPermitidas = new StringBuilder();
            if (bloqueioAgenda != null && bloqueioAgenda.getDataInicio() != null && bloqueioAgenda.getDataFim() != null) {
                List<Atendimento> atendimentosMarcados = new ArrayList<>(atendimentoService.findByAtendenteIdAndDataBetween(bloqueioAgenda.getUsuario().getPessoa().getId(), bloqueioAgenda.getDataInicio(), bloqueioAgenda.getDataFim()));
                if (atendimentosMarcados.isEmpty()) {
                    if (verificaBloqueioAgendaExistente()) {
                        bloqueioAgenda.setUsuario(atendente);
                        bloqueioAgenda.setHorarioInicio(primeiroHorarioAtivo);
                        bloqueioAgenda.setHorarioFim(ultimoHorarioAtivo);
                        bloqueioAgendaService.salvar(bloqueioAgenda);
                        showSuccessMessage();
                        novoBloqueioAgenda(this.atendente);
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.execute("PF('calendario').update();");
                        carregaAgenda();
                        context.execute("PF('calendario').update();");
                    } else {
                        showErrorMessage(getMessage("mensagemErroDataBloqueadaExistente"));
                    }
                } else {
                    for (Atendimento atendimentosMarcado : atendimentosMarcados) {
                        datasNaoPermitidas.append(atendimentosMarcado.dataAtendimentoAgendadoFormatado()).append("\n");
                    }
                    showErrorMessage(getMessage("mensagemErroNaoPossivelSalvarAgendaBloqueioAtendimentosExistentes", datasNaoPermitidas));
                }
            }
        } catch (Exception e) {
            showErrorMessage(getMessage("ErroCadastrar"));
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public void excluirBloqueioAgenda() {
        try {
            if (bloqueioAgenda != null && bloqueioAgenda.getId() != null) {
                bloqueioAgendaService.excluir(bloqueioAgenda.getId());
                showSuccessMessage();
                RequestContext context = RequestContext.getCurrentInstance();
                novoBloqueioAgenda(this.atendente);
                context.execute("PF('calendario').update();");
                carregaAgenda();
                context.execute("PF('calendario').update();");
            }
        } catch (Exception e) {
            showErrorMessage(getMessage("ErroExcluir"));
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    private boolean verificaBloqueioAgendaExistente() {
        try {
            Collection<BloqueioAgenda> bloqueioAgendasExistentes = bloqueioAgendaService.findByUsuarioId(atendente.getId());
            for (BloqueioAgenda ba : bloqueioAgendasExistentes) {
                if (DataUtils.DataExistenteEntreIntervalo(bloqueioAgenda.getDataInicio(), bloqueioAgenda.getDataFim()
                        , ba.getDataInicio(), ba.getDataFim())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public void setDataInicial() {
        if (bloqueioAgenda.getDataInicio() != null && bloqueioAgenda.getDataFim() != null) {
            if (bloqueioAgenda.getDataInicio().compareTo(bloqueioAgenda.getDataFim()) > 0) {
                bloqueioAgenda.setDataInicio(bloqueioAgenda.getDataFim());
            }
        }
    }

    public void setDataFinal() {
        if (bloqueioAgenda.getDataInicio() != null && bloqueioAgenda.getDataFim() != null) {
            if (bloqueioAgenda.getDataFim().compareTo(bloqueioAgenda.getDataInicio()) < 0) {
                bloqueioAgenda.setDataFim(bloqueioAgenda.getDataInicio());
            }
        }
    }

    public void setHorariosBloqueioAgendaInicio() {
        if (bloqueioAgenda != null) {
            if (bloqueioAgenda.getHorarioInicio().getId() >= bloqueioAgenda.getHorarioFim().getId()) {
                bloqueioAgenda.setHorarioFim(proximoHorario(bloqueioAgenda.getHorarioInicio()));
            }
        }
    }

    public void setHorariosBloqueioAgendaFim() {
        if (bloqueioAgenda != null) {
            if (bloqueioAgenda.getHorarioFim().getId() <= bloqueioAgenda.getHorarioInicio().getId()) {
                bloqueioAgenda.setHorarioInicio(anteriorHorario(bloqueioAgenda.getHorarioFim()));
            }
        }
    }

    // FIM BLOQUEIO AGENDA

    public void setHorariosAgendaItemInicio() {
        if (agendaItem != null) {
            if (agendaItem.getHorarioInicio().getId() >= agendaItem.getHorarioFim().getId()) {
                agendaItem.setHorarioFim(proximoHorario(agendaItem.getHorarioInicio()));
            }
        }
    }

    public void setHorariosAgendaItemFim() {
        if (agendaItem != null) {
            if (agendaItem.getHorarioFim().getId() <= agendaItem.getHorarioInicio().getId()) {
                agendaItem.setHorarioInicio(anteriorHorario(agendaItem.getHorarioFim()));
            }
        }
    }

    private Horario anteriorHorario(Horario horario) {
        if (horario.getId() > 0) {
            return horarios.get(horario.getId() - 2);
        } else {
            return horario;
        }
    }

    private Horario proximoHorario(Horario horario) {
        if (horario.getId() < horarios.size()) {
            return horarios.get(horario.getId());
        } else {
            return horario;
        }
    }

    public Horario getProximoHorario(Horario horario) {
        if (horario.getId() < horarios.size()) {
            return horarios.get(horario.getId());
        } else {
            return horario;
        }
    }

    public PeriodoEnum[] getPeriodos() {
        return PeriodoEnum.values();
    }

    public TipoAgendamentoEnum[] getTipoAgendamentos() {
        return TipoAgendamentoEnum.values();
    }

    @Override
    public String getNomeFormulario() {
        return "protected/form-agenda.xhtml";
    }

    @Override
    protected String getMsgTraceAlteracao() {
        return "Alterou o objeto " + agenda.getClass().getName() + " com id " + agenda.getId();
    }

    @Override
    protected String getMsgTraceInclusao() {
        return "Cadastrou o objeto " + agenda.getClass().getName() + " com id " + agenda.getId();
    }

    @Override
    protected String getMsgTraceExclusao() {
        return "Excluiu o objeto " + agenda.getClass().getName() + " com id " + agenda.getId();
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Collection<Usuario> getAtendentes() {
        return atendentes;
    }

    public void setAtendentes(Collection<Usuario> atendentes) {
        this.atendentes = atendentes;
    }

    public String getNomePesq() {
        return nomePesq;
    }

    public void setNomePesq(String nomePesq) {
        this.nomePesq = nomePesq;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEventoSelecionado() {
        return eventoSelecionado;
    }

    public void setEventoSelecionado(ScheduleEvent eventoSelecionado) {
        this.eventoSelecionado = eventoSelecionado;
    }

    public AgendaItem getAgendaItem() {
        return agendaItem;
    }

    public void setAgendaItem(AgendaItem agendaItem) {
        this.agendaItem = agendaItem;
    }

    public List<AgendaItem> getAgendaItems() {
        return agendaItems;
    }

    public void setAgendaItems(List<AgendaItem> agendaItems) {
        this.agendaItems = agendaItems;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public Set<Usuario> getAtendentesSelecionados() {
        return atendentesSelecionados;
    }

    public void setAtendentesSelecionados(Set<Usuario> atendentesSelecionados) {
        this.atendentesSelecionados = atendentesSelecionados;
    }

    public List<Usuario> getAtendentesDisponiveis() {
        return atendentesDisponiveis;
    }

    public void setAtendentesDisponiveis(List<Usuario> atendentesDisponiveis) {
        this.atendentesDisponiveis = atendentesDisponiveis;
    }

    public PeriodoEnum getPeriodoEnum() {
        return periodoEnum;
    }

    public void setPeriodoEnum(PeriodoEnum periodoEnum) {
        this.periodoEnum = periodoEnum;
    }

    public Usuario getAtendente() {
        return atendente;
    }

    public void setAtendente(Usuario atendente) {
        this.atendente = atendente;
    }

    public Collection<BloqueioAgenda> getBloqueioAgendas() {
        return bloqueioAgendas;
    }

    public void setBloqueioAgendas(Collection<BloqueioAgenda> bloqueioAgendas) {
        this.bloqueioAgendas = bloqueioAgendas;
    }

    public List<Horario> getHorariosInicio() {
        return horariosInicio;
    }

    public void setHorariosInicio(List<Horario> horariosInicio) {
        this.horariosInicio = horariosInicio;
    }

    public List<Horario> getHorariosFim() {
        return horariosFim;
    }

    public void setHorariosFim(List<Horario> horariosFim) {
        this.horariosFim = horariosFim;
    }

    public Boolean getDiaPermitido() {
        return diaPermitido;
    }

    public BloqueioAgenda getBloqueioAgenda() {
        return bloqueioAgenda;
    }

    public void setBloqueioAgenda(BloqueioAgenda bloqueioAgenda) {
        this.bloqueioAgenda = bloqueioAgenda;
    }

    public Boolean getAtendimentoNaoAgendado() {
        return atendimentoNaoAgendado;
    }

    public void setAtendimentoNaoAgendado(Boolean atendimentoNaoAgendado) {
        this.atendimentoNaoAgendado = atendimentoNaoAgendado;
    }

    public String getMensagemRetorno() {
        return mensagemRetorno;
    }

    public Boolean getConfirmarSalvar() {
        return confirmarSalvar;
    }

    public List<AgendaItem> getAgendaItemsExclusao() {
        return agendaItemsExclusao;
    }

    public void setAgendaItemsExclusao(List<AgendaItem> agendaItemsExclusao) {
        this.agendaItemsExclusao = agendaItemsExclusao;
    }

    public void setConfirmarSalvar(Boolean confirmarSalvar) {
        this.confirmarSalvar = confirmarSalvar;
    }
}
