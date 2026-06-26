# Pokemon RPG

Progetto per il corso di **Metodologie di Programmazione** (AA 2025/26) - Università di Camerino.

Applicativo Java con interfaccia grafica ispirato al celebre gioco Pokemon: 
il giocatore crea il proprio allenatore, sceglie uno starter tra Charmander, 
Squirtle e Bulbasaur, esplora una mappa a griglia, affronta Pokemon selvatici 
in battaglie 1 vs 1 a turni e può catturare i Pokemon sconfitti.

## Descrizione

Il gioco si sviluppa in quattro fasi principali:

- **Creazione allenatore**: il giocatore inserisce il proprio username
- **Scelta dello starter**: si sceglie tra i tre Pokemon iniziali, ognuno con 
  statistiche e mosse diverse
- **Esplorazione della mappa**: griglia 20x20 con zone di erba e acqua, ciascuna 
  con Pokemon selvatici diversi. Il giocatore si muove con le frecce direzionali
- **Battaglia**: incontri casuali con Pokemon selvatici, sistema a turni basato 
  su velocità, efficacia dei tipi e calcolo del danno. A fine battaglia si può 
  scegliere se catturare il Pokemon sconfitto

## Come eseguire il progetto

### Prerequisiti

- JDK 21 o superiore
- Gradle

### Istruzioni

```bash
git clone https://github.com/KevinFerrari055/pokemonRPG.git
```

### Build

```bash
./gradlew build        # Linux/Mac
.\gradlew build        # Windows PowerShell
```

### Esecuzione

```bash
./gradlew run          # Linux/Mac
.\gradlew run          # Windows PowerShell
```

## Struttura del progetto

Il codice è organizzato nel package `it.unicam.cs.mpgc.rpg130668` seguendo 
il pattern **MVC**:

| Package | Responsabilità |
|---|---|
| `model` | Logica e regole di gioco, senza dipendenze da GUI o persistenza |
| `persistence` | Interfacce repository e implementazioni JSON con Gson |
| `controller` | Unico mediatore tra GUI e model/persistence |
| `gui` | Interfaccia grafica Swing |
| `app` | Classe Main, composition root dell'applicazione |

I dati vengono salvati nella cartella `data/` nella root del progetto:
- `data/allenatori.json`
- `data/pokemon.json`

## Comandi

| Tasto | Azione |
|---|---|
| ↑ ↓ ← → | Movimento sulla mappa |
| Mouse SX | Scegliere la mossa del Pokemon da usare nella battaglia |

