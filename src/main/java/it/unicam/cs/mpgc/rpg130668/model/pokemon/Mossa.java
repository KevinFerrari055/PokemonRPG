package it.unicam.cs.mpgc.rpg130668.model.pokemon;

/**
 * Rappresenta una mossa (attacco) che un Pokemon puo' eseguire in battaglia.
 * E' un dato immutabile: la stessa Mossa puo' essere condivisa da piu' specie
 * (es. "Graffio" la conoscono sia Charmander che Squirtle).
 *
 * @param nome il nome della mossa (es. "Fiammata")
 * @param tipo il tipo elementale della mossa
 * @param potenza la potenza base dell'attacco
 * @param precisione la probabilita' (in percentuale) che l'attacco abbia successo
 * @param categoria se la mossa e' fisica, speciale o di stato
 */
public record Mossa(String nome, TipoPokemon tipo, int potenza, int precisione, CategoriaMossa categoria) {
}