package de.oliver_arend.VVStray;

import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ChangeTransportPreferencesDialog {
    private static JFrame frame;
    private JCheckBox sBahnCheckbox;
    private JCheckBox uBahnCheckbox;
    private JCheckBox busCheckbox;
    private VVStray parent;
    
    public ChangeTransportPreferencesDialog(VVStray parent) {
    	this.parent = parent;
    	
    	frame = new JFrame("");
    	frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));  
    	frame.setIconImage(new ImageIcon("resources/vvslogo_16x16.png").getImage());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
    	JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        panelInput.add(new JLabel("Use: "));
        sBahnCheckbox = new JCheckBox("S-Bahn");
        uBahnCheckbox = new JCheckBox("U-Bahn");
        busCheckbox = new JCheckBox("Bus");
    	panelInput.add(sBahnCheckbox);
    	panelInput.add(uBahnCheckbox);
    	panelInput.add(busCheckbox);

        JButton OK = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        OK.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
            	UserSettings u = UserSettingsProvider.getUserSettings();
            	u.setUseSBahn(sBahnCheckbox.isSelected());
            	u.setUseUBahn(uBahnCheckbox.isSelected());
            	u.setUseBus(busCheckbox.isSelected());
            	UserSettingsProvider.setUserSettings(u);
            	parent.update();
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
        
        frame.add(panelInput);
        frame.add(panelButtons);

        frame.pack();
    }
    
    public void open() {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        sBahnCheckbox.setSelected(UserSettingsProvider.getUserSettings().isUseSBahn());
        uBahnCheckbox.setSelected(UserSettingsProvider.getUserSettings().isUseUBahn());
        busCheckbox.setSelected(UserSettingsProvider.getUserSettings().isUseBus());
        frame.setLocation(mousePosition.x - frame.getWidth(), mousePosition.y - frame.getHeight());
        frame.setVisible(true);
    }
    
    public void close() {
    	frame.setVisible(false);
    }

}
