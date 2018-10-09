package battleship.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

public class ConnectionWindow {
    public  ConnectionWindow() throws UnknownHostException {
        JFrame frame = new JFrame("Connection Type");
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        JButton btn1 = new JButton("TCP");
        JButton btn2 = new JButton("UDP");
        btn1.setBounds(95, 20, 200, 30);
        btn2.setBounds(95, 60, 200, 30);
        frame.add(btn1);
        frame.add(btn2);
        frame.setVisible(true);
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LobbyTCP b = new LobbyTCP();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LobbyUDP c = new LobbyUDP();
            }
        });
    }
}
