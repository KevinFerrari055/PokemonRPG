package it.unicam.cs.mpgc.rpg130668.app;

import it.unicam.cs.mpgc.rpg130668.model.pokemon.*;
import it.unicam.cs.mpgc.rpg130668.persistence.file.PokemonRepositoryFile;

import java.util.List;
import java.util.Optional;

/**
 * Piccola classe di prova per verificare che il salvataggio/caricamento
 * su file JSON funzioni davvero, in particolare per il tipo del Pokemon
 * (il punto piu' delicato, gestito dall'adapter personalizzato).
 * Da eseguire una volta, poi puoi cancellarla o tenerla come test manuale.
 */
public class Main
{
    public static void main(String[] args)
    {
        PokemonSpecie charmander = new PokemonSpecie(
                "charmander", "Charmander",
                List.of(TipoElementale.FUOCO),
                39, 52, 43, 65,
                List.of()
        );

        Pokemon pokemonDiProva = new Pokemon("test1", charmander, 5);

        PokemonRepositoryFile repository = new PokemonRepositoryFile("data/test_pokemon.json");

        repository.salva(pokemonDiProva);
        System.out.println("Pokemon salvato su file.");

        Optional<Pokemon> ritrovato = repository.trovaPerId("test1");
        if (ritrovato.isPresent()) {
            Pokemon p = ritrovato.get();
            System.out.println("Nome specie: " + p.getSpecie().nome());
            System.out.println("Tipo: " + p.getSpecie().tipi().get(0).getNome());
            System.out.println("Livello: " + p.getLivello());
            System.out.println("PV attuali: " + p.getPvAttuali());
        } else {
            System.out.println("ERRORE: il pokemon non e' stato ritrovato dopo il salvataggio!");
        }

        List<Pokemon> tutti = repository.trovaTutti();
        System.out.println("Numero totale di pokemon nel file: " + tutti.size());
    }
}