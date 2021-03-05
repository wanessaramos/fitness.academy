package br.fitness.academy.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="aluno")
public class Aluno {

		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
		private long id;
		
		@Column(unique = true)
		private String matricula;
		
		@Column(nullable=false,length=100)
		private String nome;
		
		@Column(nullable=false,length=10)
		private String login;
		
		@Column(nullable=false, length=10)
		private String senha;
		
		@Column(nullable=false,length=14)
		private String cpf;
		
		@Column(nullable=false,length=20)
		private String rg;
		
		@Column(nullable=false,length=25)
		private String telefone;
		
		@Column(nullable = true, length = 64)
		private String photos;
		 
		@OneToOne(cascade = CascadeType.REMOVE,  orphanRemoval = true)
		private Endereco endereco;
		
		@OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true, fetch = FetchType.LAZY)
		@JoinColumn(name="id_aluno")
		private Set<Mensalidade> mensalidades = new HashSet<>();
		
		@JsonIgnore
		@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true ,fetch = FetchType.LAZY)
		@JoinColumn(name = "id_aluno")
		private Set<Cronograma> cronogramas  = new HashSet<>();

		public Aluno() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		public Aluno(String matricula, String nome, String login, String senha, String cpf, String rg, String telefone,
				Endereco endereco) {
			super();
			this.matricula = matricula;
			this.nome = nome;
			this.login = login;
			this.senha = senha;
			this.cpf = cpf;
			this.rg = rg;
			this.telefone = telefone;
			this.endereco = endereco;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getMatricula() {
			return matricula;
		}

		public void setMatricula(String matricula) {
			this.matricula = matricula;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}

		public String getCpf() {
			return cpf;
		}

		public void setCpf(String cpf) {
			this.cpf = cpf;
		}

		public String getRg() {
			return rg;
		}

		public void setRg(String rg) {
			this.rg = rg;
		}

		public Endereco getEndereco() {
			return endereco;
		}
		
		public void setEndereco(Endereco endereco) {
			this.endereco = endereco;
		}
		
		public Set<Mensalidade> getMensalidades() {
			return mensalidades;
		}

		public void setMensalidades(Set<Mensalidade> mensalidades) {
			this.mensalidades = mensalidades;
		}

		public String getTelefone() {
			return telefone;
		}

		public void setTelefone(String telefone) {
			this.telefone = telefone;
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
	         
	        return "/aluno-photos/" + id + "/" + photos;
	    }

		public Set<Cronograma> getCronogramas() {
			return cronogramas;
		}

		public void setCronogramas(Set<Cronograma> cronogramas) {
			this.cronogramas = cronogramas;
		}

		public boolean adicionarCronograma(Cronograma  cronograma) {
			if(cronogramas.contains( cronograma)) {
				return false;
			}else {
				 cronogramas.add( cronograma);
				return true;
			}
		}
		
		public boolean removerCronograma(Cronograma  cronograma) {
			if( cronogramas.contains(cronograma)) {
				 cronogramas.remove( cronograma);
				return true;
			}else {
				return false;
			}
		}
		
		public boolean addMensalidade(Mensalidade  mensalidade) {
			if(mensalidades.contains(mensalidade)) {
				return false;
			}else {
				mensalidades.add(mensalidade);
				return true;
			}
		}
		
		public boolean removeMensalidade(Mensalidade  mensalidade) {
			if(mensalidades.contains(mensalidade)) {
				mensalidades.remove(mensalidade);
				return true;
			}else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
			result = prime * result + ((cronogramas == null) ? 0 : cronogramas.hashCode());
			result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
			result = prime * result + (int) (id ^ (id >>> 32));
			result = prime * result + ((login == null) ? 0 : login.hashCode());
			result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
			result = prime * result + ((mensalidades == null) ? 0 : mensalidades.hashCode());
			result = prime * result + ((nome == null) ? 0 : nome.hashCode());
			result = prime * result + ((rg == null) ? 0 : rg.hashCode());
			result = prime * result + ((senha == null) ? 0 : senha.hashCode());
			result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
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
			Aluno other = (Aluno) obj;
			if (cpf == null) {
				if (other.cpf != null)
					return false;
			} else if (!cpf.equals(other.cpf))
				return false;
			if (cronogramas == null) {
				if (other.cronogramas != null)
					return false;
			} else if (!cronogramas.equals(other.cronogramas))
				return false;
			if (endereco == null) {
				if (other.endereco != null)
					return false;
			} else if (!endereco.equals(other.endereco))
				return false;
			if (id != other.id)
				return false;
			if (login == null) {
				if (other.login != null)
					return false;
			} else if (!login.equals(other.login))
				return false;
			if (matricula == null) {
				if (other.matricula != null)
					return false;
			} else if (!matricula.equals(other.matricula))
				return false;
			if (mensalidades == null) {
				if (other.mensalidades != null)
					return false;
			} else if (!mensalidades.equals(other.mensalidades))
				return false;
			if (nome == null) {
				if (other.nome != null)
					return false;
			} else if (!nome.equals(other.nome))
				return false;
			if (rg == null) {
				if (other.rg != null)
					return false;
			} else if (!rg.equals(other.rg))
				return false;
			if (senha == null) {
				if (other.senha != null)
					return false;
			} else if (!senha.equals(other.senha))
				return false;
			if (telefone == null) {
				if (other.telefone != null)
					return false;
			} else if (!telefone.equals(other.telefone))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Aluno [id=" + id + ", matricula=" + matricula + ", nome=" + nome + ", login=" + login + ", senha="
					+ senha + ", cpf=" + cpf + ", rg=" + rg + ", telefone=" + telefone + ", endereco=" + endereco + "]";
		}	
}
