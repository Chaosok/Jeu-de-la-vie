public abstract class Commande {
    
    // La cellule (le receveur) sur laquelle la commande va agir
    protected Cellule cellule;
    
    // La méthode que chaque commande concrète devra définir
    public abstract void executer();
}