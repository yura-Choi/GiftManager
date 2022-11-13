package db;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Item {
	private int id = -1;
	private String type = null;
	private String companyName = null;
	private String name = null;
	private String expiredDate = null;
	private String storedLocation = null;
	
	
	public Item(int id, String type, String companyName, String name, String expiredDate, String storedLocation){
		this.id = id;
		this.type = type;
		this.companyName = companyName;
		this.name = name;
		this.expiredDate = expiredDate;
		this.storedLocation = storedLocation;
	}

	public String[] getData() {
		String[] data = new String[6];
		
		data[0] = Integer.toString(id);
		data[1] = type;
		data[2] = companyName;
		data[3] = name;
		data[4] = expiredDate;
		data[5] = storedLocation;
		
		return data;
	}

	public void setData(String[] data) {
		id = Integer.parseInt(data[0]);
		type = data[1];
		companyName = data[2];
		name = data[3];
		expiredDate = data[4];
		storedLocation = data[5];
	}
}
