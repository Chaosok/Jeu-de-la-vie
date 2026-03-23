import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    // La liste de ceux qui observent le jeu
    private List<Observateur> observateurs = new ArrayList<>();

    public void attacheObservateur(Observateur o) {
        observateurs.add(o);
    }

    public void detacheObservateur(Observateur o) {
        observateurs.remove(o);
    }

    public void notifieObservateurs() {
        for (Observateur o : observateurs) {
            o.actualise(); // dire à chaque observateur de se mettre à jour
        }
    }
}