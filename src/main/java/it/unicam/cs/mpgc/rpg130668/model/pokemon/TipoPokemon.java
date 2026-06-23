package it.unicam.cs.mpgc.rpg130668.model.pokemon;

/**
 * Rappresenta un tipo elementale di Pokemon (es. Fuoco, Acqua, Erba).
 * E' un'interfaccia, e non direttamente un enum, perche' in questo modo
 * nuovi tipi possono essere aggiunti in futuro (es. caricati da dati esterni,
 * o con un comportamento piu' complesso) senza modificare il codice che dipende
 * gia' da questa astrazione (Open/Closed Principle).
 */
public interface TipoPokemon
{
    /**
     * @return il nome leggibile del tipo (es. "Fuoco").
     */
    String getNome();
}