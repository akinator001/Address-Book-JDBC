package com.cp.addressbookjdbc;

import java.util.List;

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
	
}
