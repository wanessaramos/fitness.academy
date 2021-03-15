package br.fitness.academy.controller;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.fitness.academy.model.Aluno;
import br.fitness.academy.model.Cronograma;
import br.fitness.academy.model.Endereco;
import br.fitness.academy.model.Turma;
import br.fitness.academy.repository.*;
import br.fitness.academy.service.FileUploadUtil;
import br.fitness.academy.service.ClearFolder;

@Controller
@RequestMapping("/aluno")
public class AlunoController {
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private TurmaRepository turmaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@RequestMapping(value = {"/new"})
	public String newAluno(ModelMap model) {
		
		Aluno aluno = new Aluno();
		model.addAttribute("aluno", aluno);
		
		Endereco endereco = new Endereco();
		model.addAttribute("endereco", endereco);
		
		List<Turma> turmas = turmaRepository.findAll();
		model.addAttribute("turmas", turmas);
		
		return "aluno/form";
	}
	
	@RequestMapping(value = {"/save"}, method = { RequestMethod.POST, RequestMethod.GET})
	public String saveAluno(@Valid @ModelAttribute Aluno aluno, Endereco endereco,RedirectAttributes attr,
			@RequestParam(name = "id_turma") Long id_turma, @RequestParam("fileImage") MultipartFile multipartFile,
			BindingResult result, ModelMap model) throws Exception{
		
		try {
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		    aluno.setPhotos(fileName);
			
	 		Calendar calendar = GregorianCalendar.getInstance();
			int anoAtual = calendar.get(Calendar.YEAR);
			
			List<Aluno> alunos = alunoRepository.findAll();
			
			if(alunos.size() == 0) {
				aluno.setMatricula(anoAtual+""+(alunoRepository.count()+1));
			}else {
				Aluno ultimoCadastrado = alunos.get(alunos.size()-1);
				String matricula = ultimoCadastrado.getMatricula();
				String modificada = matricula.substring(4, matricula.length());
				int modificadaConvertida = Integer.parseInt(modificada);
				int digitoMatricula = modificadaConvertida + 1;
				aluno.setMatricula(anoAtual+""+digitoMatricula);
			}
			
			Aluno alunoSaved  = alunoRepository.save(aluno);
			
			if(alunoSaved != null) {
				Endereco enderecoAluno = enderecoRepository.save(endereco);
				
				alunoSaved.setEndereco(enderecoAluno);
				
				//System.out.println("enderecoAluno"+enderecoAluno);
				
				String uploadDir = "aluno-photos/" + alunoSaved.getId(); 
			    FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				if(aluno.getId() != 0) {
					Turma turma = turmaRepository.getOne(id_turma);
					turma.addAluno(aluno);
					turmaRepository.save(turma);
				}
				
				alunoRepository.saveAndFlush(aluno);
				
				attr.addFlashAttribute("aviso","sucesso salvar");
					
				return "redirect:/aluno/edit-"+aluno.getId()+"-aluno";
			}
			
			return "aluno/form";
				
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro salvar");
			
			return "aluno/form";
		}
			
	}

	@RequestMapping(value = {"/edit-{id}-aluno"})
	public String editAluno(@PathVariable("id") Long id, ModelMap model) {
		
		Aluno aluno = alunoRepository.getOne(id);
		model.addAttribute("aluno", aluno);
		
		List<Turma> turmas = turmaRepository.findAll();
		model.addAttribute("turmas", turmas);
		
		Set<Cronograma> cronogramas = aluno.getCronogramas();
		model.addAttribute("cronogramas", cronogramas);
		
		Cronograma cronograma = new Cronograma();
		model.addAttribute("cronograma", cronograma);
		
		for(Turma turma : turmas) {
			if(turma.getAlunos().contains(aluno)) {
				model.addAttribute("turma", turma);
			}
		}
			
		return "aluno/formUpdate";
	}
	
	@RequestMapping(value = {"/update"},  method = { RequestMethod.POST, RequestMethod.GET})
	public String updateAluno(@Valid Aluno aluno,@Valid @RequestParam(name = "id_turma") Long id_turma,
			@RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes attr,
			BindingResult result, ModelMap model) throws Exception {
		
		try {
			
			Aluno a = alunoRepository.getOne(aluno.getId());
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			aluno.setPhotos(fileName);
			
			if(fileName.equals("") || fileName == null) {
				aluno.setPhotos(a.getPhotos());
				System.out.println("aluno "+ a.getPhotos());
			}
		
			 //System.out.println("filename"+fileName);	
		   
			if(!aluno.getEndereco().equals(a.getEndereco())) {
				Endereco nextEndereco = enderecoRepository.getOne(a.getEndereco().getId());
				aluno.getEndereco().setId(nextEndereco.getId());
				enderecoRepository.saveAndFlush(aluno.getEndereco());
				//System.out.println(aluno.getEndereco());
			}
				
				List<Turma> turmas = turmaRepository.findAll();
				
				for(Turma t : turmas) {
					if(t.getAlunos().contains(aluno)) {
						t.removeAluno(aluno);
					}
				}
			
			Aluno alunoSaved  = alunoRepository.saveAndFlush(aluno);
			
			Turma turma = turmaRepository.getOne(id_turma);
			turma.addAluno(alunoSaved);
			turmaRepository.saveAndFlush(turma);
			
			if(!fileName.equals("")) {
				ClearFolder.remover(new File("aluno-photos/"+alunoSaved.getId()));
				String uploadDir = "aluno-photos/" + alunoSaved.getId();  
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}	
			
			//System.out.println("a"+aluno);
			//alunoRepository.saveAndFlush(aluno);
			
			attr.addFlashAttribute("aviso","sucesso salvar");
			
			return "redirect:/aluno/edit-"+alunoSaved.getId()+"-aluno";	
				
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro salvar");
			
			return "/aluno/formUpdate";
		}	
	}
	
	@RequestMapping(value = { "/delete-{id}-aluno" }, method = RequestMethod.GET)
	public String deleteAluno(@PathVariable Long id,  RedirectAttributes attr) {
		try {
			Optional <Aluno> a = Optional.ofNullable(alunoRepository.getOne(id));
			if(a.isPresent()) {
				ClearFolder.remover(new File("aluno-photos/"+id));	
				alunoRepository.deleteById(id);
			}
			
			attr.addFlashAttribute("aviso","sucesso excluir");
			
			return "redirect:/aluno/listar-alunos";	
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro excluir");
			
			return "redirect:/aluno/listar-alunos";
		}
	
	
	}
	
	@RequestMapping(value="/listar-alunos", method = RequestMethod.GET)
	public ModelAndView getAlunos() {
		ModelAndView mv = new ModelAndView("aluno/listar-alunos");
		List<Aluno> alunos = alunoRepository.findAll();
		mv.addObject("alunos",alunos);
		return mv;
	}
}

