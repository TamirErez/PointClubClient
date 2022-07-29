package pointclub.pointclubclient.chess.enums;

import pointclub.pointclubclient.R;

public enum PieceImage {
    BLACK_ROOK(R.drawable.black_rook),
    BLACK_BISHOP(R.drawable.black_bishop),
    BLACK_KNIGHT(R.drawable.black_knight),
    BLACK_QUEEN(R.drawable.black_queen),
    BLACK_PAWN(R.drawable.black_pawn),
    BLACK_KING(R.drawable.black_king),
    WHITE_ROOK(R.drawable.white_rook),
    WHITE_BISHOP(R.drawable.white_bishop),
    WHITE_KNIGHT(R.drawable.white_knight),
    WHITE_QUEEN(R.drawable.white_queen),
    WHITE_PAWN(R.drawable.white_pawn),
    WHITE_KING(R.drawable.white_king);

    private final int value;

    PieceImage(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
