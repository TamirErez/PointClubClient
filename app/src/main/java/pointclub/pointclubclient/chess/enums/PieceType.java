package pointclub.pointclubclient.chess.enums;

import lombok.Getter;

public enum PieceType {
    NONE("NONE"),
    ROOK("ROOK"),
    BISHOP("BISHOP"),
    KING("KING"),
    KNIGHT("KNIGHT"),
    QUEEN("QUEEN"),
    PAWN("PAWN");

    @Getter
    private final String name;

    PieceType(String name) {
        this.name = name;
    }
}
