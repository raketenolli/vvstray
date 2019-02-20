package de.oliver_arend.VVStray;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class AlertAndTransferDialog extends JDialog {
	
	public AlertAndTransferDialog(ArrayList<String> alerts, ArrayList<String> transfers) {
		super();
		if(alerts.size() > 0 && transfers.size() > 0) {
			this.setTitle("Traffic alerts and transfer information");
		} else if (alerts.size() > 0) {
			this.setTitle("Traffic alerts");
		} else if (transfers.size() > 0) {
			this.setTitle("Transfer information");
		}
		this.setIconImage(new ImageIcon("resources/vvslogo_16x16.png").getImage());
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		alerts.forEach((alert) -> this.add(new JLabel(alert)));
		this.add(new JLabel(" "));
		transfers.forEach((transfer) -> this.add(new JLabel(transfer)));
		this.pack();
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        this.setLocation(mousePosition.x - this.getWidth(), mousePosition.y - this.getHeight() - 32);
        this.setVisible(true);
	}

}
