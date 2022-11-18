package controller;

import db.Item;

import javax.swing.*;
import java.util.Vector;

public class Controller {
	private JFrame frame = null;
	private JPanel mainPanel = null;
	private JPanel listPanel = null;
	private Vector<String[]> listData = null;
	
	public Controller(){
		frame = new JFrame();
		frame.setTitle("GiftManager");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		getList();
		mainPanel = Resource.gui.getMainPanel();
		listPanel = Resource.gui.getList(listData);
		mainPanel.add(listPanel);
		frame.add(mainPanel);
		
		frame.setSize(500, 600);
		frame.setVisible(true);
	}
	
	// DB request
	private void getList() {
		listData = Resource.db.getList("", true);
	}
	
	public void addItem(Item item) {
		if(!Resource.db.insertItem(item)) {
			JOptionPane.showMessageDialog(frame, "이모티콘 추가에 실패하였습니다. 다시 시도해주세요.");
			return;
		} else {
			repaint();
		}	
	}
	
	public void modifyItem(Item item) {
		if(!Resource.db.modifyItem(item)) {
			JOptionPane.showMessageDialog(frame, "이모티콘 정보 수정에 실패하였습니다. 다시 시도해주세요.");
			return;
		} else {
			repaint();
		}
	}
	
	public void deleteItem(int id) {
		if(!Resource.db.deleteItem(id)) {
			JOptionPane.showMessageDialog(frame, "삭제에 실패했습니다. 다시 시도해주세요.");
		} else {
			repaint();
		}
	}
	
	private void repaint() {
		mainPanel.remove(listPanel);
		listData = Resource.db.getList("", true);
		listPanel=Resource.gui.getList(listData);
		mainPanel.add(listPanel);
		mainPanel.revalidate();
	}
}
