import java.awt.*;
import javax.swing.*;

public class  ColorFrame{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Teste Frame");
        frame.setSize(new Dimension(500, 300));
        frame.setLocation(525, 250);

        Container CONTEUDO_PAINEL = frame.getContentPane();
        CONTEUDO_PAINEL.setBackground(Color.BLACK);

        frame.setVisible(true);
    }
}