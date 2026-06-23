package it.unicam.cs.mpgc.rpg130668.model.mappa;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.Pokemon;

import java.util.Optional;

/**
 * Decide se e quale Pokemon selvatico compare quando l'allenatore si trova
 * in una determinata Zona. Essendo un'interfaccia, la strategia di generazione
 * degli incontri (casuale, basata sul livello dell'allenatore, eventi di trama...)
 * puo' essere cambiata in futuro senza modificare Mappa o Zona, che dipendono
 * solo da questa astrazione (Open/Closed Principle).
 */
public interface GeneratoreIncontri
{
    /**
     * @param zona la zona in cui l'allenatore si trova attualmente
     * @return un Optional contenente il Pokemon selvatico incontrato,
     * oppure Optional.empty() se questa volta non c'e' stato nessun incontro
     */
    Optional<Pokemon> generaIncontro(Zona zona);
}