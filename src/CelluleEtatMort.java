/**
 * Classe représentant l'état "Mort" d'une cellule.
 * Implémentée en tant que Singleton pour économiser la mémoire (une seule instance partagée).
 */
public class CelluleEtatMort implements CelluleEtat {
    private static CelluleEtatMort instance = new CelluleEtatMort();
    private CelluleEtatMort() {}

    /**
     * Récupère l'instance unique de l'état mort.
     * @return L'instance Singleton de CelluleEtatMort.
     */
    public static CelluleEtatMort getInstance() { return instance; }

    @Override
    public void vit(Cellule c) { c.setEtat(CelluleEtatVivant.getInstance()); }
    
    @Override
    public void meurt(Cellule c) { /* Déjà morte */ }
    
    @Override
    public boolean estVivante() { return false; }

    @Override
    public void accepte(Visiteur v, Cellule c) { v.visiteCelluleMorte(c); }
}