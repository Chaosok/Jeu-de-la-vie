// État Mort 
public class CelluleEtatMort implements CelluleEtat {
    private static CelluleEtatMort instance = new CelluleEtatMort();
    private CelluleEtatMort() {}

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