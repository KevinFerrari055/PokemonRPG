package it.unicam.cs.mpgc.rpg130668.model.combattimento;


import it.unicam.cs.mpgc.rpg130668.model.pokemon.TipoPokemon;

import java.util.HashMap;
import java.util.Map;

/**
 * Consente le relazioni di efficacia tra tipi (es. il Fuoco è debole contro l'Acqua, forte contro l'Erba).
 * Questa logica è volutamente separata dall'interfaccia TipoPokemon: aggiungere un nuovo tipo
 * significa solo aggiornare questa tabella, senza modificare le implementazioni
 * di tipo esistenti (Open/Closed Principle).
 *
 * Le relazioni sono memorizzate usando il NOME del tipo (getNome())
 * come chiave, e non direttamente TipoElementale: così la tabella
 * funziona con qualunque implementazione futura di TipoPokemon,
 * non solo con quella attuale.
 */
public class TabellaEfficacia
{
    //Mappa "a griglia": la chiave esterna e' il tipo che attacca,
    //il valore associato e' un'altra mappa che contiene, per ogni possibie tipo difensore
    //il moltiplicatore di danno corrispondente.
    private final Map<String, Map<String, Double>> tabella;

    public TabellaEfficacia()
    {
        tabella = new HashMap<>();

        //Riga del Fuoco: tutte le sue efficacie contro gli altri tipi.
        Map<String, Double> efficaciaFuoco = new HashMap<>();
        efficaciaFuoco.put("Erba", 2.0); //super efficace
        efficaciaFuoco.put("Acqua", 0.5); //poco efficace
        tabella.put("Fuoco", efficaciaFuoco);

        //Riga dell'Acqua: tutte le sue efficacie contro gli altri tipi.
        Map<String, Double> efficaciaAcqua = new HashMap<>();
        efficaciaAcqua.put("Fuoco", 2.0); //super efficace
        efficaciaAcqua.put("Erba", 0.5); //poco efficace
        tabella.put("Acqua", efficaciaAcqua);

        //Riga del Fuoco: tutte le sue efficacie contro gli altri tipi.
        Map<String, Double> efficaciaErba = new HashMap<>();
        efficaciaErba.put("Acqua", 2.0); //super efficace
        efficaciaErba.put("Fuoco", 0.5); //poco efficace
        tabella.put("Erba", efficaciaErba);

        //Riga dell'Elettrico: tutte le sue efficacie contro gli altri tipi.
        Map<String, Double> efficaciaElettrico = new HashMap<>();
        efficaciaElettrico.put("Acqua", 2.0); //super efficace
        efficaciaElettrico.put("Erba", 0.5); //poco efficace
        tabella.put("Elettrico", efficaciaElettrico);

        //Normale non ha ne' forza ne' debolezze: non gli serve nessuna riga,
        //il caso "non trovato" viene gestito di default in moltiplicatore().
    }

    /**
     * @param attaccante il tipo della mossa che attacca
     * @param difensore il tipo del Pokemon che subisce l'attacco
     * @return il moltiplicatore di danno: 2.0 se super efficace, 0.5 se poco
     * efficace, 1.0 se l'efficacia e' normale (incluso il caso in cui la
     * combinazione di tipi non sia stata definita esplicitamente in tabella)
     * @throws NullPointerException se uno dei pokemon passati è null
     */
    public double moltiplicatore(TipoPokemon attaccante, TipoPokemon difensore)
    {
        if(attaccante == null || difensore == null) throw new NullPointerException("Uno dei Pokemon passati è null");
        // Primo livello: trovo la "riga" del tipo attaccante. Se quel tipo
        // non ha nessuna riga (es. Normale), uso una mappa vuota come
        // alternativa, cosi' evito un NullPointerException alla riga dopo.
        Map<String, Double> efficaciaContro = tabella.getOrDefault(attaccante.getNome(), Map.of());

        // Secondo livello: dentro quella riga, cerco il tipo difensore.
        // Se non e' stato definito esplicitamente, l'efficacia e' normale (1.0).
        return efficaciaContro.getOrDefault(difensore.getNome(), 1.0);
    }
}

