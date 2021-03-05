package br.fitness.academy.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cronograma")
public class Cronograma {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	private Date data_inicio;
	
	@Column
	private Date data_fim;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name="cronograma_exercicio",
	joinColumns = {
			@JoinColumn(name = "id_cronograma", referencedColumnName = "id")},
	inverseJoinColumns = {
			@JoinColumn(name = "id_exercicio", referencedColumnName = "id")})
	private Set<Exercicio> exercicios = new HashSet<>();

	public Cronograma() {
		super();
	}

	public Cronograma(Date data_inicio, Date data_fim, Set<Exercicio> exercicios) {
		super();
		this.data_inicio = data_inicio;
		this.data_fim = data_fim;
		this.exercicios = exercicios;
	}

	public long getId() {
		return id;
	}

	public Date getData_inicio() {
		return data_inicio;
	}

	public void setData_inicio(Date data_inicio) {
		this.data_inicio = data_inicio;
	}

	public Date getData_fim() {
		return data_fim;
	}

	public void setData_fim(Date data_fim) {
		this.data_fim = data_fim;
	}

	public Set<Exercicio> getExercicios() {
		return exercicios;
	}

	public void setExercicios(Set<Exercicio> exercicios) {
		this.exercicios = exercicios;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean addExercicio(Exercicio exercicio) {
		if(exercicios.contains(exercicio)) {
			return false;
		}else {
			exercicios.add(exercicio);
			return true;
		}
	}
	
	public boolean removeExercicio(Exercicio exercicio) {
		if(exercicios.contains(exercicio)) {
			exercicios.remove(exercicio);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Cronograma [id=" + id + ", data_inicio=" + data_inicio + ", data_fim=" + data_fim + ", exercicios="
				+ exercicios + "]";
	}
	
}
