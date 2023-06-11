package enums;

import lombok.Getter;
import pointclub.chess.R;

public enum PieceType {
    NONE("NONE", -1),
    BLACK_ROOK("ROOK", R.drawable.black_rook),
    BLACK_BISHOP("BISHOP", R.drawable.black_bishop),
    BLACK_KNIGHT("KNIGHT", R.drawable.black_knight),
    BLACK_QUEEN("QUEEN", R.drawable.black_queen),
    BLACK_PAWN("PAWN", R.drawable.black_pawn),
    BLACK_KING("KING", R.drawable.black_king),
    WHITE_ROOK("ROOK", R.drawable.white_rook),
    WHITE_BISHOP("BISHOP", R.drawable.white_bishop),
    WHITE_KNIGHT("KNIGHT", R.drawable.white_knight),
    WHITE_QUEEN("QUEEN", R.drawable.white_queen),
    WHITE_PAWN("PAWN", R.drawable.white_pawn),
    WHITE_KING("KING", R.drawable.white_king);

    @Getter
    private final String name;
    private final int imageId;

    PieceType(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public int getValue() {
        return imageId;
    }

    public boolean isNone() {
        return this.name.equals("NONE");
    }
    public boolean isRook() {
        return this.name.equals("ROOK");
    }

    public boolean isQueen() {
        return this.name.equals("QUEEN");
    }

    public boolean isKing() {
        return this.name.equals("KING");
    }

    public boolean isPawn() {
        return this.name.equals("PAWN");
    }

    public boolean isBishop() {
        return this.name.equals("BISHOP");
    }

    public boolean isKnight() {
        return this.name.equals("KNIGHT");
    }

    public String getNotationName() {
        return isRook() ? "R" : isQueen() ? "Q" : isKing() ? "K" : isBishop() ? "B" : isKnight() ? "N" : "";
    }
}
