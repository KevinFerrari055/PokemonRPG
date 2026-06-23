package it.unicam.cs.mpgc.rpg130668.model.allenatore;

/**
 * Rappresenta la posizione attuale dell'allenatore nella mappa
 * E' un oggetto immutabile, quando l'allenatore si sposta, non modifico la sua posizione esistente,
 * ne creo una nuova e la assegno
 * @param x la coordinata x
 * @param y la coordinata y
 */
public record Posizione(int x, int y)
{

}
