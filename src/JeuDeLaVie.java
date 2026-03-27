import java.util.ArrayList;
import java.util.List;

public class JeuDeLaVie extends Observable{
    // Le tableau à deux dimensions qui sert de grille
    private Cellule[][] grille;
    private int xMax;
    private int yMax;
    private List<Commande> commandes;
    private Visiteur visiteur;

    // Constructeur : on définit la taille de la grille
    public JeuDeLaVie(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.grille = new Cellule[xMax][yMax];
        this.commandes = new ArrayList<>();
        this.visiteur = new VisiteurClassique(this);
        // On remplit la grille dès la création du jeu
        initialiseGrille();

    }

    // Méthode demandée par l'énoncé pour peupler le tableau
    // L'ancienne méthode appelle maintenant la nouvelle avec 50% par défaut
    private void initialiseGrille() {
        reinitialiserGrille(0.5); 
    }

    // NOUVELLE MÉTHODE : Permet de choisir la densité (de 0.0 à 1.0)
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
        // Très important : on prévient la fenêtre qu'il faut tout redessiner !
        notifieObservateurs(); 
    }

    // Méthode pour récupérer une cellule
    public Cellule getGrilleXY(int x, int y) {
        // On vérifie qu'on ne déborde pas de la grille
        if (x >= 0 && x < xMax && y >= 0 && y < yMax) {
            return grille[x][y];
        }
        return null;
    }

    public int getXMax() { return xMax; }
    public int getYMax() { return yMax; }

    // Permet d'ajouter une action dans la file d'attente
    public void ajouterCommande(Commande c) {
        commandes.add(c);
    }

    // Exécute toutes les actions en attente, puis vide la file
    public void executeCommandes() {
        for (Commande c : commandes) {
            c.executer(); // Fait vivre ou mourir la cellule concernée
        }
        commandes.clear(); // On vide la liste pour la génération suivante
    }

    // Le jeu demande à chaque cellule d'accepter le visiteur
    public void distribueVisiteur() {
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                grille[x][y].accepte(visiteur);
            }
        }
    }

    public void calculerGenerationSuivante() {
        //  On analyse la grille et on crée les commandes
        distribueVisiteur();
        
        // On applique toutes les modifications d'un coup
        executeCommandes();
        
        //On prévient l'interface graphique de se redessiner
        notifieObservateurs();
    }

    public void setVisiteur(Visiteur v) {
        this.visiteur = v;
    }

    // Remet toutes les cellules de la grille à l'état "Mort"
    public void viderGrille() {
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                grille[x][y] = new Cellule(x, y, CelluleEtatMort.getInstance());
            }
        }
    }

    // LE PLANEUR (Glider) : Se déplace en diagonale infiniment
    public void chargerPlaneur() {
        viderGrille();
        grille[1][0].vit(); grille[2][1].vit();
        grille[0][2].vit(); grille[1][2].vit(); grille[2][2].vit();
        notifieObservateurs();
    }

    // LE CLIGNOTANT (Blinker) : Un oscillateur basique (période 2)
    public void chargerClignotant() {
        viderGrille();
        // On le place un peu au centre (ex: coordonnées 10,10)
        grille[10][9].vit(); 
        grille[10][10].vit(); 
        grille[10][11].vit();
        notifieObservateurs();
    }

    // LE VAISSEAU SPATIAL LÉGER (LWSS) : Se déplace horizontalement très vite
    public void chargerVaisseauLeger() {
        viderGrille();
        grille[1][10].vit(); grille[4][10].vit();
        grille[0][11].vit();
        grille[0][12].vit(); grille[4][12].vit();
        grille[0][13].vit(); grille[1][13].vit(); grille[2][13].vit(); grille[3][13].vit();
        notifieObservateurs();
    }

    // LE CANON À PLANEURS DE GOSPER (Gosper Glider Gun)
    // La structure la plus célèbre : elle tire un planeur toutes les 30 générations !
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

    public void redimensionner(int nouveauX, int nouveauY) {
        this.xMax = nouveauX;
        this.yMax = nouveauY;
        this.grille = new Cellule[xMax][yMax]; // On recrée un tableau vide
        reinitialiserGrille(0.3); // On le remplit
    }

    public static void main(String[] args) {
        // On instancie un JeuDeLaVie
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