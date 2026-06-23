package it.unicam.cs.mpgc.rpg130668.gui;

import it.unicam.cs.mpgc.rpg130668.controller.GiocoController;
import it.unicam.cs.mpgc.rpg130668.model.combattimento.Battaglia;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.Mossa;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.Pokemon;

import javax.swing.*;
import java.awt.*;

/**
 * Schermata di battaglia 1 vs 1: mostra lo stato dei due Pokemon,
 * le mosse disponibili e permette di catturare l'avversario se sconfitto.
 */
public class SchermataBattaglia extends JFrame
{
    private final GiocoController controller;
    private JLabel labelNomePokemonAllenatore;
    private JLabel labelPvPokemonAllenatore;
    private JLabel labelNomePokemonAvversario;
    private JLabel labelPvPokemonAvversario;
    private JTextArea logBattaglia;
    private JPanel pannelloMosse;

    public SchermataBattaglia(GiocoController controller)
    {
        this.controller = controller;

        setTitle("Pokemon RPG - Battaglia!");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        costruisciGui();
        aggiornaStato();

        setVisible(true);
    }

    private void costruisciGui()
    {
        JPanel pannelloPrincipale = new JPanel(new BorderLayout(10, 10));
        pannelloPrincipale.setBackground(new Color(30, 30, 60));
        pannelloPrincipale.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- AREA POKEMON ---
        JPanel areaPokemon = new JPanel(new GridLayout(1, 2, 20, 0));
        areaPokemon.setOpaque(false);
        areaPokemon.add(creaPannelloPokemon("Il tuo Pokemon", true));
        areaPokemon.add(creaPannelloPokemon("Pokemon avversario", false));
        pannelloPrincipale.add(areaPokemon, BorderLayout.NORTH);

        // --- LOG ---
        logBattaglia = new JTextArea(4, 40);
        logBattaglia.setEditable(false);
        logBattaglia.setBackground(new Color(20, 20, 40));
        logBattaglia.setForeground(Color.WHITE);
        logBattaglia.setFont(new Font("Arial", Font.PLAIN, 12));
        logBattaglia.setLineWrap(true);
        logBattaglia.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(logBattaglia);
        scrollLog.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pannelloPrincipale.add(scrollLog, BorderLayout.CENTER);

        // --- MOSSE ---
        pannelloMosse = new JPanel(new GridLayout(2, 2, 10, 10));
        pannelloMosse.setOpaque(false);
        pannelloPrincipale.add(pannelloMosse, BorderLayout.SOUTH);

        add(pannelloPrincipale);
    }

    private JPanel creaPannelloPokemon(String titolo, boolean isAllenatore)
    {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(new Color(50, 50, 90));
        pannello.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel labelTitolo = new JLabel(titolo, SwingConstants.CENTER);
        labelTitolo.setForeground(Color.LIGHT_GRAY);
        labelTitolo.setFont(new Font("Arial", Font.ITALIC, 11));
        labelTitolo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelNome = new JLabel("...", SwingConstants.CENTER);
        labelNome.setForeground(Color.WHITE);
        labelNome.setFont(new Font("Arial", Font.BOLD, 16));
        labelNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelPv = new JLabel("PV: ...", SwingConstants.CENTER);
        labelPv.setForeground(new Color(100, 220, 100));
        labelPv.setFont(new Font("Arial", Font.PLAIN, 13));
        labelPv.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (isAllenatore) {
            labelNomePokemonAllenatore = labelNome;
            labelPvPokemonAllenatore = labelPv;
        } else {
            labelNomePokemonAvversario = labelNome;
            labelPvPokemonAvversario = labelPv;
        }

        pannello.add(Box.createRigidArea(new Dimension(0, 5)));
        pannello.add(labelTitolo);
        pannello.add(Box.createRigidArea(new Dimension(0, 8)));
        pannello.add(labelNome);
        pannello.add(Box.createRigidArea(new Dimension(0, 8)));
        pannello.add(labelPv);
        pannello.add(Box.createRigidArea(new Dimension(0, 5)));

        return pannello;
    }

    private void aggiornaStato()
    {
        Battaglia battaglia = controller.getBattagliaInCorso();
        if (battaglia == null) return;

        Pokemon pokemonAllenatore = controller.getAllenatoreCorrente()
                .getSquadra().getPokemonAttivo().orElse(null);
        if (pokemonAllenatore == null) return;

        labelNomePokemonAllenatore.setText(pokemonAllenatore.getSpecie().nome());
        labelPvPokemonAllenatore.setText(
                "PV: " + pokemonAllenatore.getPvAttuali() +
                        " / " + pokemonAllenatore.getSpecie().pvBase()
        );

        Pokemon avversario = controller.getPokemonAvversario();
        if (avversario != null) {
            labelNomePokemonAvversario.setText(avversario.getSpecie().nome());
            labelPvPokemonAvversario.setText(
                    "PV: " + avversario.getPvAttuali() +
                            " / " + avversario.getSpecie().pvBase()
            );
        }

        // Ricreo i bottoni delle mosse
        pannelloMosse.removeAll();
        for (Mossa mossa : pokemonAllenatore.getMosseApprese())
        {
            JButton bottoneMossa = new JButton(
                    mossa.nome() + " (" + mossa.tipo().getNome() + ")"
            );
            bottoneMossa.setFont(new Font("Arial", Font.BOLD, 12));
            bottoneMossa.setBackground(new Color(220, 50, 50));
            bottoneMossa.setForeground(Color.WHITE);
            bottoneMossa.setFocusPainted(false);
            bottoneMossa.setOpaque(true);
            bottoneMossa.setBorderPainted(false);
            bottoneMossa.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bottoneMossa.addActionListener(e -> eseguiTurno(mossa, avversario));
            pannelloMosse.add(bottoneMossa);
        }

        pannelloMosse.revalidate();
        pannelloMosse.repaint();
    }

    private void eseguiTurno(Mossa mossa, Pokemon avversario)
    {
        Battaglia battaglia = controller.getBattagliaInCorso();
        if (battaglia == null || battaglia.isFinita()) return;

        // Salvo il nome PRIMA del turno, quando il Pokemon e' ancora in piedi
        String nomePokemonAllenatore = controller.getAllenatoreCorrente()
                .getSquadra().getPokemonAttivo()
                .map(p -> p.getSpecie().nome())
                .orElse("?");

        battaglia.eseguiTurno(mossa);

        Pokemon pokemonAllenatore = controller.getAllenatoreCorrente()
                .getSquadra().getPokemonAttivo().orElse(null);

        logBattaglia.append(nomePokemonAllenatore + " usa " + mossa.nome() + "!\n");

        if (avversario != null && avversario.isFuoriCombattimento()) {
            logBattaglia.append(avversario.getSpecie().nome() + " e' stato sconfitto!\n");
        } else if (pokemonAllenatore != null && pokemonAllenatore.isFuoriCombattimento()) {
            logBattaglia.append("Il tuo Pokemon e' stato sconfitto!\n");
        }

        aggiornaStato();

        if (battaglia.isFinita())
        {
            Pokemon vincitore = battaglia.getVincitore().orElse(null);
            boolean allenatoraHaVinto = vincitore != null && avversario != null
                    && !vincitore.getId().equals(avversario.getId());

            if (allenatoraHaVinto)
            {
                int scelta = JOptionPane.showConfirmDialog(this,
                        "Hai sconfitto " + avversario.getSpecie().nome() +
                                "! Vuoi catturarlo?",
                        "Cattura Pokemon!",
                        JOptionPane.YES_NO_OPTION);

                if (scelta == JOptionPane.YES_OPTION) {
                    controller.catturaPokemon(avversario);
                    logBattaglia.append(avversario.getSpecie().nome() + " catturato!\n");
                } else {
                    logBattaglia.append("Hai lasciato andare " +
                            avversario.getSpecie().nome() + ".\n");
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Il tuo Pokemon e' stato sconfitto!",
                        "Hai perso!",
                        JOptionPane.WARNING_MESSAGE);

                // Controlla se tutti i Pokemon sono esauriti
                if (controller.isGameOver()) {
                    JOptionPane.showMessageDialog(this,
                            "Tutti i tuoi Pokemon sono stati sconfitti!\nGame Over!",
                            "Game Over",
                            JOptionPane.ERROR_MESSAGE);
                    controller.terminaBattaglia();
                    System.exit(0); // chiude il gioco
                }
            }

            // Termina la battaglia e torna alla mappa
            controller.terminaBattaglia();
            new SchermataMappa(controller);
            dispose();
        }
    }
}