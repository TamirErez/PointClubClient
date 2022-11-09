package pointclub.pointclubclient.chess.enums;

import java.io.Serializable;

public enum Colour implements Serializable {
   WHITE, BLACK;

   public static Colour getOppositeColor(Colour colour) {
      return colour == BLACK ? WHITE : BLACK;
   }
}