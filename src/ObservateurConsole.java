public class ObservateurConsole implements Observateur{

    private JeuDeLaVie jeu;
    private int generation = 0;

    //Constructeur
    public ObservateurConsole(JeuDeLaVie jeu){
        this.jeu = jeu;

    }

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
