package it.unicam.cs.mpgc.rpg130668.gui;

import it.unicam.cs.mpgc.rpg130668.controller.GiocoController;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.PokemonSpecie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Schermata di scelta dello starter: mostra i tre Pokemon disponibili
 * (Charmander, Squirtle, Bulbasaur) con immagine, statistiche e bottone di scelta.
 */
public class SchermataSceltaStarter extends JFrame
{
    private final GiocoController controller;

    public SchermataSceltaStarter(GiocoController controller)
    {
        this.controller = controller;

        setTitle("Pokemon RPG - Scegli il tuo starter");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        costruisciGui();
        setVisible(true);
    }

    private void costruisciGui()
    {
        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(new Color(30, 30, 60));
        pannelloPrincipale.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- TITOLO ---
        JLabel titolo = new JLabel("Scegli il tuo Pokemon starter!", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 20));
        titolo.setForeground(Color.YELLOW);
        pannelloPrincipale.add(titolo, BorderLayout.NORTH);

        // --- AREA CENTRALE con i tre starter ---
        JPanel pannelloStarter = new JPanel(new GridLayout(1, 3, 20, 0));
        pannelloStarter.setOpaque(false);
        pannelloStarter.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        List<PokemonSpecie> starters = controller.getStarterDisponibili();
        for (PokemonSpecie specie : starters)
        {
            pannelloStarter.add(creaPannelloStarter(specie));
        }

        pannelloPrincipale.add(pannelloStarter, BorderLayout.CENTER);

        // --- SUGGERIMENTO IN BASSO ---
        JLabel suggerimento = new JLabel("Clicca su un Pokemon per sceglierlo", SwingConstants.CENTER);
        suggerimento.setForeground(Color.LIGHT_GRAY);
        suggerimento.setFont(new Font("Arial", Font.ITALIC, 12));
        pannelloPrincipale.add(suggerimento, BorderLayout.SOUTH);

        add(pannelloPrincipale);
    }

    /**
     * Crea il pannello grafico per un singolo starter,
     * con immagine, nome, tipo, statistiche e bottone di scelta.
     */
    private JPanel creaPannelloStarter(PokemonSpecie specie)
    {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setBackground(new Color(50, 50, 90));
        pannello.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // --- IMMAGINE ---
        JComponent componenteImmagine = caricaImmagine(specie);
        componenteImmagine.setAlignmentX(Component.CENTER_ALIGNMENT);
        pannello.add(Box.createRigidArea(new Dimension(0, 10)));
        pannello.add(componenteImmagine);

        // --- NOME ---
        JLabel nome = new JLabel(specie.nome(), SwingConstants.CENTER);
        nome.setFont(new Font("Arial", Font.BOLD, 16));
        nome.setForeground(Color.WHITE);
        nome.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- TIPO con colore ---
        JLabel tipo = new JLabel(specie.tipi().get(0).getNome(), SwingConstants.CENTER);
        tipo.setFont(new Font("Arial", Font.PLAIN, 12));
        tipo.setForeground(colorePerTipo(specie.tipi().get(0).getNome()));
        tipo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- STATISTICHE in HTML per andare a capo facilmente ---
        JLabel stats = getJLabel(specie);

        // --- BOTTONE ---
        JButton bottone = getJButton(specie);

        pannello.add(Box.createRigidArea(new Dimension(0, 8)));
        pannello.add(nome);
        pannello.add(Box.createRigidArea(new Dimension(0, 6)));
        pannello.add(tipo);
        pannello.add(Box.createRigidArea(new Dimension(0, 10)));
        pannello.add(stats);
        pannello.add(Box.createRigidArea(new Dimension(0, 10)));
        pannello.add(bottone);
        pannello.add(Box.createRigidArea(new Dimension(0, 10)));

        return pannello;
    }

    private static JLabel getJLabel(PokemonSpecie specie) {
        JLabel stats = new JLabel(
                "<html><center>" +
                        "PV: " + specie.pvBase() + "&nbsp;&nbsp;" +
                        "ATK: " + specie.attaccoBase() + "<br>" +
                        "DEF: " + specie.difesaBase() + "&nbsp;&nbsp;" +
                        "VEL: " + specie.velocitaBase() +
                        "</center></html>",
                SwingConstants.CENTER
        );
        stats.setForeground(Color.LIGHT_GRAY);
        stats.setFont(new Font("Arial", Font.PLAIN, 11));
        stats.setAlignmentX(Component.CENTER_ALIGNMENT);
        return stats;
    }

    private JButton getJButton(PokemonSpecie specie) {
        JButton bottone = new JButton("Scegli!");
        bottone.setFont(new Font("Arial", Font.BOLD, 13));
        bottone.setBackground(new Color(220, 50, 50));
        bottone.setForeground(Color.WHITE);
        bottone.setFocusPainted(false);
        bottone.setOpaque(true);
        bottone.setBorderPainted(false);
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottone.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottone.addActionListener(e -> {
            controller.scegliStarter(specie);
            new SchermataMappa(controller);
            dispose();
        });
        return bottone;
    }

    /**
     * Carica l'immagine del Pokemon da resources.
     */
    private JComponent caricaImmagine(PokemonSpecie specie)
    {
        try {
            java.net.URL url = getClass().getClassLoader()
                    .getResource("images/" + specie.id() + ".png");

            if (url != null) {
                ImageIcon icona = new ImageIcon(url);
                Image ridimensionata = icona.getImage()
                        .getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(ridimensionata));
            }
        } catch (Exception ex) {
            // immagine non trovata
        }

        // Fallback: cerchio colorato con iniziale del nome
        return new JPanel() {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(90, 90));
                setMaximumSize(new Dimension(90, 90));
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(colorePerTipo(specie.tipi().get(0).getNome()));
                g2d.fillOval(5, 5, 80, 80);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                FontMetrics fm = g2d.getFontMetrics();
                String lettera = specie.nome().substring(0, 1);
                int x = (90 - fm.stringWidth(lettera)) / 2;
                int y = (90 + fm.getAscent()) / 2 - 4;
                g2d.drawString(lettera, x, y);
            }
        };
    }

    /**
     * Restituisce il colore associato al tipo per distinguerli visivamente.
     */
    private Color colorePerTipo(String nomeTipo)
    {
        return switch (nomeTipo) {
            case "Fuoco" -> new Color(255, 100, 50);
            case "Acqua" -> new Color(80, 160, 255);
            case "Erba" -> new Color(80, 200, 80);
            default -> Color.WHITE;
        };
    }
}