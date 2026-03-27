import javax.swing.*;
import java.awt.*;

public class JeuDeLaVieUI extends JFrame implements Observateur {
    
    private JeuDeLaVie jeu;
    private int tailleCellule = 7;
    private ZoneGrille zoneGrille; // NOUVEAU : Un panneau dédié uniquement au dessin

    public JeuDeLaVieUI(JeuDeLaVie jeu) {
        this.jeu = jeu;
        this.setTitle("Jeu de la Vie");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // 1. LA ZONE DE DESSIN (Au centre)
        zoneGrille = new ZoneGrille();
        this.add(zoneGrille, BorderLayout.CENTER);

        // 2. LE TIMER
        Timer timer = new Timer(500, e -> jeu.calculerGenerationSuivante());

        // 3. LE PANNEAU DE CONTRÔLE (En bas)
        // On utilise un GridLayout(2, 1) pour forcer le panneau à avoir 2 lignes !
        JPanel panneauControle = new JPanel(new GridLayout(2, 1));
        
        // --- LIGNE 1 : Les boutons d'action ---
        JPanel ligne1 = new JPanel();
        
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
        ligne1.add(btnPlayPause);

        JButton btnSuivant = new JButton("Suivant");
        btnSuivant.addActionListener(e -> jeu.calculerGenerationSuivante());
        ligne1.add(btnSuivant);

        JButton btnReset = new JButton("Réinitialiser");
        btnReset.addActionListener(e -> jeu.reinitialiserGrille(0.3)); // Par exemple 30%
        ligne1.add(btnReset);

        JButton btnZoomMoins = new JButton("Zoom -");
        btnZoomMoins.addActionListener(e -> { if (tailleCellule > 2) { tailleCellule -= 2; actualise(); }});
        ligne1.add(btnZoomMoins);

        JButton btnZoomPlus = new JButton("Zoom +");
        btnZoomPlus.addActionListener(e -> { tailleCellule += 2; actualise(); });
        ligne1.add(btnZoomPlus);

        JButton btnPlaneur = new JButton("Générer Planeur");
        btnPlaneur.addActionListener(e -> jeu.chargerPlaneur()); // Décommente si tu as la méthode
        ligne1.add(btnPlaneur);

        // --- LIGNE 2 : Les réglages (Vitesse, Densité, Règles) ---
        JPanel ligne2 = new JPanel();

        JSlider sliderVitesse = new JSlider(50, 1000, 500);
        sliderVitesse.addChangeListener(e -> timer.setDelay(sliderVitesse.getValue()));
        ligne2.add(new JLabel("Vitesse:"));
        ligne2.add(sliderVitesse);

        JSlider sliderDensite = new JSlider(0, 100, 30);
        JButton btnResetDensite = new JButton("Reset Aléatoire");
        btnResetDensite.addActionListener(e -> jeu.reinitialiserGrille(sliderDensite.getValue() / 100.0));
        ligne2.add(new JLabel("Densité:"));
        ligne2.add(sliderDensite);
        ligne2.add(btnResetDensite);

        String[] regles = {"Classique (Conway)", "HighLife", "Day & Night"};
        JComboBox<String> comboRegles = new JComboBox<>(regles);
        comboRegles.addActionListener(e -> {
            int choix = comboRegles.getSelectedIndex();
            if (choix == 0) jeu.setVisiteur(new VisiteurClassique(jeu));
            else if (choix == 1) jeu.setVisiteur(new VisiteurHighLife(jeu)); // Décommente si tu l'as
            else if (choix == 2) jeu.setVisiteur(new VisiteurDayNight(jeu)); // Décommente si tu l'as
        });
        ligne2.add(comboRegles);

        // On assemble les lignes dans le panneau de contrôle principal
        panneauControle.add(ligne1);
        panneauControle.add(ligne2);
        
        // On ajoute tout ça en bas de la fenêtre
        this.add(panneauControle, BorderLayout.SOUTH);

        // 4. TAILLE DE LA FENÊTRE
        this.pack(); // Demande à Swing de calculer la meilleure taille possible
        // On fixe une taille minimum pour éviter d'écraser l'interface
        this.setMinimumSize(new Dimension(800, 600)); 
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actualise() {
        zoneGrille.repaint(); // On redessine uniquement la zone de la grille
    }

    // =========================================================
    // NOUVEAU : Une classe interne dédiée au dessin de la grille
    // =========================================================
    private class ZoneGrille extends JPanel {
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Nettoie le fond proprement
            
            // Calcul mathématique pour CENTRER la grille dans l'espace vide
            int largeurGrille = jeu.getXMax() * tailleCellule;
            int hauteurGrille = jeu.getYMax() * tailleCellule;
            int offsetX = (this.getWidth() - largeurGrille) / 2;
            int offsetY = (this.getHeight() - hauteurGrille) / 2;
            
            // Si la grille est plus grande que l'écran (zoom), on bloque le décalage à 0
            offsetX = Math.max(0, offsetX);
            offsetY = Math.max(0, offsetY);

            // Double buffering implicite de Swing géré ici !
            for (int x = 0; x < jeu.getXMax(); x++) {
                for (int y = 0; y < jeu.getYMax(); y++) {
                    Cellule c = jeu.getGrilleXY(x, y);
                    
                    if (c != null && c.estVivante()) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    
                    int posX = offsetX + (x * tailleCellule);
                    int posY = offsetY + (y * tailleCellule);
                    
                    g.fillRect(posX, posY, tailleCellule, tailleCellule);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(posX, posY, tailleCellule, tailleCellule);
                }
            }
        }
    }
}