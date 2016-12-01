package dataImporter;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import shared.modelClasses.Project;
import shared.modelClasses.User;

/**
 * @author hartley9
 *IndexerData takes a root Element that was created by the DataImporter form an XML file. 
 *The IndexerData then creates an array of users and projects that contain modelClass objects of their respective types.
 *Using these arrays we create the sequelStatements to create tables
 */
public class IndexerData {

	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Project> projects = new ArrayList<Project>();
	
//Creates the tree-like data structure using the Records.xml
	public IndexerData(Element root){

		NodeList userElements = root.getElementsByTagName("user");
		for(int i=0; i<userElements.getLength();i++) {
			users.add(new User((Element)userElements.item(i)));
		}
		
		NodeList projectElements = root.getElementsByTagName("project");
		for(int i=0;i<projectElements.getLength();i++){
			projects.add(new Project((Element)projectElements.item(i)));
		}
		
	}

	
	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}
	
}
