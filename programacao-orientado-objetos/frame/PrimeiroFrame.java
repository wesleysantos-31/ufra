import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class  PrimeiroFrame{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Teste Frame");
        frame.setSize(new Dimension(500, 300));
        frame.setLocation(525, 250);

        Container CONTEUDO_PAINEL = frame.getContentPane();
        JLabel label = new JLabel("Isto é um LABEL");

        JTextField texto = new JTextField();

        JButton button = new JButton("BUTTON");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                System.out.println(texto.getText());
            }

        });

        JPanel PAINEL = new JPanel();
        PAINEL.add(label);
        PAINEL.add(texto);
        PAINEL.add(button);

        CONTEUDO_PAINEL.add(label);
        CONTEUDO_PAINEL.add(texto, BorderLayout.NORTH);
        CONTEUDO_PAINEL.add(button, BorderLayout.SOUTH);        

        frame.setVisible(true);
    }
}