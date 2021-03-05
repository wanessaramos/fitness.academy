package br.fitness.academy.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="exercicio")
public class Exercicio {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false, length=100)
	private String nome;
	
	@Column(nullable=false, length=100)
	private String serie;
	
	@Column(nullable=false, length=100)
	private String repeticoes;
	
	@Column(nullable=false, length=100)
	private String carga;
	
	@Column(nullable=false)
	private int intervalo;
	
	@ManyToMany(mappedBy="exercicios")
	private List<Cronograma> cronograma;
	
	public Exercicio() {
		super();	
	}
	
	public Exercicio(String nome, String serie, String repeticoes, String carga, int intervalo) {
		super();
		this.nome = nome;
		this.serie = serie;
		this.repeticoes = repeticoes;
		this.carga = carga;
		this.intervalo = intervalo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getRepeticoes() {
		return repeticoes;
	}

	public void setRepeticoes(String repeticoes) {
		this.repeticoes = repeticoes;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCarga() {
		return carga;
	}
	
	public void setCarga(String carga) {
		this.carga = carga;
	}
	
	public int getIntervalo() {
		return intervalo;
	}
	
	public void setIntervalo(int intervalo) {
		this.intervalo = intervalo;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carga == null) ? 0 : carga.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + intervalo;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((repeticoes == null) ? 0 : repeticoes.hashCode());
		result = prime * result + ((serie == null) ? 0 : serie.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exercicio other = (Exercicio) obj;
		if (carga == null) {
			if (other.carga != null)
				return false;
		} else if (!carga.equals(other.carga))
			return false;
		if (id != other.id)
			return false;
		if (intervalo != other.intervalo)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (repeticoes == null) {
			if (other.repeticoes != null)
				return false;
		} else if (!repeticoes.equals(other.repeticoes))
			return false;
		if (serie == null) {
			if (other.serie != null)
				return false;
		} else if (!serie.equals(other.serie))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Exercicio [id=" + id + ", nome=" + nome + ", serie=" + serie + ", repeticoes=" + repeticoes + ", carga="
				+ carga + ", intervalo=" + intervalo + "]";
	}
}

