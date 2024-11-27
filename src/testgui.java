import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class testgui extends JFrame{

    private JButton button1;
    private JPanel mainpannel;

    public testgui (){
        setContentPane(mainpannel);
        setTitle("ian");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300,200);
        setLocationRelativeTo(null);
        setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(testgui.this, "helloWorld");
            }
        });
    }

    public static void main(String[] args) {
        new testgui();
    }
}
