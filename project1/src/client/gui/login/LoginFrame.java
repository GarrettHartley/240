package client.gui.login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ClientFacade;
import shared.communicationClasses.ValidateUserParam;
import shared.communicationClasses.ValidateUserResult;

public class LoginFrame extends JFrame  {


	JButton login = new JButton("Login");
	JPasswordField passwordTextField = new JPasswordField(25);
	JTextField nameTextField = new JTextField(25);
	static JFrame self;
	static ClientFacade clientFacade;

	public  JFrame getLoginFrame() {
		return this;
	}


	public  JTextField getPasswordTextField() {
		return passwordTextField;
	}

	public void setPasswordTextField(JPasswordField passwordTextField) {
		this.passwordTextField = passwordTextField;
	}

	public  JTextField getNameTextField() {
		return nameTextField;
	}

	public  void setNameTextField(JTextField nameTextField) {
		this.nameTextField = nameTextField;
	}

	public LoginFrame(ClientFacade clientFacade){
		this.clientFacade = clientFacade;
		login.addActionListener(al);
		self = this;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Name Panel
		JLabel name = new JLabel("Name: ");
		name.setPreferredSize(new Dimension(100,20));
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(name, BorderLayout.LINE_START);
		namePanel.add(nameTextField, BorderLayout.CENTER);

		// Password Panel		
		JLabel password = new JLabel("Password: ");
		password.setPreferredSize(new Dimension(100,20));
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new BorderLayout());
		passwordPanel.add(password, BorderLayout.LINE_START);
		passwordPanel.add(passwordTextField,BorderLayout.CENTER);

		// Username & Password Panel
		JPanel usernamePassPanel = new JPanel();
		usernamePassPanel.setLayout(new BorderLayout());
		usernamePassPanel.add(passwordPanel, BorderLayout.CENTER);
		usernamePassPanel.add(namePanel,BorderLayout.NORTH);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		JButton close = new JButton("Close");

		close.addActionListener(alClose);

		close.setPreferredSize(new Dimension(50,40));
		close.addActionListener(exitListener);
		login.setPreferredSize(new Dimension(50,40));
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.add(login);
		buttonPanel.add(close);

		JPanel containerPanel = new JPanel();
		JPanel contentPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(usernamePassPanel, BorderLayout.CENTER);
		contentPanel.add(buttonPanel,BorderLayout.SOUTH);
		containerPanel.add(contentPanel, BorderLayout.NORTH);

		this.getContentPane().add(containerPanel,BorderLayout.CENTER);

		this.pack();
		this.setSize(new Dimension(400, 100));
		this.setMaximumSize(new Dimension(400, 100));
		this.setMinimumSize(new Dimension(400, 100));
		this.setResizable(false);
		this.setVisible(true);

	}

	public JButton getLogin() {
		return login;
	}

	public void setLogin(JButton login) {
		this.login = login;
	}

	private ActionListener exitListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			self.dispose();
		}

	};

	private static ActionListener al = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			ValidateUserParam validateUserParam = new ValidateUserParam();
			ValidateUserResult validateUserResult = new ValidateUserResult();
			clientFacade.setPassword(((LoginFrame) self).getPasswordTextField().getText());
			clientFacade.setUser(((LoginFrame) self).getNameTextField().getText());
			if(clientFacade.login()){
				self.dispose();
			}

		}

	};

	private static ActionListener alClose = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			self.dispose();	
		}

	};

}
