import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

public class ButtonFuncionalidade {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Teste Frame");
        frame.setSize(new Dimension(500, 300));
        frame.setLocation(525, 250);

        Container CONTEUDO_PAINEL = frame.getContentPane();
        String[] options = {"Opção 1", "Opção 2", "Opção 3"};
        JList<String> lista = new JList<>(options);

        JButton button = new JButton("Botão");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(lista.getSelectedValue().toString());
            }
        });

        JPanel PAINEL = new JPanel();
        PAINEL.add(lista);
        PAINEL.add(button);
        CONTEUDO_PAINEL.add(lista, BorderLayout.NORTH);
        CONTEUDO_PAINEL.add(button, BorderLayout.SOUTH);    

        frame.setVisible(true);
    }
}