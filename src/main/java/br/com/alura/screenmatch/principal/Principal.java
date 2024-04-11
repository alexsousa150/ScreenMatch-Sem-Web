package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverterDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;

import java.util.*;
import java.util.stream.Collectors;

public class    Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=faecb298";
    private List<DadosSerie> dadosSerie = new ArrayList<>();

    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;
    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }



    public void exibirMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Série
                    2 - Episódios
                    3 - Lista de Série
                    4 - Buscar Série por Titulo
                    5 - Buscar Série por Ator
                    6 - Top 5 melhores séries
                    7 - Série por Categoria
                    8 - Filtrar séries
                    9 - Buscar episódio por trecho
                    10 - Buscar Top 5 episodios por serie
                    11 - Buscar episódio por data
                                    
                    0 - Sair
                                    
                    Digite a opção:
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();
            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSerieBuscada();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7: buscarSeriePorCategoria();
                    break;
                case 8:
                    filtrarSeries();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10: top5EpisodiosPorSerie();
                    break;
                case 11: buscarEpisodiosPorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }




    private void buscarSerieWeb() {
            DadosSerie dados = getDadosSerie();
            Serie serie = new Serie(dados);
            repositorio.save(serie);
            System.out.println(dados);
        }

        private DadosSerie getDadosSerie() {
            System.out.println("Digite o nome da série para busca:");
            var nomeSerie = leitura.nextLine();
            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
            return dados;
        }

        private void buscarEpisodioPorSerie() {
            listarSerieBuscada();
            System.out.println("Digite a Serie para busca:");
            var nomeSerie = leitura.nextLine();

            Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

            if(serie.isPresent()){
                var serieEncontrada = serie.get();
                List<DadosTemporada> temporadas = new ArrayList<>();

                for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                    var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                    DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                    temporadas.add(dadosTemporada);
                }
                temporadas.forEach(System.out::println);

                List<Episodio> episodios = temporadas.stream()
                        .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                        .collect(Collectors.toList());
                serieEncontrada.setEpisodios(episodios);
                repositorio.save(serieEncontrada);
            }else {
                System.out.println("Serie Não encontrada!");
            }
        }

        public void listarSerieBuscada(){
        series = repositorio.findAll();
            series.stream()
                    .sorted(Comparator.comparing(Serie::getGenero))
                    .forEach(System.out::println);
        }
    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série:");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()){
            System.out.println("Dados da série: " + serieBusca.get());
        }else {
            System.out.println("Séria não encotrada.");
        }
    }

    private void buscarSeriePorAtor(){
        System.out.println("Qual nome queres buscar");
        var nomeAtor = leitura.nextLine();
        System.out.println("Séries a partir de qual avaliação?");
        var avaliacao = leitura.nextDouble();
        List<Serie> serieEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Série em que "+ nomeAtor + " participou");
        serieEncontradas.forEach(s->
                System.out.println(s.getTitulo() + " Avaliação: " + s.getAvaliacao()));

    }
    private void buscarTop5Series(){
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s->
                System.out.println(s.getTitulo() + " Avaliação: " + s.getAvaliacao()));

    }
    private void buscarSeriePorCategoria() {
        System.out.println("Qual catogoria gostaria de buscar ?");
        var nomeGenero = leitura.nextLine();
        Catergoria catergoria = Catergoria.fromPortugues(nomeGenero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(catergoria);
        System.out.println("Séries da Categoria selecionada "+ nomeGenero);
        seriePorCategoria.forEach(System.out::println);

    }

    private void filtrarSeries() {
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas,avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));

    }
    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual episódio para busca ? ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                        System.out.printf("Série %s - Temporada %s - Episodios %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroDoEpisodio(),
                                e.getTitulo()));
    }

    private void top5EpisodiosPorSerie() {
        buscarSeriePorTitulo();

        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.TopEpisodioPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série %s - Temporada %s - Episodios %s - %s - Avaliação: %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroDoEpisodio(),
                            e.getTitulo(), e.getAvaliacao()));

        }
    }
    private void buscarEpisodiosPorData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Qual o ano de lançamento");
            var dataDoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repositorio.episodioPorSerieEAno(serie, dataDoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }


}




