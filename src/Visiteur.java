public abstract class Visiteur {
    // Le visiteur a besoin de connaître le jeu pour ajouter des commandes
    // et pour permettre aux cellules de compter leurs voisines
    protected JeuDeLaVie jeu;

    public Visiteur(JeuDeLaVie jeu) {
        this.jeu = jeu;
    }

    // Ce que le visiteur fera s'il tombe sur une cellule vivante
    public abstract void visiteCelluleVivante(Cellule c);

    // Ce que le visiteur fera s'il tombe sur une cellule morte
    public abstract void visiteCelluleMorte(Cellule c);
}