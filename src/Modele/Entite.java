package Modele;

public abstract class Entite {
    protected Direction currentDirection;

    protected abstract void realiserAction();

    public abstract int getX();

    public abstract int getY();

    public abstract void setX(int x);

    public abstract void setY(int y);

    public abstract Direction getDirection();
}
