package it.unicam.cs.mpgc.rpg130668.persistence.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.Pokemon;
import it.unicam.cs.mpgc.rpg130668.persistence.PokemonRepository;
import it.unicam.cs.mpgc.rpg130668.persistence.json.GsonProvider;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PokemonRepositoryFile implements PokemonRepository
{
    private final Gson gson;
    private final String percorsoFile;
    private final Type tipoLista;

    public PokemonRepositoryFile(String percorsoFile)
    {
        this.gson = GsonProvider.creaGson();
        this.percorsoFile = percorsoFile;
        this.tipoLista = new TypeToken<List<Pokemon>>(){}.getType();
    }

    @Override
    public void salva(Pokemon pokemon)
    {
        List<Pokemon> tutti = new ArrayList<>(trovaTutti());
        tutti.removeIf(p -> p.getId().equals(pokemon.getId()));
        tutti.add(pokemon);
        scriviTutti(tutti);
    }

    @Override
    public Optional<Pokemon> trovaPerId(String id)
    {
        return trovaTutti().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Pokemon> trovaTutti()
    {
        File file = new File(percorsoFile);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new BufferedReader(new FileReader(file))) {
            List<Pokemon> letti = gson.fromJson(reader, tipoLista);
            return letti != null ? letti : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore durante la lettura: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void elimina(String id)
    {
        List<Pokemon> tutti = new ArrayList<>(trovaTutti());
        tutti.removeIf(p -> p.getId().equals(id));
        scriviTutti(tutti);
    }

    private void scriviTutti(List<Pokemon> pokemons)
    {
        File file = new File(percorsoFile);
        File cartella = file.getParentFile();
        if (cartella != null) {
            cartella.mkdirs();
        }

        try (Writer writer = new BufferedWriter(new FileWriter(percorsoFile))) {
            gson.toJson(pokemons, tipoLista, writer);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura: " + e.getMessage());
        }
    }
}