package com.cp.addressbookjdbc;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
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
    	addBookService.updateContactsCity("nik", "agra", IOService.DB_IO);
    	boolean result = addBookService.checkAddressBookDataInSyncWithDB("nik");
    	Assert.assertTrue(result);
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
    	addBookService.addContactToBook("ankit","pandey", "ct", "patna", "bihar", "700055", "9191919191", "ankit@gmail.com");
    	boolean result = addBookService.checkAddressBookDataInSyncWithDB("ankit");
    	Assert.assertTrue(result);
    }
	
	@Test 
    public void given3Contacts_WhenAdded_ShouldMatchContactsCount() {
    	AddressBookData[] addBookData = {
    			new AddressBookData("strot", "Paliya", "army colony","chandigarh", "punjab", "700066", "9123912391", "strot@gmail.com"),
    			new AddressBookData("sandeep", "dhaka", "Amazon","gurugram", "haryana", "700023", "9156912391", "sandeep@gmail.com"),
    			new AddressBookData("kanishka", "Mukherjee", "saket","mount abu", "rajasthan", "700044", "9128712391", "kani@gmail.com"),
    	};
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	Instant threadStart = Instant.now();
    	addBookService.addContactsWithThreads(Arrays.asList(addBookData));
    	Instant threadEnd = Instant.now();
    	System.out.println("Duration with thread : " + Duration.between(threadStart, threadEnd));
    	List<AddressBookData> addressBookData = addBookService.readAddresBookData(IOService.DB_IO);
    	System.out.println(addressBookData.size());
    	Assert.assertEquals(4, addressBookData.size());
    }
	
}
