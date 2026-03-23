import javax.swing.*;
import java.awt.*;

// La classe implémente Observateur pour être notifiée des changements
public class JeuDeLaVieUI extends JFrame implements Observateur {
    
    private JeuDeLaVie jeu;
    private static final int TAILLE_CELLULE = 10;

    public JeuDeLaVieUI(JeuDeLaVie jeu) {
        this.jeu = jeu;
        this.setTitle("Jeu de la Vie");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. On sépare la fenêtre en zones pour pouvoir placer les boutons en bas
        this.setLayout(new BorderLayout());

        // 2. Le Timer pour l'animation (On le crée ici, au départ il est arrêté)
        Timer timer = new Timer(500, e -> {
            jeu.calculerGenerationSuivante();
        });

        // 3. On crée un panneau de contrôle pour le bas
        JPanel panneauControle = new JPanel();

        // --- BOUTON PLAY/PAUSE ---
        JButton btnPlayPause = new JButton("Commencer");
        btnPlayPause.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                btnPlayPause.setText("Commencer");
            } else {
                timer.start();
                btnPlayPause.setText("Pause");
            }
        });
        panneauControle.add(btnPlayPause);

        // --- BOUTON SUIVANT ---
        JButton btnSuivant = new JButton("Suivant");
        btnSuivant.addActionListener(e -> jeu.calculerGenerationSuivante());
        panneauControle.add(btnSuivant);

        // --- BOUTON Reintialisation ---
        JButton btnReinit = new JButton("Réinitaliser");
        btnReinit.addActionListener(e -> jeu.reinitialiserGrille(0.5));
        panneauControle.add(btnReinit);

        // --- SLIDER DE VITESSE ---
        JSlider sliderVitesse = new JSlider(50, 1000, 500);
        sliderVitesse.addChangeListener(e -> timer.setDelay(sliderVitesse.getValue()));
        panneauControle.add(new JLabel("Vitesse:"));
        panneauControle.add(sliderVitesse);

        // --- COMBOBOX RÈGLES ---
        String[] regles = {"Classique", "HighLife"};
        JComboBox<String> comboRegles = new JComboBox<>(regles);
        comboRegles.addActionListener(e -> {
            if (comboRegles.getSelectedIndex() == 0) {
                jeu.setVisiteur(new VisiteurClassique(jeu));
            } else {
                jeu.setVisiteur(new VisiteurHighLife(jeu)); // Assure-toi d'avoir créé cette classe !
            }
        });
        panneauControle.add(comboRegles);

        // 4. On ajoute le panneau de contrôle en bas de la fenêtre
        this.add(panneauControle, BorderLayout.SOUTH);

        // 5. Calcul de la taille de la fenêtre
        this.setVisible(true); 
        Insets insets = this.getInsets();
        int largeurTotale = (jeu.getXMax() * TAILLE_CELLULE) + insets.left + insets.right;
        
        // ATTENTION : On ajoute ~40 pixels en plus en hauteur pour avoir la place d'afficher le panneau de contrôle !
        int hauteurTotale = (jeu.getYMax() * TAILLE_CELLULE) + insets.top + insets.bottom + 40; 
        
        this.setSize(largeurTotale, hauteurTotale);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actualise() {
        // Quand le jeu notifie un changement, on demande à redessiner la fenêtre
        this.repaint();
    }

    // classe interne pour desinner la grille
    /*@Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // On récupère les bordures pour décaler le dessin dynamiquement
        Insets insets = this.getInsets();
        int offsetX = insets.left; // Décalage à gauche
        int offsetY = insets.top;  // Décalage en haut 

        for (int x = 0; x < jeu.getXMax(); x++) {
            for (int y = 0; y < jeu.getYMax(); y++) {
                
                Cellule c = jeu.getGrilleXY(x, y);
                
                if (c != null && c.estVivante()) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                
                // On dessine en ajoutant les décalages X et Y
                int positionX = offsetX + (x * TAILLE_CELLULE);
                int positionY = offsetY + (y * TAILLE_CELLULE);
                
                g.fillRect(positionX, positionY, TAILLE_CELLULE, TAILLE_CELLULE);
                
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(positionX, positionY, TAILLE_CELLULE, TAILLE_CELLULE);
            }
        }
    }*/
   @Override
    public void paint(Graphics g) {
        // DOUBLE BUFFERING : On crée une image "invisible" de la taille de la fenêtre
        Image imageCachee = createImage(getWidth(), getHeight());
        
        // au tout premier lancement, l'image peut ne pas être prête
        if (imageCachee == null) {
            super.paint(g);
            return;
        }
        
        // On récupère le "pinceau" de cette image invisible
        Graphics pinceauCache = imageCachee.getGraphics();

        // On dessine TOUT avec ce pinceau caché (le fond de la fenêtre)
        super.paint(pinceauCache);
        
        Insets insets = this.getInsets();
        int offsetX = insets.left;
        int offsetY = insets.top;

        for (int x = 0; x < jeu.getXMax(); x++) {
            for (int y = 0; y < jeu.getYMax(); y++) {
                
                Cellule c = jeu.getGrilleXY(x, y);
                
                if (c != null && c.estVivante()) {
                    pinceauCache.setColor(Color.BLACK);
                } else {
                    pinceauCache.setColor(Color.WHITE);
                }
                
                int positionX = offsetX + (x * TAILLE_CELLULE);
                int positionY = offsetY + (y * TAILLE_CELLULE);
                
                // On colorie sur la toile cachée
                pinceauCache.fillRect(positionX, positionY, TAILLE_CELLULE, TAILLE_CELLULE);
                
                pinceauCache.setColor(Color.LIGHT_GRAY);
                pinceauCache.drawRect(positionX, positionY, TAILLE_CELLULE, TAILLE_CELLULE);
            }
        }
        
        // On affiche l'image terminée sur l'écran en un seul bloc !
        g.drawImage(imageCachee, 0, 0, this);
    }
}