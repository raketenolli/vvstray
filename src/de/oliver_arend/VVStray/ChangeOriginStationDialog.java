package de.oliver_arend.VVStray;

import java.awt.Button;
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
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
//import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.GlazedLists;

public class ChangeOriginStationDialog {
    private static Frame frame;
    private JComboBox<Station> originStationDropdown;
    private VVStray parent;
	
    public ChangeOriginStationDialog(VVStray parent) {
    	this.parent = parent;
    	
    	frame = new Frame("Change mode of transport preferences");
    	frame.setType(Type.UTILITY);
    	
    	Panel panelInput = new Panel(new FlowLayout(FlowLayout.CENTER));
    	Panel panelButtons = new Panel(new FlowLayout(FlowLayout.CENTER));
        
    	frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
    	
        panelInput.add(new Label("Choose departure station: "));
        
        originStationDropdown = new JComboBox<>(new Station[0]);
        originStationDropdown.setEditable(true);
        originStationDropdown.setPreferredSize(new Dimension(360, originStationDropdown.getPreferredSize().height));
        
        AutoCompleteSupport.install(originStationDropdown, GlazedLists.eventList(parent.getStationsArrayList()));

        panelInput.add(originStationDropdown); 	

    	Button OK = new Button("OK");
        Button cancel = new Button("Cancel");

        OK.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {
            	// do something with parent after clicking OK!
//            	parent.update();
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
        frame.setLocation(mousePosition.x - frame.getWidth(), mousePosition.y - frame.getHeight());
        frame.setVisible(true);
    }
    
    public void close() {
    	frame.setVisible(false);
    }

}
