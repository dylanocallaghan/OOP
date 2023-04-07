/*
Name: Dylan O'Callaghan
Student Number: C21752815
Project: Assignment for OOP, My Search Engine
Description: Create a program where a user Uses a GUI to search 
			 through a set of text file contents.
*/

package com.A1.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyGUI extends JFrame implements ActionListener {
    private JTextField textField;
    private JTextArea textArea;
    private JTextArea fileListArea;

    public static void main(String[] args) {
        MyGUI gui = new MyGUI();
        gui.setVisible(true);
    }

 // Create a GUI for a file manager application 
    public MyGUI() {
        super("File Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        // Create text box
        JPanel textBoxPanel = new JPanel();
        JLabel textBoxLabel = new JLabel("Enter file name:");
        textField = new JTextField(20);
        textBoxPanel.add(textBoxLabel);
        textBoxPanel.add(textField);

        // Create text area for search results
        textArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Create text area for list
        fileListArea = new JTextArea(10, 30);
        JScrollPane fileListScrollPane = new JScrollPane(fileListArea);

        // Create file button
        JButton createFileButton = new JButton("Create file");
        createFileButton.addActionListener(this);

        // Create search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        // Create delete file button
        JButton deleteFileButton = new JButton("Delete file");
        deleteFileButton.addActionListener(this);

        // Add components to window
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(createFileButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteFileButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textBoxPanel, BorderLayout.NORTH);

        JPanel textAreasPanel = new JPanel(new GridLayout(1, 2));
        textAreasPanel.add(scrollPane);
        textAreasPanel.add(fileListScrollPane);

        mainPanel.add(textAreasPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateFileListArea();
    }

    // Shows the files in available for search
    private void updateFileListArea() {
        File directory = new File(".");
        String[] txtFiles = directory.list((dir, name) -> name.endsWith(".txt"));
        if (txtFiles.length == 0) {
            fileListArea.setText("No text files found in current directory.");
        } else {
            StringBuilder fileListBuilder = new StringBuilder( 
            	 "Try searching from first-fifth" + "\n" + "You can also used multi word search"
            	+ "\n" + "for example try searching 'the first - the fifth'"
            	+ "\n" + "Text files available to be searched:");
            for (String txtFile : txtFiles) {
                fileListBuilder.append("\n").append(txtFile);
            }
            fileListArea.setText(fileListBuilder.toString());
        }
    }
// create file button press
    public void actionPerformed(ActionEvent e) {
        String fileName = textField.getText();
        if (e.getActionCommand().equals("Create file")) {
        	// Makes sure name includes ".txt"
            if (!fileName.endsWith(".txt")) {
                JOptionPane.showMessageDialog(this, "Your file name must include .txt");
            } else {
            	// Create a new file with the name provided
                File file = new File(fileName);
                try {
                FileWriter writer = new FileWriter(file);
                // put it in the text area
                writer.write(textArea.getText());
                writer.close();
                // display success message
                JOptionPane.showMessageDialog(this, "File created successfully.");
                
                // Update file list area
                updateFileListArea();
                
                // if there is an error it will display this message
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Type file name above.");
            }
            }
            // if search button pressed
        } else if (e.getActionCommand().equals("Search")) {
            String searchWord = JOptionPane.showInputDialog(this, "Enter a word to search for:");
            
            File directory = new File(".");
            // make an array of the files that ends with ".txt"
            String[] txtFiles = directory.list((dir, name) -> name.endsWith(".txt"));
            // if no files display this message
            if (txtFiles.length == 0) {
                JOptionPane.showMessageDialog(this, "No text files found in current directory.");
            } else {
                boolean wordFound = false;
                // loop through .txt files in the array
                for (String txtFile : txtFiles) {
                    File file = new File(txtFile);
                    try {
                        java.util.Scanner scanner = new java.util.Scanner(file);
                        // use scanner to go through each line
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line.toLowerCase().contains(searchWord.toLowerCase())) {
                                textArea.setText("The word is present in these files:" + "\n" + txtFile + "\n");
                                wordFound = true;
                                
                             // Add code to show the contents of the file
                                textArea.append("File Contents: \n");
                                java.util.Scanner fileScanner = new java.util.Scanner(file);
                                // loop goes through each line
                                while (fileScanner.hasNextLine()) {
                                    String fileLine = fileScanner.nextLine();
                                    textArea.append(fileLine + "\n");
                                }
                                // closes the file scanner
                                fileScanner.close();
                                break;
                            }
                        }
                        // closes the scanner which takes input from the user
                        scanner.close();
                        // if there is an error it displays message
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error reading file.");
                    }
                }
                // if word is not found display message
                if (!wordFound) {
                    JOptionPane.showMessageDialog(this, "The word '" + searchWord + "' was not found in any file.");
                }
            }
            // if delete file is clicked
        } else if (e.getActionCommand().equals("Delete file")) {
            File file = new File(fileName);
            // check if file exists
            if (file.exists()) {
            	// gets response from user about deleting file
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this file?");
                // if yes then remove file
                if (response == JOptionPane.YES_OPTION) {
                    file.delete();
                    // show message when removed
                    JOptionPane.showMessageDialog(this, "File deleted successfully.");
                    
                    // Update file list area
                    updateFileListArea();
                    
                }
                // if file not found give message
            } else {
                JOptionPane.showMessageDialog(this, "File not found.");
            }
        }
    }
}
