package TTT;


import TCP.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

class MessageSender extends JFrame implements ActionListener, Runnable {
    public final static int PORT = 7331;
    private DatagramSocket sock;
    private String hostname;
    //Component
    boolean first;
    JPanel panel;
    JPanel controlPanel;
    JButton A[][] = new JButton[3][3], newgame, exit;
    boolean tick[][] = new boolean[3][3];
    int ctr, p1, p2;
    JLabel player1, player2;
    ImageIcon cross, zero, myicon, enemyicon;
    public void setMyIcon(ImageIcon img) {
        myicon = img;
    }
    public void setEnemyIcon(ImageIcon img) {
        this.enemyicon = img;
    }
    public ImageIcon getMyIcon() {
        return this.myicon;
    }
    public ImageIcon getEnemyIcon() {
        return this.enemyicon;
    }
    public void setFirst(boolean b) {
        this.first = b;
    }
    public ImageIcon getCross() {
        return this.cross;
    }
    public ImageIcon getZero() {
        return this.zero;
    }
    MessageSender(DatagramSocket s, String h) throws Exception {
        sock = s;
        hostname = h;
        //Game interface
        this.setTitle("Tic Tac Toe PvP Online");
        this.getContentPane();
        this.setLayout(new BorderLayout());
        panel = new JPanel(new GridLayout(3, 3));
        controlPanel = new JPanel(new GridLayout(2, 2));
        this.add(panel, "Center");
        for (int i = 0;i < 3;i++) {
            for (int j = 0;j < 3;j++) {
                A[i][j] = new JButton(new ImageIcon(""));
                A[i][j].setSize(10, 10);
                A[i][j].setBorder(null);
                panel.add(A[i][j]);
                A[i][j].addActionListener(this);
                tick[i][j] = false;
            }
        }
        sendMessage("New Player");
        ctr = 0;
        cross = new ImageIcon("F:\\IT\\Java\\Networking\\crossmark.png");
        zero = new ImageIcon("F:\\IT\\Java\\Networking\\checkmark.png");
        p1 = 0;
        p2 = 0;
        player1 = new JLabel("Player 1: " + p1);
        player2 = new JLabel("Player 2: " + p2);
        newgame = new JButton("New Game");
        newgame.addActionListener(this);
        controlPanel.add(newgame);
        exit = new JButton("Quit Game");
        exit.addActionListener(this);
        controlPanel.add(exit);
        controlPanel.add(player1);
        controlPanel.add(player2);
        this.add(controlPanel, "North");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 370);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    private void sendMessage(String s) throws Exception {
        byte buf[] = s.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        sock.send(packet);
    }
    public void run() {
        boolean connected = false;
        do {
            try {
                sendMessage("Connecting...");
                connected = true;
            } catch (Exception e) {
                
            }
        } while (!connected);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                while (!in.ready()) {
                    Thread.sleep(100);
                }
                sendMessage(in.readLine());
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
    public boolean checkRow() {
        for (int i = 0;i < 3;i++) {
            Icon icon = A[i][0].getIcon();
            if ((A[i][1].getIcon().equals(icon)) && (A[i][2].getIcon().equals(icon))) {
                return true;
            }
        }
        return false;
    }
    public boolean checkColumn() {
        for (int i = 0;i < 3;i++) {
            Icon icon = A[0][i].getIcon();
            if ((A[1][i].getIcon().equals(icon)) && (A[2][i].getIcon().equals(icon))) {
                return true;
            }
        }
        return false;
    }
    public boolean checkCross() {
        Icon icon = A[1][1].getIcon();
        return ((A[0][0].getIcon().equals(icon)) && A[2][2].getIcon().equals(icon)) || ((A[0][2].getIcon().equals(icon)) && (A[2][0].getIcon().equals(icon)));
    }
    public boolean isDraw() {
        return ctr == 9;
    }
    public void rematch() {
        this.ctr = 0;
        for(int i = 0;i < 3;i++)
            for (int j = 0;j < 3;j++) {
                tick[i][j] = false;
                A[i][j].setIcon(new ImageIcon(""));
            }
    }
    public void isOver() throws Exception {
        //Check row, column
        if (checkRow() || checkColumn() || checkCross()) {
            if (first) {
                JOptionPane.showMessageDialog(null, "You losed!", "Game Over!", -1);
                player2.setText("Player 2: " + ++p2);
                first = false;
                rematch();
            }
            else {
                JOptionPane.showMessageDialog(null, "You win!", "Game Over!", -1);
                player1.setText("Player 1: " + ++p1);
                sendMessage("I won!");
                first = true;
                rematch();
            }
            
        }
        else if (isDraw()) {
            JOptionPane.showMessageDialog(null, "TIE!", "Game Over!", 1);
        }
    }
    public void fill(int i, int j) throws Exception {
        if (!tick[i][j]) {
            //Nếu First thì tới lượt
            if (first) {
                A[i][j].setIcon(this.getMyIcon());
                sendMessage(i + "" + j);
            }
            else {
                A[i][j].setIcon(this.getEnemyIcon());
            }
            tick[i][j] = true;
            ctr++;
            first = !first;
        }
        isOver();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newgame) {
            rematch();
        }
        else if (e.getSource() == exit) {
            int k = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Warning!", 0);
            if (k == 0) {
                this.dispose();
            }
        }
        else {
            JButton buttonPressed = (JButton) e.getSource();
            for (int i = 0;i < 3;i++) {
                for (int j = 0;j < 3;j++)
                    if (buttonPressed.equals(A[i][j])) {
                    try {
                        //Toa do o duoc chon
                        //System.out.println("(" + i + ", " + j + ")");
                        if(first) fill(i, j);
                        //sendMessage(i + ", " + j);
                        break;
                    } catch (Exception ex) {
                        Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
            }
        }
    }
}
class MessageReceiver implements Runnable {
    DatagramSocket sock;
    byte buf[];
    MessageSender messageSender;
    MessageReceiver(DatagramSocket s, MessageSender ms) {
        sock = s;
        messageSender = ms;
        buf = new byte[1024];
    }
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                sock.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
                if (received.trim().equals("You first")) {
                    messageSender.setFirst(true);
                    messageSender.setMyIcon(messageSender.getZero());
                    messageSender.setEnemyIcon(messageSender.getCross());
                }
                else if (received.trim().equals("Not you")) {
                    messageSender.setFirst(false);
                    messageSender.setMyIcon(messageSender.getCross());
                    messageSender.setEnemyIcon(messageSender.getZero());
                }
                else if (received.trim().length() == 2){
                    int i = received.charAt(0) - 48;
                    int j = received.charAt(1) - 48;
                    messageSender.fill(i, j);
                }
                
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
}
public class ChatClient {
    
    public static void main(String args[]) throws Exception {
        String host = "localhost";
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        DatagramSocket socket = new DatagramSocket();
        MessageSender s = new MessageSender(socket, host);
        MessageReceiver r = new MessageReceiver(socket, s);
        Thread rt = new Thread(r);
        Thread st = new Thread(s);
        rt.start();
        st.start();
    }
}
