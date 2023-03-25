package board;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import lombok.Data;
import enums.Colour;
import enums.PieceType;
import move.Position;
import piece.AbstractPiece;

@Data
public class Board {
    public static final int DEFAULT_BOARD_SIZE = 8;
    private int columns;
    private int rows;
    private final Square[][] squares;
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

    public Board(Board board) {
        this.columns = board.columns;
        this.rows = board.rows;
        this.firstSquareColour = board.firstSquareColour;
        squares = new Square[rows][columns];
        cleanBoard();
        copyPieces(board);
    }

    private void copyPieces(Board board) {
        for (int i = 0; i < board.squares.length; i++) {
            for (int j = 0; j < board.squares[i].length; j++) {
                this.squares[i][j].setPiece(board.squares[i][j].getPiece().clone());
            }
        }
    }

    private void cleanBoard() {
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

    public void addNewPiece(AbstractPiece piece) {
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
                boardString.append(square);
                boardString.append("|");
            }
            boardString.append("\n");
        }

        return boardString.append("  a  b  c  d  e  f  g  h").toString();
    }

    public AbstractPiece removePieceFromPosition(Position position) {
        return getSquareByPosition(position).removePiece();
    }

    public void movePieceToPosition(AbstractPiece piece, Position position) {
        getSquareByPosition(position).setPiece(piece);
    }

    public List<AbstractPiece> getPieces() {
        return Arrays.stream(squares)
                .flatMap(row -> Arrays.stream(row).map(Square::getPiece))
                .filter(abstractPiece -> !abstractPiece.getType().isNone())
                .collect(Collectors.toList());
    }

    public List<AbstractPiece> getPiecesOfType(PieceType type) {
        return getPieces().stream()
                .filter(piece -> piece.getType().equals(type))
                .collect(Collectors.toList());
    }

    public Position getPositionTowardsTarget(Position startPosition, Position targetPosition, int steps) {
        int startColumn = startPosition.getColumn();
        int startRow = startPosition.getRow();
        int targetRow = targetPosition.getRow();
        int targetColumn = targetPosition.getColumn();

        return startRow == targetRow
                ? new Position(startRow, startColumn + steps * (startColumn < targetColumn ? 1 : -1))
                : new Position(startRow + steps * (startRow < targetRow ? 1 : -1), startColumn);

    }
}