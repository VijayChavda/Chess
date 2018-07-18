package chess;

public enum Team {

    WHITE, BLACK;

    public static boolean isWhite(Team team) {
        return team == WHITE;
    }

    public static boolean isBlack(Team team) {
        return team == BLACK;
    }

    public static Team getOpponentTeam(Team team) {
        return isWhite(team) ? BLACK : WHITE;
    }
}
