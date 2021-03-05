package br.fitness.academy.util;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.fitness.academy.model.*;
import br.fitness.academy.repository.*;

@Component
public class createDataTurnos {
	@Autowired
	TurnoRepository turnoRepository;

	@PostConstruct
	public void createTurnos() {
		if(turnoRepository.count() == 0) {
			List<Turno> turnos = new ArrayList<Turno>();
			Turno manha = new Turno("Manhã");
			Turno tarde = new Turno("Tarde");
			Turno noite = new Turno("Noite");
			turnos.add(manha);
			turnos.add(tarde);
			turnos.add(noite);
			
			for(Turno turno : turnos) {
				turnoRepository.save(turno);
			}
		}
	}
}
