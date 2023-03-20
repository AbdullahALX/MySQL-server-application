import javax.swing.*;


/*
Name: Abdullah AL Hinaey
Course: CNT 4714 Spring 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: March 9, 2023
Class: CNT4714

 */

public class DBmain {

    public static void main(String[] args) {


        JFrame frame = new JFrame("SQL Client App- (ALH-Spring 2023-Project 3)");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setResizable(false);
        frame.getContentPane().add(new myFrame());

        frame.setVisible(true);






    }
}
