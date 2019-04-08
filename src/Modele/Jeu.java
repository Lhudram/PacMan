package Modele;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Observable;

public class Jeu extends Observable implements Runnable {

    private volatile boolean exit = false;
    private Grille grille = new Grille();
    private PacMan pacMan;
    private ArrayList<Fantome> tabFantome = new ArrayList<>();
    private Score score = new Score();

    private boolean estInvincible = false;
    private boolean estEnKillStreak = false;
    private int timerInvincible = 0;
    private int nbrGomme = 0;
    private boolean estFini = false;

    public Jeu() {
        this.pacMan = new PacMan(grille);
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        this.exit = true;
    }

    @Override
    public void run() {
        while (!this.exit){
            if (!this.estFini) {
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

            } else {
                finJeu(false);
            }

            try {
                Thread.sleep(300); // pause
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }


    public void ajouterFantome(int x, int y, Image image) {
        Fantome f = new Fantome(x, y, this.grille, image);
        this.tabFantome.add(f);
    }

    private void finJeu(boolean gameOver) {
        if (gameOver)
            System.out.println("Game Over !");
        else
            System.out.println("GG !");
        this.exit = true;
    }

    private boolean detectionCollision(int i) {
        if (this.tabFantome.get(i).getX() == this.pacMan.getX() && this.tabFantome.get(i).getY() == this.pacMan.getY()) {
            return true;
        } else {
            switch (this.pacMan.getDirection()) {
                case UP:
                    if (this.tabFantome.get(i).getDirection() == Direction.DOWN && !caseSansFantome(this.pacMan.getX() - 1, this.pacMan.getY())) {
                        return true;
                    }
                    break;
                case DOWN:
                    if (this.tabFantome.get(i).getDirection() == Direction.UP && !caseSansFantome(this.pacMan.getX() + 1,this. pacMan.getY())) {
                        return true;
                    }
                    break;
                case RIGHT:
                    if (this.tabFantome.get(i).getDirection() == Direction.LEFT && !caseSansFantome(this.pacMan.getX(), this.pacMan.getY() + 1)) {
                        return true;
                    }
                    break;
                case LEFT:
                    if (this.tabFantome.get(i).getDirection() == Direction.RIGHT && !caseSansFantome(this.pacMan.getX(), this.pacMan.getY() - 11)) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private boolean caseSansFantome(int x, int y) {
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
            this.score.ajouterScoreKillStreak();
        }
    }


    private void augmenterNombreGomme() {
        if (this.nbrGomme == 149) {
            this.estFini = true;
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
}
