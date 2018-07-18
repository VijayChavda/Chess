package chess;

import java.awt.Image;

public interface ChessGame extends UserConfiguration {

    public void newGame();

    public void endGame();

    public void showTip(Object message, Image img);

    public boolean promptYesNo(String message);

    public Type showPromotion(Team team);
}
