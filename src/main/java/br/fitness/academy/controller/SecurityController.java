package br.fitness.academy.controller;

import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.fitness.academy.model.Aluno;
import br.fitness.academy.model.Role;
import br.fitness.academy.model.SenhaTemporaria;
import br.fitness.academy.model.Usuario;
import br.fitness.academy.repository.AlunoRepository;
import br.fitness.academy.repository.SenhaTemporariaRepository;
import br.fitness.academy.repository.UsuarioRepository;
import br.fitness.academy.security.AutenticacaoService;
import br.fitness.academy.service.GeradorDeSenhaTemporaria;
import br.fitness.academy.service.ValidadorSenhaTemporaria;

@Controller
public class SecurityController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private SenhaTemporariaRepository senhaTemporariaRepository;
	
	@Autowired private JavaMailSender mailSender;
	
	@RequestMapping(value = {"/"})
	public String index(ModelMap model) {
		String userName = getUsuarioLogado();
		System.out.println("logado"+ userName);
		Usuario usuario = usuarioRepository.findByLogin(userName);
		if(usuario != null) {
			Aluno aluno = alunoRepository.findByEmail(userName);
			if(usuario.getRole().getNome().equals("ROLE_USUARIO")) {
				return "redirect:/cronograma/aluno-"+aluno.getId()+"-listar-cronogramas";
			}else {
				return "/index";
			}
		}
		return "/login";
	}
	
	public static String getUsuarioLogado() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			return ((UserDetails)principal).getUsername();
		}else {
			return principal.toString();
		}
	}
	
	
	@RequestMapping(value = {"/login"},  method = { RequestMethod.POST, RequestMethod.GET})
	public String login(ModelMap model) {
		String userName = getUsuarioLogado();
		Usuario usuario = usuarioRepository.findByLogin(userName);
		if(usuario == null) {
			return "/login";
		}else {
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/novoUsuario")
	public String novoUsuario(Model model) {
		Usuario usuario = new Usuario() ;
		model.addAttribute("usuario",usuario);
		return "/novoUsuario";
	}
	
	@PostMapping("/salvarUsuario") 
	public String salvarUsuario(@ModelAttribute("usuario")
		@Valid Usuario usuario, BindingResult result,RedirectAttributes attr) {
		
		Usuario u = usuarioRepository.findByLogin(usuario.getLogin());
		System.out.println(u);
		if(u != null) {
			result.rejectValue("login", null, "Já existe cadastro com este e-mail");
		}
		
		if (result.hasErrors()) {
			attr.addFlashAttribute("mensagem", "Erro ao gerar novo usuário.");
			return "novoUsuario";
		}
			autenticacaoService.salvarUsuario(usuario);
			attr.addFlashAttribute("mensagem","Usuário cadastrado com sucesso!");
			
			return "redirect:/login";
	}
	
	@RequestMapping(value = {"/update"},  method = { RequestMethod.POST, RequestMethod.GET})
	public String setPermissao(@Valid Usuario usuario, RedirectAttributes attr,
			BindingResult result, ModelMap model) throws Exception {
		autenticacaoService.salvarUsuario(usuario);
		
		return "/usuario/form";
		
	}
	
	@RequestMapping(value="/listar-usuarios", method = RequestMethod.GET)
	public ModelAndView getUsuarios() {
		ModelAndView mv = new ModelAndView("usuario/listar-usuarios");
		List<Usuario> usuarios = usuarioRepository.findAll();
		mv.addObject("usuarios",usuarios);
		return mv;
	}
	
	@RequestMapping(value = {"/edit-{id}-usuario"})
	public String editUsuario(@PathVariable("id") Long id, ModelMap model) {
			
		Usuario usuario = usuarioRepository.getOne(id);
		model.addAttribute("usuario", usuario);
		System.out.println("usuario"+usuario);
		model.addAttribute("roles", Arrays.asList(Role.ALL));
		
		return "/usuario/form";
	}
	
	@RequestMapping(value = { "/delete-{id}-usuario" }, method = RequestMethod.GET)
	public String deleteAluno(@PathVariable Long id,  RedirectAttributes attr) {
		try {
			usuarioRepository.deleteById(id);
			
			attr.addFlashAttribute("aviso","sucesso excluir");
			
			return "redirect:/listar-usuarios";	
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro excluir");
			
			return "redirect:/listar-usuarios";
		}
	
	}
	
	@GetMapping("/esqueciSenha")
	public String esqueciSenha() {
		return "/esqueciSenha";
	}
	
	@RequestMapping(value = {"/recuperarSenha"},  method = { RequestMethod.POST, RequestMethod.GET})
    public String sendMail(@RequestParam String email, RedirectAttributes attr) {
		
		Usuario usuario = usuarioRepository.findByLogin(email);
		if(usuario != null) {
			
			SenhaTemporaria senha = GeradorDeSenhaTemporaria.criarSenhaAleatoria(usuario);
			SenhaTemporaria getSenha = senhaTemporariaRepository.findByUsuario(usuario);
			senha.setId(getSenha.getId());
			SenhaTemporaria newSenhaTemp = senhaTemporariaRepository.saveAndFlush(senha);
			
			SimpleMailMessage message = new SimpleMailMessage();
	        message.setText("Ola "+usuario.getNome()+" sua senha temporaria: "+newSenhaTemp.getSenha());
	        message.setTo("kwrs1@ifal.edu.br");
	        message.setFrom(email);
	        
	        
	        try {
	            mailSender.send(message);
	            return "/redefinirSenha";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "/login";
	        }
			
		}
			attr.addFlashAttribute("mensagem","Email não cadastrado!");
			
			return "/esqueciSenha";
			
    }

	@RequestMapping(value = {"/redefinirSenha"})
	public String redefinirSenha(RedirectAttributes attr) {
		
		attr.addFlashAttribute("mensagem","Verifique seu e-mail");
		
		return "/redefinirSenha";
	}
	
	@RequestMapping(value = {"/modificarSenha"},  method = { RequestMethod.POST, RequestMethod.GET})
	public String modificarSenha(@RequestParam String username, RedirectAttributes attr, 
			@RequestParam String newsenha, @RequestParam String tempsenha, ModelMap model) throws Exception {
		
		Usuario user = usuarioRepository.findByLogin(username);
		if(user != null) {
			SenhaTemporaria senhaTemp = senhaTemporariaRepository.findByUsuario(user);
			if(senhaTemp != null) {
				boolean verificador = ValidadorSenhaTemporaria.validarSenhaTemporaria(senhaTemp,tempsenha);
				System.out.println(verificador);
				if(verificador) {
					autenticacaoService.modificarSenha(user, newsenha);
					
					return "/login";
				}
			}
		
			attr.addFlashAttribute("mensagem","Erro ao tentar modificar senha !");
		}
		return "/redefinirSenha";
	}
	
}
