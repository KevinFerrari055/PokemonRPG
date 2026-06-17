package it.unicam.cs.mpgc.rpg130668.persistence.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.TipoElementale;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.TipoPokemon;

/**
 * Crea l'istanza di Gson configurata con gli adapter personalizzati
 * necessari per leggere/scrivere correttamente gli oggetti del progetto.
 * TipoPokemon, essendo un'interfaccia, ha bisogno di un serializzatore
 * e di un deserializzatore dedicati: senza di questi, Gson non saprebbe
 * quale classe concreta istanziare quando rilegge il JSON.
 */
public class GsonProvider
{
    public static Gson creaGson()
    {
        return new GsonBuilder()
                .registerTypeAdapter(TipoPokemon.class, new TipoPokemonSerializer())
                .registerTypeAdapter(TipoPokemon.class, new TipoPokemonDeserializer())
                .registerTypeAdapter(TipoElementale.class, new TipoPokemonSerializer())
                .registerTypeAdapter(TipoElementale.class, new TipoPokemonDeserializer())
                .setPrettyPrinting()
                .create();
    }
}