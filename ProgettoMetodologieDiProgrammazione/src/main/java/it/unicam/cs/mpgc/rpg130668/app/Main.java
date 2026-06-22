package it.unicam.cs.mpgc.rpg130668.app;

import it.unicam.cs.mpgc.rpg130668.controller.GiocoController;
import it.unicam.cs.mpgc.rpg130668.gui.SchermataBenvenuto;
import it.unicam.cs.mpgc.rpg130668.persistence.file.AllenatoreRepositoryFile;
import it.unicam.cs.mpgc.rpg130668.persistence.file.PokemonRepositoryFile;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        // Imposta il Look and Feel di sistema (Windows nativo)
        // evita rendering strani dei componenti Swing
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        GiocoController controller = new GiocoController(
                new AllenatoreRepositoryFile("data/allenatori.json"),
                new PokemonRepositoryFile("data/pokemon.json")
        );

        SwingUtilities.invokeLater(() -> new SchermataBenvenuto(controller));
    }
}