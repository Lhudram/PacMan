/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import VueControleur.SimpleVC;
import javafx.scene.input.KeyCode;

import java.util.Observable;
import java.util.Random;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


/**
 * @author fred
 */
public class SimplePacMan extends Observable implements Runnable {

    public int nbrfantome;
    private volatile boolean exit = false;
    private int x, y, sizeX, sizeY;
    private int[][] grille;//0=sol,1=mur,2=fruits,3=gommes,4=porte fantome
    private boolean estInvincible;
    private boolean estEnKillStreak;
    private int[] fantomex;
    private int[] fantomey;
    private int score;
    private int score_f;
    private int timerInvincible;

    public SimplePacMan(int _sizeX, int _sizeY) {
        this.x = 10;
        this.y = 15;
        this.score = 0;
        this.score_f = 0;
        this.sizeX = _sizeX;
        this.sizeY = _sizeY;
        this.timerInvincible = 0;

        this.nbrfantome = 4;
        this.fantomex = new int[]{10, 10, 9, 11};
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
                {1, 1, 1, 2, 1, 1, 1, 0, 4, 0, 1, 0, 1, 1, 1, 2, 1, 1, 1, 2, 1},
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
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        this.exit = true;
    }

    private void gameOver() {
        this.exit = true;
    }

    public void deplacement(KeyCode direction) {
        int addx = 0, addy = 0;

        switch (direction) {
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
            if (!estMur(x + addx, y + addy)) {
                x += addx;
                y += addy;
            }
        } else {
            if (x + addx < 0) {
                if (!deplacementPossible(20, y))
                    x = 20;
            }
            if (x + addx >= sizeX) {
                if (!deplacementPossible(0, y))
                    x = 0;
            }
            if (y + addy >= sizeY) {
                if (!deplacementPossible(x, 0))
                    y = 0;
            }
            if (y + addy < 0) {
                if (!deplacementPossible(x, 20))
                    y = 20;
            }
        }
    }

    public void deplacementfantome(int i) {
        //haut,bas,droite,gauche
        boolean[] listeDeplacement;
        listeDeplacement = new boolean[]{false, false, false, false};
        listeDeplacement[0] = !estMur(fantomex[i], fantomey[i] - 1);
        listeDeplacement[1] = !estMur(fantomex[i], fantomey[i] + 1);
        listeDeplacement[2] = !estMur(fantomex[i] + 1, fantomey[i]);
        listeDeplacement[3] = !estMur(fantomex[i] - 1, fantomey[i]);

        boolean estTrouve = false;
        while (!estTrouve) {
            int k = (int) (Math.random() * listeDeplacement.length);
            if (listeDeplacement[k]) {
                estTrouve = true;
                switch (k) {
                    case 0:
                        fantomey[i] = fantomey[i] - 1;
                        break;
                    case 1:
                        fantomey[i] = fantomey[i] + 1;
                        break;
                    case 2:
                        fantomex[i] = fantomex[i] + 1;
                        break;
                    case 3:
                        fantomex[i] = fantomex[i] - 1;
                        break;
                }
            }
        }
    }

    private boolean estMur(int x, int y) {
        return grille[x][y] == 1;
    }

    private boolean deplacementPossible(int newx, int newy) {
        if (newx != x) {
            if (estMur(newx, y) || grille[newx][y] == 4) {
                return true;
            }
        }
        if (newy != y) {
            return estMur(x, newy) || grille[x][newy] == 4;
        }
        return false;
    }

    public void collisionFantome() {
        for (int i = 0; i < nbrfantome; i++) {
            if (fantomex[i] == x && fantomey[i] == y) {
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
                    gameOver();
                }
            }
        }

    }
    public boolean caseAvecFantome() {
        for (int i = 0; i < nbrfantome; i++) {
            if (fantomex[i] == x && fantomey[i] == y) {
                return true;
            }
        }
        return false;
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
        if (point == 0)
            this.score += 10;
            //power pellet
        else if (point == 1)
            this.score += 50;
            //1er fantôme
        else if (point == 2)
            this.score += 200;
        else
            this.score_f += this.score_f * 2;
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

    public int[] getFantomex() {
        return fantomex;
    }

    public int[] getFantomey() {
        return fantomey;
    }
    

}
