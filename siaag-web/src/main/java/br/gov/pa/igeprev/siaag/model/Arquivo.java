package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_ARQUIVO")
public class Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String descricao;

    @Column(nullable = false)
    private byte[] arquivo;

    @ManyToOne
    @JoinColumn(name = "id_atendimento", referencedColumnName = "id", nullable = false)
    private Atendimento atendimento;

    public Arquivo(Integer id) {
        this.id = id;
    }

}
