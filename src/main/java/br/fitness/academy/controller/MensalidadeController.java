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
import com.itextpdf.text.DocumentException;
import br.fitness.academy.model.Aluno;
import br.fitness.academy.model.Mensalidade;
import br.fitness.academy.report.GeradorDeRelatorio;
import br.fitness.academy.repository.AlunoRepository;
import br.fitness.academy.repository.MensalidadeRepository;

@Controller
@RequestMapping("/mensalidade")
public class MensalidadeController {
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private MensalidadeRepository mensalidadeRepository;
	
	private GeradorDeRelatorio geradorRelatorio = new GeradorDeRelatorio();
	
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
	
	@RequestMapping(value="/listar-todas-mensalidades", method = RequestMethod.GET)
	public ModelAndView getAllMensalidades() {
		ModelAndView mv = new ModelAndView("mensalidade/listar-todas-mensalidades");
		Double totalpagas = 0.0; Double totalpendentes = 0.0;
		List<Mensalidade> menspagas = mensalidadeRepository.findByStatus("paga");
		for(Mensalidade paga : menspagas) {
			totalpagas+=paga.getValor();
		}
		mv.addObject("pagas",totalpagas);
		List<Mensalidade> menspendentes = mensalidadeRepository.findByStatus("pendente");
		for(Mensalidade pendente : menspendentes) {
			totalpendentes+=pendente.getValor();
		}
		mv.addObject("pendentes",totalpendentes);
		List<Aluno> alunos = alunoRepository.findAll();
		mv.addObject("alunos",alunos);
		return mv;
	}
	
	@RequestMapping(value="/listar", method = RequestMethod.GET)
	public String listarMensalidadesPorData(@RequestParam Date data, ModelMap model) {
		try {
			Double totalpagas = 0.0; Double totalpendentes = 0.0;
			List<Aluno> allunos = alunoRepository.findAll();
			List<Aluno> alunos = new ArrayList<>();
			for(Aluno aluno : allunos) {
				for(Mensalidade mensalidade : aluno.getMensalidades()) {
					if(mensalidade.getVencimento().equals(data)){
						alunos.add(aluno);
						if(mensalidade.getStatus().equals("paga")) {
							totalpagas+=mensalidade.getValor();
						}else {
							totalpendentes+=mensalidade.getValor();
						}
					}
				}
			}
			model.addAttribute("pagas",totalpagas);
			model.addAttribute("pendentes",totalpendentes);
			model.addAttribute("alunos",alunos);
			model.addAttribute("date",data);
			
			return "mensalidade/listar-todas-mensalidades";
			
		} catch (Exception e) {
			return "mensalidade/listar-todas-mensalidades";
		}	
	}
	
	@RequestMapping("/imprimir")
	public String relatorioMensalidades(
			RedirectAttributes attr) throws IOException, DocumentException {

	 long time = System.currentTimeMillis();
	 List<Mensalidade> mensalidades = mensalidadeRepository.findAll();
	 
	 if(!mensalidades.isEmpty()) {
		 int numMensalidades = mensalidades.size();
		 String[] colunas = new String[] {"ID", "MÊS", "STATUS",
				 "VALOR","VENCIMENTO"};
		
		 geradorRelatorio.imagem(time+"mensalidades.pdf","fitness.png", 750, 200, 45, 45);
		 geradorRelatorio.cabecalho("ACADEMY FITNESS", "Relatório de Mensalidades");
		 //geradorRelatorio.qrcode("Exemplo de QRCode", 600, 250, 200);
		 
		StringBuilder stringMensalidades = new StringBuilder("");
		 
		for(Mensalidade mensalidade : mensalidades) {
			 stringMensalidades.append(mensalidade.getId()).append(",");
			 stringMensalidades.append(mensalidade.getMes()).append(",");
			 stringMensalidades.append(mensalidade.getStatus()).append(",");
			 stringMensalidades.append(Double.toString(mensalidade.getValor())).append(",");
			 stringMensalidades.append(mensalidade.getVencimento()).append(",");	
		 }
		 
		 //System.out.println("strings "+stringMensalidades);
		 geradorRelatorio.createTabela(colunas, stringMensalidades);
		 geradorRelatorio.rodape();
		 
		 attr.addAttribute("mensagem", "Relatório gerado com sucesso!");
		 
	 } else {
		 
		 attr.addAttribute("mensagem", "Erro ao gerar o Relatório!");
	 }
	
	 return "mensalidade/listar-todas-mensalidades";
	 }
}
