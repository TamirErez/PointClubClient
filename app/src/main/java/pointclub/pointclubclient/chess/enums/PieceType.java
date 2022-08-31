package pointclub.pointclubclient.chess.enums;

import lombok.Getter;

public enum PieceType {
    NONE("None"),
    ROOK("Rook"),
    BISHOP("BISHOP"),
    KING("KING"),
    KNIGHT("KNIGHT");

    @Getter
    private final String name;

    PieceType(String name) {
        this.name = name;
    }
}
