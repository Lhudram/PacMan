package Modele;

import javafx.scene.input.KeyCode;

public enum Direction {
    UP, RIGHT, DOWN, LEFT;

    public static Direction valueFor(KeyCode keyCode) {
        return valueOf(keyCode.name());
    }
}

