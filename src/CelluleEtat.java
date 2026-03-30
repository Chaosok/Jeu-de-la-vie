/**
 * Interface définissant le comportement d'un état d'une cellule (Pattern État).
 * Permet de déléguer les actions de transition (naître, mourir) 
 * et l'acceptation d'un visiteur à l'état courant.
 */
public interface CelluleEtat {
    
    /**
     * Tente de faire vivre la cellule.
     * @param c La cellule concernée.
     */
    void  vit(Cellule c);

    /**
     * Tente de faire vivre la cellule.
     * @param c La cellule concernée.
     */
    void meurt(Cellule c);

    /**
     * Vérifie si l'état actuel correspond à une cellule vivante.
     * @return true si vivante, false si morte.
     */
    boolean estVivante();

    /**
     * Accepte un visiteur pour appliquer les règles du jeu (Pattern Visiteur).
     * @param v Le visiteur contenant les règles de survie/naissance.
     * @param c La cellule visitée.
     */
    void accepte(Visiteur v, Cellule c);
}
