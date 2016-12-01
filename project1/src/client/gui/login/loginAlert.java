package client.gui.login;

import javax.swing.JOptionPane;

public class loginAlert extends JOptionPane {
	
	loginAlert(String username, int numberof ){
		JOptionPane.showMessageDialog(this, "Welcome, "+username+". "
				+ "You have indexed "+Integer.toString(numberof)
				+" records.","Welcome to Indexer",JOptionPane.PLAIN_MESSAGE);
	}
		
}