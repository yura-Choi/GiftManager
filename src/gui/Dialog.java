package gui;

import controller.Resource;
import db.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class Dialog {
	private int id = -1;
	private JComboBox<String> type = new JComboBox<String>();
	private TextField companyName = new TextField(30);
	private TextField name = new TextField(30);
	private TextField expiredDate = new TextField("0000-00-00", 30);
	private TextField storedLocation = new TextField(30);
	private JButton submitButton = new JButton("create");
	
	private JDialog dialog = null;
	
	Dialog(){
		dialog = createDialogFrame();
		dialog.setTitle("기프티콘 추가");
	}
	
	Dialog(Item item){
		dialog = createDialogFrame();
		dialog.setTitle("기프티콘 정보 수정");
		submitButton.setText("modify");
		
		setDialogField(item);
	}
	
	private void setDialogField(Item item) {
		String[] data = item.getData();
		
		id = Integer.parseInt(data[0]);
		type.setSelectedItem(data[1]);
		companyName.setText(data[2]);
		name.setText(data[3]);
		expiredDate.setText(data[4]);
		storedLocation.setText(data[5]);
	}
	
	class EventHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			if(button.getText().equals("create")) {
				boolean result = validateItems();
				if(result) {
					Item item = new Item(-1,
							String.valueOf(type.getSelectedItem()),
							companyName.getText(),
							name.getText(),
							expiredDate.getText(),
							storedLocation.getText());

					dialog.setVisible(false);
					Resource.gui.sendFormDataToController("add", item);
				}
				
			} else if(button.getText().equals("modify")) {
				boolean result = validateItems();
				if(result) {
					Item item = new Item(id,
							String.valueOf(type.getSelectedItem()),
							companyName.getText(),
							name.getText(),
							expiredDate.getText(),
							storedLocation.getText());
					
					dialog.setVisible(false);
					Resource.gui.sendFormDataToController("modify", item);	
				
				}
			}
		}
		
	}
	
	private JDialog createDialogFrame() {
		dialog = new JDialog(new JFrame());
		dialog.setSize(400, 400);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		Dimension d;
		
		JPanel panel_type = new JPanel();
		JLabel label_type = new JLabel("유형");
		d = label_type.getPreferredSize();
		label_type.setPreferredSize(new Dimension(d.width+30, d.height));
		panel_type.add(label_type);
		type.setModel(new DefaultComboBoxModel<String>(new String[] {"카페", "디저트", "패스트푸드", "생활", "기타"}));
		panel_type.add(type);
		mainPanel.add(panel_type);
		
		JPanel panel_companyName = new JPanel();
		JLabel label_companyName = new JLabel("상호명");
		d = label_companyName.getPreferredSize();
		label_companyName.setPreferredSize(new Dimension(d.width+20, d.height));
		panel_companyName.add(label_companyName);
		panel_companyName.add(companyName);
		mainPanel.add(panel_companyName);
		
		JPanel panel_name = new JPanel();
		JLabel label_name = new JLabel("이름");
		d = label_name.getPreferredSize();
		label_name.setPreferredSize(new Dimension(d.width+30, d.height));
		panel_name.add(label_name);
		panel_name.add(name);
		mainPanel.add(panel_name);
		
		JPanel panel_expired = new JPanel();
		JLabel label_expired = new JLabel("만료일");
		d = label_expired.getPreferredSize();
		label_expired.setPreferredSize(new Dimension(d.width+20, d.height));
		panel_expired.add(label_expired);
		panel_expired.add(expiredDate);
		mainPanel.add(panel_expired);
		
		JPanel panel_stored = new JPanel();
		JLabel label_stored = new JLabel("저장위치");
		d = label_stored.getPreferredSize();
		label_stored.setPreferredSize(new Dimension(d.width+12, d.height));
		panel_stored.add(label_stored);
		panel_stored.add(storedLocation);
		mainPanel.add(panel_stored);
		
		JPanel panel_button = new JPanel();
		panel_button.add(submitButton);
		mainPanel.add(panel_button);
		
		dialog.setLocationRelativeTo(null);
		dialog.add(mainPanel);

		submitButton.addActionListener(new EventHandler());
		
		return dialog;
	}

	JDialog getDialog() {
		return this.dialog;
	}
	
	private boolean validateItems() {
		if(companyName.getText().equals("") || name.getText().equals("") || expiredDate.getText().equals("") || storedLocation.getText().equals("")) {
			messagePopUp("모든 항목을 입력하세요");
			return false;
		}

		String numberPattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
		if(!Pattern.matches(numberPattern, expiredDate.getText())) {
			messagePopUp("만료일을 0000-00-00 형식으로 입력해주세요");
			return false;
		}
		
		return true;
	}
	
	private void messagePopUp(String message) {
		JOptionPane.showMessageDialog(dialog, message, "알림", JOptionPane.ERROR_MESSAGE);
	}
}



