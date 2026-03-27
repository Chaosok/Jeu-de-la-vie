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
    ├── Commande.java             ← classe abstraite (DP Commande)
    ├── CommandeVit.java          ← commande naissance
    ├── CommandeMeurt.java        ← commande mort
    ├── Visiteur.java             ← classe abstraite (DP Visiteur)
    ├── VisiteurClassique.java    ← règles Conway
    ├── VisiteurHighLife.java     ← règles HighLife
    ├── VisiteurDayNight.java     ← règles Day & Night
    ├── Observateur.java          ← interface (DP Observateur)
    ├── Observable.java           ← classe abstraite — gère la liste d'observateurs
    ├── ObservateurConsole.java   ← observateur console
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

Avec Java 17+ :
```bash
javac --release 17 -d bin src/*.java
java -cp bin JeuDeLaVie
```

---

## Design Patterns utilisés

| Pattern       | Classes concernées                                                                 | Rôle |
|---|---|---|
| **État**       | `CelluleEtat`, `CelluleEtatVivant`, `CelluleEtatMort`, `Cellule`                  | Délégation du comportement à l'état courant |
| **Singleton**  | `CelluleEtatVivant`, `CelluleEtatMort`                                             | Une seule instance de chaque état |
| **Observateur**| `Observateur`, `Observable`, `JeuDeLaVie`, `JeuDeLaVieUI`, `ObservateurConsole`  | Notification automatique des vues |
| **Commande**   | `Commande`, `CommandeVit`, `CommandeMeurt`, `JeuDeLaVie`, `Cellule`               | File d'attente d'actions — évite de modifier la grille pendant l'analyse |
| **Visiteur**   | `Visiteur`, `VisiteurClassique`, `VisiteurHighLife`, `VisiteurDayNight`            | Règles découplées de la structure |

---

## Interface graphique

| Contrôle | Action |
|---|---|
| ▶ Commencer / ⏸ Pause | Lance ou stoppe la boucle de simulation |
| ⏭ Suivant | Avance d'une seule génération |
| ↺ Réinitialiser | Regénère une grille aléatoire à 30 % de densité |
| Combo Structures | Charge un pattern prédéfini (planeur, clignotant, vaisseau léger, canon à planeurs) |
| 🔍 - / 🔍 + | Zoom arrière / avant |
| ↕ Ajuster | Réinitialise le zoom pour que la grille remplisse la fenêtre |
| Slider Vitesse | Délai entre générations : 50 ms (rapide) → 1000 ms (lent) |
| Slider Densité | Densité lors de la réinitialisation aléatoire (0 % → 100 %) |
| Combo Règles | Bascule entre Conway, HighLife, Day & Night |
| Couleur Cellules Vivantes | Sélecteur de couleur pour les cellules vivantes |
| Couleur Cellules Mortes | Sélecteur de couleur pour le fond |
| Clic / Drag sur la grille | Toggle ou tracé manuel de cellules vivantes |

---

## Structures prédéfinies

| Nom | Description |
|---|---|
| Planeur (Glider) | Se déplace en diagonale indéfiniment |
| Clignotant (Blinker) | Oscillateur de période 2 |
| Vaisseau Léger (LWSS) | Se déplace horizontalement |
| Canon à Planeurs (Gosper) | Tire un planeur toutes les 30 générations |

---

## Règles du jeu

### Classique (Conway) — B3/S23
- Cellule vivante avec **< 2** ou **> 3** voisines → **meurt**
- Cellule vivante avec **2 ou 3** voisines → **survit**
- Cellule morte avec exactement **3** voisines → **naît**

### HighLife — B36/S23
- Survie : 2 ou 3 voisines
- Naissance : 3 ou **6** voisines

### Day & Night — B3678/S34678
- Survie : 3, 4, 6, 7 ou 8 voisines
- Naissance : 3, 6, 7 ou 8 voisines