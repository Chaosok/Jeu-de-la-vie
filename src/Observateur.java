/**
 * Interface définissant un Observateur (Pattern Observateur).
 * Les classes qui implémentent cette interface (comme l'interface graphique)
 * seront notifiées automatiquement des changements d'état du modèle.
 */
public interface Observateur {
    /**
     * Méthode appelée par l'Observable (le jeu) pour notifier un changement.
     * Permet à l'Observateur de se mettre à jour (ex: redessiner la grille).
     */
    void actualise();
}