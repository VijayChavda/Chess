package chess;

import board.ChessBoard;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public final class Rules extends JFrame {

    private static final Dimension FRAME_SIZE = new Dimension
            ((int) (0.87890625 * Chess.INITBOUNDS.width), Chess.INITBOUNDS.height);
    private static final Dimension PAGE_SIZE = new Dimension
            ((int) (0.87890625 * Chess.INITBOUNDS.width), (int) (7.6953125 * Chess.INITBOUNDS.height));
    private static Image img;
    private final JScrollPane scroller;
    private final Component page;

    static {
        img = ChessBoard.getImgSrc(Rules.class.getClassLoader(), "Rules.jpg", PAGE_SIZE);
    }

    public Rules() {
        setSize(FRAME_SIZE);
        setLocation(Chess.INITBOUNDS.width / 2 - getWidth() / 2, 0);
        setLayout(null);
        setType(Type.UTILITY);
        setTitle("Rules of Chess...");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                setLocation(Chess.INITBOUNDS.width / 2 - getWidth() / 2, 0);
            }
        });

        img = img.getScaledInstance(PAGE_SIZE.width, PAGE_SIZE.height, Image.SCALE_SMOOTH);

        page = new JLabel(new ImageIcon(img));
        page.setLocation(0, 0);
        page.setSize(PAGE_SIZE);

        scroller = new JScrollPane(page);
        scroller.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(scroller);
        scroller.setLocation(0, 0);
        scroller.setSize(FRAME_SIZE.width - 6, FRAME_SIZE.height);
    }
}
