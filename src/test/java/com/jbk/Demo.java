package com.jbk;


import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Demo {
	Sheet sh;
	int rows;
	
	@BeforeSuite
	public void setUp()throws Exception{
		FileInputStream fis = new FileInputStream("Test.xls");
		Workbook wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("postData");
		rows=sh.getPhysicalNumberOfRows();
		
	}
	public String getData(int row, int col)  {
	    
		Cell cell = sh.getRow(row).getCell(col);
		return cell.toString();

	}
	Response response;
	// GET POST PUT DELETE---request types
	
	@Test
	public void VerifyResponseAllUser() {
		System.out.println("*****************************************");
		response = RestAssured.get("https://reqres.in/api/users");

		String responseBody = response.getBody().asString();

		System.out.println("Response Body is =>  " + responseBody);

	}	// Object and String---toString
	// SELECT * From EMPLOYEE WHERE id=2
	
	@Test
	public void VerifyResponseSpecificUser(){  
		System.out.println("*****************************************");
		RestAssured.baseURI = "https://reqres.in/api/users";//URL
        RequestSpecification httpRequest = RestAssured.given();//pre--conditions
		Response response = httpRequest.request(Method.GET, "?id=2");
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);
		
	
	}	
	
	public String getBodyOfSpecificUser(String key, String value) {
		
		System.out.println("*****************************************");
		RestAssured.baseURI = "https://reqres.in/api/users";//URL
        RequestSpecification httpRequest = RestAssured.given();//pre--conditions
		Response response = httpRequest.request(Method.GET, "?"+key+"="+value+"");
		String responseBody = response.getBody().asString();
		return responseBody;
	}
	
	@Test
	public void VerifyResponse() {
		System.out.println("*****************************************");
	    response = RestAssured.get("https://reqres.in/api/users");
	     int actualStatusCode = response.getStatusCode();
		System.out.println("Status Code: "+actualStatusCode);
		
		int expCode=200;
		Assert.assertEquals(actualStatusCode, expCode);
		
		String statusLine = response.getStatusLine();
		System.out.println("Status Line: "+statusLine );
		Assert.assertEquals(statusLine, "HTTP/1.1 200 OK");
	}
	
	@Test
	public void verifyAllHeaders() {
		System.out.println("*****************************************");
		response = RestAssured.get("https://reqres.in/api/users");
	    Headers allHeaders = response.getHeaders();
	    
		for (Header header : allHeaders) {
			System.out.println("Key: " + header.getName() + " Value: " + header.getValue());
		}
		
	}
	@Test
	public void verifySpecificHeader() {
		System.out.println("*****************************************");
		response = RestAssured.get("https://reqres.in/api/users/2");
	    String contentType = response.header("Content-Type");
    	System.out.println("Content-Type value: " + contentType);
    	String serverType =  response.header("Server");
    	System.out.println("Server value: " + serverType);
    	String date = response.header("Date");
    	System.out.println("Date value: " + date);
        String contentEnco = response.header("Content-Encoding");
    	System.out.println("Content-Encoding: " + contentEnco);
        String transferEnco = response.header("Transfer-Encoding");
    	System.out.println("Transfer-Encoding value: " + transferEnco);   	
	}
	@Test
	public void postUSer() throws Exception {
		System.out.println("*****************************************");
		
		for (int i = 1; i <rows ; i++) {
			
			RestAssured.baseURI = "https://reqres.in/api/users";
			RequestSpecification request = RestAssured.given();
			request.header("Content-Type", "application/json");
			JSONObject requestParams = new JSONObject();
			requestParams.put("id", getData(i, 0));
			requestParams.put("email", getData(i, 3));
			requestParams.put("first_name", getData(i, 1));
			requestParams.put("last_name", getData(i, 2));
			requestParams.put("avatar", getData(i, 4));

			JSONObject requestParams1 = new JSONObject();
			requestParams1.put("company", getData(i, 5));
			requestParams1.put("url", getData(i, 6));
			requestParams1.put("text", getData(i, 7));

			request.body(requestParams.toString());
			request.body(requestParams1.toString());

			Response response = request.post("/user");

			int statusCode = response.getStatusCode();
			System.out.println(statusCode);
			Assert.assertEquals(statusCode, 201);
			System.out.println(response.getStatusLine());
			Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 201 Created");
		}
	}
	
	
}
