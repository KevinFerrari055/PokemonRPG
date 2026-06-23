package it.unicam.cs.mpgc.rpg130668.model.combattimento;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.Mossa;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.Pokemon;

/**
 * Calcola il danno infierito da una mossa durante un combattimento.
 * Isolare questo calcolo in un'interfaccia permette di cambiare la formula
 * (o usarne una diversa, ad esempio per scopi di test) senza modificare
 * la classe Battaglia, che dipendera' solo da questa astrazione.
 */
public interface CalcolatoreDanno
{
    /**
     * @param attaccante il Pokemon che esegue l'attacco
     * @param difensore il Pokemon che subisce l'attacco
     * @param mossa la mossa usata per l'attacco
     * @return il danno (sempre almeno 1) da sottrarre ai punti vita attuali del difensore
     */
    int calcolaDanno(Pokemon attaccante, Pokemon difensore, Mossa mossa);
}