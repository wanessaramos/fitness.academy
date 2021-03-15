package br.fitness.academy.controller;

import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.fitness.academy.model.Aluno;
import br.fitness.academy.model.Mensalidade;
import br.fitness.academy.repository.AlunoRepository;
import br.fitness.academy.repository.MensalidadeRepository;

@Controller
@RequestMapping("/mensalidade")
public class MensalidadeController {
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private MensalidadeRepository mensalidadeRepository;
	
	@RequestMapping(value = {"/new-{alunoId}"}, method = RequestMethod.GET)
	public String newMensalidade(@PathVariable("alunoId") Long alunoId, ModelMap model) {

		Aluno aluno = alunoRepository.getOne(alunoId);
		model.addAttribute("aluno", aluno);
		Mensalidade mensalidade = new Mensalidade();
		model.addAttribute("mensalidade", mensalidade);
	
		return "mensalidade/form";
	}
	
	@RequestMapping(value = {"/save-{alunoId}"}, method={RequestMethod.POST, RequestMethod.GET})
	public String saveMensalidade(@PathVariable("alunoId") Long alunoId,
			@Valid @ModelAttribute Mensalidade mensalidade, BindingResult result,
							ModelMap model,RedirectAttributes attr){		
		try {
			
			Mensalidade mensalidadeSaved = mensalidadeRepository.save(mensalidade);
			Aluno aluno = alunoRepository.getOne(alunoId);
			aluno.addMensalidade(mensalidadeSaved);
			alunoRepository.saveAndFlush(aluno);
			attr.addFlashAttribute("aviso","sucesso salvar");
			
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("erro","sucesso salvar");
		}	
		return "redirect:/mensalidade/aluno-"+alunoId+"-listar-mensalidades";
	}
	
	@RequestMapping(value = {"/aluno-{id}-listar-mensalidades"})
	public String listarMensalidadesAluno(@PathVariable("id") Long id, ModelMap model) {
		
		Aluno aluno = alunoRepository.getOne(id);
		model.addAttribute("aluno", aluno);
		Set<Mensalidade> mensalidades = aluno.getMensalidades();
		model.addAttribute("mensalidades", mensalidades);
		Mensalidade mensalidade = new Mensalidade();
		model.addAttribute("mensalidade", mensalidade);
			
		return "mensalidade/listar-mensalidades";
	}
	
	@RequestMapping(value={"/edit-{mensalidadeId}-mensalidade-{alunoId}-aluno"}, method=RequestMethod.GET)
	public String editarMensalidade(@PathVariable("mensalidadeId") Long mensalidadeId,
			@PathVariable("alunoId") Long alunoId,ModelMap model) {
		
		Mensalidade mensalidade = mensalidadeRepository.getOne(mensalidadeId);
		model.addAttribute("mensalidade", mensalidade);
		Aluno aluno = alunoRepository.getOne(alunoId);
		model.addAttribute("aluno", aluno);
		
		return "mensalidade/formUpdate";
	}
	
	@RequestMapping(value={"/update-{alunoId}"},method={RequestMethod.PUT, RequestMethod.GET})
	public String updateMensalidade(@Valid @ModelAttribute Mensalidade mensalidade,  RedirectAttributes attr,
			BindingResult result, ModelMap model, @PathVariable("alunoId") Long alunoId 
			) {
		
		try {
			mensalidadeRepository.saveAndFlush(mensalidade);
			
			attr.addFlashAttribute("aviso","sucesso salvar");
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro salvar");
		}
		
		return "redirect:/mensalidade/aluno-"+alunoId+"-listar-mensalidades";
	}
	
	@RequestMapping(value={"/delete-{mensalidadeId}-mensalidade-{alunoId}-aluno"}, method=RequestMethod.GET)
	public String deletarMensalidade(@PathVariable("mensalidadeId") Long mensalidadeId,
			@PathVariable("alunoId") Long alunoId, RedirectAttributes attr) {
		try {
			mensalidadeRepository.deleteById(mensalidadeId);
			
			attr.addFlashAttribute("aviso","sucesso excluir");	
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro excluir");
		}
		
		return "redirect:/mensalidade/aluno-"+alunoId+"-listar-mensalidades";
	}
	
}
