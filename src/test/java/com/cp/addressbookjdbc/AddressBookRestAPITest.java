package com.cp.addressbookjdbc;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookRestAPITest {
	@Before
    public void setup() {
    	RestAssured.baseURI = "http://localhost";
    	RestAssured.port = 3000;
    }

	private AddressBookData[] getContactList() {
		Response response = RestAssured.get("/contacts");
		AddressBookData[] arrOfCon = new Gson().fromJson(response.asString(),  AddressBookData[].class);
		return arrOfCon;
	}

	private Response addContactToJSONServer(AddressBookData addressBookData) {
		String conJson = new Gson().toJson(addressBookData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(conJson);
		return request.post("/contacts");
	}

	private Response updateContactToJSONServer(AddressBookData addressBookData) {
		String conJson = new Gson().toJson(addressBookData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(conJson);
		return request.put("/contacts/" + addressBookData.firstName);
	}

	private Response deleteContactFromJSONServer(AddressBookData addressBookData) {
		String conJson = new Gson().toJson(addressBookData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(conJson);
		return request.delete("/contacts/" + addressBookData.firstName);
	}

	@Test
	public void givenContactsDataInJsonServer_WhenRetrived_ShouldMatchCount() {
		AddressBookData[] arrOfCon = getContactList();
		AddressBookService addBookService;
		addBookService = new AddressBookService(Arrays.asList(arrOfCon));
		long entries = addBookService.countEntries(AddressBookService.IOService.REST_IO);
		assertEquals(3, entries);
	}

	@Test
	public void givenNewContact_WhenAdded_ShouldReturn201ResponseAndCount() {
		AddressBookData[] arrOfCon = getContactList();
		AddressBookService addBookService;
		addBookService = new AddressBookService(Arrays.asList(arrOfCon));

		AddressBookData addBookData = null;
		addBookData = new AddressBookData("rahul", "tewtia", "sdk", "bhopal", "MP", "700087", "9875987534", "rahul@gmail.com");
		Response response = addContactToJSONServer(addBookData);
		int statusCode = response.getStatusCode();
		assertEquals(201, statusCode);

		AddressBookData[] arrOfContacts = getContactList();
		addBookService = new AddressBookService(Arrays.asList(arrOfContacts));
		long entries = addBookService.countEntries(AddressBookService.IOService.REST_IO);
		assertEquals(4, entries);
	}

	@Test
	public void givenCity_WhenUpdated_ShouldReturn200Response() {
		AddressBookData[] arrOfCon = getContactList();
		AddressBookService addBookService;
		addBookService = new AddressBookService(Arrays.asList(arrOfCon));

		addBookService.updateContactsCity("rahul", "tewtia", AddressBookService.IOService.REST_IO);
		AddressBookData addBookData = addBookService.getContactsData("rahul");

		Response response = updateContactToJSONServer(addBookData);
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
	}

	@Test
	public void givenContactToDelete_WhenDeleted_ShouldReturn200ResponseAndCount() {
		AddressBookData[] arrOfCon = getContactList();
		AddressBookService addBookService;
		addBookService = new AddressBookService(Arrays.asList(arrOfCon));

		AddressBookData addBookData = addBookService.getContactsData("rahul");

		Response response = deleteContactFromJSONServer(addBookData);
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		addBookService.deleteEmployeeFromPayroll("rahul", AddressBookService.IOService.REST_IO);
		long entries = addBookService.countEntries(AddressBookService.IOService.REST_IO);
		assertEquals(3, entries);
	}

}