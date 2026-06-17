package it.unicam.cs.mpgc.rpg130668.persistence.json;

import com.google.gson.*;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.TipoElementale;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.TipoPokemon;

import java.lang.reflect.Type;

public class TipoPokemonDeserializer implements JsonDeserializer<TipoPokemon>
{
    @Override
    public TipoPokemon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String nome = jsonObject.get("nome").getAsString();

        for (TipoElementale tipo : TipoElementale.values()) {
            if (tipo.getNome().equals(nome)) {
                return tipo;
            }
        }

        throw new JsonParseException("Tipo non riconosciuto: " + nome);
    }
}