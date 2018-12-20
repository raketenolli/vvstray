package de.oliver_arend.VVStray;

import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ChangeWalkingTimeDialog {
    private static JFrame frame;
    private JTextField walkingTimeField;
    
    public ChangeWalkingTimeDialog() {
    	frame = new JFrame("");
    	frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));  
    	frame.setIconImage(new ImageIcon("resources/vvslogo_16x16.png").getImage());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    	
    	JPanel panelDescription = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        panelDescription.add(new JLabel("Walking time from where you are to your departure stop: "));

        walkingTimeField = new JTextField(2);
        panelInput.add(walkingTimeField);
        panelInput.add(new JLabel("minutes"));

        JButton OK = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        OK.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
            	String walkingTimeString = walkingTimeField.getText();
                if(Utils.isNumeric(walkingTimeString)) {
                	UserSettings u = UserSettingsProvider.getUserSettings();
                	u.setWalkingTimeToStation(Integer.parseInt(walkingTimeString));
                	UserSettingsProvider.setUserSettings(u);
                	close();
                } else {
                	walkingTimeField.setText("");
                }
            }  
        });  

        cancel.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                close();
            }  
        });  

        panelButtons.add(OK);   
        panelButtons.add(cancel);
        
        frame.add(panelDescription);
        frame.add(panelInput);
        frame.add(panelButtons);

        frame.pack();
    }
    
    public void open() {
    	walkingTimeField.setText(Integer.toString(UserSettingsProvider.getUserSettings().getWalkingTimeToStation()));
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        frame.setLocation(mousePosition.x - frame.getWidth(), mousePosition.y - frame.getHeight());
        frame.setVisible(true);
    }
    
    public void close() {
    	frame.setVisible(false);
    }

}
