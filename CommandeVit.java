public class CommandeVit extends Commande {
    
    // Constructeur : on mémorise la cellule cible
    public CommandeVit(Cellule c) {
        this.cellule = c;
    }

    @Override
    public void executer() {
        // Le receveur (la cellule) exécute l'action "vit"
        cellule.vit();
    }
}