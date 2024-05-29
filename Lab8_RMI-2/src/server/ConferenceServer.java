package server;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ConcurrentModificationException;

import task.RegTask;

public class ConferenceServer {
    private final TextArea textField;
    private static TextField Port;
    private static JLabel l3;
    private final ConferenceData confData;
    private static final ConferenceServer instance = new ConferenceServer();
    private Registry registry = null;

    private ConferenceServer() {
        confData = new ConferenceData();

        JFrame jFrame = new JFrame("Conference Server");
        jFrame.setBounds(0, 0, 640, 480);
        jFrame.setVisible(true);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(1, 5, 10, 10));
        {
            JButton startBtn = new JButton("Start");
            JButton stopBtn = new JButton("Stop");
            JButton saveBtn = new JButton("Save");
            JButton loadBtn = new JButton("Load");
            JButton exitBtn = new JButton("Exit");

            btnPanel.add(startBtn);
            btnPanel.add(stopBtn);
            btnPanel.add(saveBtn);
            btnPanel.add(loadBtn);
            btnPanel.add(exitBtn);

            loadBtn.addActionListener(l -> {
                ConferenceData conferenceData = new ConferenceData();
                try {
                    conferenceData.loadStruct("Conferees.xml");
                } catch (ParserConfigurationException | SAXException e) {
                    throw new RuntimeException(e);
                } catch (ConcurrentModificationException e) {
                    printText("Complete!");
                }

                for (UserRecord userRecord : conferenceData.getStruct()) {
                    registerUser(userRecord);
                }
            });

            saveBtn.addActionListener(l -> {
                confData.exportToXML("Conferees.xml");
                JOptionPane.showMessageDialog(jFrame, "XML saved");
            });

            startBtn.addActionListener(l -> {
                try {
                    if (registry == null) {
                        registry = LocateRegistry.createRegistry(Integer.parseInt(Port.getText()));
                    }
                    String name = "register";
                    RegTask service = new RegImpl();
                    registry.rebind(name, service);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                JOptionPane.showMessageDialog(jFrame, "The Conference Server has started!");
                printText("The Server has started!");
            });

            exitBtn.addActionListener(l -> {
                JOptionPane.showMessageDialog(jFrame, "The Conference Server will close");
                System.exit(0);
            });

            stopBtn.addActionListener(l -> {
                try {
                    registry.unbind("register");
                } catch (RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(1, 6, 10, 10));

        JLabel textElement = new JLabel("host: ");
        JLabel ip = new JLabel("localhost");
        JLabel textElementl2 = new JLabel("port: ");
        TextField l2 = new TextField("1049");
        l2.setEnabled(true);
        Port = l2;
        JLabel textElementl3 = new JLabel("participating: ");
        l3 = new JLabel("0");
        {
            dataPanel.add(textElement);
            dataPanel.add(ip);
            dataPanel.add(textElementl2);
            dataPanel.add(l2);
            dataPanel.add(textElementl3);
            dataPanel.add(l3);
        }
        textField = new TextArea("");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(Color.yellow);
        scrollPane.add(textField);

        jFrame.setLayout(new BorderLayout());
        jFrame.add(scrollPane, BorderLayout.CENTER);
        jFrame.add(btnPanel, BorderLayout.SOUTH);
        jFrame.add(dataPanel, BorderLayout.NORTH);

        jFrame.repaint();
        jFrame.revalidate();
    }

    private void printText(String txt) {
        textField.setText(textField.getText() + "\n" + txt);
    }

    public void registerUser(UserRecord userRecord) {
        assert userRecord != null;
        confData.addUser(userRecord);
        textField.setText(textField.getText() + "\n" + userRecord);
        l3.setText(String.valueOf(1 + Integer.parseInt(l3.getText())));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConferenceServer::getInstance);
    }

    public static ConferenceServer getInstance() {
        return instance;
    }
}