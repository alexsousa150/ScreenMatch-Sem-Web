package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadoEpisodio;
import br.com.alura.screenmatch.model.DadoSerie;
import br.com.alura.screenmatch.model.DadoTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverterDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

        temporadas.forEach(t ->t.episodios().forEach(e-> System.out.println(e.titulo())));

        List<DadoEpisodio> dadoEpisodios =temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //--Seleção dos 5 melhores episodios baseados na Avaliação--
        System.out.println("\nTop 5 Episodios");
        dadoEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadoEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        // --Pega todos episodios e orderna por temporadas--
        List<Episodio> episodio = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d))
                ).collect(Collectors.toList());

        episodio.forEach(System.out::println);

        System.out.println("Qual trecho do titulo do episodio...");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodio.stream()
                .filter(e-> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        }else{
            System.out.println("Episodio não encontrado");
        }

        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano,1,1);

        DateTimeFormatter  formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodio.stream()
                .filter(e -> e.getDataDoLancamento()!= null && e.getDataDoLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada()+
                                " Episódio: " + e.getTitulo() +
                                " Data de Lançamento: " + e.getDataDoLancamento().format(formatador)
               ));

        //Pega as avaliaçoes e faz uma media de avaliação por temporada
        Map<Integer, Double> avaliacaoPorTemporada = episodio.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodio.stream()
                .filter(e->e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println(est);
        System.out.println("Média: " +est.getAverage());
        System.out.println("Melhor Episódio: "+est.getMax());
        System.out.println("Pior episódio: "+est.getMin());
        System.out.println("Quantidade episódio: "+ est.getCount());
    }
}
