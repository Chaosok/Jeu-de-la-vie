import java.util.ArrayList;
import java.util.List;

/**
 * Modèle principal du Jeu de la Vie. 
 * Gère la grille, les règles (Visiteur), l'exécution des tours (Commandes) 
 * et avertit l'interface graphique (Observable).
 */
public class JeuDeLaVie extends Observable{
    // Le tableau à deux dimensions qui sert de grille
    private Cellule[][] grille;
    private int xMax;
    private int yMax;
    private List<Commande> commandes;
    private Visiteur visiteur;

    /**
     * Initialise un nouveau jeu avec les dimensions spécifiées.
     * @param xMax La largeur de la grille (nombre de colonnes).
     * @param yMax La hauteur de la grille (nombre de lignes).
     */
    public JeuDeLaVie(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.grille = new Cellule[xMax][yMax];
        this.commandes = new ArrayList<>();
        this.visiteur = new VisiteurClassique(this);
        // On remplit la grille dès la création du jeu
        initialiseGrille();

    }

    /** Initialise la grille avec une densité par défaut de 50% de cellules vivantes. */
    private void initialiseGrille() {
        reinitialiserGrille(0.5); 
    }

    /**
     * Remplit la grille aléatoirement selon une densité donnée.
     * @param densite La probabilité (entre 0.0 et 1.0) qu'une cellule naisse.
     */
    public void reinitialiserGrille(double densite) {
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                
                Cellule c = new Cellule(x, y, CelluleEtatMort.getInstance());
                
                // Si le hasard fait moins que la densité demandée, la cellule naît
                if (Math.random() < densite) { 
                    c.vit();
                }
                
                grille[x][y] = c;
            }
        }
        //on prévient la fenêtre qu'il faut tout redessiner
        notifieObservateurs(); 
    }

    /**
     * Récupère une cellule spécifique sur la grille de manière sécurisée.
     * @param x La coordonnée X.
     * @param y La coordonnée Y.
     * @return La cellule correspondante, ou null si les coordonnées débordent.
     */
    public Cellule getGrilleXY(int x, int y) {
        // On vérifie qu'on ne déborde pas de la grille
        if (x >= 0 && x < xMax && y >= 0 && y < yMax) {
            return grille[x][y];
        }
        return null;
    }

    public int getXMax() { return xMax; }
    public int getYMax() { return yMax; }

    /**
     * Ajoute une action (naissance/mort) dans la file d'attente du prochain tour.
     * @param c La commande à ajouter.
     */
    public void ajouterCommande(Commande c) {
        commandes.add(c);
    }

    /** Exécute toutes les commandes en attente en une seule fois, puis vide la file. */
    public void executeCommandes() {
        for (Commande c : commandes) {
            c.executer(); // Fait vivre ou mourir la cellule concernée
        }
        commandes.clear(); // On vide la liste pour la génération suivante
    }

    /** Fait passer le visiteur (les règles) sur chaque cellule pour analyser le prochain tour. */
    public void distribueVisiteur() {
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                grille[x][y].accepte(visiteur);
            }
        }
    }

    /** Calcule et applique l'intégralité d'une nouvelle génération, puis notifie l'UI. */
    public void calculerGenerationSuivante() {
        //  On analyse la grille et on crée les commandes
        distribueVisiteur();
        
        // On applique toutes les modifications d'un coup
        executeCommandes();
        
        //On prévient l'interface graphique de se redessiner
        notifieObservateurs();
    }

    /**
     * Change les règles du jeu en cours de route.
     * @param v Le nouveau visiteur contenant les nouvelles règles.
     */
    public void setVisiteur(Visiteur v) {
        this.visiteur = v;
    }

    /** Tue instantanément toutes les cellules de la grille. */
    public void viderGrille() {
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                grille[x][y] = new Cellule(x, y, CelluleEtatMort.getInstance());
            }
        }
    }

    /** Insère la structure "Planeur" en haut à gauche. */
    public void chargerPlaneur() {
        viderGrille();
        grille[1][0].vit(); grille[2][1].vit();
        grille[0][2].vit(); grille[1][2].vit(); grille[2][2].vit();
        notifieObservateurs();
    }

    /** Insère la structure "Clignotant". */
    public void chargerClignotant() {
        viderGrille();
        // On le place un peu au centre (ex: coordonnées 10,10)
        grille[10][9].vit(); 
        grille[10][10].vit(); 
        grille[10][11].vit();
        notifieObservateurs();
    }

    /** Insère la structure "Vaisseau Spatial Léger" (LWSS). */
    public void chargerVaisseauLeger() {
        viderGrille();
        grille[1][10].vit(); grille[4][10].vit();
        grille[0][11].vit();
        grille[0][12].vit(); grille[4][12].vit();
        grille[0][13].vit(); grille[1][13].vit(); grille[2][13].vit(); grille[3][13].vit();
        notifieObservateurs();
    }

    /** Insère la structure complexe du Canon à Planeurs de Gosper. */
    // La structure la plus célèbre, elle tire un planeur toutes les 30 générations !
    public void chargerCanonAPlaneurs() {
        viderGrille();
        
        // Liste des coordonnées (x, y) exactes du canon
        int[][] canon = {
            {24, 1}, {22, 2}, {24, 2}, {12, 3}, {13, 3}, {20, 3}, {21, 3}, {34, 3}, {35, 3},
            {11, 4}, {15, 4}, {20, 4}, {21, 4}, {34, 4}, {35, 4}, {0, 5}, {1, 5}, {10, 5},
            {16, 5}, {20, 5}, {21, 5}, {0, 6}, {1, 6}, {10, 6}, {14, 6}, {16, 6}, {17, 6},
            {22, 6}, {24, 6}, {10, 7}, {16, 7}, {24, 7}, {11, 8}, {15, 8}, {12, 9}, {13, 9}
        };

        // On parcourt la liste et on donne vie aux cellules
        for(int[] coord : canon) {
            // Sécurité : on vérifie que la grille est assez grande pour contenir le canon
            if(coord[0] < xMax && coord[1] < yMax) {
                grille[coord[0]][coord[1]].vit();
            }
        }
        notifieObservateurs();
    }

    /**
     * Modifie la taille de la grille et la réinitialise.
     * @param nouveauX Nouvelle largeur.
     * @param nouveauY Nouvelle hauteur.
     */
    public void redimensionner(int nouveauX, int nouveauY) {
        this.xMax = nouveauX;
        this.yMax = nouveauY;
        this.grille = new Cellule[xMax][yMax]; // On recrée un tableau vide
        reinitialiserGrille(0.3); // On le remplit
    }

    public static void main(String[] args) {
        
        JeuDeLaVie jeu = new JeuDeLaVie(70, 70);
        
        // On instancie l'interface graphique
        JeuDeLaVieUI ui = new JeuDeLaVieUI(jeu);

        // Instanciation de console pour info de cellules
        ObservateurConsole console = new ObservateurConsole(jeu);
        
        // On enregistre l'interface comme observateur du jeu
        jeu.attacheObservateur(ui);
        jeu.attacheObservateur(console);
        jeu.notifieObservateurs();
    }
}