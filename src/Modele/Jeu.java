package Modele;

import VueControleur.SimpleVC;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Observable;

public class Jeu extends Observable implements Runnable {

    private volatile boolean estFini = false;
    private Grille grille = new Grille();
    private PacMan pacMan;
    private ArrayList<Fantome> tabFantome = new ArrayList<>();
    private Score score = new Score();

    private boolean estInvincible = false;
    private boolean estEnKillStreak = false;
    private int timerInvincible = 0;
    private int nbrGomme = 0;

    private boolean gameOver = false;

    public Jeu() {
        this.pacMan = new PacMan(grille);
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        this.estFini = true;
    }

    @Override
    public void run() {
        do{
                setChanged();
                // notification de l'observer
                notifyObservers();
                //diminution du temps d'invincibilite
                if (estInvincible()) {
                    decrementerTimer();
                }
                //check des collisions
                collisionFantome();
                //update du score
                if (this.grille.getElement(this.pacMan.getX(), this.pacMan.getY()) == 2) {
                    this.score.augmenterScore(0);
                    augmenterNombreGomme();
                } else if (this.grille.getElement(this.pacMan.getX(), this.pacMan.getY()) == 3) {
                    this.score.augmenterScore(1);
                    augmenterNombreGomme();
                    setInvincible();
                }
                //deplacement des entites
                this.pacMan.realiserAction();
                for (Fantome f : this.tabFantome)
                    f.realiserAction();
            try {
                Thread.sleep(250); // pause
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }while (!this.estFini);
    }


    public void ajouterFantome(int x, int y, Image image) {
        Fantome f = new Fantome(x, y, this.grille, image);
        this.tabFantome.add(f);
    }

    private void finJeu(boolean gameOver) {
        this.gameOver=gameOver;
        this.estFini = true;
    }

    private boolean detectionCollision(int i) {
        Fantome f = this.tabFantome.get(i);
        if (f.getX() == this.pacMan.getX() && f.getY() == this.pacMan.getY()) {
            return true;
        } else {
            switch (this.pacMan.getDirection()) {
                case UP:
                    if (f.getDirection() == Direction.DOWN && f.getX()==this.pacMan.getX() && f.getY()==this.pacMan.getY() - 1) {
                        return true;
                    }
                    break;
                case DOWN:
                    if (f.getDirection() == Direction.UP && f.getX()==this.pacMan.getX() && f.getY()==this.pacMan.getY() + 1) {
                        return true;
                    }
                    break;
                case RIGHT:
                    if (f.getDirection() == Direction.LEFT && f.getY()==this.pacMan.getY() && f.getX()==this.pacMan.getX() + 1) {
                        return true;
                    }
                    break;
                case LEFT:
                    if (f.getDirection() == Direction.RIGHT && f.getY()==this.pacMan.getY() && f.getX()==this.pacMan.getX()- 1) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public boolean caseSansFantome(int x, int y) {
        for (Fantome fantome : this.tabFantome) {
            if (x == fantome.getX() && y == fantome.getY()) {
                return false;
            }
        }
        return true;
    }

    public void collisionFantome() {
        for (int i = 0; i < this.tabFantome.size(); i++) {
            if (detectionCollision(i)) {
                if (estInvincible()) {
                    if (this.estEnKillStreak)
                        score.augmenterScore(3);
                    else {
                        this.estEnKillStreak = true;
                        score.augmenterScore(2);
                    }
                    this.tabFantome.get(i).setX(10);
                    this.tabFantome.get(i).setY(9);
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
        this.score.setScoreF(0);
    }

    public boolean estInvincible() {
        return this.estInvincible;
    }

    public void decrementerTimer() {
        this.timerInvincible--;
        if (this.timerInvincible == 0) {
            this.estInvincible = false;
        }
    }


    private void augmenterNombreGomme() {
        if (this.nbrGomme == 149) {
            finJeu(false);
        } else {
            this.nbrGomme++;
        }
    }

    public Grille getGrille() {
        return this.grille;
    }

    public ArrayList<Fantome> getTabFantome() {
        return tabFantome;
    }

    public PacMan getPacMan() {
        return pacMan;
    }

    public String getScore() {
        return Integer.toString(score.getScore());
    }

    public boolean estFini() {
        return estFini;
    }

    public boolean estGameOver() {
        return gameOver;
    }
}
