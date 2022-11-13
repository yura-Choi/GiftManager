package controller;

import db.DB;
import gui.GUI;

public class Resource {
	public static Controller controller = null;
	public static DB db = null;
	public static GUI gui = null;
	
	Resource(){
		db = new DB();
		gui = new GUI();
		controller = new Controller();
	}
	
	public static void main(String[] args) {
		new Resource();
	}
}
