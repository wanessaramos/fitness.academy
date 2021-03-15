package br.fitness.academy.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import br.fitness.academy.model.Endereco;
import br.fitness.academy.repository.EnderecoRepository;

@Controller
@RequestMapping("/endereco")
public class EnderecoController {
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@RequestMapping(value = {"/new"}, method = RequestMethod.GET)
	public String newEndereco(ModelMap model) {
		
		Endereco endereco = new Endereco();
		model.addAttribute("endereco", endereco);
		model.addAttribute("edit", false);
		
		return "aluno/form";
	}
	
	@RequestMapping(value = {"/save-{id}"}, method = RequestMethod.POST)
	public String saveEndereco(@Valid @ModelAttribute Endereco endereco,@PathVariable Long id,BindingResult result,
							ModelMap model) {
		
		System.out.println(endereco);
		
		if (result.hasErrors()) {
			return "aluno/form";
		}
		
		enderecoRepository.save(endereco);
		
		model.addAttribute("mensagem", "Endereço cadastrado com sucesso");
		
		return "redirect:../aluno/edit-"+id+"-aluno";
	}
}
