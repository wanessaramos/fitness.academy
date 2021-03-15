package br.fitness.academy.model;

public enum Role {

	ROLE_PROFESSOR("professor"),
	ROLE_USUARIO("usuario"),
	ROLE_ADMIN("admin");

	private final String nome;
	
	public static final Role[] ALL = {ROLE_PROFESSOR,ROLE_USUARIO,ROLE_ADMIN};
	
	Role(final String  nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
}
