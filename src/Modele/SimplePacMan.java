/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.Observable;


/**
 *
 * @author fred
 */
public class SimplePacMan extends Observable implements Runnable {

    private volatile boolean exit = false;
    private int x, y, sizeX, sizeY;
    private int[][] grille;//0=sol,1=mur,2=fruits,3=gommes,4=porte fantome

    public SimplePacMan(int _sizeX, int _sizeY) {
        x = 10; y = 15;
        sizeX = _sizeX;
        sizeY = _sizeY;

        grille = new int[][]{
                {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0},
                {0,1,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,1,0},
                {0,1,3,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,3,1,0},
                {0,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,1,0},
                {0,1,2,1,1,2,1,2,1,1,1,1,1,2,1,2,1,1,2,1,0},
                {0,1,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,1,0},
                {0,1,1,1,1,2,1,1,1,0,1,0,1,1,1,2,1,1,1,1,0},
                {0,0,0,0,1,2,1,0,0,0,0,0,0,0,1,2,1,0,0,0,0},
                {1,1,1,1,1,2,1,0,1,1,4,1,1,0,1,2,1,1,1,1,1},
                {0,0,0,0,0,2,0,0,1,0,0,0,1,0,0,1,2,0,0,0,0},
                {1,1,1,1,1,2,1,0,1,1,1,1,1,0,1,2,1,1,1,1,1},
                {0,0,0,0,1,2,1,0,0,0,0,0,0,0,1,2,1,0,0,0,0},
                {0,1,1,1,1,2,1,0,1,1,1,1,1,0,1,2,1,1,1,1,0},
                {0,1,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,1,0},
                {0,1,2,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,2,1,0},
                {0,1,3,2,1,2,2,2,2,2,2,2,2,2,2,2,1,2,3,1,0},
                {0,1,1,2,1,2,1,2,1,1,1,1,1,2,1,2,1,2,1,1,0},
                {0,1,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,1,0},
                {0,1,2,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,2,1,0},
                {0,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,0},
                {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0},
                {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0},
        };
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void start() {
        new Thread(this).start();
    }
    public void stop(){
        exit = true;
    }

    public void setxy(int addx, int addy){

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

    public boolean estMur(int newx, int newy){
        if(newx!=x) {
            if (grille[newx][y]!=1) {
               return false;
            }
        }
        if(newy!=y) {
            return grille[x][newy] == 1;
        }
        return true;
    }

    public void initXY() {
        x = 0;
        y = 0;
    }
    
    @Override
    public void run() {
        while(!exit) { // spm descent dans la grille à chaque pas de temps
            
           /*int deltaX = r.nextInt(2);

           if (x + deltaX > 0 && x + deltaX < sizeX) {
               x += deltaX;
           }
           
           int deltaY = r.nextInt(2);
           if (y + deltaY > 0 && y + deltaY < sizeY) {
               y += deltaY;
           } 

           */
           
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

    public void setTab(int i, int j ,int elem) {
        this.grille[i][j] = elem;
    }
    
    public int getTab(int i, int j) {
        return this.grille[j][i];
    }
}
