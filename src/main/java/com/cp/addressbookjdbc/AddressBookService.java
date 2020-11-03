package com.cp.addressbookjdbc;

import java.util.List;
import java.util.Map;

public class AddressBookService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	private List<AddressBookData> addBookList;
	private AddressBookDBService addBookDB;
	
	public AddressBookService() {
		addBookDB = AddressBookDBService.getInstance();
	}
	
	public AddressBookService(List<AddressBookData> addBookList) {
		this();
		this.addBookList = addBookList;
	}
	
	public List<AddressBookData> readAddresBookData(IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			this.addBookList = addBookDB.readData();
		}
		return this.addBookList;
	}

	public void updateContactsCity(String firstName, String city) {
		int result = addBookDB.updateData(firstName, city);
		if(result == 0)	return;
		AddressBookData addBookData = this.checkAddressBookDataInSyncWithDB(firstName);
		if(addBookData != null)	addBookData.city = city;
	}

	public AddressBookData checkAddressBookDataInSyncWithDB(String firstName) {
		List<AddressBookData> addBookDataList = addBookDB.getAddressBookData(firstName);
		return addBookDataList.stream()
				  .filter(con -> con.firstName.equals(firstName))
				  .findFirst()
				  .orElse(null);
	}
	
	public Map<String, Integer> readCountContactsByCity(IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			return addBookDB.getCountByCity();
		}
		return null;
	}

	public Map<String, Integer> readCountContactsByState(IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			return addBookDB.getCountByState();
		}
		return null;
	}
	
	public void addContactToBook(String firstName, String lastName, String  address, String city, String state, String zipcode, String phone, String email) {
		addBookList.add(addBookDB.addContactToBook(firstName, lastName, address, city, state, zipcode, phone, email));
	}
}
