package de.oliver_arend.VVStray;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class ChangeIconStyleDialog {
    private static Frame frame;
    private CheckboxGroup iconStyleRadio;
    private Checkbox colorRadio;
    private Checkbox windows10Radio;
    private VVStray parent;
    
    public ChangeIconStyleDialog(VVStray parent) {
    	this.parent = parent;
    	
    	frame = new Frame("Change preferred icon style");
    	frame.setType(Type.UTILITY);
    	
    	Panel panelDescription = new Panel(new FlowLayout(FlowLayout.CENTER));
    	Panel panelInputTop = new Panel(new FlowLayout(FlowLayout.LEFT));
    	Panel panelInputBottom = new Panel(new FlowLayout(FlowLayout.LEFT));
    	Panel panelButtons = new Panel(new FlowLayout(FlowLayout.CENTER));
        
    	frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));  

        panelDescription.add(new Label("Choose your preferred icon style: "));

        iconStyleRadio = new CheckboxGroup();
        colorRadio = new Checkbox("", iconStyleRadio, false);
        JLabel colorRadioLabel = new JLabel("Color", new ImageIcon("resources/coloricon.png"), SwingConstants.LEFT);
        colorRadioLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        windows10Radio = new Checkbox("", iconStyleRadio, false);
        JLabel windows10RadioLabel = new JLabel("Windows 10", new ImageIcon("resources/windows10icon.png"), SwingConstants.LEFT);
        windows10RadioLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        
        panelInputTop.add(Box.createHorizontalStrut(36));
        panelInputTop.add(colorRadio);
        panelInputTop.add(colorRadioLabel);
        panelInputBottom.add(Box.createHorizontalStrut(36));
        panelInputBottom.add(windows10Radio);
        panelInputBottom.add(windows10RadioLabel);

        Button OK = new Button("OK");
        Button cancel = new Button("Cancel");

        OK.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
//            	String walkingTimeString = walkingTimeField.getText();
//                if(Utils.isNumeric(walkingTimeString)) {
//                	UserSettings u = UserSettingsProvider.getUserSettings();
//                	u.setWalkingTimeToStation(Integer.parseInt(walkingTimeString));
//                	UserSettingsProvider.setUserSettings(u);
//            		parent.update();
//                	close();
//                } else {
//                	walkingTimeField.setText("");
//                }
            	close();
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
        frame.add(panelInputTop);
        frame.add(panelInputBottom);
        frame.add(panelButtons);

        frame.pack();
    }
    
    public void open() {
//    	walkingTimeField.setText(Integer.toString(UserSettingsProvider.getUserSettings().getWalkingTimeToStation()));
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        frame.setLocation(mousePosition.x - frame.getWidth(), mousePosition.y - frame.getHeight());
        frame.setVisible(true);
    }
    
    public void close() {
    	frame.setVisible(false);
    }

}
