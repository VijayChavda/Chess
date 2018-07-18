
import board.ChessBoard;
import chess.Chess;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class ClassicChess extends JFrame {

    private Image loadingImg = ChessBoard.getImgSrc(getClass().getClassLoader(), "LoadingImg.jpg",
            Chess.INITBOUNDS.getSize());

    public static void main(String[] args) {
        System.out.println("Classic Chess");
        System.out.println("Version : 1.1");
        System.out.println("by- Vijay Chavda");
        ClassicChess chess = new ClassicChess();
        chess.repaint();
    }

    static {
        try {
            for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
                if (lafInfo.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(lafInfo.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                UnsupportedLookAndFeelException ex) {
            //no problem...
        }
    }

    public ClassicChess() {
        setTitle("Classic Chess...");
        setSize(Chess.INITBOUNDS);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setUndecorated(true);
        setVisible(true);
        Component chess = add(new Chess());
        loadingImg = null;
        repaint();
        chess.repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(loadingImg, 0, 0, this);
    }
}
