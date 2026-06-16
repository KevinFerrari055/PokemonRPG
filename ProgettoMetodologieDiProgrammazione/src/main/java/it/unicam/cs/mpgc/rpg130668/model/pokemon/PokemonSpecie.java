package it.unicam.cs.mpgc.rpg130668.model.pokemon;

import java.util.List;

/**
 * Rappresenta il "template" di una specie di Pokemon (es. Charmander):
 * statistiche base, tipi e mosse apprendibili.
 * E' separata dalla classe Pokemon (che rappresenta invece la singola
 * istanza posseduta da un allenatore) per garantire estendibilita':
 * aggiungere una nuova specie in futuro significa creare un nuovo oggetto
 * PokemonSpecie (anche caricato da un file di dati), non scrivere nuovo codice Java.
 */
public record PokemonSpecie(
        String id,
        String nome,
        List<TipoPokemon> tipi,
        int pvBase,
        int attaccoBase,
        int difesaBase,
        int velocitaBase,
        List<Mossa> mosseDisponibili
) {
}
