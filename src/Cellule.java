/**
 * Représente une cellule individuelle sur la grille du Jeu de la Vie.
 * Son comportement (vivre/mourir) est délégué à son état courant (Pattern État).
 */
public class Cellule {
    // La cellule mémorise son état actuel
    private CelluleEtat etat;
    private int x;
    private int y;

    /**
     * Construit une nouvelle cellule avec des coordonnées et un état initial.
     * @param x La coordonnée horizontale.
     * @param y La coordonnée verticale.
     * @param etatInitial L'état de départ (vivant ou mort).
     */
    public Cellule(int x, int y, CelluleEtat etatInitial) {
        this.etat = etatInitial;
        this.x = x;
        this.y = y;
    }

    /**
     * Modifie l'état actuel de la cellule.
     * @param etat Le nouvel état à appliquer.
     */
    public void setEtat(CelluleEtat etat) {
        this.etat = etat;
    }

    /** Demande à l'état courant de faire vivre la cellule. */
    public void vit() {
        // La cellule demande à son état de gérer l'action "vit"
        etat.vit(this);
    }

    /** Demande à l'état courant de faire mourir la cellule. */
    public void meurt() {
        // La cellule demande à son état de gérer l'action "meurt"
        etat.meurt(this);
    }

    /**
     * Vérifie si la cellule est actuellement vivante.
     * @return true si la cellule est vivante, false sinon.
     */
    public boolean estVivante() {
        return etat.estVivante();
    }

    /**
     * Accepte un visiteur contenant les règles du jeu.
     * @param v Le visiteur (les règles) à appliquer.
     */
    public void accepte(Visiteur v) {
        etat.accepte(v, this);
    }

    /**
     * Calcule le nombre de cellules voisines actuellement vivantes.
     * @param jeu L'instance principale du jeu contenant la grille.
     * @return Le nombre de voisines vivantes (entre 0 et 8).
     */
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