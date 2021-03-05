package br.fitness.academy.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name="professor")
public class Professor extends Funcionario{
	
	@ManyToMany(mappedBy="professores")
	private Set<Turma> turmas = new HashSet<>();

	public Professor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Professor(Set<Turma> turmas) {
		super();
		this.turmas = turmas;
	}
	
	public Professor(String matricula, String nome, String telefone, String rg, String cpf, double salario,
			Endereco endereco) {
		super(matricula, nome, telefone, rg, cpf, salario, endereco);
		this.matricula = matricula;
		this.nome = nome;
		this.telefone = telefone;
		this.rg = rg;
		this.cpf = cpf;
		this.salario = salario;
		this.endereco = endereco;
		
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	@Transient
    public String getPhotosImagePath() {
        if (photos == null || id == 0) return null;
         
        return "/funcionario-photos/" + id + "/" + photos;
    }
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Set<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(Set<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
	
	public Set<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(Set<Turma> turmas) {
		this.turmas = turmas;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}
	
	public boolean addPagamento(Pagamento  pagamento) {
		if(pagamentos.contains(pagamento)) {
			return false;
		}else {
			pagamentos.add(pagamento);
			return true;
		}
	}
	public boolean removeMensalidade(Pagamento  pagamento) {
		if(pagamentos.contains(pagamento)) {
			pagamentos.remove(pagamento);
			return true;
		}else {
			return false;
		}
	}
}

