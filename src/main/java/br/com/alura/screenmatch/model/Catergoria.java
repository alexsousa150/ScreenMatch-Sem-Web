package br.com.alura.screenmatch.model;

public enum Catergoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    CRIME("Crime", "Crime"),
    DRAMA("Drama", "Drama"),
    AVENTURA("Adventure", "Aventura");

    private String categoriaOmdb;
    private String categoriaPortugues;

    Catergoria(String categoriaOmdb, String categoriaPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }
    public static Catergoria fromString(String text) {
        for (Catergoria categoria : Catergoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Catergoria fromPortugues(String text) {
        for (Catergoria categoria : Catergoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
