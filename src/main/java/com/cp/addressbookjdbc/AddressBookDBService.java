package com.cp.addressbookjdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {
	private static AddressBookDBService addBookDB;
	private PreparedStatement addBookDataStatement;

	public AddressBookDBService() {}

	public static AddressBookDBService getInstance() {
		if(addBookDB == null) {
			addBookDB = new AddressBookDBService();
		}
		return addBookDB;
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/addressbook_service?allowPublicKeyRetrieval=true&&useSSL=false";
		String userName = "root";
		String password = "Aakash@123";
		Connection connection;
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		return connection;
	}

	public List<AddressBookData> readData() {
		String sql = "select * from addressbook;";
		List<AddressBookData> addBookList = new ArrayList<>();
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			addBookList = this.getAddressBookData(result);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return addBookList;
	}

	public List<AddressBookData> getAddressBookData(String firstName) {
		List<AddressBookData> addBookList = null;
		if(this.addBookDataStatement == null) {
			this.prepareStatementForEmployeeData();
		}
		try {
			addBookDataStatement.setString(1, firstName);
			ResultSet result = addBookDataStatement.executeQuery();
			addBookList = this.getAddressBookData(result);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return addBookList;
	}
	
	private List<AddressBookData> getAddressBookData(ResultSet result) {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try {
			while(result.next()) {
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String address = result.getString("address");
				String city = result.getString("city");
				String state = result.getString("state");
				String zipcode = result.getString("zip");
				String phone = result.getString("phone");
				String email = result.getString("email");
				addressBookList.add(new AddressBookData(firstName, lastName, address, city, state, zipcode, phone, email));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return addressBookList;
	}
	
	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT * FROM addressbook WHERE first_name = ?";
			addBookDataStatement = connection.prepareStatement(sql);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public int updateData(String firstName, String city) {
		return this.updateAddressBookDataUsingStatement(firstName, city);
	}

	private int updateAddressBookDataUsingStatement(String firstName, String city) {
		String sql = String.format("update addressbook set city = %s where first_name = %s", city, firstName);
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Map<String, Integer> getCountByCity() {
		String sql = "SELECT city, COUNT(city) AS count_city FROM addressbook GROUP BY city";
		Map<String, Integer> cityToContactsMap = new HashMap<>();
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				String city = result.getString("city");
				int count = result.getInt("count_city");
				cityToContactsMap.put(city, count);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return cityToContactsMap;
	}

	public Map<String, Integer> getCountByState() {
		String sql = "SELECT state, COUNT(state) AS count_state FROM addressbook GROUP BY state";
		Map<String, Integer> stateToContactsMap = new HashMap<>();
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				String state = result.getString("state");
				int count = result.getInt("count_state");
				stateToContactsMap.put(state, count);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return stateToContactsMap;
	}
}