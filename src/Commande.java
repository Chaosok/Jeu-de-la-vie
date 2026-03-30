/**
 * Classe abstraite de base pour le Pattern Commande.
 * Permet d'encapsuler une action (naître ou mourir) pour l'exécuter plus tard.
 */
public abstract class Commande {
    
    /** La cellule (le receveur) sur laquelle la commande va agir. */
    protected Cellule cellule;
    
    /** Exécute l'action spécifique encapsulée par la commande. */
    public abstract void executer();
}