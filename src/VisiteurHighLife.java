public class VisiteurHighLife extends Visiteur {

    public VisiteurHighLife(JeuDeLaVie jeu) { super(jeu); }

    @Override
    public void visiteCelluleVivante(Cellule c) {
        int v = c.nombreVoisinesVivantes(jeu);
        if (v != 2 && v != 3) {
            jeu.ajouterCommande(new CommandeMeurt(c));
        }
    }

    @Override
    public void visiteCelluleMorte(Cellule c) {
        int v = c.nombreVoisinesVivantes(jeu);
        if (v == 3 || v == 6) { 
            jeu.ajouterCommande(new CommandeVit(c));
        }
    }
}