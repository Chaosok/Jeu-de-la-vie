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

    public void viderGrille() {
        // Remet toutes les cellules à l'état "Mort"
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                grille[x][y] = new Cellule(x, y, CelluleEtatMort.getInstance());
            }
        }
    }

    public void chargerPlaneur() {
        viderGrille(); // On nettoie d'abord
        
        // On dessine le planeur en haut à gauche
        grille[1][0].vit();
        grille[2][1].vit();
        grille[0][2].vit();
        grille[1][2].vit();
        grille[2][2].vit();
        
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