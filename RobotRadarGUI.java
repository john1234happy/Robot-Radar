import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class RobotRadarGUI extends Thread implements Runnable, ActionListener, KeyListener {



    public void createGUI(RobotManager r) throws Exception {
            //This is the container for the base interface without the panels yet.
            JFrame j = new JFrame(); //Set up of the initial container where everything will lay.
            j.setTitle("Robot Radar");//Sets the title of the container
            j.setSize(900,800);//Sets the size of the container
            j.setLayout(null); //Not using a layout manager as of now.
            j.setVisible(true); //Sets whether or not we can see the GUI.

            //Centers the GUI
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) ((dimension.getWidth() - j.getWidth()) / 2);
            int y = (int) ((dimension.getHeight() - j.getHeight()) / 2);
            j.setLocation(x, y);

            //Buttons for stop and reset
            JButton b1 = new JButton("Halt"); //Halt
            JButton b2 = new JButton("Reset"); //Reset Scan
            JButton b3 = new JButton("Connect"); //Connect to server
            JButton b4 = new JButton("Scan");

            b1.setBounds(500,500,80,20);
            b2.setBounds(700,500, 80, 20);//Coordinates and size of the reset button.
            b3.setBounds(600,600,90,20);
            b4.setBounds(600,500, 80,20);

            //Action Listening
            b1.addActionListener(this);
            b2.addActionListener(this);
            b3.addActionListener(this);
            b4.addActionListener(this);

            j.add(b1);
            j.add(b2);
            j.add(b3);
            j.add(b4);
            j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit on close

            //Paneling part
            JPanel p1 = new JPanel(); // Radar Panel
            JPanel p2 = new JPanel();// Camera Panel
            p1.setBounds(500,100,300,300);
            p1.setBackground(Color.darkGray);


            p2.setBounds(50,100,300,300);
            p2.setBackground(Color.darkGray);

            j.add(p1);
            j.add(p2);

            //Slider
            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            slider.setBounds(500,450,300,50);
            slider.setMinorTickSpacing(2);
            slider.setMajorTickSpacing(10);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            j.add(slider);


        //Text area of commands for robot
            JLabel label = new JLabel("List of Commands:");
            label.setBounds(100,380, 200,200);
            JLabel label1 = new JLabel("Move Forward --> w");
            label1.setBounds(100,400, 200,200);
            JLabel label2 = new JLabel("Move Backward --> s");
            label2.setBounds(100,420, 200,200);
            JLabel label3 = new JLabel("Move Right --> d");
            label3.setBounds(100,440, 200,200);
            JLabel label4 = new JLabel("Move Left --> a");
            label4.setBounds(100,460, 200,200);
            JLabel label5 = new JLabel("Turn Head Right --> Right Arrow Key");
            label5.setBounds(100,480, 300,200);
            JLabel label6 = new JLabel("Turn Head Left --> Left Arrow Key");
            label6.setBounds(100,500, 300,200);

            j.add(label);
            j.add(label1);
            j.add(label2);
            j.add(label3);
            j.add(label4);
            j.add(label5);
            j.add(label6);

            j.setVisible(true);




        }
    private final Set<Integer> pressedKeys = new HashSet<>(); //For storing what keys were pressed

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        //Not used
    }

    @Override
    public synchronized void keyPressed(KeyEvent keyEvent) { //This is supposed to check if buttons are being pressed at the same time but I'm not sure if it's correct.
        pressedKeys.add(keyEvent.getKeyCode());
        Point o = new Point();
        if(!pressedKeys.isEmpty()){
            for(Iterator<Integer> i = pressedKeys.iterator(); i.hasNext();){
                switch(i.next()){
                    case KeyEvent.VK_ALT:
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_S:
                        o.y = -1;
                        break;

                }
            }
        }


    }

    @Override
    public synchronized void keyReleased(KeyEvent keyEvent) {
            pressedKeys.remove(keyEvent.getKeyCode());
    }

    public void UpdateInterfaceInformation(Dot DotList[]) {

    }


    @Override
    public void run() {
        RobotManager r = new RobotManager();
        try {
            createGUI(r);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




//Testing
public static void main(String[] args){
    new Thread(new RobotRadarGUI()).start();
}}

