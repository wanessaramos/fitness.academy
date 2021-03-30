package br.fitness.academy.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.fitness.academy.report.GeradorDeRelatorio;
import com.itextpdf.text.DocumentException;
import br.fitness.academy.model.Funcionario;
import br.fitness.academy.model.Pagamento;
import br.fitness.academy.repository.FuncionarioRepository;
import br.fitness.academy.repository.PagamentoRepository;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {

	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	private GeradorDeRelatorio geradorRelatorio = new GeradorDeRelatorio();
	
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
	
	@RequestMapping(value="/listar-todos-pagamentos", method = RequestMethod.GET)
	public ModelAndView getAllPagamentos() {
		ModelAndView mv = new ModelAndView("pagamento/listar-todos-pagamentos");
		Double totalpagos = 0.0; Double totalpendentes = 0.0;
		List<Pagamento> pagpagas = pagamentoRepository.findByStatus("pago");
		for(Pagamento pago : pagpagas) {
			totalpagos+=pago.getValor();
		}
		mv.addObject("pagos",totalpagos);
		List<Pagamento> pagpendentes = pagamentoRepository.findByStatus("pendente");
		for(Pagamento pendente : pagpendentes) {
			totalpendentes+=pendente.getValor();
		}
		mv.addObject("pendentes",totalpendentes);
		List<Funcionario> funcionarios = funcionarioRepository.findAll();
		mv.addObject("funcionarios",funcionarios);
		return mv;
	}
	
	@RequestMapping(value="/listar", method = RequestMethod.GET)
	public String listarPagamentosPorData(@RequestParam Date data, ModelMap model) {
		try {
			Double totalpagos = 0.0; Double totalpendentes = 0.0;
			List<Funcionario> funcionarioAll = funcionarioRepository.findAll();
			List<Funcionario> funcionarios = new ArrayList<>();
			for(Funcionario funcionario : funcionarioAll) {
				for(Pagamento pagamento : funcionario.getPagamentos()) {
					if(pagamento.getEntrega().equals(data)){
						funcionarios.add(funcionario);
						if(pagamento.getStatus().equals("pago")) {
							totalpagos+=pagamento.getValor();
						}else {
							totalpendentes+=pagamento.getValor();
						}
					}
				}
			}
			model.addAttribute("pagos",totalpagos);
			model.addAttribute("pendentes",totalpendentes);
			model.addAttribute("funcionarios",funcionarios);
			model.addAttribute("date",data);
			
			return "pagamento/listar-todos-pagamentos";
			
		} catch (Exception e) {
			return "pagamento/listar-todos-pagamentos";
		}	
	}
	
	/*@RequestMapping("/imprimir")
	public String relatorioPagamentos(
			RedirectAttributes attr) throws IOException, DocumentException {

	 long time = System.currentTimeMillis();
	 List<Pagamento> pagamentos = pagamentoRepository.findAll();
	 
	 if(!pagamentos.isEmpty()) {
		 int numPagamentos = pagamentos.size();
		 String[] colunas = new String[] {"ID", "DESCRIÇÃO",
				 "DATA", "STATUS","VALOR"};
		
		 geradorRelatorio.imagem(time+"pagamentos.pdf","fitness.png", 750, 200, 45, 45);
		 geradorRelatorio.cabecalho("ACADEMY FITNESS", "Relatório de Pagamentos");
		 //geradorRelatorio.qrcode("Exemplo de QRCode", 600, 250, 200);
		 
		StringBuilder stringPagamentos = new StringBuilder("");
		 
		for(Pagamento pagamento : pagamentos) {
			stringPagamentos.append(pagamento.getId()).append(",");
			stringPagamentos.append(pagamento.getDescricao()).append(",");
			stringPagamentos.append(pagamento.getEntrega()).append(",");
			stringPagamentos.append(pagamento.getStatus()).append(",");
			stringPagamentos.append(Double.toString(pagamento.getValor())).append(",");	
		 }
		 
		 //System.out.println("strings "+stringPagamentos);
		 geradorRelatorio.createTabela(colunas, stringPagamentos);
		 geradorRelatorio.rodape();
		 
		 attr.addAttribute("mensagem", "Relatório gerado com sucesso!");
		 
	 } else {
		 
		 attr.addAttribute("mensagem", "Erro ao gerar o Relatório!");
	 }
	
	 return "pagamento/listar-todos-pagamentos";
	 }*/
	
	@RequestMapping("/imprimir")
	public String relatorioPagamentos(
			RedirectAttributes attr) throws IOException, DocumentException {

	 long time = System.currentTimeMillis();
	 List<Funcionario> funcionarios = funcionarioRepository.findAll();
	 
	 if(!funcionarios.isEmpty()) {
		 String[] colunas = new String[] {"NOME", "CPF","DESCRIÇÃO",
				 "DATA", "STATUS","VALOR"};
		
		 geradorRelatorio.imagem(time+"pagamentos.pdf","fitness.png", 750, 200, 45, 45);
		 geradorRelatorio.cabecalho("ACADEMY FITNESS", "Relatório de Pagamentos");
		 //geradorRelatorio.qrcode("Exemplo de QRCode", 600, 250, 200);
		 
		StringBuilder stringPagamentos = new StringBuilder("");
		 
		for(Funcionario funcionario : funcionarios) {
			stringPagamentos.append(funcionario.getNome()).append(",");
			stringPagamentos.append(funcionario.getCpf()).append(",");
			for(Pagamento pagamento: funcionario.getPagamentos()) {
				stringPagamentos.append(pagamento.getDescricao()).append(",");
				stringPagamentos.append(pagamento.getEntrega()).append(",");
				stringPagamentos.append(pagamento.getStatus()).append(",");
				stringPagamentos.append(Double.toString(pagamento.getValor())).append(",");
			}	
		 }
		 
		 //System.out.println("strings "+stringPagamentos);
		 geradorRelatorio.createTabela(colunas, stringPagamentos);
		 geradorRelatorio.rodape();
		 
		 attr.addAttribute("mensagem", "Relatório gerado com sucesso!");
		 
	 } else {
		 
		 attr.addAttribute("mensagem", "Erro ao gerar o Relatório!");
	 }
	
	 return "pagamento/listar-todos-pagamentos";
	 }
}
