import javax.swing.*;
import java.awt.*;

public class RobotRadarGUI extends Thread {

        public static void createGUI(){
            //This is the container for the base interface without the panels yet.
            JFrame j = new JFrame(); //Set up of the initial container where everything will lay.
            j.setTitle("Robot Radar");//Sets the title of the container
            j.setSize(900,600);//Sets the size of the container
            j.setLayout(null); //Not using a layout manager as of now.
            j.setVisible(true); //Sets whether or not we can see the GUI.

            //Buttons for stop and reset
            JButton b1 = new JButton("Halt"); //Halt
            JButton b2 = new JButton("Reset"); //Reset Scan

            b1.setBounds(500,500,80,20);//Coordinates and size of the reset button.
            b2.setBounds(700,500, 80, 20);

            j.add(b1);
            j.add(b2);
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
            j.setVisible(true);



        }

       public void UpdateInterfaceInformation(Dot DotList[]) {

       }

        public static void main(String[] args){
            createGUI();
        }

        }


