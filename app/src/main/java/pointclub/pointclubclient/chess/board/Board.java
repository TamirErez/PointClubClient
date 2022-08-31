package pointclub.pointclubclient.chess.board;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import lombok.Data;
import pointclub.pointclubclient.chess.enums.Colour;
import pointclub.pointclubclient.chess.move.Position;
import pointclub.pointclubclient.chess.piece.AbstractPiece;

@Data
public class Board {
    public static final int DEFAULT_BOARD_SIZE = 8;
    private int columns;
    private int rows;
    private final Square[][] squares;
    private List<AbstractPiece> pieces = new ArrayList<>();
    private Colour firstSquareColour;

    public Board(int columns, int rows) {
        this(columns, rows, Colour.BLACK);
    }

    public Board(int columns, int rows, Colour firstSquareColour) {
        this.columns = columns;
        this.rows = rows;
        this.firstSquareColour = firstSquareColour;
        squares = new Square[rows][columns];
        cleanBoard();
    }

    private void cleanBoard() {
        pieces.clear();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                squares[row][column] = new Square(getSquareInitialColor(new Position(row, column)));
            }
        }
    }

    private Colour getSquareInitialColor(Position position) {
        return isPositionEven(position) ? firstSquareColour : Colour.getOppositeColor(firstSquareColour);
    }

    private boolean isPositionEven(Position position) {
        return (position.getColumn() + position.getRow()) % 2 == 0;
    }

    public void addPiece(AbstractPiece piece) {
        pieces.add(piece);
        getSquareByPosition(piece.getStartingPosition()).setPiece(piece);
    }

    public Square getSquareByPosition(Position position) {
        return squares[position.getRow()][position.getColumn()];
    }

    public Position getPositionOfPiece(AbstractPiece piece) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (piece.equals(squares[row][column].getPiece())) {
                    return new Position(row, column);
                }
            }
        }
        return Position.EMPTY_POSITION;
    }

    public boolean isPositionLegal(Position position) {
        return position.getColumn() >= 0 && position.getColumn() < getColumns() &&
                position.getRow() >= 0 && position.getRow() < getRows();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int row = rows - 1; row >= 0; row--) {
            boardString.append(row + 1).append(" ");
            for (int column = 0; column < columns; column++) {
                Square square = squares[row][column];
                boardString.append(square.getPiece().getAsciiName());
                boardString.append("|");
            }
            boardString.append("\n");
        }

        return boardString.append("  a  b  c  d  e  f  g  h").toString();
    }
}