package it.unicam.cs.mpgc.rpg130668.model.combattimento;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.Mossa;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.Pokemon;

/**
 * Implementazione standard del calcolo del danno. La formula e' una
 * semplificazione personale (non quella esatta del gioco originale,
 * scelta deliberatamente per restare semplice): rapporto tra l'attacco
 * base dell'attaccante e la difesa base del difensore, moltiplicato per
 * la potenza della mossa e per il moltiplicatore di efficacia di tipo
 * preso dalla TabellaEfficacia.
 */
public class CalcolatoreDannoStandard implements CalcolatoreDanno
{
    private final TabellaEfficacia tabella;

    /**
     * Costruttore del calcolatore
     * @param tabella la tabella di efficacia del gioco
     * @throws NullPointerException se la tabella è null
     */
    public CalcolatoreDannoStandard(TabellaEfficacia tabella)
    {
        if(tabella == null) throw new NullPointerException("tabella di efficacia null");
        this.tabella = tabella;
    }

    /**
     * Calcola il danno da infliggere al difensore
     * @param attaccante il Pokemon che esegue l'attacco
     * @param difensore il Pokemon che subisce l'attacco
     * @param mossa la mossa usata per l'attacco
     * @throws NullPointerException se uno dei parametri passati è null
     * @return il danno calcolato da infliggere all'avversario, dipende chi chiama il metodo se è l'attaccante o il difensore.
     * Da notare che questo danno inflitto non è mai zero, minimo ritorna 1.
     */
    @Override
    public int calcolaDanno(Pokemon attaccante, Pokemon difensore, Mossa mossa)
    {
        if(attaccante == null || difensore == null || mossa == null) throw new NullPointerException("Uno dei parametri passati è null");
        // Efficacia ricavata dal tipo della MOSSA (non dal Pokemon attaccante:
        // un Pokemon puo' usare mosse di tipo diverso dal proprio) contro il
        // tipo del difensore. Per ora, visto che un Pokemon puo' avere piu'
        // tipi, uso semplicemente il primo della lista.
        double efficacia = tabella.moltiplicatore(mossa.tipo(), difensore.getSpecie().tipi().get(0));

        // Formula personale: più alto è l'attacco rispetto alla difesa
        // del difensore, maggiore è il rapporto, e quindi il danno.
        double rapporto = (double) attaccante.getSpecie().attaccoBase() / difensore.getSpecie().difesaBase();

        // Math.max(1, ...) garantisce che il danno non sia mai zero
        // per via degli arrotondamenti.
        return Math.max(1, (int) (rapporto * mossa.potenza() * efficacia));
    }
}