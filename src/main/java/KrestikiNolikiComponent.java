import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class KrestikiNolikiComponent extends JComponent {

    public static final int EMPTY_FIELD = 0;
    public static final int FIELD_X = 1;
    public static final int FIELD_O = 10;
    private int[][] field;
    boolean isXturn;

    public KrestikiNolikiComponent() {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        field = new int[3][3];
        Arrays.stream(field).forEach(row -> Arrays.fill(row, EMPTY_FIELD));
        isXturn = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, getWidth(), getHeight());
        drawGrid(g);
        drawXO(g);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            int x = e.getX();
            int y = e.getY();
            int i = (int) ((float) x / getWidth() * 3);
            int j = (int) ((float) y / getHeight() * 3);
            if (field[i][j] == EMPTY_FIELD) {
                field[i][j] = isXturn ? FIELD_X : FIELD_O;
                isXturn = !isXturn;
                repaint();
                showWinner();
            }
        }
    }

    private void drawGrid(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        int dw = w / 3;
        int dh = h / 3;
        g.setColor(Color.BLUE);
        for (int i = 1; i < 3; i++) {
            g.drawLine(0, dh * i, w, dh * i);
            g.drawLine(dw * i, 0, dw * i, h);
        }
    }

    private void drawX(int i, int j, Graphics g) {
        g.setColor(Color.GREEN);
        int dw = getWidth() / 3;
        int dh = getHeight() / 3;
        int x = i * dw;
        int y = j * dh;
        int otstup = 15;
        g.drawLine(x + otstup, y + otstup, x + dw - otstup, y + dh - otstup);
        g.drawLine(x + otstup, y + dh - otstup, x + dw - otstup, y + otstup);
    }

    private void drawO(int i, int j, Graphics g) {
        g.setColor(Color.RED);
        int dw = getWidth() / 3;
        int dh = getHeight() / 3;
        int x = i * dw;
        int y = j * dh;
        g.drawOval(x + dw / 7, y + dh / 7, dw * 7 / 10, dh * 7 / 10);
    }

    private void drawXO(Graphics g) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == FIELD_X) {
                    drawX(i, j, g);
                } else if (field[i][j] == FIELD_O) {
                    drawO(i, j, g);
                }
            }
        }
    }

    private int checkWinner() {
        Set<Integer> sums = new HashSet<>();

        sums.add(field[0][0] + field[0][1] + field[0][2]);
        sums.add(field[1][0] + field[1][1] + field[1][2]);
        sums.add(field[2][0] + field[2][1] + field[2][2]);

        sums.add(field[0][0] + field[1][0] + field[2][0]);
        sums.add(field[0][1] + field[1][1] + field[2][1]);
        sums.add(field[0][2] + field[1][2] + field[2][2]);

        sums.add(field[0][0] + field[1][1] + field[2][2]);
        sums.add(field[0][2] + field[1][1] + field[2][0]);
        if (sums.contains(30))
            return 30;
        else if (sums.contains(3))
            return 3;
        else {
            for(int[] row: field){
                for(int i:row){
                    if (i==0){
                        return 0;
                    }
                }
            }
            return 5;
        }
    }

    private void showWinner(){
        int res = checkWinner();
        if(res!=0){
            if(res ==FIELD_O*3) JOptionPane.showMessageDialog(this,"Нолики выиграли","Победа!", JOptionPane.INFORMATION_MESSAGE);
            else if(res ==FIELD_X*3) JOptionPane.showMessageDialog(this,"Крестики выиграли","Победа!", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this,"Ничья","Ничья!", JOptionPane.INFORMATION_MESSAGE);
            Arrays.stream(field).forEach(row -> Arrays.fill(row, EMPTY_FIELD));
            isXturn = true;
            repaint();
        }
    }
}
