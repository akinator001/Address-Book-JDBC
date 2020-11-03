package com.cp.addressbookjdbc;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.cp.addressbookjdbc.AddressBookService.IOService;

public class AddressBookTest {
	@Test
    public void givenEmpPayrollDataInDB_ShouldMatchEmpCount() {
    	AddressBookService addBookService = new AddressBookService();
    	List<AddressBookData> addBookData = addBookService.readAddresBookData(IOService.DB_IO);
    	Assert.assertEquals(4, addBookData.size());
    }
	
	@Test 
    public void givenNewCity_WhenUpdated_shouldMatchWithDB() {
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	addBookService.updateContactsCity("nik","agra");
    	AddressBookData contact = addBookService.checkAddressBookDataInSyncWithDB("nik");
    	Assert.assertEquals("agra", contact.city);
    }
	
	@Test
    public void givenContactsData_WhenCountByCity_ShouldReturnProperValue() {
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	Map<String, Integer> countContactsByCity = addBookService.readCountContactsByCity(IOService.DB_IO);
    	Assert.assertTrue(countContactsByCity.get("bangalore").equals(1) && countContactsByCity.get("panipat").equals(1) && countContactsByCity.get("greater noida").equals(1) && countContactsByCity.get("agra").equals(1));
    }

	@Test
    public void givenContactsData_WhenCountByState_ShouldReturnProperValue() {
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	Map<String, Integer> countContactsByState = addBookService.readCountContactsByState(IOService.DB_IO);
    	Assert.assertTrue(countContactsByState.get("karnataka").equals(1) && countContactsByState.get("haryana").equals(2) && countContactsByState.get("up").equals(1));
    }
	
	@Test
    public void givenNewContact_WhenAdded_ShouldSyncWithDB() {
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	addBookService.addContactToBook("ankit", "pandey", "ct", "patna", "Bihar", "700055", "9191919191", "ankit@gmail.com");
    	AddressBookData contact = addBookService.checkAddressBookDataInSyncWithDB("ankit");
    	Assert.assertEquals("ankit@gmail.com", contact.email);
    }
}
