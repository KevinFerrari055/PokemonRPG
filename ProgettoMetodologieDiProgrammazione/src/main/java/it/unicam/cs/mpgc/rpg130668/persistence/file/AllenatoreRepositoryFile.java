package it.unicam.cs.mpgc.rpg130668.persistence.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg130668.model.allenatore.Allenatore;
import it.unicam.cs.mpgc.rpg130668.persistence.AllenatoreRepository;
import it.unicam.cs.mpgc.rpg130668.persistence.json.GsonProvider;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementazione di AllenatoreRepository basata su un singolo file JSON,
 * che contiene la lista di tutti gli allenatori salvati.
 */
public class AllenatoreRepositoryFile implements AllenatoreRepository
{
    private final Gson gson;
    private final String percorsoFile;
    private final Type tipoLista;

    public AllenatoreRepositoryFile(String percorsoFile)
    {
        this.gson = GsonProvider.creaGson();
        this.percorsoFile = percorsoFile;
        this.tipoLista = new TypeToken<List<Allenatore>>(){}.getType();
    }

    @Override
    public void salva(Allenatore allenatore)
    {
        // "salva" funziona sia da creazione che da aggiornamento: se esiste
        // gia' un allenatore con lo stesso id lo sostituisco, altrimenti lo aggiungo.
        List<Allenatore> tutti = new ArrayList<>(trovaTutti());
        tutti.removeIf(a -> a.getId().equals(allenatore.getId()));
        tutti.add(allenatore);
        scriviTutti(tutti);
    }

    @Override
    public Optional<Allenatore> trovaPerId(String id)
    {
        return trovaTutti().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Allenatore> trovaTutti()
    {
        File file = new File(percorsoFile);
        if (!file.exists()) {
            // Prima esecuzione, il file non esiste ancora: nessun allenatore salvato.
            return new ArrayList<>();
        }
        try (Reader reader = new BufferedReader(new FileReader(file))) {
            List<Allenatore> letti = gson.fromJson(reader, tipoLista);
            return letti != null ? letti : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore durante la lettura: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void elimina(String id)
    {
        List<Allenatore> tutti = new ArrayList<>(trovaTutti());
        tutti.removeIf(a -> a.getId().equals(id));
        scriviTutti(tutti);
    }

    private void scriviTutti(List<Allenatore> allenatori)
    {
        File file = new File(percorsoFile);
        File cartella = file.getParentFile();
        if (cartella != null) {
            cartella.mkdirs();
        }

        try (Writer writer = new BufferedWriter(new FileWriter(percorsoFile))) {
            gson.toJson(allenatori, tipoLista, writer);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura: " + e.getMessage());
        }
    }
}