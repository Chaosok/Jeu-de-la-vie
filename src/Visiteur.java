/**
 * Classe abstraite représentant un Visiteur (Pattern Visiteur).
 * Elle permet de séparer les règles d'évolution (naissance/survie) 
 * de la structure de données des cellules.
 */
public abstract class Visiteur {
    /** * Le visiteur a besoin de connaître le jeu pour ajouter des commandes
     * et pour permettre aux cellules de compter leurs voisines.
     */
    protected JeuDeLaVie jeu;

    /**
     * Initialise le visiteur avec une référence au jeu.
     * @param jeu Le jeu en cours d'évaluation.
     */
    public Visiteur(JeuDeLaVie jeu) {
        this.jeu = jeu;
    }

    /**
     * Applique les règles de survie à une cellule actuellement vivante.
     * @param c La cellule vivante visitée.
     */
    public abstract void visiteCelluleVivante(Cellule c);

    /**
     * Applique les règles de naissance à une cellule actuellement morte.
     * @param c La cellule morte visitée.
     */
    public abstract void visiteCelluleMorte(Cellule c);
}