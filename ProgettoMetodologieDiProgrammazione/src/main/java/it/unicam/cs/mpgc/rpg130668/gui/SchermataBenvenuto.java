package it.unicam.cs.mpgc.rpg130668.gui;

import it.unicam.cs.mpgc.rpg130668.controller.GiocoController;

import javax.swing.*;
import java.awt.*;

/**
 * Prima schermata del gioco: chiede al giocatore di inserire il suo username.
 * Estende JFrame, che è la finestra principale di swing.
 */
public class SchermataBenvenuto extends JFrame
{
    //Attributi
    private final GiocoController controller;

    public SchermataBenvenuto(GiocoController controller)
    {
        if(controller == null) throw new NullPointerException("il controller passato è nulll");
        this.controller = controller;

        //Titolo della finestra
        setTitle("Pokemon RPG - Benvenuto");

        //Dimensioni della finestra in pixel
        setSize(500, 400);

        //Quando si chiude la finestra, termina l'applicazione
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Centra la finestra sullo schermo
        setLocationRelativeTo(null);

        //Non permette di ridimensionare la finestra
        setResizable(false);

        //Costruisce i componenti grafici
        costruisciGui();

        //Rende la finestra visibile
        setVisible(true);
    }

    private void costruisciGui()
    {
        //Creo un contenitore JPanel di componenti, di preciso ho creato il BorderLayout per dividere
        //il pannello in 5 aree: NORD, SUD, EST, OVEST, CENTER
        JPanel pannelloPrincipale = new JPanel(new BorderLayout());

        // sfondo blu scuro
        pannelloPrincipale.setBackground(new Color(30, 30, 60));

        //margini
        pannelloPrincipale.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        //TITOLO (area Nord)
        JLabel titolo = new JLabel("Benvenuto nel mondo dei Pokemon!", SwingConstants.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 20));
        titolo.setForeground(Color.YELLOW);
        pannelloPrincipale.add(titolo, BorderLayout.NORTH);

        //Area centrale con il campo username
        JPanel pannelloCentro = new JPanel();
        pannelloCentro.setLayout(new BoxLayout(pannelloCentro, BoxLayout.Y_AXIS)); //Dispongo i componenti in verticale
        pannelloCentro.setOpaque(false); //transparente, mostra lo sfondo del pannello
        JLabel istruzione = new JLabel("Inserisci il tuo nome da allenatore:");
        istruzione.setForeground(Color.WHITE);
        istruzione.setFont(new Font("Arial", Font.PLAIN, 14));
        istruzione.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Campo di testo dove il giocatore digita il suo username
        JTextField campoUsername = new JTextField();
        campoUsername.setMaximumSize(new Dimension(250, 35));
        campoUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        campoUsername.setHorizontalAlignment(JTextField.CENTER);

        //Spaziatura verticale tra i componenti
        pannelloCentro.add(Box.createVerticalGlue());
        pannelloCentro.add(istruzione);
        pannelloCentro.add(Box.createRigidArea(new Dimension(0, 10)));
        pannelloCentro.add(campoUsername);
        pannelloCentro.add(Box.createVerticalGlue());
        pannelloPrincipale.add(pannelloCentro, BorderLayout.CENTER);

        //Bottone (area SUD)
        JButton bottoneInizio = getJButton(campoUsername);

        //Se premo nel campo testo, ottengo lo stesso effetto del click sul bottone
        campoUsername.addActionListener(e -> bottoneInizio.doClick());

        JPanel pannelloBottone = new JPanel();
        pannelloBottone.setOpaque(false);
        pannelloBottone.add(bottoneInizio);
        pannelloPrincipale.add(pannelloBottone, BorderLayout.SOUTH);

        //Aggiungo il pannello principale alla finestra
        add(pannelloPrincipale);




    }

    private JButton getJButton(JTextField campoUsername) {
        JButton bottoneInizio = new JButton("Inizia l'avventura!");
        bottoneInizio.setFont(new Font("Arial", Font.BOLD, 14));
        bottoneInizio.setBackground(new Color(220, 50, 50));
        bottoneInizio.setForeground(Color.WHITE);
        bottoneInizio.setFocusPainted(false);
        bottoneInizio.setOpaque(true);
        bottoneInizio.setBorderPainted(false);
        bottoneInizio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottoneInizio.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Evento ActionListener: codice che viene eseguito quando si clicca il bottone
        bottoneInizio.addActionListener(e -> {
            String username = campoUsername.getText().trim();

            //Validazione minima: almeno 3 caratteri
            if(username.length() < 3)
            {
                //Mostro errore
                JOptionPane.showMessageDialog(this, "Il nome deve avere almeno 3 caratteri!",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Chiamo il controller per creare l'allenatore
            //uso username sia come id che come username
            controller.creaAllenatore(username, username);

            //Apro la schermata successiva e chiudo questa
            new SchermataSceltaStarter(controller);
            dispose();
        });
        return bottoneInizio;
    }
}
