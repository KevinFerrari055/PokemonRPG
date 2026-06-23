package it.unicam.cs.mpgc.rpg130668.persistence;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID>
{
    void salva(T elemento);
    Optional<T> trovaPerId(ID id);
    List<T> trovaTutti();
    void elimina(ID id);
}
