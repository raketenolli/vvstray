package de.oliver_arend.VVStray;

import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;

public class ChangeDelayDialog {
    private static Frame frame;  
    
    public static boolean isNumeric(String str)  
    {  
      try { int i = Integer.parseInt(str); }  
      catch(NumberFormatException nfe) { return false; }  
      return true;  
    }

    public ChangeDelayDialog(VVStray parent) {  
    	frame = new Frame("Change walking time");
    	
    	Panel panelDescription = new Panel(new FlowLayout(FlowLayout.CENTER));
    	Panel panelInput = new Panel(new FlowLayout(FlowLayout.CENTER));
    	Panel panelButtons = new Panel(new FlowLayout(FlowLayout.CENTER));
        
    	frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));  

        panelDescription.add(new Label("Walking time from where you are to your departure stop: "));

        TextField delayField = new TextField(2);
        panelInput.add(delayField);
        panelInput.add(new Label("minutes"));

        Button OK = new Button("OK");
        Button cancel = new Button("Cancel");

        OK.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
            	String delayText = delayField.getText();
                if(isNumeric(delayText)) {
            		parent.setDelay(Integer.parseInt(delayText));
            		parent.update();
                	close();
                } else {
                	delayField.setText("");
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
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        frame.setLocation(mousePosition.x - frame.getWidth(), mousePosition.y - frame.getHeight());
        frame.setVisible(true);
    }
    
    public void close() {
    	frame.setVisible(false);
    }

}
