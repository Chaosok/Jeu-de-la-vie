public class Cellule {
    // La cellule mémorise son état actuel
    private CelluleEtat etat;
    private int x;
    private int y;

    // Constructeur : on définit l'état de départ
    public Cellule(int x, int y, CelluleEtat etatInitial) {
        this.etat = etatInitial;
        this.x = x;
        this.y = y;
    }

    // Permet aux états de modifier l'état de la cellule
    public void setEtat(CelluleEtat etat) {
        this.etat = etat;
    }

    // --- Délégation des actions à l'état courant ---

    public void vit() {
        // La cellule demande à son état de gérer l'action "vit"
        etat.vit(this);
    }

    public void meurt() {
        // La cellule demande à son état de gérer l'action "meurt"
        etat.meurt(this);
    }

    public boolean estVivante() {
        return etat.estVivante();
    }

    public void accepte(Visiteur v) {
        etat.accepte(v, this);
    }

    // methode pour compter les voisins
    public int nombreVoisinesVivantes(JeuDeLaVie jeu) {
        int nbVoisines = 0;
        
        // On fait une boucle de -1 à +1 en X et en Y pour faire un carré autour de la cellule
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                
                // Si i=0 et j=0, c'est la cellule elle-même ! On ne la compte pas.
                if (i == 0 && j == 0) {
                    continue; 
                }
                
                // donne la cellule voisine
                Cellule voisine = jeu.getGrilleXY(x + i, y + j);
                
                // Si la voisine existe (pas au-delà du bord) et qu'elle est vivante
                if (voisine != null && voisine.estVivante()) {
                    nbVoisines++;
                }
            }
        }
        return nbVoisines;
    }

}