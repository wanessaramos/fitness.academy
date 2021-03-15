package br.fitness.academy.model;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false,length=100)
	private String nome;
	
	@Column(nullable=false)
	private String login;
	
	@Column(nullable=false,length=60)
	private String senha;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name="usuario_permissao",
	joinColumns = {
			@JoinColumn(name = "id_usuario", referencedColumnName = "id")},
	inverseJoinColumns = {
			@JoinColumn(name = "id_permissao", referencedColumnName = "id")})
	private Collection<Permissao> permissoes;
	
	@Column()
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
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

	public Collection<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Collection<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public boolean addPermissao(Permissao  permissao) {
		if(permissoes.contains(permissao)) {
			return false;
		}else {
			permissoes.add(permissao);
			return true;
		}
	}
	
	public boolean removePermissao(Permissao  permissao) {
		if(permissoes.contains(permissao)) {
			permissoes.remove(permissao);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", login=" + login + ", senha=" + senha + ", permissoes="
				+ permissoes + ", role=" + role + "]";
	}
}
