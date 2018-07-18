package board;

import board.ChessBoard.ChessSquare;
import chess.Team;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Knight extends AbstractChessMen {

    private Image wKnightImg, bKnightImg;

    {
        wKnightImg = chessBoard.getImgSrc("wKnight.gif");
        bKnightImg = chessBoard.getImgSrc("bKnight.gif");
    }

    public Knight(Knight knight) {
        super(knight);
    }

    public Knight(ChessBoard chessBoard, Team team) {
        super(chessBoard, team);
    }

    public Knight(ChessBoard chessBoard, ChessSquare position, Team team) {
        super(chessBoard, position, team);
    }

    @Override
    public Set<AbstractMovement> getMovement() {
        Movement upR = new Movement(1, -2, 1);
        Movement downR = new Movement(1, 2, 1);
        Movement upL = new Movement(-1, -2, 1);
        Movement downL = new Movement(-1, 2, 1);
        Movement leftU = new Movement(-2, -1, 1);
        Movement leftD = new Movement(-2, 1, 1);
        Movement rightU = new Movement(2, -1, 1);
        Movement rightD = new Movement(2, 1, 1);
        Set<AbstractMovement> moves = new HashSet(10);
        moves.addAll(Arrays.asList(new Movement[]{
                    upR, downR, upL, downL, leftU, leftD, rightU, rightD}));
        return moves;
    }

    @Override
    public Image getImg() {
        return isWhite() ? wKnightImg : bKnightImg;
    }

    @Override
    public String getMenName() {
        return "Knight";
    }

    @Override
    public String getInfo() {
        return "Knights can only be moved one step up or down,<br>"
                + " and then two steps over, or vice versa.";
    }
}
