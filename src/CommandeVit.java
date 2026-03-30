/**
 * Commande concrète demandant à une cellule de naître (Pattern Commande).
 */
public class CommandeVit extends Commande {
    
    /**
     * Construit la commande pour cibler une cellule spécifique.
     * @param c La cellule qui devra naître.
     */
    public CommandeVit(Cellule c) {
        this.cellule = c;
    }

    @Override
    public void executer() {
        // Le receveur (la cellule) exécute l'action "vit"
        cellule.vit();
    }
}