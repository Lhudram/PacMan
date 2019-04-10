package Modele;

public class Score {

    private int score;
    private int scoreKillStreak;

    public Score() {
        this.score = 0;
        this.scoreKillStreak = 0;
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
        else if (point == 2){
            this.scoreKillStreak =200;this.score += this.scoreKillStreak;
        } //enchaine les fantomes
        else{
            this.scoreKillStreak += this.scoreKillStreak * 2;
            this.score += this.scoreKillStreak;
        };
    }

    public int getScore() {
        return this.score;
    }

    public void setScoreF(int i) {
        this.scoreKillStreak =0;
    }
}
