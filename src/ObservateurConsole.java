/**
 * Observateur concret qui affiche l'état du jeu dans la console texte.
 * Utile pour exécuter le programme sans interface graphique ou pour du débogage.
 */
public class ObservateurConsole implements Observateur{

    private JeuDeLaVie jeu;
    private int generation = 0;

    /**
     * Construit un observateur affichant les statistiques dans la sortie standard.
     * @param jeu Le jeu observé dont on va analyser l'état.
     */
    public ObservateurConsole(JeuDeLaVie jeu){
        this.jeu = jeu;

    }

    /**
     * Incrémente le numéro de génération, compte toutes les cellules vivantes
     * et affiche le résumé directement dans la console.
     */
    @Override
    public void actualise(){
        generation++;
        int vivantes = 0;

        for (int i=0; i<jeu.getXMax(); i++){
            for(int j=0; j<jeu.getYMax(); j++){
                Cellule c = jeu.getGrilleXY(i, j);
                if (c != null && c.estVivante()){
                    vivantes++;
                }
            }
        }
        System.out.println("Numéro de génération: " + generation + "|| Cellules vivantes: " + vivantes + " cellule(s).");
    }
}
