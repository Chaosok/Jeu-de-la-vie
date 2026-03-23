public class CommandeMeurt extends Commande {
    
    public CommandeMeurt(Cellule c) {
        this.cellule = c;
    }

    @Override
    public void executer() {
        // Le receveur (la cellule) exécute l'action "meurt"
        cellule.meurt();
    }
}