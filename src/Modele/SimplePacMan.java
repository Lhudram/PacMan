/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.util.Observable;


/**
 *
 * @author fred
 */
public class SimplePacMan extends Observable implements Runnable {

    public int nbrfantome;
    private volatile boolean exit = false;
    private int x, y, sizeX, sizeY;
    private int[][] grille;//0=sol,1=mur,2=fruits,3=gommes,4=porte fantome
    private int score;
    private boolean estInvincible;
    private int[] fantomex;
    private int[] fantomey;
    private int score_dot;
    private int score_f;

    public SimplePacMan(int _sizeX, int _sizeY) {
        x = 10; 
        y = 15;
        score_dot=0;
        score_f=0;
        sizeX = _sizeX;
        sizeY = _sizeY;

        this.nbrfantome=4;
        this.fantomex=new int []{10,10,9,11};
        this.fantomey=new int []{7,9,9,9};

        this.score=0;
        this.estInvincible=false;

        this.grille = new int[][]{
                {0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,0,1,0,1,0,1,1,1,1,1,1,1,1,1},
                {1,2,3,2,2,2,1,0,1,0,1,0,1,2,2,3,1,2,2,2,1},
                {1,2,1,2,1,2,1,0,1,0,1,0,1,2,1,2,2,2,1,2,1},
                {1,2,1,2,1,2,1,1,1,0,1,1,1,2,1,1,1,2,1,2,1},
                {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,2,1},
                {1,2,1,2,1,1,1,1,1,0,1,1,1,2,1,2,1,1,1,2,1},
                {1,2,1,2,2,2,1,0,0,0,0,0,0,2,1,2,2,2,1,2,1},
                {1,2,1,2,1,2,1,0,1,1,1,0,1,2,1,2,1,2,1,2,1},
                {1,2,2,2,1,2,0,0,1,0,1,0,1,2,2,2,1,2,2,2,1},
                {1,1,1,2,1,1,1,0,4,0,1,0,1,1,1,2,1,1,1,2,1},
                {1,2,2,2,1,2,0,0,1,0,1,0,1,2,2,2,1,2,2,2,1},
                {1,2,1,2,1,2,1,0,1,1,1,0,1,2,1,2,1,2,1,2,1},
                {1,2,1,2,2,2,1,0,0,0,0,0,0,2,1,2,2,2,1,2,1},
                {1,2,1,2,1,1,1,1,1,0,1,1,1,2,1,2,1,1,1,2,1},
                {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,2,1},
                {1,2,1,2,1,2,1,1,1,0,1,1,1,2,1,1,1,2,1,2,1},
                {1,2,1,2,1,2,1,0,1,0,1,0,1,2,1,2,2,2,1,2,1},
                {1,2,3,2,2,2,1,0,1,0,1,0,1,2,2,3,1,2,2,2,1},
                {1,1,1,1,1,1,1,0,1,0,1,0,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0}
        };
    }
    @Override
    public void run() {
        while(!exit) {
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
    public void stop(){
        this.exit = true;
    }

    public void deplacement(KeyCode direction){
        int addx=0,addy=0;

        switch(direction){
            case UP:addx=0;addy=-1;
                break;
            case DOWN:addx=0;addy=1;
                break;
            case LEFT:addx=-1;addy=0;
                break;
            case RIGHT:addx=1;addy=0;
                break;
        }
        if(x+addx>=0 && x+addx<sizeX && y+addy >=0 && y+addy<sizeY) {
            if (!estMur(x+addx, y+addy)) {
                x+=addx;
                y+=addy;
            }
        }
        else {
            if (x + addx < 0) {
                if (!estMur(20, y))
                    x =20;
            }
            if (x + addx >= sizeX) {
                if (!estMur(0, y))
                    x =0;
            }
            if (y + addy >= sizeY) {
                if (!estMur(x, 0))
                    y=0;
            }
            if (y + addy < 0) {
                if (!estMur(x, 20))
                    y=20;
            }
        }
    }

    public void deplacementfantome() {
    }

    private boolean estMur(int newx, int newy){
        if(newx!=x) {
            if (grille[newx][y]==1 || grille[newx][y]==4 ) {
               return true;
            }
        }
        if(newy!=y) {
            return grille[x][newy] == 1 || grille[x][newy] == 4;
        }
        return false;
    }

    public void initXY() {
        x = 0;
        y = 0;
    }

    public void collisionFantome() {
        for(int i=0;i<nbrfantome;i++){
            if(fantomex[i]==x && fantomey[i]==y){
                if(estInvincible()){
                    augmenterScore(2);
                }
                else{
                    gameOver();
                }
            }
        }

    }

    private void gameOver() {
        this.exit = true;
    }

    private boolean estInvincible() {
        return this.estInvincible;
    }

    public void setTab(int i, int j ,int elem) {
        this.grille[i][j] = elem;
    }
    
    public int getTab(int i, int j) {
        return this.grille[i][j];
    }
    
    public void augmenterScore(int point){
        //point pac-dot
        if (point==0)
            this.score_dot+=10;
        //power pellet
        else if(point==1)
            this.score_dot+=50;
        //1er fantôme
        else if(point==2)
            this.score_f+=200;
        else
            this.score_f=this.score_f*2;
    }

    public int getScoreDot() {
        return this.score_dot;
    }
    
    public int getScoreF() {
        return this.score_f;
    }
    
    public void setInvincible(){
        this.estInvincible = true;
        this.timerInvincible=
    }

    public void setNonInvicible(){
        this.estInvincible=false;
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
