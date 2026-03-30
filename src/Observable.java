import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant le Sujet (Pattern Observateur).
 * Gère une liste d'observateurs et les avertit lorsque son état change.
 */
public abstract class Observable {
    /** La liste des entités qui observent ce sujet. */
    private List<Observateur> observateurs = new ArrayList<>();

    /**
     * Inscrit un nouvel observateur pour qu'il reçoive les notifications.
     * @param o L'observateur à attacher.
     */
    public void attacheObservateur(Observateur o) {
        observateurs.add(o);
    }

    /**
     * Désinscrit un observateur de la liste.
     * @param o L'observateur à détacher.
     */
    public void detacheObservateur(Observateur o) {
        observateurs.remove(o);
    }

    /**
     * Notifie tous les observateurs attachés qu'un changement est survenu.
     * Appelle la méthode actualise() de chaque observateur.
     */
    public void notifieObservateurs() {
        for (Observateur o : observateurs) {
            o.actualise(); // dire à chaque observateur de se mettre à jour
        }
    }
}