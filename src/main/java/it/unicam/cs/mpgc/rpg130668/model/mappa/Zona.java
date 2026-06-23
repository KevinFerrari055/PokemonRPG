package it.unicam.cs.mpgc.rpg130668.model.mappa;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.PokemonSpecie;

import java.util.List;

/**
 * Rappresenta le zone di cui la mappa sarà poi formata. E' un oggetto immutabile
 * poichè le zone durante il gioco non si possono cambiare e se voglio modificare la mappa
 * posso tranquillamente aggiungere Zone o rimuoverle.
 * @param nome della zona
 * @param tassoIncontro una probabilità tra 0 e 1 che rappresenta quando spesso capita un incontro in quella zona
 * @param specieDisponibili le specie che possono comparire lì.
 */
public record Zona(String nome, double tassoIncontro, List<PokemonSpecie> specieDisponibili) {
}
