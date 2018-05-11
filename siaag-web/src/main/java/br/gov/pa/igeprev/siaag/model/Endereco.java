package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_ENDERECO")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 8)
    private String cep;

    @Column(length = 200)
    private String logradouro;

    @Column(length = 10)
    private String numero;

    @Column(length = 200)
    private String complemento;

    @Column(length = 50)
    private String bairro;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String uf;

    public String formatado() {
        try {
            return logradouro + ", " + numero + " - " + bairro + ", " + cidade + "-" + uf + ", " + cepFormatado();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String cepFormatado(){
        try{
            if(cep != null && cep.length() == 8){
                StringBuilder sb = new StringBuilder(cep);
                return sb.insert(5,"-").toString();
            }else{
                return cep;
            }
        }catch (Exception e){
            e.printStackTrace();
            return cep;
        }
    }
}
