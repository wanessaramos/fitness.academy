package br.fitness.academy.controller;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.fitness.academy.model.Endereco;
import br.fitness.academy.model.Professor;
import br.fitness.academy.model.Turma;
import br.fitness.academy.model.Turno;
import br.fitness.academy.repository.EnderecoRepository;
import br.fitness.academy.repository.FuncionarioRepository;
import br.fitness.academy.repository.ProfessorRepository;
import br.fitness.academy.repository.TurmaRepository;
import br.fitness.academy.repository.TurnoRepository;
import br.fitness.academy.service.ClearFolder;
import br.fitness.academy.service.FileUploadUtil;

@Controller
@RequestMapping("/professor")
public class ProfessorController {
	@Autowired
	ProfessorRepository professorRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	@Autowired
	TurmaRepository turmaRepository;
	
	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	@Autowired
	TurnoRepository turnoRepository;
	
	@Autowired private JavaMailSender mailSender;
	
	@RequestMapping(value = {"/new"})
	public String newProfessor(ModelMap model) {
		
		Professor funcionario = new Professor();
		model.addAttribute("funcionario", funcionario);
	
		Endereco endereco = new Endereco();
		model.addAttribute("endereco", endereco);
		
		return "professor/form";
	}
	
	@RequestMapping(value = {"/save"}, method = { RequestMethod.POST, RequestMethod.GET})
	public String saveProfessor(@Valid @ModelAttribute Professor funcionario, Endereco endereco,
			@RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes attr,
			BindingResult result, ModelMap model) throws Exception{
		
		try {
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			funcionario.setPhotos(fileName);
			
			Endereco enderecoProfessor= enderecoRepository.save(endereco);
			
	 		Calendar calendar = GregorianCalendar.getInstance();
			int anoAtual = calendar.get(Calendar.YEAR);
			//System.out.println("count"+professorRepository.count());
			
			List<Professor> professores = professorRepository.findAll();
			Professor ultimoCadastrado = professores.get(professores.size()-1);
			String matricula = ultimoCadastrado.getMatricula();
			String modificada = matricula.substring(4, matricula.length());
			int modificadaConvertida = Integer.parseInt(modificada);
			int digitoMatricula = modificadaConvertida + 1;
			funcionario.setMatricula(anoAtual+""+digitoMatricula); 
			
			
			funcionario.setEndereco(enderecoProfessor );
				
			Professor professorSaved  = professorRepository.save(funcionario);
			
			String uploadDir = "funcionario-photos/" + professorSaved.getId();
		    FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
			attr.addFlashAttribute("aviso","sucesso salvar");
			
			return "redirect:/professor/edit-"+funcionario.getId()+"-professor";
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro salvar");
			
			return "professor/form";		
		}	
	}
	
	@RequestMapping(value = {"/edit-{id}-professor"})
	public String editProfessor(@PathVariable("id") Long id, ModelMap model) {
	
		Professor funcionario = professorRepository.getOne(id);
		model.addAttribute("funcionario", funcionario);
			
		return "professor/formUpdate";
	}
	
	@RequestMapping(value = {"/update"},  method = { RequestMethod.POST, RequestMethod.GET})
	public String updateProfessor(@Valid Professor funcionario, @RequestParam("fileImage") MultipartFile multipartFile,
			BindingResult result, ModelMap model, RedirectAttributes attr) throws Exception {
		
		try {
			Professor p = professorRepository.getOne(funcionario.getId());
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			funcionario.setPhotos(fileName);
			
			if(fileName.equals("") || fileName == null) {
				funcionario.setPhotos(p.getPhotos());
				System.out.println("professor "+ p.getPhotos());
			}
		
			 System.out.println("filename"+fileName);	
		   
			if(!funcionario.getEndereco().equals(p.getEndereco())) {
				Endereco nextEndereco = enderecoRepository.getOne(p.getEndereco().getId());
				funcionario.getEndereco().setId(nextEndereco.getId());
				enderecoRepository.saveAndFlush(funcionario.getEndereco());
				//System.out.println(professor.getEndereco());
			}
			
			Professor professorSaved  = professorRepository.saveAndFlush(funcionario);
			
			if(!fileName.equals("")) {
				ClearFolder.remover(new File("funcionario-photos/"+professorSaved.getId()));
				String uploadDir = "funcionario-photos/" + professorSaved.getId();  
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}	
			
			//System.out.println("a"+professor);
			//professorRepository.saveAndFlush(professor);
			
			attr.addFlashAttribute("aviso","sucesso salvar");
			
			return "redirect:/professor/edit-"+professorSaved.getId()+"-professor";	
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro salvar");
			
			return "/professor/formUpdate";
		}
	}
	
	@RequestMapping(value = { "/delete-{id}-professor" }, method = RequestMethod.GET)
	public String deleteProfessor(@PathVariable Long id,RedirectAttributes attr) {
		
		try {
			
			Optional <Professor> p = Optional.ofNullable(professorRepository.getOne(id));
			if(p.isPresent()) {
				ClearFolder.remover(new File("funcionario-photos/"+id));
				professorRepository.deleteById(id);
			}
			
			attr.addFlashAttribute("aviso","sucesso excluir");
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro excluir");
		}
		
		return "redirect:/professor/listar-professores";
	}
	
	@RequestMapping(value="/listar-professores", method = RequestMethod.GET)
	public ModelAndView getProfessores() {
		ModelAndView mv = new ModelAndView("professor/listar-professores");
		List<Professor> professores = professorRepository.findAll();
		mv.addObject("professores",professores);
		return mv;
	}
	
	@RequestMapping(value="/listar-turmas-{id}", method = RequestMethod.GET)
	public String turmas(@PathVariable("id") Long id, ModelMap model) {
		
		Professor funcionario = professorRepository.getOne(id);
		model.addAttribute("funcionario",funcionario);
		List<Turno> turnos = turnoRepository.findAll();
		model.addAttribute("turnos", turnos);
		Turno turno = turnos.get(0);
		model.addAttribute("turno", turno);
		Set<Turma> turmas = turno.getTurmas();
		model.addAttribute("turmas",turmas);
		
	
		return "/professor/adicionar-turmas";
	}
	
	@RequestMapping(value="/adicionar-{idTurma}-turma-{idProfessor}-professor", method = RequestMethod.GET)
	public String addTurmas(@PathVariable("idProfessor") Long idProfessor,
			@PathVariable("idTurma") Long idTurma, ModelMap model) {
		
		Professor funcionario = professorRepository.getOne(idProfessor);
		Turma turma = turmaRepository.getOne(idTurma);
		turma.addProfessor(funcionario);
		turmaRepository.saveAndFlush(turma);
		List<Turno> turnos = turnoRepository.findAll();
		model.addAttribute("turnos", turnos);
		Turno turno = turnos.get(0);
		model.addAttribute("turno", turno);
	
		return "redirect:/professor/listar-turmas-"+idProfessor;
	}
	
	@RequestMapping(value="/listar-{idProfessor}-turno-{idTurno}", method = RequestMethod.GET)
	public String listarTurmasPorTurno(@PathVariable("idProfessor") Long idProfessor,
			@PathVariable("idTurno") Long idTurno, ModelMap model) {
		
		Professor funcionario = professorRepository.getOne(idProfessor);
		model.addAttribute("funcionario", funcionario);
		List<Turno> turnos = turnoRepository.findAll();
		model.addAttribute("turnos", turnos);
		Turno turno = turnoRepository.getOne(idTurno);
		model.addAttribute("turno", turno);
		Set<Turma> turmas = turno.getTurmas();
		model.addAttribute("turmas", turmas);
		
		return "/professor/adicionar-turmas";
	}
	
	@RequestMapping(value="/listar-turmas-{id}-professor", method = RequestMethod.GET)
	public String listarTurmas(@PathVariable("id") Long id, ModelMap model) {
		
		Professor funcionario = professorRepository.getOne(id);
		model.addAttribute("funcionario",funcionario);
		Set<Turma> turmas = funcionario.getTurmas();
		model.addAttribute("turmas",turmas);
	
		
		return "/professor/listar-turmas";
	}
	
	@RequestMapping(value = {"/"})
	public String login(ModelMap model) {
		
		Professor funcionario = new Professor();
		model.addAttribute("funcionario", funcionario);
		
		return "professor/index";
	}
	
	@RequestMapping(value = {"/redefinir-senha"})
	public String redefinirSenha(ModelMap model) {
		
		Professor funcionario = new Professor();
		model.addAttribute("funcionario", funcionario);
		
		return "professor/redefinir-senha";
	}
	
	@RequestMapping(path = "/email-send", method = RequestMethod.GET)
    public String sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Hello from Spring Boot Application");
        message.setTo("kwrs1@ifal.edu.br");
        message.setFrom("kwrs1@ifal.edu.br");

        try {
            mailSender.send(message);
            return "/professor/index";
        } catch (Exception e) {
            e.printStackTrace();
            return "/professor/index";
        }
    }

}
