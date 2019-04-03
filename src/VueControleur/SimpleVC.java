/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VueControleur;

import Modele.SimplePacMan;

import java.util.Observer;

import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author frederic.armetta
 */
public class SimpleVC extends Application {

    private final int SIZE_X = 21;
    private final int SIZE_Y = 21;
    private int oldx = 0;
    private int oldy = 0;
    private KeyCode direction = KeyCode.N;


    @Override
    public void start(Stage primaryStage) {
        SimplePacMan spm = new SimplePacMan(SIZE_X, SIZE_Y); // initialisation du modèle

        GridPane root = new GridPane(); // création de la grille

        // Pacman.svg.png 
        Image imPM = new Image("Pacman.png"); // préparation des images
        Image imSol = new Image("sol.png");
        Image imMur = new Image("mur.png");
        Image imPetiteGomme = new Image("mini_gomme.png");
        Image imGomme = new Image("gomme.png");
        Image imGhost0 = new Image("fantome_bleu.png");
        Image imGhost1 = new Image("fantome_rouge.png");
        Image imGhost2 = new Image("fantome_orange.png");
        Image imGhost3 = new Image("fantome_vert.png");
        Image imGhostAfraid = new Image("fantome_vulnerable.png");

        //img.setScaleY(0.01); 
        //img.setScaleX(0.01); 

        ImageView[][] tab = new ImageView[SIZE_X][SIZE_Y]; // tableau permettant de récupérer les cases graphiques lors du rafraichissement

        for (int i = 0; i < SIZE_X; i++) { // initialisation de la grille (sans image) 
            for (int j = 0; j < SIZE_Y; j++) {
                ImageView img = new ImageView();
                tab[i][j] = img;
                root.add(img, i, j);

                switch (spm.getTab(i, j)) {
                    case 0:
                        tab[i][j].setImage(imSol);
                        break;
                    case 1:
                        tab[i][j].setImage(imMur);
                        break;
                    case 2:
                        tab[i][j].setImage(imPetiteGomme);
                        break;
                    case 3:
                        tab[i][j].setImage(imGomme);
                        break;
                }

            }

        }

        // l'observer observe l'obervable (update est exécuté dès notifyObservers() est appelé côté modèle )
        Observer o = (o1, arg) -> {
            int[] fantomex = spm.getFantomex();
            int[] fantomey = spm.getFantomey();
            int[] oldfantomex;
            int[] oldfantomey;
            Image[] oldImage = new Image[spm.nbrfantome];

            for (int i = 0; i < SIZE_X; i++) { // rafraichissement graphique 
                for (int j = 0; j < SIZE_Y; j++) {
                    if (spm.getX() == i && spm.getY() == j) { // spm est à la position i, j => le dessiner
                        tab[i][j].setImage(imPM);
                    }
                    oldfantomex = fantomex;
                    oldfantomey = fantomey;
                    for (int k = 0; k < spm.nbrfantome; k++) {
                        if (fantomex[k] == i && fantomey[k] == j) {
                            oldImage[k] = tab[i][j].getImage();
                            switch (k) {
                                case 0:
                                    if (spm.estInvincible())
                                        tab[i][j].setImage(imGhostAfraid);
                                    else
                                        tab[i][j].setImage(imGhost0);
                                    break;
                                case 1:
                                    if (spm.estInvincible())
                                        tab[i][j].setImage(imGhostAfraid);
                                    else
                                        tab[i][j].setImage(imGhost1);
                                    break;
                                case 2:
                                    if (spm.estInvincible())
                                        tab[i][j].setImage(imGhostAfraid);
                                    else
                                        tab[i][j].setImage(imGhost2);
                                    break;
                                case 3:
                                    if (spm.estInvincible())
                                        tab[i][j].setImage(imGhostAfraid);
                                    else
                                        tab[i][j].setImage(imGhost3);
                                    break;
                            }
                            spm.deplacementfantome(k);
                            if (!spm.caseAvecFantome()) {
                                tab[oldfantomex[k]][oldfantomey[k]].setImage(oldImage[k]);
                            }
                        }
                    }
                }
            }

            if (oldx != spm.getX() || oldy != spm.getY())
                tab[oldx][oldy].setImage(imSol);

            if (spm.getTab(spm.getX(), spm.getY()) == 2) {
                spm.augmenterScore(0);
                spm.setTab(spm.getX(), spm.getY(), 0);
            } else if (spm.getTab(spm.getX(), spm.getY()) == 3) {
                spm.augmenterScore(1);
                spm.setInvincible();
                spm.setTab(spm.getX(), spm.getY(), 0);
            }

            oldx = spm.getX();
            oldy = spm.getY();

            if (spm.estInvincible()) {
                spm.decrementerTimer();
            }
            spm.collisionFantome();
            spm.deplacement(direction);
        };

        spm.addObserver(o);
        spm.start(); // on démarre spm 

        Scene scene = new Scene(root, 500, 500);

        primaryStage.setTitle("PacMan");
        primaryStage.setScene(scene);
        primaryStage.show();

        // on écoute le clavier 
        root.setOnKeyPressed(event -> {
            direction = event.getCode();
        });

        root.requestFocus();
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
            spm.stop();
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

} 
