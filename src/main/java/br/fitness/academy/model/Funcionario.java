package br.fitness.academy.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Funcionario {
	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		protected long id;
		
		@Column(unique = true)
		protected String matricula;
		
		@Column(nullable=false, length=100)
		protected String nome;
		
		@Column(nullable=false,length=25)
		protected String telefone;
		
		@Column(nullable=false,length=20)
		protected String rg;
		
		@Column(nullable=false,length=14)
		protected  String cpf;
		
		@Column(nullable=false)
		protected String email;
		
		@Column(nullable = true, length = 64)
		protected String photos;
		
		@Column(nullable=false)
		protected double salario;
		
		@OneToOne(cascade = CascadeType.REMOVE,  orphanRemoval = true)
		protected Endereco endereco;
		
		@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
		@JoinColumn(name="id_funcionario")
		protected Set<Pagamento> pagamentos = new HashSet<>();
		
		public Funcionario() {
			super();
		}
		
		public Funcionario(String matricula, String nome, String telefone, String rg, String cpf, String email,
				double salario) {
			super();
			this.matricula = matricula;
			this.nome = nome;
			this.telefone = telefone;
			this.rg = rg;
			this.cpf = cpf;
			this.email = email;
			this.salario = salario;
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
		
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
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

