package Modele;


import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class Fantome extends Entite {

    private Direction currentDirection;
    private int x, y;
    private Grille grille;
    private Image image;

    public Fantome(int x, int y, Grille grille, Image image) {
        this.x = x;
        this.y = y;
        this.grille = grille;
        this.image = image;
        this.currentDirection = Direction.UP;
    }

    @Override
    protected void realiserAction() {

        ArrayList<Direction> listeDeplacement = new ArrayList<>();
        boolean memeDirection;


        memeDirection = deplacementPossible(currentDirection);

        if (deplacementPossible(Direction.UP) && (currentDirection != Direction.DOWN || !memeDirection)) {
            listeDeplacement.add(Direction.UP);
        }
        if (deplacementPossible(Direction.DOWN) && (currentDirection != Direction.UP || !memeDirection)) {
            listeDeplacement.add(Direction.DOWN);
        }
        if (deplacementPossible(Direction.RIGHT) && (currentDirection != Direction.LEFT || !memeDirection)) {
            listeDeplacement.add(Direction.RIGHT);
        }
        if (deplacementPossible(Direction.LEFT) && (currentDirection != Direction.RIGHT || !memeDirection)) {
            listeDeplacement.add(Direction.LEFT);
        }

        if (listeDeplacement.size() > 0) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(listeDeplacement.size());
            Direction randomElement = listeDeplacement.get(randomIndex);
            switch (randomElement) {
                case UP:
                    y--;
                    currentDirection = Direction.UP;
                    break;
                case DOWN:
                    y++;
                    currentDirection = Direction.DOWN;
                    break;
                case RIGHT:
                    x++;
                    currentDirection = Direction.RIGHT;
                    break;
                case LEFT:
                    x--;
                    currentDirection = Direction.LEFT;
                    break;
            }
            listeDeplacement.clear();
        }
    }

    private boolean deplacementPossible(Direction direction) {
        switch (direction) {
            case UP:
                return grille.getElement(x, y - 1) != 1;
            case DOWN:
                if (grille.getElement(x, y + 1) == 4)
                    return false;
                return grille.getElement(x, y + 1) != 1;
            case LEFT:
                if (x - 1 == 0)
                    return false;
                return grille.getElement(x - 1, y) != 1;
            case RIGHT:
                if (x + 1 == grille.getSizeX())
                    return false;
                return grille.getElement(x + 1, y) != 1;
        }

        return false;
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

    public Image getImage() {
        return image;
    }
}
