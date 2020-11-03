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
}
