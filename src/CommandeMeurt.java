/**
 * Commande concrète demandant à une cellule de mourir (Pattern Commande).
 */
public class CommandeMeurt extends Commande {
    
    /**
     * Construit la commande pour cibler une cellule spécifique.
     * @param c La cellule qui devra mourir.
     */
    public CommandeMeurt(Cellule c) {
        this.cellule = c;
    }

    @Override
    public void executer() {
        // Le receveur (la cellule) exécute l'action "meurt"
        cellule.meurt();
    }
}