package it.unicam.cs.mpgc.rpg130668.model.combattimento;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.Mossa;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.Pokemon;

import java.util.List;
import java.util.Optional;

/**
 * E' l'orchestratore di una battaglia 1 vs 1 a turni tra il Pokemon attivo
 * dell'allenatore e un Pokemon avversario (selvatico o di un altro allenatore).
 *
 * Ogni chiamata a eseguiTurno(...) risolve un solo scambio di colpi.
 */
public class Battaglia
{
    private final Pokemon pokemonAllenatore;
    private final Pokemon pokemonAvversario;
    private final CalcolatoreDanno calcolatore;

    /**
     * @param pokemonAllenatore il Pokemon attivo dell'allenatore controllato dal giocatore
     * @param pokemonAvversario il Pokemon contro cui si combatte
     * @param calcolatore la strategia usata per calcolare il danno di ogni colpo
     * @throws NullPointerException se uno dei parametri e' null
     */
    public Battaglia(Pokemon pokemonAllenatore, Pokemon pokemonAvversario, CalcolatoreDanno calcolatore)
    {
        if(pokemonAllenatore == null || pokemonAvversario == null || calcolatore == null) {
            throw new NullPointerException("Parametro nullo");
        }
        this.pokemonAllenatore = pokemonAllenatore;
        this.pokemonAvversario = pokemonAvversario;
        this.calcolatore = calcolatore;
    }

    /**
     * Risolve un singolo scambio di colpi: chi e' piu' veloce attacca per primo,
     * e se l'altro Pokemon e' ancora in piedi dopo quel colpo, risponde a sua volta.
     * Se la battaglia e' gia' finita, non fa nulla.
     *
     * @param mossaScelta la mossa scelta dal giocatore per il Pokemon dell'allenatore in questo turno
     * @throws NullPointerException se la mossaScelta è null
     */
    public void eseguiTurno(Mossa mossaScelta)
    {
        if(mossaScelta == null) throw new NullPointerException("La mossa passata è null");

        //Se la partita è finita, il turno non viene eseguito
        if (isFinita()) {
            return;
        }

        //Controllo sulla velocità chi inizia per primo(scelto da me)
        boolean allenatorePiuVeloce = pokemonAllenatore.getSpecie().velocitaBase()
                >= pokemonAvversario.getSpecie().velocitaBase();

        if (allenatorePiuVeloce)
        {
            attacca(pokemonAllenatore, pokemonAvversario, mossaScelta);
            if (!pokemonAvversario.isFuoriCombattimento())
            {
                attaccaSePossibile(pokemonAvversario, pokemonAllenatore, sceglieMossaAvversario());
            }
        } else
        {
            attaccaSePossibile(pokemonAvversario, pokemonAllenatore, sceglieMossaAvversario());
            if (!pokemonAllenatore.isFuoriCombattimento())
            {
                attacca(pokemonAllenatore, pokemonAvversario, mossaScelta);
            }
        }
    }

    /**
     * @return true se almeno uno dei due Pokemon e' fuori combattimento,
     * e quindi la battaglia e' conclusa
     */
    public boolean isFinita()
    {
        return pokemonAllenatore.isFuoriCombattimento() || pokemonAvversario.isFuoriCombattimento();
    }

    /**
     * @return il Pokemon vincitore, se la battaglia e' conclusa;
     * Optional.empty() se e' ancora in corso
     */
    public Optional<Pokemon> getVincitore()
    {
        if (!isFinita()) {
            return Optional.empty();
        }
        Pokemon vincitore = pokemonAllenatore.isFuoriCombattimento() ? pokemonAvversario : pokemonAllenatore;
        return Optional.of(vincitore);
    }

    /**
     * Esegue un singolo attacco: calcola il danno tramite il CalcolatoreDanno
     * e lo applica al difensore.
     */
    private void attacca(Pokemon attaccante, Pokemon difensore, Mossa mossa)
    {
        int danno = calcolatore.calcolaDanno(attaccante, difensore, mossa);
        difensore.subisciDanno(danno);
    }

    /**
     * Come attacca(...), ma non fa nulla se la mossa e' null: serve per il caso
     * in cui l'avversario non conosca ancora nessuna mossa (es. un Pokemon
     * selvatico appena generato), evitando un attacco "a vuoto" invece di un crash.
     */
    private void attaccaSePossibile(Pokemon attaccante, Pokemon difensore, Mossa mossa)
    {
        if (mossa == null) {
            return;
        }
        attacca(attaccante, difensore, mossa);
    }

    /**
     * @return una mossa scelta a caso tra quelle conosciute dall'avversario,
     * oppure null se non ne conosce ancora nessuna
     */
    private Mossa sceglieMossaAvversario()
    {
        List<Mossa> mosse = pokemonAvversario.getMosseApprese();
        if (mosse.isEmpty()) {
            return null;
        }
        int indiceCasuale = (int) (Math.random() * mosse.size());
        return mosse.get(indiceCasuale);
    }

    public Pokemon getPokemonAvversario()
    {
        return pokemonAvversario;
    }
}