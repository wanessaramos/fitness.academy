package br.fitness.academy.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.fitness.academy.model.Exercicio;
import br.fitness.academy.repository.ExercicioRepository;

@Controller
@RequestMapping("/exercicio")
public class ExercicioController {
	
	@Autowired
	private ExercicioRepository exercicioRepository;
	
	@RequestMapping(value = {"/new"}, method = RequestMethod.GET)
	public String newExercicio(ModelMap model) {
		
		Exercicio exercicio = new Exercicio();
		model.addAttribute("exercicio", exercicio);
		model.addAttribute("edit", false);
		
		return "exercicio/form";
	}
	
	@RequestMapping(value = {"/save"}, method={RequestMethod.POST, RequestMethod.GET})
	public String saveExercicio(@Valid @ModelAttribute Exercicio exercicio, BindingResult result,
							ModelMap model, RedirectAttributes attr) {
		try {
			exercicioRepository.save(exercicio);
			attr.addFlashAttribute("aviso","sucesso salvar");
			return "redirect:/exercicio/edit-"+exercicio.getId()+"-exercicio";
		} catch (Exception e) {
			attr.addFlashAttribute("aviso","erro salvar");
			return "/exercicio/form";
		}	
	}
	
	@RequestMapping(value={"/edit-{id}-exercicio"}, method=RequestMethod.GET)
	public String editarExercicio(@PathVariable("id") Long id, ModelMap model) {
	
			Exercicio exercicio = exercicioRepository.getOne(id);
			model.addAttribute("exercicio", exercicio);		
			
		
		return "exercicio/formUpdate";
	}
	
	@RequestMapping(value={"/update"},  method={RequestMethod.PUT, RequestMethod.GET})
	public String updateExercicio(@Valid Exercicio exercicio, BindingResult result, ModelMap model,
			RedirectAttributes attr) {
		
		try {
			exercicioRepository.saveAndFlush(exercicio);
			attr.addFlashAttribute("aviso","sucesso salvar");
		} catch (Exception e) {
			attr.addFlashAttribute("aviso","erro salvar");
		}
	
		return "exercicio/formUpdate";
		
	}
	
	@RequestMapping(value={"/delete-{id}-exercicio"}, method=RequestMethod.GET)
	public String deletarExercicio(@PathVariable Long id,RedirectAttributes attr) {
		try {
			exercicioRepository.deleteById(id);
			attr.addFlashAttribute("aviso","sucesso excluir");	
		} catch (Exception e) {
			attr.addFlashAttribute("aviso","erro excluir");
		}
		
		return "redirect:/exercicio/listar-exercicios";
	}
	
	@RequestMapping(value="/listar-exercicios", method = RequestMethod.GET)
	public ModelAndView getTurmas() {
		ModelAndView mv = new ModelAndView("exercicio/listar-exercicios");
		List<Exercicio> exercicios = exercicioRepository.findAll();
		mv.addObject("exercicios",exercicios);
		return mv;
	}

}
