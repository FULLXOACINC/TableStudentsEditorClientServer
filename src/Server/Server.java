package Server;


import library.AddComponent;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by alex on 4.4.17.
 */
public class Server {
    private int port = 1337;
    private List<Session> sessionList=new ArrayList<Session>();
    private JTextArea textArea;
    private ServerSocket serverSocket;

    private boolean run = true;

    public Server() {
        JFrame frame = new JFrame("Server Student Table");
        frame.setSize(400,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        textArea.setBackground(Color.decode("#2C001E"));
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);
        frame.add(createMenuPanel(), BorderLayout.PAGE_START);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.setVisible(true);
        frame.add(scrollPane);
    }

    private void runServer() {
        try {
            textArea.append("Run server\n");
            serverSocket = new ServerSocket(port);
            while (run) {
                Socket socket = serverSocket.accept();
                Session session=new Session(socket, this);
                sessionList.add(session);
                new Thread(session).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    JTextArea getTextArea() {
        return textArea;
    }

    boolean getRun() {
        return run;
    }

    private JToolBar createMenuPanel() {
        JToolBar toolBar = new JToolBar();

        toolBar.add(AddComponent.makeButton(new JButton(), "run.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                run=true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runServer();
                    }
                }).start();
            }
        }));
        toolBar.add(AddComponent.makeButton(new JButton(), "stop.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    serverSocket.close();
                    run=false;
                    for (Session session:sessionList)
                        session.stopSession();
                    textArea.append("Stop server\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }));

        return toolBar;

    }
    void log(String text){
        if(run)
            textArea.append(text);
    }

    public static void main(String[] args) {
                new Server();
    }

}
