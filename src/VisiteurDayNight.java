/**
 * Implémente les règles de la variante "Day & Night" (B3678/S34678).
 * Dans cet univers, la survie et la naissance sont possibles avec de nombreuses
 * voisines, ce qui crée des structures symétriques très denses.
 */
public class VisiteurDayNight extends Visiteur {

    public VisiteurDayNight(JeuDeLaVie jeu) {
        super(jeu);
    }

    @Override
    public void visiteCelluleVivante(Cellule c) {
        int v = c.nombreVoisinesVivantes(jeu);
        // Elle survit si elle a 3, 4, 6, 7 ou 8 voisines.
        // Donc elle meurt si elle en a 0, 1, 2 ou 5.
        if (v == 0 || v == 1 || v == 2 || v == 5) {
            jeu.ajouterCommande(new CommandeMeurt(c));
        }
    }

    @Override
    public void visiteCelluleMorte(Cellule c) {
        int v = c.nombreVoisinesVivantes(jeu);
        // Elle nait si elle a 3, 6, 7 ou 8 voisines.
        if (v == 3 || v == 6 || v == 7 || v == 8) {
            jeu.ajouterCommande(new CommandeVit(c));
        }
    }
}