/*
 *  Bean
 *
 *  1.0.0
 *
 *  © Copyright 2017, Tribunal de Justiça do Estado Roraima
 *  http://www.tjrr.jus.br
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.bean;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.gov.pa.igeprev.siaag.enumeration.EstadoEnum;
import br.gov.pa.igeprev.siaag.enumeration.PaginasEnum;
import br.gov.pa.igeprev.siaag.enumeration.PerfilEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Endereco;
import br.gov.pa.igeprev.siaag.model.EnderecoWeb;
import br.gov.pa.igeprev.siaag.model.Pessoa;
import br.gov.pa.igeprev.siaag.model.Usuario;
import br.gov.pa.igeprev.siaag.model.external.VwCadastroSiaag;
import br.gov.pa.igeprev.siaag.service.ConsultaCEPService;
import br.gov.pa.igeprev.siaag.service.PessoaService;
import br.gov.pa.igeprev.siaag.service.UsuarioService;
import br.gov.pa.igeprev.siaag.service.external.VwCadastroSiaagService;
import br.gov.pa.igeprev.siaag.utils.DataUtils;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Super Classe para os Managed Bean.
 *
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 04/11/2016
 */
public class Bean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Properties properties = new Properties();
    private static final String MESSAGEM_PROPERTIES = "../i18n/pt_BR.properties";
    private PerfilEnum perfilUsuarioEnum = PerfilEnum.CLIENTE;

    private Boolean pesquisouCpf = false;

    @Autowired
    private ConsultaCEPService consultaCEPService;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private VwCadastroSiaagService vwCadastroSiaagService;

    protected StreamedContent file;
    private UploadedFile uploadedFile;

    public String getAppURL() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        return url.split("/pages/")[0];
    }

    public String redirectContext(String pagina) {
        String pageRedirect = null;
        pageRedirect = (!pagina.contains("?faces-redirect=true")) ? pagina.concat("?faces-redirect=true") : pagina;
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(Boolean.TRUE);
        return pageRedirect;
    }

    public void redirect(String pagina) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getExternalContext().getFlash().setKeepMessages(Boolean.TRUE);
            ctx.getExternalContext().redirect(pagina);
        } catch (IOException e) {
            showErrorMessage(getMessage("ErroRedirecionamento"));
        }
    }

    public void redirectWithMessage(String pagina, String mensagem) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getExternalContext().getFlash().setKeepMessages(Boolean.TRUE);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msg.success"), mensagem));
            ctx.getExternalContext().redirect(pagina);
        } catch (IOException e) {
            showErrorMessage(getMessage("ErroRedirecionamento"));
        }
    }

    public void showSuccessMessage(String mensagem) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msg.success"), mensagem));
    }

    void showSuccessMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msg.success"), getMessage("OperacaoRealizadaComSucesso")));
    }

    public void showAlertMessage(String mensagem) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("msg.alert"), mensagem));
    }

    public void showErrorMessage(String mensagem) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msg.error"), mensagem));
    }

    /**
     * Mostra a mensagem de erro genérica do sistema.
     */
    public void showErrorMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msg.error"),
                getMessage("mensagemErroDesconhecido")));
    }

    public void showErrorMessage(String clientId, String mensagem) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msg.error"), mensagem));
    }

    public String getMessage(String key) {
        try {
            InputStream stream = Bean.class.getResourceAsStream(MESSAGEM_PROPERTIES);
            properties.load(stream);
            return MessageFormat.format(properties.getProperty(key), (Object) null);
        } catch (Exception e) {
            return null;
        }
    }

    public String getMessage(String key, Object... params) {
        try {
            InputStream stream = Bean.class.getResourceAsStream(MESSAGEM_PROPERTIES);
            properties.load(stream);
            return MessageFormat.format(properties.getProperty(key), params);
        } catch (Exception e) {
            return null;
        }
    }

    public StreamedContent getFile() {
        return file;
    }


    /**
     * Realiza o download do documento que foi enviado e abre em uma nova
     * página.
     *
     * @param arquivo
     */
    public void downloadArquivo(byte[] arquivo, String nome) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=\\" + nome);
        ServletOutputStream streamDeSaida = response.getOutputStream();
        streamDeSaida.write(arquivo);
        streamDeSaida.close();
        facesContext.responseComplete();
    }

    /**
     * Retorna a data Calendar em String
     *
     * @param cal
     * @return Calendar
     */
    public String getDataString(Calendar cal) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        String formatted = format1.format(cal.getTime());
        return formatted;
    }

    public Date minDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, 1, 1);
        return calendar.getTime();
    }

    public Date currentDate() {
        return new Date();
    }

    public String currentDateFormatado() {
        return DataUtils.Formatada(new Date());
    }

    public String currentDatePorExtenso() {
        return DataUtils.DataPorExtenso(new Date());
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void getEnderecoCep(Endereco endereco) {
        try {
            if (endereco.getCep().length() == 8) {
                EnderecoWeb enderecoWeb = consultaCEPService.getEnderecoWeb(endereco.getCep());
                if (enderecoWeb != null) {
                    endereco.setBairro(enderecoWeb.getBairro());
                    endereco.setCidade(enderecoWeb.getLocalidade());
                    endereco.setComplemento(enderecoWeb.getComplemento());
                    endereco.setLogradouro(enderecoWeb.getLogradouro());
                    endereco.setUf(enderecoWeb.getUf());
                }
            }
        } catch (Exception e) {
            showAlertMessage(getMessage(e.getMessage()));
        }
    }

    public Usuario buscaVerificaUsuarioExistente(Usuario usuario) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCpf(usuario.getPessoa().getCpf());
        try {
            if (usuarioService.usuarioMesmoLogin(usuario)) {
                usuario.getPessoa().setCpf("");
                showErrorMessage(getMessage("mensagemERROCpfExistente"));
            } else {
                usuario.setPessoa(buscaPessoa(pessoa));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage();
        }
        return usuario;
    }

    public Pessoa buscaPessoa(Pessoa pessoa) {
        Pessoa pessoaSiaag = buscaPessoaSiaag(pessoa);
        Pessoa pessoaEprev;
        if (pessoaSiaag != null && pessoaSiaag.getId() != null) {
            pessoa = pessoaSiaag;
        } else {
            pessoaEprev = buscaPessoaEprev(pessoa);
            if (pessoaEprev != null && pessoaEprev.getId() != null) {
                pessoa = pessoaEprev;
            }
        }
        pesquisouCpf = true;
        if (pessoa != null && (pessoa.getNome() == null || (pessoa.getNome() != null && pessoa.getNome().isEmpty()))) {
            showAlertMessage(getMessage("mensagemCPFNaoEncontrado"));
        }
        return pessoa;
    }

    public Pessoa buscaPessoaSiaag(Pessoa pessoa) {
        try {
            CPFValidator cpfValidator = new CPFValidator();
            if (pessoa.getCpf() != null && !pessoa.getCpf().isEmpty() && pessoa.getCpf().length() == 11) {
                cpfValidator.assertValid(pessoa.getCpf());
                Pessoa pessoaExistente = pessoaService.findByCpf(pessoa.getCpf());
                if (pessoaExistente != null) {
                    pessoa = pessoaExistente;
                }
                pesquisouCpf = true;
            }
            return pessoa;
        } catch (InvalidStateException e) {
            pesquisouCpf = false;
            pessoa = new Pessoa();
            showErrorMessage(getMessage("CPFInvalido"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pessoa;
    }

    public Pessoa buscaPessoaEprev(Pessoa pessoa) {
        try {
            List<VwCadastroSiaag> vwCadastroSiaags = new ArrayList<>(vwCadastroSiaagService.findByCpf(pessoa.getCpf()));
            if (vwCadastroSiaags != null && !vwCadastroSiaags.isEmpty()) {
                pessoa.setTelefone1(vwCadastroSiaags.get(0).getTelefone());
                pessoa.setCelular(vwCadastroSiaags.get(0).getTelefone());
                pessoa.setDataNascimento(vwCadastroSiaags.get(0).getDataNascimento());
                pessoa.setEmail(vwCadastroSiaags.get(0).getEmail());
                pessoa.setNome(vwCadastroSiaags.get(0).getNome());
                Endereco endereco = new Endereco();
                endereco.setCep(vwCadastroSiaags.get(0).getCep());
                endereco.setBairro(vwCadastroSiaags.get(0).getBairro());
                endereco.setCidade(vwCadastroSiaags.get(0).getMunicipio());
                endereco.setComplemento(vwCadastroSiaags.get(0).getComplemento());
                endereco.setLogradouro(vwCadastroSiaags.get(0).getLogradouro());
                endereco.setUf(vwCadastroSiaags.get(0).getUf());
                pessoa.setEndereco(endereco);
            }
            pesquisouCpf = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pessoa;
    }


    public void redirect(PaginasEnum pagina) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (pagina.equals(PaginasEnum.LOGIN)) {
                ctx.getExternalContext().redirect("login.xhtml");
            } else {
                ctx.getExternalContext().redirect(pagina.getMsg());
            }
        } catch (IOException e) {
            showErrorMessage(getMessage("mensagemErroRedirecionamento"));
        }
    }

    public List<String> getHorasAtendimento() {
        int inicioHora = 8;
        int inicioMinuto = 0;
        int fimHora = 14;
        int fimMinuto = 55;
        List<String> horarios = new ArrayList<>();
        for (int i = inicioHora; i <= fimHora; i++) {
            for (int j = inicioMinuto; j <= fimMinuto; j = j + 5) {
                String horario = "";
                if (i <= 9) {
                    horario = "0" + i + ":";
                } else {
                    horario = i + ":";
                }
                if (j <= 9) {
                    horario += "0" + j;
                } else {
                    horario += j;
                }
                horarios.add(horario);
            }
        }
        return horarios;
    }

    public EstadoEnum[] getEstados() {
        return EstadoEnum.values();
    }

    public PerfilEnum getPerfilUsuarioEnum() {
        return perfilUsuarioEnum;
    }

    public Boolean getPesquisouCpf() {
        return pesquisouCpf;
    }

    public void setPesquisouCpf(Boolean pesquisouCpf) {
        this.pesquisouCpf = pesquisouCpf;
    }
}
