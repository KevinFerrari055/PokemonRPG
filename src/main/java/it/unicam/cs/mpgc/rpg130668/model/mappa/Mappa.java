package it.unicam.cs.mpgc.rpg130668.model.mappa;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta a tutti gli effetti la Mappa del gioco.
 * Non ha metodi in particolare in quanto le zone sono gestite a sè per far sì che Mappa
 * non abbia la dipendenza di gestirle lei, così come anche gli incontri tra il pokemon principale
 * e quelli presenti nella mappa che sono gestiti a parte.
 * Non contiene un riferimento all'Allenatore che la eslpora perchè sarà il GiocoController a coordinare chi esplora cosa.
 */
public class Mappa
{
    //Attributi
    private int larghezza;
    private int altezza;
    private List<Zona> zone;
    private final TipoCella[][] griglia;

    /**
     * Costruttore della Mappa
     * @param larghezza della Mappa
     * @param altezza della Mappa
     * @param zone la lista delle zone presentei nella Mappa
     * @param griglia la griglia delle celle
     * @throws IllegalArgumentException se altezza e larghezza sono negativi
     * @throws NullPointerException se la lista delle zone è null
     * @throws NullPointerException se la griglia è null.
     */
    public Mappa(int larghezza, int altezza, TipoCella[][] griglia, List<Zona> zone)
    {
        if(larghezza < 0 || altezza < 0) throw new IllegalArgumentException("dimensioni non valide");
        if(zone == null) throw new NullPointerException("zone è un array nullo");
        if(griglia == null) throw new NullPointerException("griglia nulla");
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.griglia = griglia;
        this.zone = new ArrayList<>(zone);
    }

    //Metodi Getter
    public int getLarghezza()
    {
        return larghezza;
    }

    public int getAltezza()
    {
        return altezza;
    }

    public List<Zona> getZone()
    {
        return List.copyOf(zone);
    }

    /**
     * Metodo che permette di aggiungere una zona alla Mappa
     * @param zona da aggiungere
     * @return false se già presente, true altrimenti
     * @throws NullPointerException se zona è null
     */
    public boolean aggiungiZona(Zona zona)
    {
        if(zona == null) throw new NullPointerException("La zona passata è null");
        if(zone.contains(zona)) return false;
        zone.add(zona);
        return true;
    }

    /**
     * @return il tipo della cella nella posizione (x, y)
     */
    public TipoCella getCella(int x, int y)
    {
        return griglia[x][y];
    }

    /**
     * @return true se la posizione e' dentro i bordi della mappa
     * e la cella non e' un muro
     */
    public boolean isPercorribile(int x, int y)
    {
        if(x < 0 || x >= larghezza || y < 0 || y >= altezza) return false;
        return griglia[x][y] != TipoCella.MURO;
    }

}
