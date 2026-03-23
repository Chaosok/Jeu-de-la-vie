# Jeu de la Vie — Implémentation Java

Automate cellulaire implémenté avec **5 Design Patterns**.

---

## Structure du projet

```
JeuDeLaVie/
└── src/
    ├── CelluleEtat.java          ← interface (DP État)
    ├── CelluleEtatVivant.java    ← Singleton + État vivant
    ├── CelluleEtatMort.java      ← Singleton + État mort
    ├── Cellule.java              ← entité principale
    ├── Commande.java             ← interface (DP Commande)
    ├── CommandeVit.java          ← commande naissance
    ├── CommandeMeurt.java        ← commande mort
    ├── Visiteur.java             ← interface (DP Visiteur)
    ├── VisiteurClassique.java    ← règles Conway
    ├── VisiteurHighLife.java     ← règles HighLife
    ├── VisiteurDayAndNight.java  ← règles Day & Night
    ├── Observateur.java          ← interface (DP Observateur)
    ├── ObservateurTexte.java     ← observateur console
    ├── JeuDeLaVie.java           ← modèle principal + main()
    └── JeuDeLaVieUI.java         ← interface graphique Swing
```

---

## Compilation et lancement

```bash
# Depuis le dossier JeuDeLaVie/
mkdir bin
javac -d bin src/*.java
java -cp bin JeuDeLaVie
```

Avec Java 11+ (module-path) :
```bash
javac --release 17 -d bin src/*.java
java -cp bin JeuDeLaVie
```

---

## Design Patterns utilisés

| Pattern      | Classes concernées | Rôle |
|---|---|---|
| **État**       | `CelluleEtat`, `CelluleEtatVivant`, `CelluleEtatMort` | Délégation du comportement à l'état courant |
| **Singleton**  | `CelluleEtatVivant`, `CelluleEtatMort` | Une seule instance de chaque état |
| **Observateur**| `Observateur`, `JeuDeLaVie`, `JeuDeLaVieUI`, `ObservateurTexte` | Notification automatique des vues |
| **Commande**   | `Commande`, `CommandeVit`, `CommandeMeurt` | File d'attente d'actions, pas de tableau temporaire |
| **Visiteur**   | `Visiteur`, `VisiteurClassique`, `VisiteurHighLife`, `VisiteurDayAndNight` | Règles découplées de la structure |

---

## Interface graphique

| Contrôle | Action |
|---|---|
| ▶ Démarrer / ⏸ Arrêter | Lance ou stoppe la boucle de simulation |
| ⏭ Pas à pas | Avance d'une seule génération |
| ↺ Réinitialiser | Regénère une grille aléatoire |
| Slider vitesse | Délai entre générations : 50 ms (rapide) → 1000 ms (lent) |
| Combo Règles | Bascule entre Conway, HighLife, Day & Night |
| Clic sur la grille | Toggle manuel d'une cellule |

---

## Règles du jeu

### Classique (Conway)
- Cellule vivante avec **< 2** ou **> 3** voisines → **meurt**
- Cellule vivante avec **2 ou 3** voisines → **survit**
- Cellule morte avec exactement **3** voisines → **naît**

### HighLife
- Survie : 2 ou 3 voisines
- Naissance : 3 ou **6** voisines

### Day & Night
- Survie : 3, 4, 6, 7 ou 8 voisines
- Naissance : 3, 6, 7 ou 8 voisines
