package it.unicam.cs.mpgc.rpg130668.model.allenatore;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Rappresenta la squadra dell'allenatore, quindi gestisce la collezione di Pokemon posseduti
 * E' separata da allenatore per non dargli due responsabilità diverse
 */
public class Squadra
{
    //Attributi
    private List<Pokemon> pokemonPosseduti;
    public static final int CAPACITA_MASSIMA = 6;

    /**
     * Costruttore della classe, vuoto senza parametri con la costruzione della lista
     */
    //Costruttore
    public Squadra()
    {
        pokemonPosseduti = new ArrayList<>(CAPACITA_MASSIMA);
    }

    //Metodi

    /**
     *
     * @return true se la lista dei pokemon della squadra è piena, false altrimenti
     */
    public boolean isPiena()
    {
        return pokemonPosseduti.size() >= CAPACITA_MASSIMA;
    }

    /**
     * Metodo aggiungi Pokemon nella squadra
     * @param pokemon che si vuole aggiungere nella squadra
     * @return false se pokemon se la squadra è piena oppure il pokemon già è presente nella squadra
     * @throws NullPointerException se pokemon è null
     * true altrimenti
     */
    public boolean aggiungiPokemon(Pokemon pokemon)
    {
        if(pokemon == null) throw new NullPointerException("Il pokemon passato non può essere null");
        if(isPiena()) return false;
        if(pokemonPosseduti.contains(pokemon)) return false;
        pokemonPosseduti.add(pokemon);
        return true;
    }

    /**
     * Rimuove il pokemon dalla Squadra
     * @param pokemon che si vuole rimuovere
     * @return false se il pokemon se la squadra è vuota o se il pokemon non è presente nella squadra
     * @throws NullPointerException se pokemon è null
     * true altrimenti
     */
    public boolean rimuoviPokemon(Pokemon pokemon)
    {
        if(pokemon == null) throw new NullPointerException("Il pokemon passato non può essere null");
        if(pokemonPosseduti.isEmpty()) return false;
        if(!pokemonPosseduti.contains(pokemon)) return false;
        pokemonPosseduti.remove(pokemon);
        return true;
    }

    /**
     *
     * @return un Optional Pokemon con il primo pokemon della squadra che non è fuori combattimento
     */
    public Optional<Pokemon> getPokemonAttivo()
    {
        return pokemonPosseduti.stream().filter(p -> !p.isFuoriCombattimento()).findFirst();
    }

    /**
     * @return true se tutti i Pokemon della squadra sono fuori combattimento
     */
    public boolean tuttiSconfitti()
    {
        return pokemonPosseduti.stream().allMatch(Pokemon::isFuoriCombattimento);
    }
}
