package br.fitness.academy.controller;

import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import br.fitness.academy.model.Aluno;
import br.fitness.academy.model.Professor;
import br.fitness.academy.model.Turma;
import br.fitness.academy.model.Turno;
import br.fitness.academy.repository.AlunoRepository;
import br.fitness.academy.repository.TurmaRepository;
import br.fitness.academy.repository.TurnoRepository;

@Controller
@RequestMapping("/turma")
public class TurmaController {

	@Autowired
	TurmaRepository turmaRepository;
	
	@Autowired
	AlunoRepository alunoRepository;
	
	@Autowired
	TurnoRepository turnoRepository;
	
	@RequestMapping(value = {"/new"}, method=RequestMethod.GET)
	public String newTurma(ModelMap model) {
			
		Turma turma = new Turma();
		model.addAttribute("turma", turma);
		List<Turno> turnos = turnoRepository.findAll();
		model.addAttribute("turnos", turnos);
		
		return "turma/form";
	}
	
	@RequestMapping(value = {"/save"}, method={RequestMethod.POST, RequestMethod.GET})
	public String saveTurma(@Valid @ModelAttribute Turma turma, BindingResult result,
			@RequestParam(name = "id_turno") Long id_turno,
							ModelMap model) {
		
		System.out.println(turma);
		
		if (result.hasErrors()) {
			return "aluno/form";
		}
		
		Turma turmaSaved = turmaRepository.save(turma);
		
		Turno turno = turnoRepository.getOne(id_turno);
		turno.addTurma(turmaSaved);
		turnoRepository.saveAndFlush(turno);
		
		model.addAttribute("mensagem", "Turma " + turma.getNome() + " cadastrada com sucesso");
		
		return "redirect:/turma/edit-"+turma.getId()+"-turma";
		
	}
	
	@RequestMapping(value={"/edit-{id}-turma"}, method=RequestMethod.GET)
	public String editarTurma(@PathVariable("id") Long id, ModelMap model) {
		
		Turma turma = turmaRepository.getOne(id);
		
		model.addAttribute("turma", turma);

		List<Turno> turnos = turnoRepository.findAll();
		model.addAttribute("turnos", turnos);
	
		
		for(Turno turno : turnos) {
			if(turno.getTurmas().contains(turma)) {
				model.addAttribute("turno", turno);
			}
		}
		
		return "turma/formUpdate";
	}
	
	@RequestMapping(value={"/update"}, method={RequestMethod.POST, RequestMethod.GET})
	public String updateTurma(@Valid Turma turma, @RequestParam(name = "id_turno") Long id_turno,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "turma/form";
		}
		
		List<Turno> turnos = turnoRepository.findAll();
			
			for(Turno t : turnos) {
				if(t.getTurmas().contains(turma)) {
					t.removeTurma(turma);
				}
			}
		
		turmaRepository.saveAndFlush(turma);
		
		Turno turno = turnoRepository.getOne(id_turno);
		turno.addTurma(turma);
			
		turnoRepository.saveAndFlush(turno);
			
		model.addAttribute("mensagem", "Turma " + turma.getNome() + " atualizada com sucesso");
		
		return "redirect:/turma/listar-turmas";
	}
	
	@RequestMapping(value={"/delete-{id}-turma"}, method=RequestMethod.GET)
	public String deletarTurma(@PathVariable Long id) {
		turmaRepository.deleteById(id);
		return "redirect:/turma/listar-turmas";
	}
	
	@RequestMapping(value="/listar-turmas", method = RequestMethod.GET)
	public ModelAndView getTurmas() {
		ModelAndView mv = new ModelAndView("turma/listar-turmas");
		List<Turma> turmas = turmaRepository.findAll();
		mv.addObject("turmas",turmas);
		return mv;
	}
	
	@RequestMapping(value="/listar-alunos-{id}-turma", method = RequestMethod.GET)
	public String listarAlunos(@PathVariable("id") Long id, ModelMap model) {
		
		Turma turma = turmaRepository.getOne(id);
		model.addAttribute("turma",turma);
		Set<Aluno> alunos = turma.getAlunos();
		model.addAttribute("alunos",alunos);
		List<Turno> turnos = turnoRepository.findAll();
		for(Turno turno : turnos) {
			if(turno.getTurmas().contains(turma)) {
				model.addAttribute("turno",turno);
			}	
		}
		
		return "/turma/listar-alunos";
	}
	
	@RequestMapping(value="/listar-professores-{id}-turma", method = RequestMethod.GET)
	public String listarProfessores(@PathVariable("id") Long id, ModelMap model) {
		
		Turma turma = turmaRepository.getOne(id);
		model.addAttribute("turma",turma);
		Set<Professor> professores = turma.getProfessores();
		model.addAttribute("professores",professores);
		List<Turno> turnos = turnoRepository.findAll();
		for(Turno turno : turnos) {
			if(turno.getTurmas().contains(turma)) {
				model.addAttribute("turno",turno);
			}	
		}
		
		return "/turma/listar-professores";
	}
	
	/*@RequestMapping(value="/add-{id}-aluno", method = RequestMethod.GET)
	public String adicionarAluno(@PathVariable("id") Long id,
			@RequestParam("id_turma") Long id_turma, ModelMap model) {
		
		Aluno aluno = alunoRepository.getOne(id);
		Turma turma = turmaRepository.getOne(id_turma);
		turma.addAluno(aluno);
		
		turmaRepository.saveAndFlush(turma);
		
		return "redirect:/turma/listar-alunos-"+id_turma+"-turma";
	}*/
}
