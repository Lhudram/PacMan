package Modele;

public class Score {

    private int score;
    private int scoreKillSreak;

    public Score() {
        this.score = 0;
        this.scoreKillSreak = 0;
    }

    public void augmenterScore(int point) {
        //point pac-dot
        if (point == 0) {
            this.score += 10;
        }
        //power pellet
        else if (point == 1) {
            this.score += 50;
        }
        //1er fantôme
        else if (point == 2)
            this.score += 200;
            //enchaine les fantomes
        else
            this.scoreKillSreak += this.scoreKillSreak * 2;
    }

    public void ajouterScoreKillStreak() {
        this.score += this.scoreKillSreak;
    }

    public int getScore() {
        return this.score;
    }

    public void setScoreF(int i) {
        this.scoreKillSreak =0;
    }
}
