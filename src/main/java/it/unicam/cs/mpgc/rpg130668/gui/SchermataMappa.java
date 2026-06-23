package it.unicam.cs.mpgc.rpg130668.gui;

import it.unicam.cs.mpgc.rpg130668.controller.GiocoController;
import it.unicam.cs.mpgc.rpg130668.model.allenatore.Posizione;
import it.unicam.cs.mpgc.rpg130668.model.mappa.Mappa;
import it.unicam.cs.mpgc.rpg130668.model.mappa.TipoCella;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Schermata di esplorazione della mappa: disegna la griglia 20x20 e permette
 * all'allenatore di muoversi con le frecce direzionali.
 * Dopo ogni movimento controlla se e' scattato un incontro, e in quel caso
 * apre la SchermataBattaglia.
 */
public class SchermataMappa extends JFrame
{
    private final GiocoController controller;
    private static final int DIMENSIONE_CELLA = 32;
    private PannelloMappa pannelloMappa;

    /**
     * Costruttore della schermata che genera la mappa del gioco.
     * Calcola le dimensioni della finestra in base alla griglia (20x20 celle
     * da 32 pixel ciascuna) e forza un ridisegno dopo 100ms per garantire
     * che l'allenatore sia visibile subito, senza aspettare il primo tasto.
     *
     * @param controller il controller principale del gioco
     * @throws NullPointerException se il controller passato e' null
     */
    public SchermataMappa(GiocoController controller)
    {
        if(controller == null) throw new NullPointerException("Il controller passato è null");
        this.controller = controller;

        setTitle("Pokemon RPG - Mappa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        costruisciGui();

        //Mi ottengo la mappa dal controller
        Mappa mappa = controller.getMappa();
        int larghezza = mappa.getLarghezza() * DIMENSIONE_CELLA;
        int altezza = mappa.getAltezza() * DIMENSIONE_CELLA + 60;
        setSize(larghezza, altezza);

        setLocationRelativeTo(null);
        setVisible(true);

        // Timer da 100ms: forza il ridisegno del pannello dopo che Swing
        // ha completato il rendering della finestra. Senza questo, l'allenatore
        // risulta invisibile al primo avvio e al ritorno da una battaglia,
        // perche' paintComponent non viene chiamato automaticamente.
        javax.swing.Timer timer = new javax.swing.Timer(100, e -> pannelloMappa.repaint());
        timer.setRepeats(false);
        timer.start();
    }

    private void costruisciGui()
    {
        setLayout(new BorderLayout());

        pannelloMappa = new PannelloMappa();
        add(pannelloMappa, BorderLayout.CENTER);

        // --- BARRA INFO ---
        JPanel barraInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraInfo.setBackground(new Color(30, 30, 60));
        JLabel labelInfo = new JLabel(
                "  Allenatore: " + controller.getAllenatoreCorrente().getUsername() +
                        "  |  Usa le frecce per muoverti"
        );
        labelInfo.setForeground(Color.WHITE);
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 13));
        barraInfo.add(labelInfo);
        add(barraInfo, BorderLayout.SOUTH);

        // InputMap: associa ogni tasto fisico a un nome stringa.
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // ActionMap: associa ogni nome stringa a un'Action concreta da eseguire.
        ActionMap actionMap = getRootPane().getActionMap();

        // Registro i quattro tasti freccia, ciascuno associato a un nome descrittivo
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "su");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "giu");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "sinistra");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "destra");

        // Per ogni nome, definisco l'Action: sposta l'allenatore, ridisegna
        // il pannello e controlla se e' scattato un incontro
        actionMap.put("su", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.muovi(0, -1);
                pannelloMappa.repaint();
                controllaBattaglia();
            }
        });
        actionMap.put("giu", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.muovi(0, 1);
                pannelloMappa.repaint();
                controllaBattaglia();
            }
        });
        actionMap.put("sinistra", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.muovi(-1, 0);
                pannelloMappa.repaint();
                controllaBattaglia();
            }
        });
        actionMap.put("destra", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.muovi(1, 0);
                pannelloMappa.repaint();
                controllaBattaglia();
            }
        });
    }

    /**
     * Classe interna che disegna la griglia della mappa e la posizione dell'allenatore.
     * E' interna a SchermataMappa perche' accede direttamente ai suoi campi
     * (controller e DIMENSIONE_CELLA) senza bisogno di passarli come parametri,
     * e non ha significato al di fuori di questa schermata.
     * Sovrascrive paintComponent per disegnare la griglia e l'allenatore
     * a mano con le API Graphics2D invece di usare componenti Swing standard.
     */
    private class PannelloMappa extends JPanel
    {
        // Immagine dell'allenatore caricata una sola volta nel costruttore
        // e riusata ad ogni chiamata di paintComponent per efficienza
        private final Image immagineAllenatore;

        public PannelloMappa()
        {
            Mappa mappa = controller.getMappa();
            setPreferredSize(new Dimension(
                    mappa.getLarghezza() * DIMENSIONE_CELLA,
                    mappa.getAltezza() * DIMENSIONE_CELLA
            ));

            // Carico l'immagine dell'allenatore dal classpath una sola volta.
            // Se non viene trovata, immagineAllenatore resta null e drawImage
            // non disegna nulla (comportamento sicuro, non lancia eccezioni).
            Image img = null;
            try {
                java.net.URL url = getClass().getClassLoader()
                        .getResource("images/allenatore.png");
                if (url != null) {
                    img = new ImageIcon(url).getImage()
                            .getScaledInstance(
                                    DIMENSIONE_CELLA - 4,
                                    DIMENSIONE_CELLA - 4,
                                    Image.SCALE_SMOOTH
                            );
                }
            } catch (Exception ex) {
            }
            this.immagineAllenatore = img;
        }

        /**
         * Ridisegna l'intera griglia e la posizione dell'allenatore.
         * Viene chiamato automaticamente da Swing quando il pannello deve
         * essere aggiornato (es. dopo repaint(), al ridimensionamento, ecc.).
         *
         * @param g il contesto grafico fornito da Swing
         */
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Mappa mappa = controller.getMappa();
            Posizione posAllenatore = controller.getPosizione();

            // Disegno griglia
            for (int x = 0; x < mappa.getLarghezza(); x++)
            {
                for (int y = 0; y < mappa.getAltezza(); y++)
                {
                    TipoCella cella = mappa.getCella(x, y);

                    Color coloreCella = switch (cella) {
                        case ERBA  -> new Color(80, 160, 80);
                        case ACQUA -> new Color(60, 120, 200);
                        case MURO  -> new Color(80, 80, 80);
                    };

                    g2d.setColor(coloreCella);
                    g2d.fillRect(x * DIMENSIONE_CELLA, y * DIMENSIONE_CELLA,
                            DIMENSIONE_CELLA, DIMENSIONE_CELLA);

                    g2d.setColor(new Color(0, 0, 0, 40));
                    g2d.drawRect(x * DIMENSIONE_CELLA, y * DIMENSIONE_CELLA,
                            DIMENSIONE_CELLA, DIMENSIONE_CELLA);
                }
            }

            // Disegno allenatore
            int px = posAllenatore.x() * DIMENSIONE_CELLA;
            int py = posAllenatore.y() * DIMENSIONE_CELLA;

            g2d.drawImage(immagineAllenatore, px + 2, py + 2, null);
        }
    }

    /**
     * Controlla se dopo un movimento e' scattato un incontro con un Pokemon selvatico.
     * Se si', apre la SchermataBattaglia e chiude questa schermata.
     */
    private void controllaBattaglia()
    {
        if (controller.isBattagliaInCorso()) {
            new SchermataBattaglia(controller);
            dispose();
        }
    }
}