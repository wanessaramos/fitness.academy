package br.fitness.academy.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name="turno")
public class Turno {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false, length=100)
	private String nome;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_turno")
	private Set <Turma> turmas  = new HashSet<>();
    
	public Turno() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Turno(String nome) {
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
	
	public Set<Turma> getTurmas() {
		return turmas;
	}
	
	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}
	
	public boolean addTurma(Turma  turma) {
		if(turmas.contains(turma)) {
			return false;
		}else {
			turmas.add(turma);
			return true;
		}
	}
	
	public boolean removeTurma(Turma  turma) {
		if(turmas.contains(turma)) {
			turmas.remove(turma);
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((turmas == null) ? 0 : turmas.hashCode());
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
		Turno other = (Turno) obj;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (turmas == null) {
			if (other.turmas != null)
				return false;
		} else if (!turmas.equals(other.turmas))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Turno [id=" + id + ", nome=" + nome + ", turmas=" + turmas + "]";
	}
}
