/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class SimplePacMan extends Observable implements Runnable {

    private volatile boolean exit = false;
    private int x, y, sizeX, sizeY;
    private int[][] grille;//0=sol,1=mur,2=fruits,3=gommes,4=porte fantome
    private boolean estInvincible;
    private boolean estEnKillStreak;
    private int[] fantomex;
    private int[] fantomey;
    private KeyCode[] directionFantome = new KeyCode[]{KeyCode.UP, KeyCode.UP, KeyCode.UP, KeyCode.UP};
    private KeyCode directionPacMan = KeyCode.N;
    private int score = 0;
    private int score_f = 0;
    private int timerInvincible = 0;
    private int nbrGomme = 0;

    public int nbrfantome = 4;

    public SimplePacMan(int _sizeX, int _sizeY) {
        this.x = 10;
        this.y = 15;
        this.sizeX = _sizeX;
        this.sizeY = _sizeY;

        this.fantomex = new int[]{10, 9, 10, 11};
        this.fantomey = new int[]{7, 9, 9, 9};
        this.estInvincible = false;
        this.estEnKillStreak = false;

        this.grille = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 2, 3, 2, 2, 2, 1, 0, 1, 0, 1, 0, 1, 2, 2, 3, 1, 2, 2, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 0, 1, 0, 1, 0, 1, 2, 1, 2, 2, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 1, 1, 0, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1},
                {1, 2, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 2, 1, 2, 2, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 0, 1, 1, 1, 0, 1, 2, 1, 2, 1, 2, 1, 2, 1},
                {1, 2, 2, 2, 1, 2, 0, 0, 1, 0, 1, 0, 1, 2, 2, 2, 1, 2, 2, 2, 1},
                {1, 1, 1, 2, 1, 1, 1, 0, 4, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 2, 1},
                {1, 2, 2, 2, 1, 2, 0, 0, 1, 0, 1, 0, 1, 2, 2, 2, 1, 2, 2, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 0, 1, 1, 1, 0, 1, 2, 1, 2, 1, 2, 1, 2, 1},
                {1, 2, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 2, 1, 2, 2, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 1, 1, 0, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1, 2, 1, 0, 1, 0, 1, 0, 1, 2, 1, 2, 2, 2, 1, 2, 1},
                {1, 2, 3, 2, 2, 2, 1, 0, 1, 0, 1, 0, 1, 2, 2, 3, 1, 2, 2, 2, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }

    @Override
    public void run() {
        URL resource = getClass().getResource("/pac-man.mp3");
        MediaPlayer mp = new MediaPlayer(new Media(resource.toString()));
        mp.setOnEndOfMedia(() -> mp.seek(Duration.ZERO));
        mp.setVolume(0.1);
        mp.play();
        while (!exit) {
            setChanged();
            notifyObservers(); // notification de l'observer
            try {
                Thread.sleep(300); // pause
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        mp.stop();
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        this.exit = true;
    }

    private void finJeu(boolean gameOver) {

        if(gameOver)
            System.out.println("Game Over !");
        else
            System.out.println("GG !");
        System.out.println(getScore());

        this.exit = true;
    }

    public void deplacement() {
        int addx = 0, addy = 0;

        switch (directionPacMan) {
            case UP:
                addx = 0;
                addy = -1;
                break;
            case DOWN:
                addx = 0;
                addy = 1;
                break;
            case LEFT:
                addx = -1;
                addy = 0;
                break;
            case RIGHT:
                addx = 1;
                addy = 0;
                break;
        }
        if (x + addx >= 0 && x + addx < sizeX && y + addy >= 0 && y + addy < sizeY) {
            if (deplacementPacManPossible(x + addx, y + addy)) {
                x += addx;
                y += addy;
            }
        } else {
            if (x + addx < 0) {
                if (deplacementPacManPossible(sizeX - 1, y))
                    x = sizeX - 1;
            }
            if (x + addx >= sizeX) {
                if (deplacementPacManPossible(0, y))
                    x = 0;
            }
            if (y + addy >= sizeY) {
                if (deplacementPacManPossible(x, 0))
                    y = 0;
            }
            if (y + addy < 0) {
                if (deplacementPacManPossible(x, sizeY - 1))
                    y = sizeY - 1;
            }
        }
    }

    public void deplacementfantome() {
        ArrayList<KeyCode> listeDeplacement = new ArrayList<>();
        boolean memeDirection;

        for (int i = 0; i < nbrfantome; i++) {

            memeDirection = deplacementFantomePossible(i, directionFantome[i]);

            if (deplacementFantomePossible(i, KeyCode.UP) && (directionFantome[i] != KeyCode.DOWN || !memeDirection)) {
                listeDeplacement.add(KeyCode.UP);
            }
            if (deplacementFantomePossible(i, KeyCode.DOWN) && (directionFantome[i] != KeyCode.UP || !memeDirection)) {
                listeDeplacement.add(KeyCode.DOWN);
            }
            if (deplacementFantomePossible(i, KeyCode.RIGHT) && (directionFantome[i] != KeyCode.LEFT || !memeDirection)) {
                listeDeplacement.add(KeyCode.RIGHT);
            }
            if (deplacementFantomePossible(i, KeyCode.LEFT) && (directionFantome[i] != KeyCode.RIGHT || !memeDirection)) {
                listeDeplacement.add(KeyCode.LEFT);
            }

            if (listeDeplacement.size() > 0) {
                Random rand = new Random();
                int randomIndex = rand.nextInt(listeDeplacement.size());
                KeyCode randomElement = listeDeplacement.get(randomIndex);
                switch (randomElement) {
                    case UP:
                        fantomey[i] = fantomey[i] - 1;
                        directionFantome[i] = KeyCode.UP;
                        break;
                    case DOWN:
                        fantomey[i] = fantomey[i] + 1;
                        directionFantome[i] = KeyCode.DOWN;
                        break;
                    case RIGHT:
                        fantomex[i] = fantomex[i] + 1;
                        directionFantome[i] = KeyCode.RIGHT;
                        break;
                    case LEFT:
                        fantomex[i] = fantomex[i] - 1;
                        directionFantome[i] = KeyCode.LEFT;
                        break;
                }
                listeDeplacement.clear();
            }
        }
    }

    private boolean deplacementFantomePossible(int i, KeyCode direction) {

        switch (direction) {
            case UP:
                return grille[fantomex[i]][fantomey[i] - 1] != 1 && caseSansFantome(fantomex[i], fantomey[i] - 1);
            case DOWN:
                if (grille[fantomex[i]][fantomey[i] + 1] == 4)
                    return false;
                return grille[fantomex[i]][fantomey[i] + 1] != 1 && caseSansFantome(fantomex[i], fantomey[i] + 1);
            case LEFT:
                if (fantomex[i] - 1 == 0)
                    return false;
                return grille[fantomex[i] - 1][fantomey[i]] != 1 && caseSansFantome(fantomex[i] - 1, fantomey[i]);
            case RIGHT:
                if (fantomex[i] + 1 == sizeX)
                    return false;
                return grille[fantomex[i] + 1][fantomey[i]] != 1 && caseSansFantome(fantomex[i] + 1, fantomey[i]);
        }

        return false;
    }


    private boolean caseSansFantome(int x, int y) {
        for (int i = 0; i < nbrfantome; i++) {
            if (x == fantomex[i] && y == fantomey[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean deplacementPacManPossible(int newx, int newy) {
        if (newx != x) {
            if (grille[newx][y] == 1 || grille[newx][y] == 4) {
                return false;
            }
        }
        if (newy != y) {
            return grille[x][newy] != 1 && grille[x][newy] != 4;
        }
        return true;
    }

    private boolean detectionCollision(int i) {
        if (fantomex[i] == x && fantomey[i] == y) {
            return true;
        } else {
            switch (directionPacMan) {
                case UP:
                    if (directionFantome[i] == KeyCode.DOWN && !caseSansFantome(x-1,y)) {
                        return true;
                    }
                    break;
                case DOWN:
                    if (directionFantome[i] == KeyCode.UP && !caseSansFantome(x+1,y)) {
                        return true;
                    }
                    break;
                case RIGHT:
                    if (directionFantome[i] == KeyCode.LEFT && !caseSansFantome(x,y+1)) {
                        return true;
                    }
                    break;
                case LEFT:
                    if (directionFantome[i] == KeyCode.RIGHT && !caseSansFantome(x,y-1)) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }


    public void collisionFantome() {
        for(int i=0; i<nbrfantome;i++) {
            if (detectionCollision(i)) {
                if (estInvincible()) {
                    if (this.estEnKillStreak)
                        augmenterScore(3);
                    else {
                        this.estEnKillStreak = true;
                        augmenterScore(2);
                    }
                    fantomex[i] = 10;
                    fantomey[i] = 9;
                } else {
                    finJeu(true);
                }
            }
        }
    }

    public void setInvincible() {
        this.estInvincible = true;
        this.estEnKillStreak = false;
        this.timerInvincible = 20;
        this.score_f = 0;
    }

    public boolean estInvincible() {
        return this.estInvincible;
    }

    public void decrementerTimer() {
        this.timerInvincible--;
        if (this.timerInvincible == 0) {
            this.estInvincible = false;
            this.score += this.score_f;
        }
    }

    public void augmenterScore(int point) {
        //point pac-dot
        if (point == 0) {
            this.score += 10;
            augmenterNombreGomme();
        }
        //power pellet
        else if (point == 1) {
            this.score += 50;
            augmenterNombreGomme();
        }
        //1er fantôme
        else if (point == 2)
            this.score += 200;
        //enchaine les fantomes
        else
            this.score_f += this.score_f * 2;
    }

    private void augmenterNombreGomme() {
        if (nbrGomme == 149) {
            finJeu(false);
        } else {
            nbrGomme++;
        }
    }


    public void setTab(int i, int j, int elem) {
        this.grille[i][j] = elem;
    }

    public int getTab(int i, int j) {
        return this.grille[i][j];
    }

    public int getScore() {
        return this.score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getFantomesx() {
        return fantomex;
    }

    public int[] getFantomesy() {
        return fantomey;
    }

    public int getFantomex(int k) {
        return fantomex[k];
    }

    public int getFantomey(int k) {
        return fantomey[k];
    }

    public void setDirectionPacMan(KeyCode directionPacMan) {
        this.directionPacMan = directionPacMan;
    }
}
