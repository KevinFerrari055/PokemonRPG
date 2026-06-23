package it.unicam.cs.mpgc.rpg130668.controller;

import it.unicam.cs.mpgc.rpg130668.model.allenatore.Allenatore;
import it.unicam.cs.mpgc.rpg130668.model.allenatore.Posizione;
import it.unicam.cs.mpgc.rpg130668.model.combattimento.Battaglia;
import it.unicam.cs.mpgc.rpg130668.model.combattimento.CalcolatoreDanno;
import it.unicam.cs.mpgc.rpg130668.model.combattimento.CalcolatoreDannoStandard;
import it.unicam.cs.mpgc.rpg130668.model.combattimento.TabellaEfficacia;
import it.unicam.cs.mpgc.rpg130668.model.mappa.*;
import it.unicam.cs.mpgc.rpg130668.model.pokemon.*;
import it.unicam.cs.mpgc.rpg130668.persistence.AllenatoreRepository;
import it.unicam.cs.mpgc.rpg130668.persistence.PokemonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Mediatore tra la GUI e il resto dell'applicazione (model e persistence).
 * E' l'unico punto di contatto: la GUI non accede mai direttamente alle classi
 * del model o della persistence, ma passa sempre da qui.
 * Questo garantisce che, se in futuro si vuole sostituire la GUI (es. passare
 * da Swing a JavaFX o a una versione web), il controller resta invariato.
 */
public class GiocoController
{
    //Attributi
    private final AllenatoreRepository allenatoreRepository;
    private final PokemonRepository pokemonRepository;
    private Allenatore allenatoreCorrente;
    private Mappa mappa;
    private Battaglia battagliaInCorso; // null se non c'è battaglia attiva
    private GeneratoreIncontri generatoreIncontri;

    //Costruttore
    /**
     * @param allenatoreRepository il repository per la persistenza degli allenatori
     * @param pokemonRepository il repository per la persistenza dei Pokemon
     * @throws NullPointerException se uno dei repository e' null
     */
    public GiocoController(AllenatoreRepository allenatoreRepository, PokemonRepository pokemonRepository)
    {
        if(allenatoreRepository == null || pokemonRepository == null) throw new NullPointerException("Uno dei parametri è null");
        this.allenatoreRepository = allenatoreRepository;
        this.pokemonRepository = pokemonRepository;
        this.generatoreIncontri = new GeneratoreIncontriCasuale();
        this.mappa = creaMappa();

    }


    //Metodi
    /**
     * Crea un nuovo allenatore con l'username fornito, lo imposta come
     * allenatore corrente e lo salva nel repository.
     *
     * @param id identificativo univoco dell'allenatore
     * @param username il nome scelto dal giocatore
     * @throws NullPointerException se uno dei parametri è null
     * @throws IllegalArgumentException se id è vuoto o username è corto
     */
    public void creaAllenatore(String id, String username)
    {
        if(id == null || username == null) throw new NullPointerException("uno dei parametri è null");
        if(id.isEmpty() || username.length() < 3) throw new IllegalArgumentException("le stringhe passate non sono valide");
        Allenatore nuovo = new Allenatore(id, username, new Posizione(5, 5));
        this.allenatoreCorrente = nuovo;
        allenatoreRepository.salva(nuovo);
    }

    /**
     * Ritorna la lista degli starte Disponibili, i quali sono charmander, squirtle e bulbasaur.
     * Se vorrò aggiungerne qualcuno, mi basterà aggiungerlo alla lista dopo averlo creato
     * @return Lista di PokemonSpecie attualmente presenti
     */
    public List<PokemonSpecie> getStarterDisponibili()
    {
        PokemonSpecie charmander = new PokemonSpecie("charmander", "Charmander", List.of(TipoElementale.FUOCO), 39, 52, 43, 65, List.of());
        PokemonSpecie squirtle = new PokemonSpecie("squirtle", "Squirtle", List.of(TipoElementale.ACQUA), 44, 48, 65, 43, List.of());
        PokemonSpecie bulbasaur = new PokemonSpecie("bulbasaur", "Bulbasaur", List.of(TipoElementale.ERBA), 45, 49, 49, 45, List.of());
        return List.of(charmander, squirtle, bulbasaur);
    }

    /**
     * Rappresenta la scelta dell'allenatore del pokemon starter, che in base alla scelta ha 2 mosse iniziali.
     * @param specie del pokemon scelto
     * @throws NullPointerException se la specie passata è null
     */
    public void scegliStarter(PokemonSpecie specie)
    {
        if(specie == null) throw new NullPointerException("La specie passata è null");

        Pokemon starter = new Pokemon(UUID.randomUUID().toString(), specie, 5);

        //Controllo scelta dello starter
        if(specie.id().equals("charmander")) {
            starter.impara(new Mossa("Graffio", TipoElementale.NORMALE, 40, 100, CategoriaMossa.FISICA));
            starter.impara(new Mossa("Fiammata", TipoElementale.FUOCO, 90, 100, CategoriaMossa.SPECIALE));
        } else if(specie.id().equals("squirtle")) {
            starter.impara(new Mossa("Tackle", TipoElementale.NORMALE, 40, 100, CategoriaMossa.FISICA));
            starter.impara(new Mossa("Pistolacqua", TipoElementale.ACQUA, 40, 100, CategoriaMossa.SPECIALE));
        } else if(specie.id().equals("bulbasaur")) {
            starter.impara(new Mossa("Tackle", TipoElementale.NORMALE, 40, 100, CategoriaMossa.FISICA));
            starter.impara(new Mossa("FogliaLama", TipoElementale.ERBA, 55, 95, CategoriaMossa.SPECIALE));
        }

        //lo aggiungo alla sua squadra iniziale
        allenatoreCorrente.getSquadra().aggiungiPokemon(starter);

        //Lo salvo nel file
        pokemonRepository.salva(starter);
    }

    /**
     * Una volta terminata la battaglia, l'allenatore può catturare il pokemon e aggiungerlo alla squadra
     * Anche questo verrà salvato sul file.
     * @param avversario che rappresenta il pokemon avversario affrontato
     * @throws NullPointerException se il pokemon avversario è null
     */
    public void catturaPokemon(Pokemon avversario)
    {
        if(avversario == null) throw new NullPointerException("Il pokemon avversario è null");
        // Ripristina metà dei PV base, cosi' il Pokemon catturato e' utilizzabile
        avversario.ripristinaHP(avversario.getSpecie().pvBase() / 2);
        this.allenatoreCorrente.getSquadra().aggiungiPokemon(avversario);
        pokemonRepository.salva(avversario);
        this.battagliaInCorso = null;
    }

    /**
     * Rappresenta la dinamica di gioco più importante, il movimento dell'allenatore.
     * Si sposterà sulla mappa in base a delle coordinate dx e dy.
     * Prima del movimento controllo se va incontro a un muro e in quel caso non permetto il movimento.
     * Una volta spostato, capisco in quale zona si trova e se è possibile generare un incontro.
     * In quel caso, viene avviata la battaglia seguendo sempre le dinamiche dell'efficacia e del calcolatore.
     * @param dx movimento verso destra o sinistra dell'allenatore sulla mappa
     * @param dy movimento verso l'alto o il basso dell'allenatore sulla mappa
     */
    public void muovi(int dx, int dy)
    {
        int nuovaX = allenatoreCorrente.getPosizione().x() + dx;
        int nuovaY = allenatoreCorrente.getPosizione().y() + dy;

        // fuori dai bordi della mappa
        if(nuovaX < 0 || nuovaX >= mappa.getLarghezza() ||
                nuovaY < 0 || nuovaY >= mappa.getAltezza()) return;

        // cella non percorribile
        if(mappa.getCella(nuovaX, nuovaY) == TipoCella.MURO) return;

        allenatoreCorrente.setPosizione(new Posizione(nuovaX, nuovaY));

        Zona zonaCorrente = getZonaCorrente();
        if(zonaCorrente != null)
        {
            Optional<Pokemon> incontro = generatoreIncontri.generaIncontro(zonaCorrente);
            if(incontro.isPresent())
            {
                CalcolatoreDanno calcolatore = new CalcolatoreDannoStandard(new TabellaEfficacia());
                Pokemon pokemonAttivo = allenatoreCorrente.getSquadra().getPokemonAttivo().orElse(null);
                if(pokemonAttivo != null)
                {
                    battagliaInCorso = new Battaglia(pokemonAttivo, incontro.get(), calcolatore);
                }
            }
        }
    }

    /**
     * Metodo creazione della Mappa molto basilare con tutta erba come default, con il muro intorno e una zona d'acqua al centro.
     * @return la mappa creata con anche le zone con le specie disponibili
     */
    private Mappa creaMappa()
    {
        int larghezza = 20;
        int altezza = 20;
        TipoCella[][] griglia = new TipoCella[larghezza][altezza];

        // riempio tutto con ERBA di default
        for (int x = 0; x < larghezza; x++)
            for (int y = 0; y < altezza; y++)
                griglia[x][y] = TipoCella.ERBA;

        // aggiungo un bordo di muri tutto intorno
        for (int x = 0; x < larghezza; x++) {
            griglia[x][0] = TipoCella.MURO;
            griglia[x][altezza - 1] = TipoCella.MURO;
        }
        for (int y = 0; y < altezza; y++) {
            griglia[0][y] = TipoCella.MURO;
            griglia[larghezza - 1][y] = TipoCella.MURO;
        }

        // aggiungo una zona d'acqua al centro
        for (int x = 8; x <= 12; x++)
            for (int y = 8; y <= 12; y++)
                griglia[x][y] = TipoCella.ACQUA;

        // creo le zone con le specie disponibili
        PokemonSpecie charmander = new PokemonSpecie("charmander", "Charmander",
                List.of(TipoElementale.FUOCO), 39, 52, 43, 65, List.of());
        PokemonSpecie bulbasaur = new PokemonSpecie("bulbasaur", "Bulbasaur",
                List.of(TipoElementale.ERBA), 45, 49, 49, 45, List.of());
        PokemonSpecie squirtle = new PokemonSpecie("squirtle", "Squirtle",
                List.of(TipoElementale.ACQUA), 44, 48, 65, 43, List.of());

        Zona zonaErba = new Zona("ERBA", 0.3, List.of(charmander, bulbasaur));
        Zona zonaAcqua = new Zona("ACQUA", 0.4, List.of(squirtle));

        return new Mappa(larghezza, altezza, griglia, List.of(zonaErba, zonaAcqua));
    }


    /**
     * Ritorna la zona corrente. Ho creato questo metodo per facilitare la dinamica delle battaglie sui movimenti
     * dell'allenatore
     * @return la zona in cui si trova l'allenatore grazie all'uso della x e della y
     */
    private Zona getZonaCorrente()
    {
        TipoCella cella = mappa.getCella(
                allenatoreCorrente.getPosizione().x(),
                allenatoreCorrente.getPosizione().y()
        );
        return mappa.getZone().stream()
                .filter(z -> z.nome().equals(cella.name()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Fa terminare la battaglia
     */
    public void terminaBattaglia()
    {
        this.battagliaInCorso = null;
    }


    //Metodi getter
    public Mappa getMappa() { return mappa; }
    public Posizione getPosizione() { return allenatoreCorrente.getPosizione(); }
    public boolean isBattagliaInCorso() { return battagliaInCorso != null; }
    public Battaglia getBattagliaInCorso() { return battagliaInCorso; }
    public Allenatore getAllenatoreCorrente() { return allenatoreCorrente; }
    public Pokemon getPokemonAvversario()
    {
        if (battagliaInCorso == null) return null;
        return battagliaInCorso.getPokemonAvversario();
    }




}
