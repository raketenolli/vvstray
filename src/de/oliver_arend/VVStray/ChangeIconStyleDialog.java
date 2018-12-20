package de.oliver_arend.VVStray;

import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class ChangeIconStyleDialog {
    private static JFrame frame;
    private ButtonGroup iconStyleRadio;
    private JRadioButton colorRadio;
    private JRadioButton windows10Radio;
    
    public ChangeIconStyleDialog() {
    	frame = new JFrame("");
    	frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));  
    	frame.setIconImage(new ImageIcon("resources/vvslogo_16x16.png").getImage());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    	
    	JPanel panelDescription = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	JPanel panelInputTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel panelInputBottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panelDescription.add(new JLabel("Choose your preferred icon style: "));

        colorRadio = new JRadioButton("");
        JLabel colorRadioLabel = new JLabel("Color", new ImageIcon("resources/coloricon.png"), SwingConstants.LEFT);
        colorRadioLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        windows10Radio = new JRadioButton("");
        JLabel windows10RadioLabel = new JLabel("Windows 10", new ImageIcon("resources/windows10icon.png"), SwingConstants.LEFT);
        windows10RadioLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        
        iconStyleRadio = new ButtonGroup();
        iconStyleRadio.add(colorRadio);
        iconStyleRadio.add(windows10Radio);

        panelInputTop.add(Box.createHorizontalStrut(22));
        panelInputTop.add(colorRadio);
        panelInputTop.add(colorRadioLabel);
        panelInputBottom.add(Box.createHorizontalStrut(22));
        panelInputBottom.add(windows10Radio);
        panelInputBottom.add(windows10RadioLabel);

        JButton OK = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        OK.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {
            	UserSettings u = UserSettingsProvider.getUserSettings();
            	if(colorRadio.isSelected()) { u.setIconStyle(IconStyle.COLOR); }
            	else { u.setIconStyle(IconStyle.WINDOWS10); }
            	UserSettingsProvider.setUserSettings(u, this);
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
    	IconStyle chosenIconStyle = UserSettingsProvider.getUserSettings().getIconStyle();
    	if(chosenIconStyle == IconStyle.COLOR) { colorRadio.setSelected(true); }
    	else { windows10Radio.setSelected(true); }
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        frame.setLocation(mousePosition.x - frame.getWidth(), mousePosition.y - frame.getHeight());
        frame.setVisible(true);
    }
    
    public void close() {
    	frame.setVisible(false);
    }

}
