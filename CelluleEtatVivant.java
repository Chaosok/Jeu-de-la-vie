// État Vivant (Singleton)

public class CelluleEtatVivant implements CelluleEtat {
    private static CelluleEtatVivant instance = new CelluleEtatVivant();
    
    private CelluleEtatVivant() {} // Constructeur privé
    
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