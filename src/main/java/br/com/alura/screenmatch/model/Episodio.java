package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private double avaliacao;
    private LocalDate dataLancamento;
    @ManyToOne
    private Serie serie;
    public Episodio(){}

        public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio){
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        try{
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try{
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataDoLancamento());
        }catch(DateTimeParseException ex){
            this.dataLancamento = null;
        }


    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
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
        return dataLancamento;
    }

    public void setDataDoLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "Temporada= " + temporada +
                ", Titulo= '" + titulo + '\'' +
                ", Numero do Episodio= " + numeroEpisodio +
                ", avaliacao= " + avaliacao +
                ", Data de Lançamento= " + dataLancamento;
    }
}
