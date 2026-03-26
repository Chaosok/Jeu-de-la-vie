import javax.swing.*;
import java.awt.*;

// La classe implémente Observateur pour être notifiée des changements
public class JeuDeLaVieUI extends JFrame implements Observateur {
    
    private JeuDeLaVie jeu;
    private int tailleCellule = 10;

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

        // --- BOUTON Zoom ---
        JButton btnZoomPlus = new JButton("Zoom +");
        btnZoomPlus.addActionListener(e -> {
            tailleCellule += 2; // Augmente la taille de 2 pixels
            actualise(); // Redessine
        });

        JButton btnZoomMoins = new JButton("Zoom -");
        btnZoomMoins.addActionListener(e -> {
            if (tailleCellule > 2) { // Empêche d'avoir une taille négative ou nulle
                tailleCellule -= 2;
                actualise();
            }
        });
        
        panneauControle.add(btnZoomMoins);
        panneauControle.add(btnZoomPlus);

        // --- config de départ ---
        JButton btnPlaneur = new JButton("Générer Planeur");
        btnPlaneur.addActionListener(e -> jeu.chargerPlaneur());
        panneauControle.add(btnPlaneur);

        // --- taille de grille ---
        JButton btnTaille = new JButton("Grille 100x100");
        btnTaille.addActionListener(e -> {
            jeu.redimensionner(100, 100);
            
            // Il faut recalculer la taille de la fenêtre !
            Insets insets = getInsets();
            int larg = (jeu.getXMax() * tailleCellule) + insets.left + insets.right;
            int haut = (jeu.getYMax() * tailleCellule) + insets.top + insets.bottom + 60; 
            setSize(larg, haut);
            
            actualise();
        });
        panneauControle.add(btnTaille);

        // --- SLIDER DE VITESSE ---
        JSlider sliderVitesse = new JSlider(50, 1000, 500);
        sliderVitesse.addChangeListener(e -> timer.setDelay(sliderVitesse.getValue()));
        panneauControle.add(new JLabel("Vitesse:"));
        panneauControle.add(sliderVitesse);

        // --- CONTRÔLE DE DENSITÉ ---
        // Curseur de 0 à 100, départ à 50
        JSlider sliderDensite = new JSlider(0, 100, 50); 
        
        JButton btnReset = new JButton("Reset Aléatoire");
        btnReset.addActionListener(e -> {
            // On divise par 100 pour transformer "50" en "0.5"
            double densite = sliderDensite.getValue() / 100.0; 
            jeu.reinitialiserGrille(densite);
        });

        panneauControle.add(new JLabel("Densité:"));
        panneauControle.add(sliderDensite);
        panneauControle.add(btnReset);

        // --- COMBOBOX RÈGLES ---
        String[] regles = {"Classique (Conway)", "HighLife", "Day & Night"};
        JComboBox<String> comboRegles = new JComboBox<>(regles);
        comboRegles.addActionListener(e -> {
            int choix = comboRegles.getSelectedIndex();
            
            if (choix == 0) {
                // Règle 0 : Le classique de John Conway
                jeu.setVisiteur(new VisiteurClassique(jeu));
                
            } else if (choix == 1) {
                // Règle 1 : HighLife
                jeu.setVisiteur(new VisiteurHighLife(jeu)); // (Si tu l'as créé !)
                
            } else if (choix == 2) {
                // Règle 2 : Day & Night
                jeu.setVisiteur(new VisiteurDayNight(jeu));
            }
        });
        panneauControle.add(comboRegles);

        // 4. On ajoute le panneau de contrôle en bas de la fenêtre
        this.add(panneauControle, BorderLayout.SOUTH);

        // 5. Calcul de la taille de la fenêtre
        this.setVisible(true); 
        Insets insets = this.getInsets();
        int largeurTotale = (jeu.getXMax() * tailleCellule) + insets.left + insets.right;
        
        // ATTENTION : On ajoute ~40 pixels en plus en hauteur pour avoir la place d'afficher le panneau de contrôle !
        int hauteurTotale = (jeu.getYMax() * tailleCellule) + insets.top + insets.bottom + 40; 
        
        this.setSize(largeurTotale, hauteurTotale);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actualise() {
        // Quand le jeu notifie un changement, on demande à redessiner la fenêtre
        this.repaint();
    }

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
                
                int positionX = offsetX + (x * tailleCellule);
                int positionY = offsetY + (y * tailleCellule);
                
                // On colorie sur la toile cachée
                pinceauCache.fillRect(positionX, positionY, tailleCellule, tailleCellule);
                
                pinceauCache.setColor(Color.LIGHT_GRAY);
                pinceauCache.drawRect(positionX, positionY, tailleCellule, tailleCellule);
            }
        }
        
        // On affiche l'image terminée sur l'écran en un seul bloc !
        g.drawImage(imageCachee, 0, 0, this);
    }
}