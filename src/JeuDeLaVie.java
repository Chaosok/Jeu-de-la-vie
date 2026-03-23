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
    private void initialiseGrille() {
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                // On crée une cellule morte par défaut.
                Cellule c = new Cellule(x, y, CelluleEtatMort.getInstance());
                
                // Math.random() donne un chiffre entre 0.0 et 1.0. 
                // Si c'est supérieur à 0.5, on la fait naître (50% de chances).
                if (Math.random() > 0.5) {
                    c.vit();
                }
                
                grille[x][y] = c; // On range la cellule dans la case
            }
        }
    }
    
    public void reinitialiserGrille(double probabiliteVie) {
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                Cellule c = new Cellule(x, y, CelluleEtatMort.getInstance());
                if (Math.random() < probabiliteVie) { // ex: 0.3 pour 30% de vivantes
                    c.vit();
                }
                grille[x][y] = c;
            }
        }
        notifieObservateurs(); // Met à jour l'écran instantanément
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
    }
}