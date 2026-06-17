package it.unicam.cs.mpgc.rpg130668.model.pokemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una singola istanza di Pokemon posseduta da un allenatore
 * (o incontrata in natura). A differenza di {@link PokemonSpecie}, che descrive
 * il "template" condiviso da tutti i Pokemon della stessa specie (statistiche base,
 * tipi, mosse apprendibili), questa classe contiene lo stato individuale che
 * cambia nel tempo: punti vita attuali, livello, esperienza e mosse effettivamente
 * apprese da questo specifico esemplare.
 */
public class Pokemon
{

    // Attributi
    private String id;
    private String nickName;
    private PokemonSpecie specie;
    private int livello;
    private int esperienza;
    private int pvAttuali;
    private List<Mossa> mosseApprese;

    /**
     * Crea un nuovo Pokemon a partire dalla sua specie e dal livello iniziale.
     * I punti vita attuali vengono impostati al valore base della specie,
     * l'esperienza parte da zero e la lista delle mosse apprese e' inizialmente vuota.
     *
     * @param id identificativo univoco del Pokemon (non null, non vuoto)
     * @param specie il "template" di specie a cui questo Pokemon appartiene (non null)
     * @param livello il livello iniziale del Pokemon (non negativo)
     * @throws IllegalArgumentException se uno dei parametri non e' valido
     * @throws NullPointerException se id o specie sono null
     */
    public Pokemon(String id, PokemonSpecie specie, int livello)
    {
        if(id == null || specie == null)
        {
            throw new NullPointerException("Id o specie sono null");
        }
        if(id.isEmpty() || livello < 0)
        {
            throw new IllegalArgumentException("Parametri non validi");
        }
        this.id = id;
        this.specie = specie;
        this.livello = livello;
        this.esperienza = 0;
        this.pvAttuali = specie.pvBase();
        this.mosseApprese = new ArrayList<>();
    }

    // Metodi getter and Setter
    public String getId()
    {
        return id;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        if(nickName == null) throw new NullPointerException("Il nuovo nickName da impostare è null");
        if(nickName.length() < 3) throw new IllegalArgumentException("Il nickname è troppo corto");
        this.nickName = nickName;
    }

    public PokemonSpecie getSpecie() {
        return specie;
    }

    public int getLivello() {
        return livello;
    }

    public int getPvAttuali() {
        return pvAttuali;
    }

    public int getEsperienza() {
        return esperienza;
    }

    /**
     * @return una copia immutabile delle mosse apprese, per non esporre
     * la lista interna a modifiche dall'esterno della classe.
     */
    public List<Mossa> getMosseApprese() {
        return List.copyOf(mosseApprese);
    }

    // Metodi della classe

    /**
     * @return true se il Pokemon ha esaurito i punti vita ed e' quindi fuori combattimento.
     */
    public boolean isFuoriCombattimento()
    {
        return pvAttuali <= 0;
    }

    /**
     * Riduce i punti vita attuali del danno subito, senza farli scendere sotto zero.
     *
     * @param danno la quantita' di danno da sottrarre ai punti vita attuali
     */
    public void subisciDanno(int danno)
    {
        pvAttuali = Math.max(0, pvAttuali - danno);
    }

    /**
     *
     * @param mossa la mossa che vuole imparare il pokemon
     * @return false se già presente, se mossa è null o superato il massimo, true altrimenti
     */
    public boolean impara(Mossa mossa)
    {
        if(mossa == null) return false;
        if(this.mosseApprese.size() >= 4) return false;
        if(mosseApprese.contains(mossa)) return false;
        this.mosseApprese.add(mossa);
        return true;
    }

    /**
     * @return il nickname se e' stato assegnato, altrimenti il nome della specie.
     */
    public String getNomeVisualizzato()
    {
        return nickName != null ? nickName : specie.nome();
    }
}