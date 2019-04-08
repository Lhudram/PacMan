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

public class SimpleVC extends Application {

    private final int SIZE_X = 21;
    private final int SIZE_Y = 21;
    private int oldx = 0;
    private int oldy = 0;
    private int[] oldfantomex = new int[]{0, 0, 0, 0};
    private int[] oldfantomey = new int[]{0, 0, 0, 0};


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
            int[] fantomex = spm.getFantomesx();
            int[] fantomey = spm.getFantomesy();

            if (spm.estInvincible()) {
                spm.decrementerTimer();
            }

            spm.collisionFantome();
            for (int i = 0; i < SIZE_X; i++) { // rafraichissement graphique  => on passe sur chaque case
                for (int j = 0; j < SIZE_Y; j++) {

                    // pacman est à la position i, j => le dessiner
                    if (spm.getX() == i && spm.getY() == j) {
                        tab[i][j].setImage(imPM);
                    }
                    //on s'occupe des fantomes
                    for (int k = 0; k < spm.nbrfantome; k++) {
                        if (fantomex[k] == i && fantomey[k] == j) {
                            //on les redesinnes où ils doivent etre
                            if (spm.estInvincible()) {
                                tab[i][j].setImage(imGhostAfraid);
                            } else {
                                switch (k) {
                                    case 0:
                                        tab[i][j].setImage(imGhost0);
                                        break;
                                    case 1:
                                        tab[i][j].setImage(imGhost1);
                                        break;
                                    case 2:
                                        tab[i][j].setImage(imGhost2);
                                        break;
                                    case 3:
                                        tab[i][j].setImage(imGhost3);
                                        break;
                                }
                            }

                        }
                    }

                    for (int k = 0; k < spm.nbrfantome; k++) {
                        if ((fantomex[k] != oldfantomex[k] || fantomey[k] != oldfantomey[k])) {
                            //on les enleve de leur ancienne posisition
                            switch (spm.getTab(oldfantomex[k], oldfantomey[k])) {
                                case 0:
                                    tab[oldfantomex[k]][oldfantomey[k]].setImage(imSol);
                                    break;
                                case 2:
                                    tab[oldfantomex[k]][oldfantomey[k]].setImage(imPetiteGomme);
                                    break;
                                case 3:
                                    tab[oldfantomex[k]][oldfantomey[k]].setImage(imGomme);
                                    break;
                                case 4:
                                    tab[oldfantomex[k]][oldfantomey[k]].setImage(null);
                                    break;
                            }
                        }

                        oldfantomex[k] = spm.getFantomex(k);
                        oldfantomey[k] = spm.getFantomey(k);
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

            spm.deplacementfantome();
            spm.deplacement();
        };

        spm.addObserver(o);
        spm.start(); // on démarre spm 

        Scene scene = new Scene(root, 21 * 18, 21 * 19);

        primaryStage.setTitle("PacMan");
        primaryStage.setScene(scene);
        primaryStage.show();

        // on écoute le clavier 
        root.setOnKeyPressed(event -> {
            if (event.getCode().isArrowKey()) {
                spm.setDirectionPacMan(event.getCode());
            }
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
