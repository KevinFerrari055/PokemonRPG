package it.unicam.cs.mpgc.rpg130668.model.pokemon;

public enum TipoElementale implements TipoPokemon
{
    FUOCO("Fuoco"),
    ACQUA("Acqua"),
    ERBA("Erba"),
    NORMALE("Normale"),
    ELETTRICO("Elettrico");

    private final String nome;

    TipoElementale(String nome) {
        this.nome = nome;
    }

    @Override
    public String getNome() {
        return nome;
    }
}
