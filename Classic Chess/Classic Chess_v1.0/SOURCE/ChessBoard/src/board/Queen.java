package board;

import board.ChessBoard.ChessSquare;
import chess.Team;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Queen extends AbstractChessMen {

    private Image wQueenImg, bQueenImg;

    {
        wQueenImg = chessBoard.getImgSrc("wQueen.gif");
        bQueenImg = chessBoard.getImgSrc("bQueen.gif");
    }

    public Queen(Queen queen) {
        super(queen);
    }

    public Queen(ChessBoard chessBoard, Team team) {
        super(chessBoard, team);
    }

    public Queen(ChessBoard chessBoard, ChessSquare position, Team team) {
        super(chessBoard, position, team);
    }

    @Override
    public Set<AbstractMovement> getMovement() {
        Movement up = new Movement(0, -1);
        Movement down = new Movement(0, 1);
        Movement left = new Movement(1, 0);
        Movement right = new Movement(-1, 0);
        Movement upR = new Movement(1, -1);
        Movement downR = new Movement(1, 1);
        Movement upL = new Movement(-1, -1);
        Movement downL = new Movement(-1, 1);
        Set<AbstractMovement> moves = new HashSet(8);
        moves.addAll(Arrays.asList(new Movement[]{up, down, left, right, upR, downR, upL, downL}));
        return moves;
    }

    @Override
    public Image getImg() {
        return isWhite() ? wQueenImg : bQueenImg;
    }

    @Override
    public String getMenName() {
        return "Queen";
    }

    @Override
    public String getInfo() {
        return "Queen can only move in a straight line, in any of the eight directions, as far as required.";
    }
}
