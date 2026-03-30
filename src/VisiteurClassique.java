/**
 * Implémente les règles classiques du Jeu de la Vie de John Conway (B3/S23).
 * - Naissance : exactement 3 voisines.
 * - Survie : 2 ou 3 voisines (meurt d'isolement ou de surpopulation sinon).
 */
public class VisiteurClassique extends Visiteur {

    public VisiteurClassique(JeuDeLaVie jeu) {
        super(jeu);
    }

    @Override
    public void visiteCelluleVivante(Cellule c) {
        int nbVoisines = c.nombreVoisinesVivantes(jeu);
        
        // Règle 1 et 2 : Solitude (<2) ou Étouffement (>3) -> La cellule va mourir
        if (nbVoisines < 2 || nbVoisines > 3) {
            jeu.ajouterCommande(new CommandeMeurt(c)); // On met l'action en attente
        }
    }

    @Override
    public void visiteCelluleMorte(Cellule c) {
        int nbVoisines = c.nombreVoisinesVivantes(jeu);
        
        // Règle 3 : Reproduction (exactement 3) -> La cellule va naître
        if (nbVoisines == 3) {
            jeu.ajouterCommande(new CommandeVit(c)); // On met l'action en attente
        }
    }
}