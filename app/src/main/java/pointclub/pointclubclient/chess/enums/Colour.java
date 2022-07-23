package pointclub.pointclubclient.chess.enums;

public enum Colour {
   WHITE, BLACK;

   public static Colour getOppositeColor(Colour colour) {
      return colour == BLACK ? WHITE : BLACK;
   }
}