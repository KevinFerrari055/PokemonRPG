package it.unicam.cs.mpgc.rpg130668.model.mappa;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementazione base del generatore di incontri: con una probabilita' pari
 * al tasso di incontro della zona, sceglie a caso una specie tra quelle
 * disponibili in quella zona e genera un nuovo Pokemon selvatico
 * a un livello casuale in un piccolo intervallo.
 */
public class GeneratoreIncontriCasuale implements GeneratoreIncontri
{
    private static final int LIVELLO_MINIMO = 2;
    private static final int LIVELLO_MASSIMO = 5;

    /**
     * Genera l'incontro tra il pokemon dell'allenatore e il Pokemon selvatico trovato nella mappa
     * @param zona la zona in cui l'allenatore si trova attualmente
     * @return Un Pokemon Selvatico che potrebbe anche non "spawnare" per via del tasso incontro
     * o per le specie non disponibili in quella zona
     * @throws NullPointerException se la zona passata è null
     */
    @Override
    public Optional<Pokemon> generaIncontro(Zona zona)
    {
        if(zona == null) throw new NullPointerException("La zona passata è null");
        // Math.random() restituisce un numero tra 0 (incluso) e 1 (escluso).
        // Se e' maggiore o uguale al tasso di incontro della zona, niente incontro.
        double casuale = Math.random();
        if (casuale >= zona.tassoIncontro())
        {
            return Optional.empty();
        }

        List<PokemonSpecie> specieDisponibili = zona.specieDisponibili();
        if (specieDisponibili.isEmpty())
        {
            // La probabilita' era favorevole, ma non c'e' nessuna specie
            // configurata per questa zona: nessun incontro possibile.
            return Optional.empty();
        }

        // Scegliamo a caso una specie tra quelle disponibili: un indice
        // intero casuale tra 0 (incluso) e size (escluso).
        int indiceCasuale = (int) (Math.random() * specieDisponibili.size());
        PokemonSpecie specieScelta = specieDisponibili.get(indiceCasuale);

        // Livello casuale in un piccolo intervallo fisso (per ora mi tengo
        // semplice, in futuro si potrebbe far dipendere dal progresso dell'allenatore).
        int livello = LIVELLO_MINIMO + (int) (Math.random() * (LIVELLO_MASSIMO - LIVELLO_MINIMO + 1));

        // Ogni Pokemon ha bisogno di un id univoco: un UUID generato
        // automaticamente garantisce che non si ripeta mai per caso.
        String id = UUID.randomUUID().toString();

        Pokemon pokemonSelvatico = getPokemon(id, specieScelta, livello);

        return Optional.of(pokemonSelvatico);
    }

    private static Pokemon getPokemon(String id, PokemonSpecie specieScelta, int livello) {
        Pokemon pokemonSelvatico = new Pokemon(id, specieScelta, livello);

        // Assegna una mossa base al Pokemon selvatico in base al suo tipo,
        // altrimenti non potrebbe mai contrattaccare in battaglia
        String nomeTipo = specieScelta.tipi().get(0).getNome();
        Mossa mossaBase = switch (nomeTipo) {
            case "Fuoco"     -> new Mossa("Braciere", TipoElementale.FUOCO, 10, 100, CategoriaMossa.SPECIALE);
            case "Acqua"     -> new Mossa("Schizzata", TipoElementale.ACQUA, 10, 100, CategoriaMossa.SPECIALE);
            case "Erba"      -> new Mossa("Fogliolina", TipoElementale.ERBA, 10, 100, CategoriaMossa.SPECIALE);
            case "Elettrico" -> new Mossa("Scintilla", TipoElementale.ELETTRICO, 10, 100, CategoriaMossa.SPECIALE);
            default          -> new Mossa("Tackle", TipoElementale.NORMALE, 20, 100, CategoriaMossa.FISICA);
        };
        pokemonSelvatico.impara(mossaBase);
        return pokemonSelvatico;
    }
}