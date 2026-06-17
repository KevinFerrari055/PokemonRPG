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

    /**
     * Costruttore della Mappa
     * @param larghezza della Mappa
     * @param altezza della Mappa
     * @param zone la lista delle zone presentei nella Mappa
     * @throws IllegalArgumentException se altezza e larghezza sono negativi
     * @throws NullPointerException se la lista delle zone è null
     */
    public Mappa(int larghezza, int altezza, List<Zona> zone)
    {
        if(larghezza < 0 || altezza < 0) throw new IllegalArgumentException("dimensioni non valide");
        if(zone == null) throw new NullPointerException("zone è un array nullo");
        this.larghezza = larghezza;
        this.altezza = altezza;
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

}
