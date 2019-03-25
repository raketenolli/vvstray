package de.oliver_arend.VVStray;

import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

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
		alerts.forEach((alert) -> {
			JPanel alertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			alertPanel.add(new JLabel(alert, new ImageIcon("resources/alert.png"), SwingConstants.LEFT));
			this.add(alertPanel);
		});
		if(alerts.size() > 0 && transfers.size() > 0) {
			this.add(new JSeparator(SwingConstants.HORIZONTAL));
		}
		transfers.forEach((transfer) -> {
			JPanel transferPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			transferPanel.add(new JLabel(transfer, new ImageIcon("resources/transfer.png"), SwingConstants.LEFT));
			this.add(transferPanel);
		});
		this.pack();
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        this.setLocation(mousePosition.x - this.getWidth(), mousePosition.y - this.getHeight() - 32);
        this.setVisible(true);
	}

}
