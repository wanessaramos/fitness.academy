package br.fitness.academy.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sun.el.parser.ParseException;
import br.fitness.academy.model.*;
import br.fitness.academy.repository.*;

@Component
public class Data {
	
	@Autowired
	AlunoRepository alunoRepository;
	
	@Autowired
	CronogramaRepository cronogramaRepository;
	
	@Autowired
	ExercicioRepository exercicioRepository;
	
	@Autowired
	MensalidadeRepository mensalidadeRepository;
	
	@Autowired
	PagamentoRepository pagamentoRepository;
	
	@Autowired
	ProfessorRepository professorRepository;
	
	@Autowired
	TurmaRepository turmaRepository;
	
	@Autowired
	TurnoRepository turnoRepository;
	
	/*@PostConstruct
	public void salveAlunos() {
	
		List<Aluno> alunos = new ArrayList<Aluno>();
		Aluno aluno0 = new Aluno("20211", "Jonata Ramalho","jonata","123",12345688, 251478,"82-6633-6996");
		Aluno aluno1 = new Aluno("20212", "Lucilene Bezerra","luh","123",1245678952,124587,"82-1233-6846");
		Aluno aluno2 = new Aluno("20213", "Katiúcia Wanessa","katy","12356",125478963,124587,"82-6583-6596");
		alunos.add(aluno0);
		alunos.add(aluno1);
		alunos.add(aluno2);
		
		for(Aluno aluno : alunos) {
			Aluno alunoSaved = alunoRepository.save(aluno);
			//System.out.println(alunoSaved.getId());
		}
	}
	
	@PostConstruct
	public void salveTurmas() {
		List<Turma> turmas = new ArrayList<Turma>();
		Turma turma0 = new Turma("Zeus");
		Turma turma1 = new Turma("Poseidon");
		Turma turma2 = new Turma("Artemis");
		turmas.add(turma0);
		turmas.add(turma1);
		turmas.add(turma2);
			
		for(Turma turma : turmas) {
			Turma turmaSaved = turmaRepository.save(turma);
				//System.out.println(turmaSaved.getId());
		}
	}
		
	@PostConstruct
	public void salveExercicios() {
		List<Exercicio> exercicios = new ArrayList<Exercicio>();
		Exercicio exercicio0 = new Exercicio("Supino Máquina", "2-3", "10-15", "leve moderada", 60);
		Exercicio exercicio1 = new Exercicio("Puxada Alta", "2-3", "10-15", "leve moderada", 60);
		Exercicio exercicio2 = new Exercicio("Mesa flexora", "2-3", "10-15", "leve moderada", 60);
		Exercicio exercicio3 = new Exercicio("Abdominal", "2-3", "10-15", "leve moderada", 60);
		Exercicio exercicio4 = new Exercicio("Tríceps polia", "2-3", "10-15", "leve moderada", 60);
		exercicios.add(exercicio0);
		exercicios.add(exercicio1);
		exercicios.add(exercicio2);
		exercicios.add(exercicio3);
		exercicios.add(exercicio4);
		
		for(Exercicio exercicio : exercicios) {
			Exercicio exercicioSaved = exercicioRepository.save(exercicio);
			//System.out.println(exercicioSaved.getId());
		}		
	}
	
	
	@PostConstruct
	public void salveCronogramas() throws ParseException, java.text.ParseException {
		List<Exercicio> exercicios = exercicioRepository.findAll();
		//System.out.println("exercicio"+exercicios.get(0).toString());
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date data = (Date) formato.parse("18/01/2021");
		Date data1 = (Date) formato.parse("22/01/2021");
		List<Cronograma> cronogramas = new ArrayList<>();
		Cronograma cronograma0 = new Cronograma(data, data1,exercicios);
		Cronograma cronograma1 = new Cronograma(data, data1,exercicios);
		Cronograma cronograma2 = new Cronograma(data, data1,exercicios);
		cronogramas.add(cronograma0);
		cronogramas.add(cronograma1);
		cronogramas.add(cronograma2);
		
		for(Cronograma cronograma : cronogramas) {
			Cronograma cronogramaSaved = cronogramaRepository.save(cronograma);
			//System.out.println(cronogramaSaved.getId());
		}
	}
	
	@PostConstruct
	public void salveMensalidades() throws ParseException, Exception {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date data = (Date) formato.parse("15/12/2020");
		Date data1 = (Date) formato.parse("15/01/2021");
		List<Mensalidade> mensalidades = new ArrayList<Mensalidade>();
		Mensalidade mensalidade0 = new Mensalidade("Dezembro", data, "paga", 150);
		Mensalidade mensalidade1 = new Mensalidade("Janeiro", data1, "paga", 150);
		mensalidades.add(mensalidade0);
		mensalidades.add(mensalidade1);
		
		for(Mensalidade mensalidade : mensalidades) {
			Mensalidade mensalidadeSaved = mensalidadeRepository.save(mensalidade);
			//System.out.println(mensalidadeSaved.getId());
		}
	}
	
	@PostConstruct
	public void salveTurnos() {
		List<Turno> turnos = new ArrayList<Turno>();
		Turno turno0 = new Turno("Manhã");
		Turno turno1 = new Turno("Tarde");
		Turno turno2 = new Turno("Noite");
		turnos.add(turno0);
		turnos.add(turno1);
		turnos.add(turno2);
		
		for(Turno turno : turnos) {
			Turno turnoSaved = turnoRepository.save(turno);
			//System.out.println(turnoSaved.getId());
		}
	}
	
	@PostConstruct
	public void salvePagamentos() throws Exception {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date data = (Date) formato.parse("15/12/2020");
		Date data1 = (Date) formato.parse("15/01/2021");
		List<Pagamento> pagamentos = new ArrayList<Pagamento>();
		Pagamento pagamento0 = new Pagamento(data, "paga", 120,"bonificação");
		Pagamento pagamento1 = new Pagamento(data1, "paga", 120,"comissão");
		pagamentos.add(pagamento0);
		pagamentos.add(pagamento1);

		for(Pagamento pagamento : pagamentos) {
			Pagamento pagamentoSaved = pagamentoRepository.save(pagamento);
			//System.out.println(professorSaved.getId());
		}
	}
	
	@PostConstruct
	public void salveFuncionarios() {
		List<Professor> professores = new ArrayList<Professor>();
		Professor professor0 = new Professor(20211, "Alexandre", "82 99213-9963",123456,002145111,"alex20","123",3500);
		Professor professor1 = new Professor(20212, "Yana", "82 95236-2085", 3500, 1207896,"yan@" ,"123",3500);
		professores.add(professor0);
		professores.add(professor1);

		for(Professor professor : professores) {
			Professor professorSaved = professorRepository.save(professor);
			//System.out.println(professorSaved.getId());
		}
	}*/
}
