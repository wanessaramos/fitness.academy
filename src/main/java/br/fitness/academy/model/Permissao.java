package br.fitness.academy.model;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;


@Entity
public class Permissao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false,length=100)
	private String nome;
	
	public Permissao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@ManyToMany(mappedBy="permissoes")
	private Collection<Usuario> usuarios;
	
	public Permissao(String nome) {
		super();
		this.nome = nome;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Permissao [id=" + id + ", nome=" + nome + "]";
	}
	
}
