public interface CelluleEtat {
    
    void  vit(Cellule c);
    void meurt(Cellule c);
    boolean estVivante();
    void accepte(Visiteur v, Cellule c);
}
