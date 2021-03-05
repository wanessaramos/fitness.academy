package br.fitness.academy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.fitness.academy.model.Aluno;
import br.fitness.academy.model.Cronograma;
import br.fitness.academy.model.Exercicio;
import br.fitness.academy.repository.AlunoRepository;
import br.fitness.academy.repository.CronogramaRepository;
import br.fitness.academy.repository.ExercicioRepository;

@Controller
@RequestMapping("/cronograma")
public class CronogramaController {
	
	@Autowired
	CronogramaRepository cronogramaRepository;
	
	@Autowired
	ExercicioRepository exercicioRepository;
	
	@Autowired
	AlunoRepository alunoRepository;
	
	@RequestMapping(value = {"/new"}, method = RequestMethod.GET)
	public String newCronograma(@Valid @ModelAttribute Aluno aluno,
			@Valid @ModelAttribute Cronograma cronograma,BindingResult result, ModelMap model) {

		model.addAttribute("aluno", aluno);
		model.addAttribute("cronograma", cronograma);
		List<Exercicio> exercicios = new ArrayList<>();
		exercicioRepository.findAll().forEach(exercicios::add);
		model.addAttribute("exercicios", exercicios);
	
		return "cronograma/form";
	}
	
	@RequestMapping(value = {"/save"}, method={RequestMethod.POST, RequestMethod.GET})
	public String saveCronograma(@Valid @ModelAttribute Aluno aluno,
			@Valid @ModelAttribute Cronograma cronograma, BindingResult result,
							ModelMap model){
		
		try {
			
			Cronograma cronogramaSaved = cronogramaRepository.save(cronograma);
			Aluno a = alunoRepository.getOne(aluno.getId());
			a.adicionarCronograma(cronogramaSaved);
			alunoRepository.saveAndFlush(a);
			
			return "redirect:/cronograma/add-"+cronogramaSaved.getId()+"-cronograma-"+a.getId()+"-aluno";
			
		} catch (Exception e) {
			
			return "redirect:/cronograma/new";
		}	
	}
	
	
	@RequestMapping(value={"/add-{cronogramaId}-cronograma-{alunoId}-aluno"}, method=RequestMethod.GET)
	public String addCronograma(@PathVariable("cronogramaId") Long cronogramaId,
			@PathVariable("alunoId") Long alunoId,ModelMap model) {
		
		Cronograma cronograma = cronogramaRepository.getOne(cronogramaId);
		model.addAttribute("cronograma", cronograma);
		Aluno aluno = alunoRepository.getOne(alunoId);
		model.addAttribute("aluno", aluno);
		List<Exercicio> exercicios = new ArrayList<>();
		exercicioRepository.findAll().forEach(exercicios::add);
		model.addAttribute("exercicios", exercicios);
		
		return "cronograma/formUpdate";
	}
	
	@RequestMapping(value={"/edit-{cronogramaId}-cronograma-{alunoId}-aluno"}, method=RequestMethod.GET)
	public String editarCronograma(@PathVariable("cronogramaId") Long cronogramaId,
			@PathVariable("alunoId") Long alunoId,ModelMap model) {
		
		Cronograma cronograma = cronogramaRepository.getOne(cronogramaId);
		model.addAttribute("cronograma", cronograma);
		Aluno aluno = alunoRepository.getOne(alunoId);
		model.addAttribute("aluno", aluno);
		
		return "cronograma/form";
	}
	
	@RequestMapping(value={"/update-{alunoId}"},method={RequestMethod.PUT, RequestMethod.GET})
	public String updateCronograma(@Valid @ModelAttribute Cronograma cronograma,  RedirectAttributes attr,
			BindingResult result, ModelMap model, @PathVariable("alunoId") Long alunoId) {
		
		try {
			Cronograma cronogramaUpdate = cronogramaRepository.getOne(cronograma.getId());
			cronograma.setExercicios(cronogramaUpdate.getExercicios());
			cronogramaRepository.saveAndFlush(cronograma);
			
			attr.addFlashAttribute("aviso","sucesso salvar");
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro salvar");
		}
		
		return "redirect:/cronograma/aluno-"+alunoId+"-listar-cronogramas";
	}
	
	
	@RequestMapping(value={"/delete-{cronogramaId}-cronograma-{alunoId}-aluno"}, method=RequestMethod.GET)
	public String deletarCronograma(@PathVariable("cronogramaId") Long cronogramaId,
			@PathVariable("alunoId") Long alunoId, RedirectAttributes attr) {
		try {
			cronogramaRepository.deleteById(cronogramaId);
			
			attr.addFlashAttribute("aviso","sucesso excluir");	
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro excluir");
		}
		
		return "redirect:/cronograma/aluno-"+alunoId+"-listar-cronogramas";
	}
	
	@RequestMapping(value = {"/aluno-{id}-new-cronograma"})
	public String newCronogramaAluno(@PathVariable("id") Long id, ModelMap model) {
		
		Aluno aluno = alunoRepository.getOne(id);
		model.addAttribute("aluno", aluno);
		Set<Cronograma> cronogramas = aluno.getCronogramas();
		model.addAttribute("cronogramas", cronogramas);
		Cronograma cronograma = new Cronograma();
		model.addAttribute("cronograma", cronograma);
			
		return "cronograma/formNew";
	}
	
	@RequestMapping(value = {"/aluno-{id}-listar-cronogramas"})
	public String listarCronogramasAluno(@PathVariable("id") Long id, ModelMap model) {
		
		Aluno aluno = alunoRepository.getOne(id);
		model.addAttribute("aluno", aluno);
		Set<Cronograma> cronogramas = aluno.getCronogramas();
		model.addAttribute("cronogramas", cronogramas);
		Cronograma cronograma = new Cronograma();
		model.addAttribute("cronograma", cronograma);
			
		return "cronograma/listar-cronogramas";
	}
	
	@RequestMapping(value = "/addExercicio")
	public String addExercicio(@RequestParam("exercicioId") Long exercicioId,
			@RequestParam("cronogramaId") Long cronogramaId,
			@RequestParam("alunoId") Long alunoId){
		
		Exercicio exercicio =  exercicioRepository.getOne(exercicioId);
		Cronograma cronograma = cronogramaRepository.getOne(cronogramaId);
		cronograma.addExercicio(exercicio);
		cronogramaRepository.saveAndFlush(cronograma);
		
		return "redirect:/cronograma/add-"+cronogramaId+"-cronograma-"+alunoId+"-aluno";
	}
	
	@RequestMapping(value = "/removeExercicio")
	public String removeExercicio(@RequestParam("exercicioId") Long exercicioId,
			@RequestParam("cronogramaId") Long cronogramaId,
			@RequestParam("alunoId") Long alunoId){
		
		Cronograma cronograma = cronogramaRepository.getOne(cronogramaId);
		cronograma.getExercicios().removeIf(e -> e.getId() == exercicioId);
		cronogramaRepository.saveAndFlush(cronograma);
		
		
		return "redirect:/cronograma/edit-"+cronogramaId+"-cronograma-"+alunoId+"-aluno";
	}
	
	@RequestMapping(value="/exercicio")
	public String findByCarga(@RequestParam("nome") String nome,ModelMap model){
		System.out.println(nome);
		Cronograma cronograma = new Cronograma();
		model.addAttribute("cronograma", cronograma);
		model.addAttribute("edit", false);
		
		List<Exercicio> exercicios = new ArrayList<>();
		if(nome == null)
			exercicioRepository.findAll().forEach(exercicios::add);
		else
			exercicioRepository.findByCarga(nome).forEach(exercicios::add);
		model.addAttribute("exercicios", exercicios);
		
		List<String> carga = new ArrayList<>();
		
		for(Exercicio ex : exercicios) {
			if(!carga.contains(ex.getCarga())) {
				carga.add(ex.getCarga());
			}
		}
		model.addAttribute("carga", carga);
	
		return "cronograma/form";
	}

}

