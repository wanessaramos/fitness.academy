package br.fitness.academy.service;

import java.time.LocalDateTime;
import br.fitness.academy.model.SenhaTemporaria;

public class ValidadorSenhaTemporaria {
	
	public static boolean validarSenhaTemporaria(SenhaTemporaria senha,String tempsenha) {
		
		LocalDateTime now = LocalDateTime.now();
        String data = now.toString();
        String dataTime = data.substring(0,10);
		
        if(senha.getSenha().equals(tempsenha)) {
        	System.out.println(senha.getSenha()+" "+tempsenha);
        	if(senha.getData_fim().equals(dataTime)) {
    			return true;
    		}
        }
		
		return false;
	
	}
}
