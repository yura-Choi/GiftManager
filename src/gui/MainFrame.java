package gui;

import db.DB;
import db.Item;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.*;


public class MainFrame{
	private DB db = new DB();
	
	private JFrame frame = null;
	private JPanel mainPanel = null;
	private JTable listTable = null;
	private JPanel listPanel = null;
	private Dialog addDialog = null;
	private Dialog modifyDialog = null;
	
	private String filter = "";
	private String selectedItem = "";
	private int selectedRow = -1;
	
	public MainFrame() {
		frame = new JFrame();
		frame.setTitle("GiftManager");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(1000, 700);
		
		mainPanel = getGUI(filter);
		listPanel = getList();
		mainPanel.add(listPanel);
		frame.add(mainPanel);
		

		frame.pack();
		frame.setVisible(true);
	}
	
	class MenuButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			String buttonType = button.getText();
			if(buttonType.equals("추가")) {
				setDialog("add", new Dialog(frame));
				getDialog("add").setVisible(true);
				
			} else if(buttonType.equals("수정")) {
				if(selectedItem.equals("")) {
					JOptionPane.showMessageDialog(frame, "수정할 항목을 선택해주세요");
					return;
				}
				
				Item data = new Item(Integer.parseInt(selectedItem), 
						(String) listTable.getValueAt(selectedRow, 0),
						(String) listTable.getValueAt(selectedRow, 1),
						(String) listTable.getValueAt(selectedRow, 2),
						(String) listTable.getValueAt(selectedRow, 3),
						(String) listTable.getValueAt(selectedRow, 4));
				
				setDialog("modify", new Dialog(frame, data));
				getDialog("modify").setVisible(true);
				
			} else if(buttonType.equals("삭제")) {
				if(selectedItem.equals("")) {
					JOptionPane.showMessageDialog(frame, "삭제할 항목을 선택해주세요");
					return;
				}
				
				String name = (String) listTable.getValueAt(selectedRow, 1) + " " + (String) listTable.getValueAt(selectedRow, 2);
				int result = JOptionPane.showConfirmDialog(frame, name+" 이 삭제됩니다.");
				if(result == JOptionPane.YES_OPTION) {
					if(!DB.deleteItem(Integer.parseInt(selectedItem))) {
						JOptionPane.showMessageDialog(frame, "삭제에 실패했습니다. 다시 시도해주세요.");
					}
					JOptionPane.showMessageDialog(frame, "삭제했습니다");
				} else {
					return;
				}
				
			}
		}
		
	}
	
	class JTableMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) { 
			JTable jtable = (JTable)e.getSource();
			selectedItem = String.valueOf(jtable.getModel().getValueAt(jtable.getSelectedRow(), 0));
			selectedRow = jtable.getSelectedRow();
			System.out.println("item : " + selectedItem +" | row : " + selectedRow);
			
		}
		public void mousePressed(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
	}
	
	void setDialog(String type, Dialog dialog) {
		if(type.equals("add")) {
			this.addDialog = dialog;
		} else if(type.equals("modify")) {
			this.modifyDialog = dialog;
		}
	}
	JDialog getDialog(String type) {
		if(type.equals("add")) {
			return addDialog.getDialog();
		} else if(type.equals("modify")) {
			return modifyDialog.getDialog();
		}
		return null;
	}

	
	private JPanel getGUI(String filter) {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		JPanel titlePanel = new JPanel();
		JLabel title = new JLabel("GiftManager");
		title.setFont(new Font("Serif", Font.BOLD, 20));
		titlePanel.add(title);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 8));
		JButton addButton = new JButton("추가");
		JButton modifyButton = new JButton("수정");
		JButton deleteButton = new JButton("삭제");
		menuPanel.add(addButton);
		menuPanel.add(modifyButton);
		menuPanel.add(deleteButton);
		addButton.addActionListener(new MenuButtonListener());
		modifyButton.addActionListener(new MenuButtonListener());
		deleteButton.addActionListener(new MenuButtonListener());
		
		mainPanel.add(titlePanel);
		mainPanel.add(menuPanel);
		
		return mainPanel;
	}
	
	private JPanel getList() {
		JPanel listPanel = new JPanel();
		Vector<String[]> items = db.getList(filter, true);
		if(items.size() == 0) {
			listPanel.add(new JLabel("Empty data"));
		} else {
			Vector<String> header_vector = new Vector<String>();
			header_vector.add("id");
			header_vector.add("유형");
			header_vector.add("상호명");
			header_vector.add("이름");
			header_vector.add("만료일");
			header_vector.add("저장위치");
			
			DefaultTableModel table = new DefaultTableModel(header_vector, 0);
			for(int i=0; i<items.size(); i++) {
				table.addRow(items.get(i));
			}
			
			listTable = new JTable(table);
			listTable.removeColumn(listTable.getColumnModel().getColumn(0));
			
			listPanel.add(new JScrollPane(listTable));
		}
		listTable.addMouseListener(new JTableMouseListener());
		
		return listPanel;
	}

	public static void main(String[] args) {
		new MainFrame();
	}
}
