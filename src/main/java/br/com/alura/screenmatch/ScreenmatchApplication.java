package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadoSerie;
import br.com.alura.screenmatch.service.ConcerterDados;
import br.com.alura.screenmatch.service.ConsumoAPI;
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
		var consumoApi = new ConsumoAPI();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=Gilmore+Girls&apikey=faecb298");
//		System.out.println(json);
//		json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		ConcerterDados conversor = new ConcerterDados();
		DadoSerie dados = conversor.obterDados(json, DadoSerie.class);
		System.out.println(dados);
	}
}
