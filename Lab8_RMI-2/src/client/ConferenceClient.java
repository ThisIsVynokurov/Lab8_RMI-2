package client;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import task.RegTask;
import server.UserRecord;

public class ConferenceClient extends JFrame {
    private static Registry registry = null;

    public ConferenceClient() {
        setTitle("Conference Client");
        setBounds(0, 0, 650, 500);
        setResizable(true);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel Jpanel = new JPanel();
        Jpanel.setBounds(10, 10, 450, 20);
        Jpanel.setLayout(new GridLayout(1, 10));

        JLabel textElement = new JLabel("Host: ");
        JLabel textElementl2 = new JLabel("Port: ");
        TextField l2 = new TextField("1049");
        JLabel textElementl3 = new JLabel("participants:");

        TextField textField = new TextField("localhost");
        Jpanel.add(textElement);
        Jpanel.add(textField);
        Jpanel.add(new JPanel());
        Jpanel.add(textElementl2);
        Jpanel.add(l2);
        Jpanel.add(textElementl3);

        JPanel buttonPanel = new JPanel();
        Button register = new Button("Register");
        Button clear = new Button("Clear");
        Button getInfo = new Button("Get Info");
        Button finish = new Button("Finish");

        buttonPanel.setLayout(new GridLayout(1, 4, 5, 10));
        buttonPanel.add(register);
        buttonPanel.add(clear);
        buttonPanel.add(getInfo);
        buttonPanel.add(finish);

        JPanel mainData = new JPanel();
        mainData.setLayout(new GridLayout(5, 3, 10, 10));
        mainData.setBounds(20, 50, 600, 200);
        JLabel name = new JLabel("Name");
        TextField tf1 = new TextField();

        JLabel surname = new JLabel("Surname");
        TextField tf2 = new TextField();

        JLabel organization = new JLabel("Organization");
        TextField tf3 = new TextField();

        JLabel report = new JLabel("Report");
        TextField tf4 = new TextField();

        JLabel email = new JLabel("E-mail");
        TextField tf5 = new TextField();

        mainData.add(name);
        mainData.add(tf1);
        mainData.add(surname);
        mainData.add(tf2);
        mainData.add(organization);
        mainData.add(tf3);
        mainData.add(report);
        mainData.add(tf4);
        mainData.add(email);
        mainData.add(tf5);


        clear.addActionListener(l -> {
            tf1.setText("");
            tf2.setText("");
            tf3.setText("");
            tf4.setText("");
            tf5.setText("");
        });

        register.addActionListener(l -> {
            try {
                registry = LocateRegistry.getRegistry(textField.getText(), Integer.parseInt(l2.getText()));
                RegTask service = (RegTask) registry.lookup("register");
                service.executeTask(new UserRecord(tf1.getText(), tf2.getText(), tf3.getText(), tf4.getText(), tf5.getText()));
                JOptionPane.showMessageDialog(null, "Registration was successful!");
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        });

        finish.addActionListener(l -> {
            JOptionPane.showMessageDialog(null, "The Conference Client will close");
            System.exit(0);
        });


        buttonPanel.setBounds(100, 400, 500, 40);
        add(buttonPanel);
        add(Jpanel);
        add(mainData);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConferenceClient::new);
    }
}