package gui;

import controller.Resource;
import db.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;


public class GUI {
	private JPanel mainPanel = null;
	private JTable listTable = null;
	private Dialog dialog = null;
	
	private String selectedItem = "";
	private int selectedRow = -1;
	
	public JPanel getMainPanel() {
		mainPanel = new JPanel();
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
	
	public JPanel getList(Vector<String[]> items) {
		JPanel listPanel = new JPanel();
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
	
	class MenuButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			String buttonType = button.getText();
			if(buttonType.equals("추가")) {
				setDialog("add", new Dialog());
				getDialog("add").setVisible(true);
				
			} else if(buttonType.equals("수정")) {
				if(selectedItem.equals("")) {
					JOptionPane.showMessageDialog(mainPanel, "수정할 항목을 선택해주세요");
					return;
				}
				
				Item data = new Item(Integer.parseInt(selectedItem), 
						(String) listTable.getValueAt(selectedRow, 0),
						(String) listTable.getValueAt(selectedRow, 1),
						(String) listTable.getValueAt(selectedRow, 2),
						(String) listTable.getValueAt(selectedRow, 3),
						(String) listTable.getValueAt(selectedRow, 4));
				
				setDialog("modify", new Dialog(data));
				getDialog("modify").setVisible(true);
				
			} else if(buttonType.equals("삭제")) {
				if(selectedItem.equals("")) {
					JOptionPane.showMessageDialog(mainPanel, "삭제할 항목을 선택해주세요");
					return;
				}
				
				String name = (String) listTable.getValueAt(selectedRow, 1) + " " + (String) listTable.getValueAt(selectedRow, 2);
				int result = JOptionPane.showConfirmDialog(mainPanel, name+" 이 삭제됩니다.");
				if(result == JOptionPane.YES_OPTION) {
					Resource.controller.deleteItem(Integer.parseInt(selectedItem));
					
				} else {
					return;
				}
			}
		}
	}
	
	void setDialog(String type, Dialog dialog) {
		if(type.equals("add")) {
			this.dialog = dialog;
		} else if(type.equals("modify")) {
			this.dialog = dialog;
		}
	}
	JDialog getDialog(String type) {
		if(type.equals("add")) {
			return dialog.getDialog();
		} else if(type.equals("modify")) {
			return dialog.getDialog();
		}
		return null;
	}
	
	public void sendFormDataToController(String type, Item item) {
		if(type == "add") {
			Resource.controller.addItem(item);
		} else {
			Resource.controller.modifyItem(item);
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
}
