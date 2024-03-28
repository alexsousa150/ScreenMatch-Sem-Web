package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private double avaliacao;
    private LocalDate dataDoLancamento;

    public Episodio(Integer numeroTemporada, DadoEpisodio dadosEpisodio){
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        try{
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try{
            this.dataDoLancamento = LocalDate.parse(dadosEpisodio.dataDoLancamento());
        }catch(DateTimeParseException ex){
            this.dataDoLancamento = null;
        }


    }
    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroDoEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroDoEpisodio(Integer numeroDoEpisodio) {
        this.numeroEpisodio = numeroDoEpisodio;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataDoLancamento() {
        return dataDoLancamento;
    }

    public void setDataDoLancamento(LocalDate dataDoLancamento) {
        this.dataDoLancamento = dataDoLancamento;
    }

    @Override
    public String toString() {
        return "Temporada=" + temporada +
                ", Titulo='" + titulo + '\'' +
                ", Numero do Episodio= " + numeroEpisodio +
                ", avaliacao= " + avaliacao +
                ", Data de Lançamento=" + dataDoLancamento;
    }
}
