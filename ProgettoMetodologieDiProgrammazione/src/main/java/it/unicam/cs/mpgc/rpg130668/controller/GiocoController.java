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
 * Questa classe rappresenta il mediatore tra la GUI e tutto il resto
 * dell'applicazione. E' l'unico punto di contatto; non parla mai direttamente con Pokemon
 * Battaglia, AllenatoreRepositoryFile o qualsiasi altra classe del modello o della persistence
 * Quindi questo vuol dire che se in futuro voglio cambiamo la GUI, il controller non lo modifico, cambierò solo le schermate
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
    public GiocoController(AllenatoreRepository allenatoreRepository, PokemonRepository pokemonRepository)
    {
        if(allenatoreRepository == null || pokemonRepository == null) throw new NullPointerException("Uno dei parametri è null");
        this.allenatoreRepository = allenatoreRepository;
        this.pokemonRepository = pokemonRepository;
        this.generatoreIncontri = new GeneratoreIncontriCasuale();
        this.mappa = creaMappa();

    }


    //Metodi
    public void creaAllenatore(String id, String username)
    {
        Allenatore nuovo = new Allenatore(id, username, new Posizione(10, 10));
        this.allenatoreCorrente = nuovo;
        allenatoreRepository.salva(nuovo);
    }

    public List<PokemonSpecie> getStarterDisponibili()
    {
        PokemonSpecie charmander = new PokemonSpecie("charmander", "Charmander", List.of(TipoElementale.FUOCO), 39, 52, 43, 65, List.of());
        PokemonSpecie squirtle = new PokemonSpecie("squirtle", "Squirtle", List.of(TipoElementale.ACQUA), 44, 48, 65, 43, List.of());
        PokemonSpecie bulbasaur = new PokemonSpecie("bulbasaur", "Bulbasaur", List.of(TipoElementale.ERBA), 45, 49, 49, 45, List.of());
        return List.of(charmander, squirtle, bulbasaur);
    }

    public void scegliStarter(PokemonSpecie specie)
    {
        Pokemon starter = new Pokemon(UUID.randomUUID().toString(), specie, 5);

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

        allenatoreCorrente.getSquadra().aggiungiPokemon(starter);
        pokemonRepository.salva(starter);
    }

    public void catturaPokemon(Pokemon avversario)
    {
        this.allenatoreCorrente.getSquadra().aggiungiPokemon(avversario);
        pokemonRepository.salva(avversario);
        this.battagliaInCorso = null;
    }

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

    //Metodi getter
    public Mappa getMappa() { return mappa; }
    public Posizione getPosizione() { return allenatoreCorrente.getPosizione(); }
    public boolean isBattagliaInCorso() { return battagliaInCorso != null; }
    public Battaglia getBattagliaInCorso() { return battagliaInCorso; }
    public Allenatore getAllenatoreCorrente() { return allenatoreCorrente; }


}
