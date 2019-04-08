/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VueControleur;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observer;

import Modele.Direction;
import Modele.Fantome;
import Modele.Jeu;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class SimpleVC extends Application {

    private int oldx = 0;
    private int oldy = 0;
    private int[] oldfantomex = new int[]{0, 0, 0, 0};
    private int[] oldfantomey = new int[]{0, 0, 0, 0};


    @Override
    public void start(Stage primaryStage) {


        // initialisation du jeu
        Jeu jeu = new Jeu();
        int SIZE_X = jeu.getGrille().getSizeX();
        int SIZE_Y = jeu.getGrille().getSizeY();

        //initialisation affichage
        VBox root = new VBox();

        GridPane grid = new GridPane(); // création de la grille

        //lancement du son
        URL resource = getClass().getResource("/pac-man.mp3");
        MediaPlayer mp = new MediaPlayer(new Media(resource.toString()));
        mp.setOnEndOfMedia(() -> mp.seek(Duration.ZERO));
        mp.setVolume(0.1);
        mp.play();

        GridPane score = new GridPane();
        //affichage du score
        Label labelScore = new Label("Score : ");
        labelScore.setStyle("-fx-text-fill: white");
        score.setPadding(new Insets(5, 10, 5, 10));
        score.setStyle("-fx-background-color: #000");
        score.add(labelScore, 0, 0);
        //affichage séparateur
        Label labelSeparateur = new Label();
        labelSeparateur.setStyle("-fx-text-fill: white");
        labelSeparateur.setAlignment(Pos.CENTER);
        score.add(labelSeparateur, 1, 0);
        //affichage invinsible/game over
        Label labelInfo = new Label();
        labelInfo.setStyle("-fx-text-fill: white");
        labelInfo.setContentDisplay(ContentDisplay.RIGHT);
        score.add(labelInfo, 2, 0);
        //contraintes des colonnes
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        score.getColumnConstraints().addAll(col1, col2, col3);

        // préparation des images
        Image imSol = new Image("sol.png");
        Image imMur = new Image("mur.png");
        Image imPetiteGomme = new Image("mini_gomme.png");
        Image imGomme = new Image("gomme.png");
        // image de PacMan
        Image imPM = new Image("Pacman.png");
        //ajout des fantomes
        Image imGhost0 = new Image("fantome_bleu.png");
        jeu.ajouterFantome(10, 11, imGhost0);
        Image imGhost1 = new Image("fantome_rouge.png");
        jeu.ajouterFantome(10, 9, imGhost1);
        Image imGhost2 = new Image("fantome_orange.png");
        jeu.ajouterFantome(10, 9, imGhost2);
        Image imGhost3 = new Image("fantome_vert.png");
        jeu.ajouterFantome(10, 9, imGhost3);

        Image imGhostAfraid = new Image("fantome_vulnerable.png");

        ImageView[][] tab = new ImageView[SIZE_X][SIZE_Y]; // tableau permettant de récupérer les cases graphiques lors du rafraichissement

        for (int i = 0; i < SIZE_X; i++) { // initialisation de la grille
            for (int j = 0; j < SIZE_Y; j++) {
                ImageView img = new ImageView();
                tab[i][j] = img;
                grid.add(img, i, j);

                switch (jeu.getGrille().getElement(i, j)) {
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

        //Affichage de tout les elements
        root.getChildren().addAll(score, grid);
        Scene scene = new Scene(root, 21 * 18, 21 * 19 + 25);
        primaryStage.setTitle("PacMan");
        primaryStage.setScene(scene);
        primaryStage.show();

        // l'observer observe l'obervable (update est exécuté dès notifyObservers() est appelé côté modèle )
        Observer o = (o1, arg) -> {
            ArrayList<Fantome> fantomes = jeu.getTabFantome();

            // on dessine pacman
            int positionXPacMan = jeu.getPacMan().getX();
            int positionYPacMan = jeu.getPacMan().getY();

            tab[positionXPacMan][positionYPacMan].setImage(imPM);

            jeu.getGrille().setElement(oldx, oldy, 0);
            if (oldx != positionXPacMan || oldy != positionYPacMan)
                tab[oldx][oldy].setImage(imSol);

            oldx = positionXPacMan;
            oldy = positionYPacMan;


            //on remplace l'endroit où ils étaient avant
            for (int k = 0; k < jeu.getTabFantome().size(); k++) {
                if ((fantomes.get(k).getX() != oldfantomex[k] || fantomes.get(k).getY() != oldfantomey[k])) {
                    //on les enleve de leur ancienne posisition
                    switch (jeu.getGrille().getElement(oldfantomex[k], oldfantomey[k])) {
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
            }
            //on dessine les fantomes
            for (int k = 0; k < jeu.getTabFantome().size(); k++) {
                int x = fantomes.get(k).getX();
                int y = fantomes.get(k).getY();
                if (jeu.estInvincible()) {
                    tab[x][y].setImage(imGhostAfraid);
                } else {
                    tab[x][y].setImage(fantomes.get(k).getImage());
                }
                //on memorise leur nouvelle position
                oldfantomex[k] = x;
                oldfantomey[k] = y;
            }


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labelScore.textProperty().bind(Bindings.format("Score :  " + jeu.getScore()));
                    if(jeu.estInvincible())
                        labelInfo.textProperty().bind(Bindings.format("Invicible !"));
                    else
                        labelInfo.textProperty().bind(Bindings.format("Non Invicible"));
                }
            });
        };


        jeu.addObserver(o);
        jeu.start(); // on démarre jeu

        // on écoute le clavier
        grid.setOnKeyPressed(event -> {
            if (event.getCode().isArrowKey()) {
                jeu.getPacMan().setDirection(Direction.valueFor(event.getCode()));
            }
        });

        grid.requestFocus();
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
            mp.stop();
            jeu.stop();
        });

    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

} 
