package it.unicam.cs.mpgc.rpg130668.app;

import it.unicam.cs.mpgc.rpg130668.controller.GiocoController;
import it.unicam.cs.mpgc.rpg130668.persistence.file.AllenatoreRepositoryFile;
import it.unicam.cs.mpgc.rpg130668.persistence.file.PokemonRepositoryFile;

public class Main
{
    public static void main(String[] args)
    {
        GiocoController controller = new GiocoController(
                new AllenatoreRepositoryFile("data/allenatori.json"),
                new PokemonRepositoryFile("data/pokemon.json")
        );

        // TODO: avviare SchermataBenvenuto passando il controller
    }
}