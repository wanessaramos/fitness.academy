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
import br.fitness.academy.model.Funcionario;
import br.fitness.academy.model.Pagamento;
import br.fitness.academy.repository.AlunoRepository;
import br.fitness.academy.repository.FuncionarioRepository;
import br.fitness.academy.repository.PagamentoRepository;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {

	@Autowired
	AlunoRepository alunoRepository;
	
	@Autowired
	PagamentoRepository pagamentoRepository;
	
	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	@RequestMapping(value = {"/funcionario-{id}-new-pagamento"})
	public String newPagamentoFuncionario(@PathVariable("id") Long id, ModelMap model) {
		
		Funcionario funcionario = funcionarioRepository.getOne(id);
		
		model.addAttribute("funcionario", funcionario);
		Set<Pagamento> pagamentos = funcionario.getPagamentos();
		model.addAttribute("pagamentos", pagamentos);
		Pagamento pagamento = new Pagamento();
		model.addAttribute("pagamento", pagamento);
			
		return "pagamento/form";
	}
	
	@RequestMapping(value = {"/save-{funcionarioId}"}, method={RequestMethod.POST, RequestMethod.GET})
	public String savePagamento(@PathVariable("funcionarioId") Long funcionarioId,
			@Valid @ModelAttribute Pagamento mensalidade, BindingResult result,
							ModelMap model,RedirectAttributes attr){		
		try {
			
			Pagamento mensalidadeSaved = pagamentoRepository.save(mensalidade);
			Funcionario funcionario = funcionarioRepository.getOne(funcionarioId);
			funcionario.addPagamento(mensalidadeSaved);
			funcionarioRepository.saveAndFlush(funcionario);
			attr.addFlashAttribute("aviso","sucesso salvar");
					
		} catch (Exception e) {
			
			attr.addFlashAttribute("erro","sucesso salvar");
		}	
		return "redirect:/pagamento/funcionario-"+funcionarioId+"-listar-pagamentos";
	}
	
	@RequestMapping(value = {"/funcionario-{id}-listar-pagamentos"})
	public String listarPagamentosFuncionario(@PathVariable("id") Long id, ModelMap model) {
		
		Funcionario funcionario = funcionarioRepository.getOne(id);
		model.addAttribute("funcionario", funcionario);
		Set<Pagamento> pagamentos = funcionario.getPagamentos();
		model.addAttribute("pagamentos", pagamentos);
		Pagamento pagamento = new Pagamento();
		model.addAttribute("pagamento", pagamento);
			
		return "pagamento/listar-pagamentos";
	}
	
	@RequestMapping(value={"/edit-{pagamentoId}-pagamento-{funcionarioId}-funcionario"}, method=RequestMethod.GET)
	public String editarPagamento(@PathVariable("pagamentoId") Long pagamentoId,
			@PathVariable("funcionarioId") Long funcionarioId,ModelMap model) {
		
		Pagamento pagamento = pagamentoRepository.getOne(pagamentoId);
		System.out.println(pagamento);
		model.addAttribute("pagamento", pagamento);
		Funcionario funcionario = funcionarioRepository.getOne(funcionarioId);
		model.addAttribute("funcionario", funcionario);
		
		return "pagamento/formUpdate";
	}
	
	@RequestMapping(value={"/update-{funcionarioId}"},method={RequestMethod.PUT, RequestMethod.GET})
	public String updatePagamento(@Valid @ModelAttribute Pagamento pagamento,  RedirectAttributes attr,
			BindingResult result, ModelMap model, @PathVariable("funcionarioId") Long funcionarioId 
			) {
		
		try {
			pagamentoRepository.saveAndFlush(pagamento);
			
			attr.addFlashAttribute("aviso","sucesso salvar");
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro salvar");
		}
		
		return "redirect:/pagamento/funcionario-"+funcionarioId+"-listar-pagamentos";
	}
	
	@RequestMapping(value={"/delete-{pagamentoId}-pagamento-{funcionarioId}-funcionario"}, method=RequestMethod.GET)
	public String deletarMensalidade(@PathVariable("pagamentoId") Long pagamentoId,
			@PathVariable("funcionarioId") Long funcionarioId, RedirectAttributes attr) {
		try {
			pagamentoRepository.deleteById(pagamentoId);
			
			attr.addFlashAttribute("aviso","sucesso excluir");	
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("aviso","erro excluir");
		}
		
		return "redirect:/pagamento/funcionario-"+funcionarioId+"-listar-pagamentos";
	}

}
