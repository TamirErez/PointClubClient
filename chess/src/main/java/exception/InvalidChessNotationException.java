package exception;

public class InvalidChessNotationException extends RuntimeException {
    public InvalidChessNotationException(String notation) {
        super("Invalid Chess Notation Parsed: " + notation);
    }

    public InvalidChessNotationException(String notation, String info) {
        super("Invalid Chess Notation Parsed: " + notation + ". " + info);
    }
}
