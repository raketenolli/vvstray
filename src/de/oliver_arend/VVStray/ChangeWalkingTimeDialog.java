package de.oliver_arend.VVStray;

import java.awt.*;
import java.awt.Window.Type;
import java.awt.event.*;
import javax.swing.BoxLayout;

public class ChangeWalkingTimeDialog {
    private static Frame frame;
    private TextField walkingTimeField;
    private VVStray parent;
    
    public ChangeWalkingTimeDialog(VVStray parent) {
    	this.parent = parent;
    	
    	frame = new Frame("Change walking time");
    	frame.setType(Type.UTILITY);
    	
    	Panel panelDescription = new Panel(new FlowLayout(FlowLayout.CENTER));
    	Panel panelInput = new Panel(new FlowLayout(FlowLayout.CENTER));
    	Panel panelButtons = new Panel(new FlowLayout(FlowLayout.CENTER));
        
    	frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));  

        panelDescription.add(new Label("Walking time from where you are to your departure stop: "));

        walkingTimeField = new TextField(2);
        panelInput.add(walkingTimeField);
        panelInput.add(new Label("minutes"));

        Button OK = new Button("OK");
        Button cancel = new Button("Cancel");

        OK.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
            	String walkingTimeString = walkingTimeField.getText();
                if(Utils.isNumeric(walkingTimeString)) {
                	UserSettings u = UserSettingsProvider.getUserSettings();
                	u.setWalkingTimeToStation(Integer.parseInt(walkingTimeString));
                	UserSettingsProvider.setUserSettings(u);
            		parent.update();
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
