package br.gov.pa.igeprev.siaag.model;

import br.com.caelum.stella.format.CPFFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TAB_USUARIO")
@DynamicUpdate
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 11,nullable = false)
    private String login;

    @Column(length = 255, nullable = false)
    private String senha;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name="PRIMEIRO_ACESSO", nullable = false)
    private Boolean primeiroAcesso = false;

    @Column(length = 20)
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "id_perfil", referencedColumnName = "id", nullable = false)
    private Perfil perfil = new Perfil();

    @ManyToOne
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    private Pessoa pessoa = new Pessoa();

    @ManyToOne
    @JoinColumn(name = "id_ponto_atendimento_setor", referencedColumnName = "id")
    private PontoAtendimentoSetor pontoAtendimentoSetor = new PontoAtendimentoSetor();

    public Usuario(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.senha = usuario.getSenha();
        this.ativo = usuario.getAtivo();
        this.perfil = usuario.getPerfil();
        this.pessoa = usuario.getPessoa();
        this.primeiroAcesso = usuario.getPrimeiroAcesso();
        this.pontoAtendimentoSetor = usuario.getPontoAtendimentoSetor();
    }

    public String loginFormatado() {
        if (login != null && !login.isEmpty()) {
            return new CPFFormatter().format(login);
        }
        return this.login;
    }
}
