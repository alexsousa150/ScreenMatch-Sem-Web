package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibirMenu();


//		List<DadoTemporada> temporadas = new ArrayList<>();
//		for (int i = 1; i <= dados.totalTemporadas(); i++){
//			json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season="+
//					i
//					+"&apikey=faecb298");
//			DadoTemporada dadosTemporada = conversor.obterDados(json, DadoTemporada.class);
//			temporadas.add(dadosTemporada);
//		}
//		temporadas.forEach(System.out::println);

	}
}
