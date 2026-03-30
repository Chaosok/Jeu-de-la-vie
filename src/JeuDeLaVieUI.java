import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Interface Graphique Principale (Swing) agissant comme Observateur.
 * Affiche la grille, gère l'interaction utilisateur et les contrôles du jeu.
 */
public class JeuDeLaVieUI extends JFrame implements Observateur {

    private JeuDeLaVie jeu;
    private int tailleCellule = 8;
    private ZoneGrille zoneGrille;
    private JScrollPane scrollPane;
    private Timer timer;
    private int generation = 0;

    private Color couleurVivante = Color.BLACK;
    private Color couleurMorte = Color.WHITE;

    /** Avance la simulation d'une génération et met à jour les statistiques. */
    private void avancerGeneration() {
        jeu.calculerGenerationSuivante();
        generation++;
        actualise();
    }

    /** * Compte le nombre total de cellules en vie sur le plateau.
     * @return Le nombre de cellules vivantes.
     */
    private int compterVivantes() {
        int count = 0;
        for (int x = 0; x < jeu.getXMax(); x++) {
            for (int y = 0; y < jeu.getYMax(); y++) {
                Cellule c = jeu.getGrilleXY(x, y);
                if (c != null && c.estVivante()) count++;
            }
        }
        return count;
    }

    /**
     * Construit et assemble l'interface graphique du jeu.
     * @param jeu L'instance du modèle de jeu à observer.
     */
    public JeuDeLaVieUI(JeuDeLaVie jeu) {
        this.jeu = jeu;
        this.setTitle("Jeu de la Vie");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        zoneGrille = new ZoneGrille();
        scrollPane = new JScrollPane(zoneGrille);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        this.add(scrollPane, BorderLayout.CENTER);

        timer = new Timer(500, e -> avancerGeneration());

        JPanel panneauControle = new JPanel();
        panneauControle.setLayout(new BoxLayout(panneauControle, BoxLayout.Y_AXIS));

        // --- LIGNE 1 : Contrôles d'exécution ---
        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

        JButton btnPlayPause = new JButton("▶ Commencer");
        btnPlayPause.addActionListener(e -> {
            if (timer.isRunning()) { timer.stop();  btnPlayPause.setText("▶ Commencer"); }
            else { timer.start(); btnPlayPause.setText("⏸ Pause"); }
            actualise();
        });
        ligne1.add(btnPlayPause);

        JButton btnSuivant = new JButton("⏭ Suivant");
        btnSuivant.addActionListener(e -> avancerGeneration());
        ligne1.add(btnSuivant);

        JButton btnReset = new JButton("↺ Réinitialiser");
        btnReset.addActionListener(e -> {jeu.reinitialiserGrille(0.3); generation = 0;});
        ligne1.add(btnReset);

        ligne1.add(new JLabel("Structures :"));
        String[] structures = {"Normale", "Planeur", "Clignotant", "Vaisseau Léger", "Canon à Planeurs"};
        JComboBox<String> comboStructures = new JComboBox<>(structures);
        comboStructures.addActionListener(e -> {
            int choix = comboStructures.getSelectedIndex();
            if (choix == 0) jeu.reinitialiserGrille(0.3);
            else if (choix == 1) jeu.chargerPlaneur();
            else if (choix == 2) jeu.chargerClignotant();
            else if (choix == 3) jeu.chargerVaisseauLeger();
            else if (choix == 4) jeu.chargerCanonAPlaneurs();

            if (choix != 0) { generation = 0; actualise(); }
        });
        ligne1.add(comboStructures);

        JButton btnZoomMoins = new JButton("Zoom-");
        btnZoomMoins.addActionListener(e -> { if (tailleCellule > 2) { tailleCellule -= 2; majPreferredSize(); }});
        ligne1.add(btnZoomMoins);

        JButton btnZoomPlus = new JButton("Zoom+");
        btnZoomPlus.addActionListener(e -> { tailleCellule += 2; majPreferredSize(); });
        ligne1.add(btnZoomPlus);

        JButton btnAjuster = new JButton("↕ Ajuster");
        btnAjuster.addActionListener(e -> { tailleCellule = 8; majPreferredSize(); });
        ligne1.add(btnAjuster);

        panneauControle.add(ligne1);

        // --- LIGNE 2 : Paramètres et Options Visuelles ---
        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        ligne2.add(new JLabel("Vitesse (ms):"));

        JSlider sliderVitesse = new JSlider(50, 1000, 500);
        sliderVitesse.setMajorTickSpacing(150); 
        sliderVitesse.setPaintTicks(true);      
        sliderVitesse.setPaintLabels(true);     
        sliderVitesse.addChangeListener(e -> {
            timer.setDelay(sliderVitesse.getValue());
            actualise();
        });
        ligne2.add(sliderVitesse);

        ligne2.add(new JLabel("Densité (%):"));
        JSlider sliderDensite = new JSlider(0, 100, 30);
        sliderDensite.setMajorTickSpacing(25); 
        sliderDensite.setMinorTickSpacing(5);  
        sliderDensite.setPaintTicks(true);     
        sliderDensite.setPaintLabels(true);    
        sliderDensite.addChangeListener(e -> {
            if (!sliderDensite.getValueIsAdjusting()) {
                jeu.reinitialiserGrille(sliderDensite.getValue() / 100.0);
            }
        });
        ligne2.add(sliderDensite);

        ligne1.add(new JLabel("Règles :"));
        String[] regles = {"Classique (Conway)", "HighLife", "Day & Night"};
        JComboBox<String> comboRegles = new JComboBox<>(regles);
        comboRegles.addActionListener(e -> {
            int choix = comboRegles.getSelectedIndex();
            if (choix == 0) jeu.setVisiteur(new VisiteurClassique(jeu));
            else if (choix == 1) jeu.setVisiteur(new VisiteurHighLife(jeu));
            else jeu.setVisiteur(new VisiteurDayNight(jeu));
        });
        ligne1.add(comboRegles);
        
        JButton btnCouleurVie = new JButton("Couleur Cellules Vivantes");
        btnCouleurVie.addActionListener(e -> {
            Color nouvelleCouleur = JColorChooser.showDialog(this, "Choisir la couleur (Vivantes)", couleurVivante);
            if (nouvelleCouleur != null) { couleurVivante = nouvelleCouleur; actualise(); }
        });
        ligne2.add(btnCouleurVie);

        JButton btnCouleurMort = new JButton("Couleur Cellules Mortes");
        btnCouleurMort.addActionListener(e -> {
            Color nouvelleCouleur = JColorChooser.showDialog(this, "Choisir la couleur (Mortes)", couleurMorte);
            if (nouvelleCouleur != null) { couleurMorte = nouvelleCouleur; actualise(); }
        });
        ligne2.add(btnCouleurMort);

        panneauControle.add(ligne1);
        panneauControle.add(ligne2);
        
        this.add(panneauControle, BorderLayout.SOUTH);

        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /** Ajuste la taille de la zone de dessin en fonction de la taille des cellules (gère l'apparition du scroll). */
    private void majPreferredSize() {
        int tc = tcEffectif();
        zoneGrille.setPreferredSize(new Dimension(jeu.getXMax() * tc, jeu.getYMax() * tc));
        zoneGrille.revalidate();
        zoneGrille.repaint();
    }

    /**
     * Calcule et retourne la taille actuelle des cellules pour le dessin.
     * Si la taille est fixée à 8, elle s'adapte dynamiquement si nécessaire pour l'écran.
     * @return La dimension d'une arête de la cellule en pixels.
     */
    private int tcEffectif() {
        if (tailleCellule == 8) {
            Dimension vp = scrollPane.getViewport().getSize();
            int vpW = vp.width  > 0 ? vp.width  : 600;
            int vpH = vp.height > 0 ? vp.height : 500;
            tailleCellule = Math.max(1, Math.min(vpW / jeu.getXMax(), vpH / jeu.getYMax()));
        }
        return Math.max(1, tailleCellule);
    }

    /** Demande à la fenêtre de se redessiner suite à une modification du modèle. */
    @Override
    public void actualise() {
        SwingUtilities.invokeLater(() -> {
            if (tailleCellule == 8) majPreferredSize();
            else zoneGrille.repaint();
        });
    }

    /**
     * Panneau interne personnalisé pour peindre la grille et l'interface Tête Haute (HUD).
     */
    private class ZoneGrille extends JPanel {

        ZoneGrille() {
            setBackground(Color.WHITE);
            MouseAdapter souris = new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) { toggle(e.getX(), e.getY(), true);  }
                @Override public void mouseDragged(MouseEvent e) { toggle(e.getX(), e.getY(), false); }
            };
            addMouseListener(souris);
            addMouseMotionListener(souris);
        }

        private int offX(int tc) { return Math.max(0, (getWidth()  - jeu.getXMax() * tc) / 2); }
        private int offY(int tc) { return Math.max(0, (getHeight() - jeu.getYMax() * tc) / 2); }

        /** Bascule l'état d'une cellule au clic de la souris. */
        private void toggle(int px, int py, boolean basculer) {
            int tc = tcEffectif();
            int cx = (px - offX(tc)) / tc;
            int cy = (py - offY(tc)) / tc;
            Cellule c = jeu.getGrilleXY(cx, cy);
            if (c == null) return;
            if (basculer) { if (c.estVivante()) c.meurt(); else c.vit(); }
            else{ if (!c.estVivante()) c.vit(); }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int tc   = tcEffectif();
            int offX = offX(tc);
            int offY = offY(tc);

            // --- Dessin de la grille de jeu ---
            for (int x = 0; x < jeu.getXMax(); x++) {
                for (int y = 0; y < jeu.getYMax(); y++) {
                    Cellule c = jeu.getGrilleXY(x, y);
                    
                    if (c != null && c.estVivante()) g.setColor(couleurVivante);
                    else g.setColor(couleurMorte);
                    
                    int px = offX + x * tc;
                    int py = offY + y * tc;
                    g.fillRect(px, py, tc, tc);
                    
                    // Bordure seulement si la case est assez grande pour éviter un rendu trop gris
                    if (tc >= 4) {
                        g.setColor(Color.LIGHT_GRAY);
                        g.drawRect(px, py, tc, tc);
                    }
                }
            }

            // --- Dessin du l'affichage d'infos superposé ---
            Rectangle vue = scrollPane.getViewport().getViewRect();
            int hudX = vue.x + 5;
            int hudY = vue.y + 5;

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRoundRect(hudX, hudY, 120, 75, 15, 15);

            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            
            g.drawString("Génération : " + generation, hudX + 10, hudY + 20);
            g.drawString("Vivantes : " + compterVivantes(), hudX + 10, hudY + 40);
            
            String texteVitesse = timer.isRunning() ? timer.getDelay() + " ms" : "En pause";
            g.drawString("Vitesse : " + texteVitesse, hudX + 10, hudY + 60);
        }
    }
}