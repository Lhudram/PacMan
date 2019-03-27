/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author fred
 */
public class SimplePacMan extends Observable implements Runnable {

    private volatile boolean exit = false;
    private int x, y, sizeX, sizeY;
    private int tab[][];//0=sol,1=mur
    private Random r = new Random();

    public SimplePacMan(int _sizeX, int _sizeY) {

        x = 10; y = 15;

        sizeX = _sizeX;
        sizeY = _sizeY;
        tab = new int[sizeX][sizeY];
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

    public void setxy(int newx, int newy){
        if(coupPossible(newx,newy)) {
            x += newx;
            y += newy;
        }
    }

    public boolean coupPossible(int newx, int newy){
        if(x + newx >= 0 && x + newx < sizeX && y + newy >= 0 && y + newy < sizeY){
            if(newx!=0) {
                if (tab[x + newx][y]==0) {
                    return true;
                }
            }
            if(newy!=0) {
                return tab[x][y + newy] == 0;
            }

        }
        return false;
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
        this.tab[i][j] = elem;
    }
}
