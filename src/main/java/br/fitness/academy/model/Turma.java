package br.fitness.academy.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="turma")
public class Turma {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false,length=100)
	private String nome;
	
	@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name="id_turma")
	private Set<Aluno> alunos = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name="professor_turma",
	joinColumns = {
			@JoinColumn(name = "id_professor", referencedColumnName = "id")},
	inverseJoinColumns = {
			@JoinColumn(name = "id_turma", referencedColumnName = "id")})
	private Set<Professor> professores  = new HashSet<>();
	
	public Turma() {
		super();
	
	}
	public Turma(String nome) {
		super();
		this.nome = nome;
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
	
	public void addAluno(Aluno aluno) {
		this.alunos.add(aluno);
	}
	
	public void removeAluno(Aluno aluno) {
		alunos.remove(aluno);	
	}
	
	public Set<Aluno> getAlunos() {
		return alunos;
	}
	
	public void setAlunos(Set<Aluno> alunos) {
		this.alunos = alunos;
	}
	
	public Set<Professor> getProfessores() {
		return professores;
	}
	
	public void setProfessores(Set<Professor> professores) {
		this.professores = professores;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public boolean adicionarAluno(Aluno aluno) {
		if(alunos.contains(aluno)) {
			return false;
		}else {
			alunos.add(aluno);
			return true;
		}
	}
	
	public boolean removerAluno(Aluno aluno) {
		if(alunos.contains(aluno)) {
			alunos.remove(aluno);
			return true;
		}else {
			return false;
		}
	}
	
	public boolean addProfessor(Professor  professor) {
		if(professores.contains(professor)) {
			return false;
		}else {
			professores.add(professor);
			return true;
		}
	}
	
	public boolean removeProfessor(Professor  professor) {
		if(professores.contains(professor)) {
			professores.remove(professor);
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
		Turma other = (Turma) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Turma [id=" + id + ", nome=" + nome + ", alunos=" + alunos + ", professores=" + professores + "]";
	}
	
}

