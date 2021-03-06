package de.oliver_arend.VVStray;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;

public class ChangeOriginStationDialog {
    private static JFrame frame;
    private JComboBox<Station> originStationDropdown;
	
    public ChangeOriginStationDialog() {
    	frame = new JFrame("");
    	frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));  
    	frame.setIconImage(new ImageIcon("resources/vvslogo_16x16.png").getImage());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		    	
    	JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.CENTER));
    	JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        panelInput.add(new JLabel("Choose departure station: "));
        
        originStationDropdown = new JComboBox<>(new Station[0]);
        originStationDropdown.setEditable(true);
        originStationDropdown.setPreferredSize(new Dimension(360, originStationDropdown.getPreferredSize().height));
        
        EventList<Station> stations = GlazedLists.eventList(StationDataProvider.getStationsArrayList());
        SortedList<Station> sortedStations = new SortedList<Station>(stations);
        AutoCompleteSupport autocomplete = AutoCompleteSupport.install(originStationDropdown, sortedStations, new StationTextFilterator());

        panelInput.add(originStationDropdown);

    	JButton OK = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        OK.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {
            	UserSettings u = UserSettingsProvider.getUserSettings();
            	u.setOriginStation((Station)originStationDropdown.getSelectedItem());
            	UserSettingsProvider.setUserSettings(u);
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
