/**
 * Classe représentant l'état "Vivant" d'une cellule.
 * Implémentée en tant que Singleton pour optimiser la mémoire.
 */
public class CelluleEtatVivant implements CelluleEtat {
    private static CelluleEtatVivant instance = new CelluleEtatVivant();
    
    private CelluleEtatVivant() {} // Constructeur privé
    
    /**
     * Récupère l'instance unique de l'état vivant.
     * @return L'instance Singleton de CelluleEtatVivant.
     */
    public static CelluleEtatVivant getInstance() { return instance; }

    @Override
    public void vit(Cellule c) { /* Déjà vivante, rien à faire */ }

    @Override
    public void meurt(Cellule c) { c.setEtat(CelluleEtatMort.getInstance()); }

    @Override
    public boolean estVivante() { return true; }

    @Override
    public void accepte(Visiteur v, Cellule c) { v.visiteCelluleVivante(c); }
}