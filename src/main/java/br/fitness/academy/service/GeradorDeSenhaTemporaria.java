package br.fitness.academy.service;

import java.time.LocalDateTime;  
import br.fitness.academy.model.SenhaTemporaria;
import br.fitness.academy.model.Usuario;

public class GeradorDeSenhaTemporaria {
	
	 public static String gerarSenhaAleatoria() {
	        int qtdeMaximaCaracteres = 8;
	        String[] caracteres = { "a", "1", "b", "2", "4", "5", "6", "7", "8",
	                "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
	                "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
	                "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I",
	                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
	                "V", "W", "X", "Y", "Z" };
	      
	        StringBuilder senha = new StringBuilder();

	        for (int i = 0; i < qtdeMaximaCaracteres; i++) {
	            int posicao = (int) (Math.random() * caracteres.length);
	            senha.append(caracteres[posicao]);
	        }
	            
	        return senha.toString();
	    }
	 
	public static SenhaTemporaria criarSenhaAleatoria(Usuario usuario) {
		
		 	SenhaTemporaria tempSenha = new SenhaTemporaria();
	        LocalDateTime now = LocalDateTime.now();
	        String data = now.toString();
	        String dataTime = data.substring(0,10);
	        tempSenha.setData_fim(dataTime);
	        tempSenha.setSenha(gerarSenhaAleatoria());
	        tempSenha.setUsuario(usuario);
	     
	        return tempSenha;
	}

}
