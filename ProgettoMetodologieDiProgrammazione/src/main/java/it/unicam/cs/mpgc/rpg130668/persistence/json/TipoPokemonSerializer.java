package it.unicam.cs.mpgc.rpg130668.persistence.json;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.TipoPokemon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class TipoPokemonSerializer implements JsonSerializer<TipoPokemon>
{
    @Override
    public JsonElement serialize(TipoPokemon p, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nome", p.getNome());
        return jsonObject;
    }

}
