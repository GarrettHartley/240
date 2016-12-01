package client.gui;

import javax.swing.JFrame;

import client.ClientFacade;
import client.gui.indexerWindow.IndexerWindow;
import client.gui.login.LoginFrame;

public class Gui {
	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 39650;
	IndexerWindow indexerWindow;
	static LoginFrame loginFrame;


	public static void main(String[] args) {
		if(args.length>0){
			SERVER_HOST = args[0];
			SERVER_PORT=Integer.parseInt(args[1]);
		}
		ClientFacade clientFacade = new ClientFacade(SERVER_HOST,SERVER_PORT);

		loginFrame = new LoginFrame(clientFacade);

	}

}

