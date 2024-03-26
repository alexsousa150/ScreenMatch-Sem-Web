package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadoSerie;
import br.com.alura.screenmatch.model.DadoTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverterDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=faecb298";
    public void exibirMenu(){
        System.out.println("Digite a séria para a busca...");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadoSerie dados = conversor.obterDados(json, DadoSerie.class);
        System.out.println(dados);

        List<DadoTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+"&season=" + i + API_KEY);
			DadoTemporada dadosTemporada = conversor.obterDados(json, DadoTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

    }
}
