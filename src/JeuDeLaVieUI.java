import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JeuDeLaVieUI extends JFrame implements Observateur {

    private JeuDeLaVie jeu;
    private int tailleCellule = 8;
    private ZoneGrille zoneGrille;
    private JScrollPane scrollPane;

    // NOUVEAU : Variables globales pour le HUD
    private Timer timer;
    private int generation = 0;

    // NOUVEAU : Les couleurs personnalisables
    private Color couleurVivante = Color.BLACK;
    private Color couleurMorte = Color.WHITE;

    // NOUVEAU : Méthode centralisée pour avancer et compter
    private void avancerGeneration() {
        jeu.calculerGenerationSuivante();
        generation++;
        actualise();
    }

    // NOUVEAU : Méthode pour compter les cellules vivantes
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

        //Timer timer = new Timer(500, e -> jeu.calculerGenerationSuivante());
        // On utilise notre nouvelle variable globale et méthode
        timer = new Timer(500, e -> avancerGeneration());

        //JPanel panneauControle = new JPanel(new GridLayout(3, 1));
        JPanel panneauControle = new JPanel();
        panneauControle.setLayout(new BoxLayout(panneauControle, BoxLayout.Y_AXIS));

        //JPanel ligne1 = new JPanel();
        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));

        JButton btnPlayPause = new JButton("▶ Commencer");
        btnPlayPause.addActionListener(e -> {
            if (timer.isRunning()) { timer.stop();  btnPlayPause.setText("▶ Commencer"); }
            else                   { timer.start(); btnPlayPause.setText("⏸ Pause"); }
            actualise();
        });
        ligne1.add(btnPlayPause);

        JButton btnSuivant = new JButton("⏭ Suivant");
        btnSuivant.addActionListener(e -> avancerGeneration() /*jeu.calculerGenerationSuivante()*/);
        ligne1.add(btnSuivant);

        JButton btnReset = new JButton("↺ Réinitialiser");
        btnReset.addActionListener(e -> {jeu.reinitialiserGrille(0.3);
            generation = 0;
        });
        ligne1.add(btnReset);

        // NOUVEAU : REMPLACE PAR LE MENU DÉROULANT DES STRUCTURES
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

            // Si on a choisi une structure, on remet le compteur de génération à 0
            if (choix != 0) {
                generation = 0; 
                actualise(); 
            }
        });
        ligne1.add(comboStructures);

        JButton btnZoomMoins = new JButton("Zoom-");
        btnZoomMoins.addActionListener(e -> {
            if (tailleCellule > 2) { tailleCellule -= 2; majPreferredSize(); }
        });
        ligne1.add(btnZoomMoins);

        JButton btnZoomPlus = new JButton("Zoom+");
        btnZoomPlus.addActionListener(e -> { tailleCellule += 2; majPreferredSize(); });
        ligne1.add(btnZoomPlus);

        JButton btnAjuster = new JButton("↕ Ajuster");
        btnAjuster.addActionListener(e -> { tailleCellule = 8; majPreferredSize(); });
        ligne1.add(btnAjuster);

        panneauControle.add(ligne1);

        //JPanel ligne2 = new JPanel();
        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        ligne2.add(new JLabel("Vitesse (ms):"));

        JSlider sliderVitesse = new JSlider(50, 1000, 500);
        
        // --- Affichage des graduations ---
        sliderVitesse.setMajorTickSpacing(150); // Affiche un nombre tous les 250
        sliderVitesse.setPaintTicks(true);      // Active les petits traits
        sliderVitesse.setPaintLabels(true);     // Active les nombres
        
        sliderVitesse.addChangeListener(e -> {
            int vitesseVisuelle = sliderVitesse.getValue();
            timer.setDelay(vitesseVisuelle);
            actualise();
        });
        ligne2.add(sliderVitesse);

        ligne2.add(new JLabel("Densité (%):"));
        JSlider sliderDensite = new JSlider(0, 100, 30);
        
        // --- Affichage des graduations ---
        sliderDensite.setMajorTickSpacing(25); // Nombres : 0, 25, 50, 75, 100
        sliderDensite.setMinorTickSpacing(5);  // Petits traits intermédiaires
        sliderDensite.setPaintTicks(true);     
        sliderDensite.setPaintLabels(true);    
        
        sliderDensite.addChangeListener(e -> {
            // (Ton code existant pour changer la densité)
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
            if      (choix == 0) jeu.setVisiteur(new VisiteurClassique(jeu));
            else if (choix == 1) jeu.setVisiteur(new VisiteurHighLife(jeu));
            else                 jeu.setVisiteur(new VisiteurDayNight(jeu));
        });
        ligne1.add(comboRegles);
        
        JButton btnCouleurVie = new JButton("Couleur Cellules Vivantes");
        btnCouleurVie.addActionListener(e -> {
            // Ouvre une fenêtre magique de choix de couleur !
            Color nouvelleCouleur = JColorChooser.showDialog(this, "Choisir la couleur (Vivantes)", couleurVivante);
            if (nouvelleCouleur != null) {
                couleurVivante = nouvelleCouleur;
                actualise(); // Redessine immédiatement
            }
        });
        ligne2.add(btnCouleurVie);

        JButton btnCouleurMort = new JButton("Couleur Cellules Mortes");
        btnCouleurMort.addActionListener(e -> {
            Color nouvelleCouleur = JColorChooser.showDialog(this, "Choisir la couleur (Mortes)", couleurMorte);
            if (nouvelleCouleur != null) {
                couleurMorte = nouvelleCouleur;
                actualise(); // Redessine immédiatement
            }
        });
        ligne2.add(btnCouleurMort);

        // N'oublie pas d'ajouter cette ligne 3 au panneau principal !
        panneauControle.add(ligne1);
        panneauControle.add(ligne2);
        
        this.add(panneauControle, BorderLayout.SOUTH);

        /*panneauControle.add(ligne2);
        this.add(panneauControle, BorderLayout.SOUTH);*/

        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Met à jour setPreferredSize de la ZoneGrille.
     * - tailleCellule == 8 : fit = remplit exactement le viewport, pas de scrollbar
     * - tailleCellule > fit : grille plus grande que le viewport → scrollbars apparaissent
     * - tailleCellule < fit : grille plus petite, centrée dans le viewport
     */
    private void majPreferredSize() {
        int tc = tcEffectif();
        zoneGrille.setPreferredSize(new Dimension(
            jeu.getXMax() * tc,
            jeu.getYMax() * tc
        ));
        zoneGrille.revalidate();
        zoneGrille.repaint();
    }

    /** Calcule la taille de cellule effective en tenant compte du viewport. */
    private int tcEffectif() {
        if (tailleCellule == 8) {
            Dimension vp = scrollPane.getViewport().getSize();
            int vpW = vp.width  > 0 ? vp.width  : 600;
            int vpH = vp.height > 0 ? vp.height : 500;
            tailleCellule = Math.max(1, Math.min(vpW / jeu.getXMax(), vpH / jeu.getYMax()));
        }
        return Math.max(1, tailleCellule);
    }

    @Override
    public void actualise() {
        SwingUtilities.invokeLater(() -> {
            if (tailleCellule == 8) majPreferredSize();
            else zoneGrille.repaint();
        });
    }

    // =========================================================
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

        // Centrage : si la grille est plus petite que le panneau visible
        private int offX(int tc) { return Math.max(0, (getWidth()  - jeu.getXMax() * tc) / 2); }
        private int offY(int tc) { return Math.max(0, (getHeight() - jeu.getYMax() * tc) / 2); }

        private void toggle(int px, int py, boolean basculer) {
            int tc = tcEffectif();
            int cx = (px - offX(tc)) / tc;
            int cy = (py - offY(tc)) / tc;
            Cellule c = jeu.getGrilleXY(cx, cy);
            if (c == null) return;
            if (basculer) { if (c.estVivante()) c.meurt(); else c.vit(); }
            else          { if (!c.estVivante()) c.vit(); }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int tc   = tcEffectif();
            int offX = offX(tc);
            int offY = offY(tc);

            for (int x = 0; x < jeu.getXMax(); x++) {
                for (int y = 0; y < jeu.getYMax(); y++) {
                    Cellule c = jeu.getGrilleXY(x, y);
                    //g.setColor(c != null && c.estVivante() ? Color.BLACK : Color.WHITE);
                    if (c != null && c.estVivante()) {
                        g.setColor(couleurVivante);
                    } else {
                        g.setColor(couleurMorte);
                    }
                    int px = offX + x * tc;
                    int py = offY + y * tc;
                    g.fillRect(px, py, tc, tc);
                    if (tc >= 4) {
                        g.setColor(Color.LIGHT_GRAY);
                        g.drawRect(px, py, tc, tc);
                    }
                }
            }

            // On récupère la position actuelle du scroll pour que le HUD "suive" la caméra
            Rectangle vue = scrollPane.getViewport().getViewRect();
            int hudX = vue.x + 5;
            int hudY = vue.y + 5;

            // 1. Dessin du fond (Noir avec 180 d'opacité = semi-transparent)
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRoundRect(hudX, hudY, 120, 75, 15, 15);

            // 2. Dessin du texte en blanc
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            
            g.drawString("Génération : " + generation, hudX + 10, hudY + 20);
            g.drawString("Vivantes : " + compterVivantes(), hudX + 10, hudY + 40);
            
            // On ajoute la vitesse (en ms) ou "En pause"
            String texteVitesse = timer.isRunning() ? timer.getDelay() + " ms" : "En pause";
            g.drawString("Vitesse : " + texteVitesse, hudX + 10, hudY + 60);
        }
    }
}