package br.gov.pa.igeprev.siaag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="TAB_PERFIL")
public class Perfil {
	@Id
	private Integer id;

	@Column(nullable=false,length=20)
	private String descricao;
	
	public Perfil(int id) {
		this.id = id;
	}
}
