package Modele;

public class PacMan extends Entite {

    private Direction currentDirection;
    private int x, y;
    private Grille grille;

    public PacMan(Grille grille) {
        this.grille = grille;
        this.x = 10;
        this.y = 15;
        this.currentDirection = Direction.UP;
    }

    @Override
    protected void realiserAction() {

        int addx = 0, addy = 0;
        int sizeX = grille.getSizeX();
        int sizeY = grille.getSizeY();

        switch (currentDirection) {
            case UP:
                addy = -1;
                break;
            case DOWN:
                addy = 1;
                break;
            case LEFT:
                addx = -1;
                break;
            case RIGHT:
                addx = 1;
                break;
        }
        if (x + addx >= 0 && x + addx < sizeX && y + addy >= 0 && y + addy < sizeY) {
            if (deplacementPossible(x + addx, y + addy)) {
                x += addx;
                y += addy;
            }
        } else {
            if (x + addx < 0) {
                if (deplacementPossible(sizeX - 1, y))
                    x = sizeX - 1;
            }
            if (x + addx >= sizeX) {
                if (deplacementPossible(0, y))
                    x = 0;
            }
            if (y + addy >= sizeY) {
                if (deplacementPossible(x, 0))
                    y = 0;
            }
            if (y + addy < 0) {
                if (deplacementPossible(x, sizeY - 1))
                    y = sizeY - 1;
            }
        }
    }

    private boolean deplacementPossible(int newx, int newy) {
        if (newx != x) {
            if (grille.getElement(newx, y) == 1 || grille.getElement(newx, y) == 4) {
                return false;
            }
        }
        if (newy != y) {
            return grille.getElement(x, newy) != 1 && grille.getElement(x, newy) != 4;
        }
        return true;
    }

    public void setDirection(Direction direction) {
        this.currentDirection = direction;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Direction getDirection() {
        return this.currentDirection;
    }
}
