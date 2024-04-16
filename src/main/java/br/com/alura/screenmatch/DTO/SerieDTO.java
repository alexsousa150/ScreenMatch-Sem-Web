package br.com.alura.screenmatch.DTO;

public record SerieDTO(long id,

                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Double genero,
                       String atores,
                       String poster,
                       String sinopse) {
}
