package it.unicam.cs.mpgc.rpg130668.model.pokemon;

/**
 * Implementazione base dei tipi elementali previsti nella prima release.
 * La logica di efficacia tra tipi (es. il Fuoco e' debole contro l'Acqua)
 * NON e' qui: sara' responsabilita' di una classe dedicata (TabellaEfficacia,
 * nel package model.combattimento), cosi' aggiungere un nuovo tipo non
 * richiedera' di modificare i tipi gia' esistenti.
 */
public enum TipoElementale implements TipoPokemon
{
    FUOCO("Fuoco"),
    ACQUA("Acqua"),
    ERBA("Erba"),
    NORMALE("Normale"),
    ELETTRICO("Elettrico");

    private final String nome;

    /**
     * Costruttore che rappresenta il tipo dell'elemento che rappresenta il pokemon
     * @param nome del tipo
     */
    TipoElementale(String nome)
    {
        if(nome == null) throw new NullPointerException("il nome passato è null");
        if(!(nome.trim().equals("Fuoco") ||
                nome.trim().equals("Acqua") ||
                nome.trim().equals("Erba") ||
                nome.trim().equals("Normale") ||
                nome.trim().equals("Elettrico"))) throw new IllegalArgumentException("il nome del tipo non corrisponde a nessuno dei valori esistenti");
        this.nome = nome;
    }
    @Override
    public String getNome() {
        return nome;
    }
}