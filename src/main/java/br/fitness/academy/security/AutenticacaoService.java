package br.fitness.academy.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import br.fitness.academy.model.Permissao;
import br.fitness.academy.model.Role;
import br.fitness.academy.model.SenhaTemporaria;
import br.fitness.academy.model.Usuario;
import br.fitness.academy.repository.SenhaTemporariaRepository;
import br.fitness.academy.repository.UsuarioRepository;
import br.fitness.academy.service.GeradorDeSenhaTemporaria;

@Service
public class AutenticacaoService implements UserDetailsService{
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	private SenhaTemporariaRepository senhaTemporariaRepository;
	
	public void salvarUsuario(Usuario usuario) {

		Usuario user = usuarioRepository.getOne(usuario.getId());
		
		if(user.getId() != 0) {
			usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
			usuario.setPermissoes(Arrays.asList(new Permissao(usuario.getRole().getNome())));
			System.out.println("upload "+usuario);
			try {
				usuarioRepository.saveAndFlush(usuario);
			} catch (Exception e) {
				System.out.println("Erro ao salvar " +e);
			}
			
		}else {
			Role role = Role.ROLE_USUARIO;
			Usuario newUsuario = new Usuario();
			newUsuario.setNome(usuario.getNome());
			newUsuario.setLogin(usuario.getLogin());
			newUsuario.setRole(role);
			newUsuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
			newUsuario.setPermissoes(Arrays.asList(new Permissao(role.getNome())));
			System.out.println("salvar "+usuario);
			
			
			try {
				Usuario usuarioSaved  = usuarioRepository.save(newUsuario);
				SenhaTemporaria senha = GeradorDeSenhaTemporaria.criarSenhaAleatoria(usuarioSaved);
				senhaTemporariaRepository.save(senha);
				
			} catch (Exception e) {
				System.out.println("Erro ao salvar " +e);
			}
				
		}
	}
	
	public void modificarSenha(Usuario usuario, String senha) {
		System.out.println("Save "+usuario);
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		usuarioRepository.saveAndFlush(usuario);
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("user "+username);
		Usuario usuario = usuarioRepository.findByLogin(username);
		 
		 if(usuario == null) {
			 throw new UsernameNotFoundException("Usuário não encontrado!");
		 }
		return new User(usuario.getLogin(),usuario.getSenha(),
				mapRolesToAuthorities(usuario.getPermissoes()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities (Collection<Permissao> permissoes) {
	
		return permissoes.stream()
			.map(permissao -> new SimpleGrantedAuthority(permissao.getNome()))
			.collect(Collectors.toList());
	}
	
	
}
