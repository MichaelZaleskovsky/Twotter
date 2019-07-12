package com.twotter;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.path.json.JsonPath.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TwotterApplicationTests {
	
	@LocalServerPort
	private int port;
	
	private static String domen;
	
	
	@Before 
	public void setUp() { 
		domen = "http://localhost:"+port; 
		
	}
  
	@Test
	public void createPostAndUser() {
		
		String endPoint = "/posts";
		String postRequest;
		System.out.println("HOST = "+domen+endPoint);
		
		/*
		 * User are registered when he sends a post for the first time. In this case he receives
		 * response with status code CREATED (201)
		 */		
		postRequest = "{\n" + 
				"	\"username\" : \"Alice\", \n" + 
				"	\"text\" : \"First Alice's posts text\"\n" + 
				"}";
		
		given().contentType("application/json").body(postRequest)
		.when()
		.post(domen + endPoint)
		.then()
		.statusCode(HttpStatus.CREATED.value());
		
		/*
		 * When user already exist he receive response with code OK (200)
		 */		
		postRequest = "{\n" + 
				"	\"username\" : \"Alice\", \n" + 
				"	\"text\" : \"Second Alice's posts text\"\n" + 
				"}";
		
		given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.OK.value());
		
		/*
		 * When size of the post is more than 140 user receive response with code
		 * PAYLOAD_TOO_LARGE (413)
		 */
		postRequest = "{\n" + 
				"	\"username\" : \"Marcin\", \n" + 
				"	\"text\" : \"In response to your advertisement for Java Development position "
				+ "please find enclosed a copy of my resume for your consideration. I have got "
				+ "relevant education, possess good knowledge in object-oriented programming and "
				+ "I am very comfortable utilizing Java and different Java technologies, as well as "
				+ "JavaScript and web-technologies. Also I’ve got 2 years’ experience in front-end "
				+ "development. I believe that my profile and aspirations make me a good match "
				+ "for a dynamic IT company. I have got strong analytical skills and scientific "
				+ "mind, ability to think outside the box and to self education .\"\n" + 
				"}";
		
		given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.PAYLOAD_TOO_LARGE.value());
	}
	
	@Test
	public void getListOfPostByPages() {
		
		String endPoint = "/posts";
		String postRequest;
		
		postRequest = "{\n" + 
				"	\"username\" : \"Ronald\", \n" + 
				"	\"text\" : \"Ronald's posts text\"\n" + 
				"}";
		
		String tokenOfRonald = given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.CREATED.value())
		.extract().response().asString();

		given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.OK.value());
		
		given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.OK.value());
		
		/*
		 * Total 3 posts posted by Ronald. 
		 * Request for 1-st page with page size 2 return 
		 * array with size 2 and status code PARTIAL_CONTENT (206)
		 */		
		String json = given().contentType("application/json").header("Authentication", tokenOfRonald)
		.when()
		.get(domen + endPoint + "/1/2")
		.then()
		.statusCode(HttpStatus.PARTIAL_CONTENT.value())
		.extract().response().asString();
		
		List<HashMap<String, Object>> list1 = from(json).get();
		assertEquals(2, list1.size());
		
		/*
		 * Total 3 posts posted by Ronald. 
		 * Request for 2-nd page with page size 2 return 
		 * array with size 1 and status code OK (200)
		 */		
		String json1 = given().contentType("application/json").header("Authentication", tokenOfRonald)
		.when()
		.get(domen + endPoint + "/2/2")
		.then()
		.statusCode(HttpStatus.OK.value())
		.extract().response().asString();
		
		List<HashMap<String, Object>> list2 = from(json1).get();
		assertEquals(1, list2.size());
		
		/*
		 * Check posts sorted in descending order by time. 
		 */		
		long[] sortedArr = list1.stream()
				.sorted((e1, e2) -> (int)((long)e2.get("time") - (long)e1.get("time")))
				.mapToLong(hm -> (long)hm.get("time"))
				.toArray();

		long[] unSortedArr = list1.stream()
				.mapToLong(hm -> (long)hm.get("time"))
				.toArray();

		assertArrayEquals(sortedArr, unSortedArr);
	}
	
	@Test
	public void getPostsOfUsersWhichUserFollow() {
		String endPoint = "/posts";
		String postRequest;
		
		postRequest = "{\n" + 
				"	\"username\" : \"John\", \n" + 
				"	\"text\" : \"John's posts text\"\n" + 
				"}";
		
		String tokenOfJohn = given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.CREATED.value())
		.extract().response().asString();

		postRequest = "{\n" + 
				"	\"username\" : \"Patric\", \n" + 
				"	\"text\" : \"Patric's posts text\"\n" + 
				"}";
		
		String tokenOfPatric = given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.CREATED.value())
		.extract().response().asString();

		given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.OK.value());

		postRequest = "{\n" + 
				"	\"username\" : \"Sara\", \n" + 
				"	\"text\" : \"Sara's posts text\"\n" + 
				"}";
		
		String tokenOfSara = given().contentType("application/json").body(postRequest)
		.when()
		.post(domen+endPoint)
		.then()
		.statusCode(HttpStatus.CREATED.value())
		.extract().response().asString();
		
		endPoint = "/leaders";
		
		/*
		 * John follow Patric
		 * Response with  status code NO_CONTENT (204)
		 */		
		given().contentType("application/json").header("Authentication", tokenOfJohn)
		.when()
		.post(domen + endPoint + "/" + tokenOfPatric)
		.then()
		.statusCode(HttpStatus.NO_CONTENT.value());
		
		/*
		 * John follow Sara
		 * Response with  status code NO_CONTENT (204)
		 */		
		given().contentType("application/json").header("Authentication", tokenOfJohn)
		.when()
		.post(domen + endPoint + "/" + tokenOfSara)
		.then()
		.statusCode(HttpStatus.NO_CONTENT.value());

		/*
		 * John follow Unknown user
		 * Response with  status code NOT_FOUND (404)
		 */		
		given().contentType("application/json").header("Authentication", tokenOfJohn)
		.when()
		.post(domen + endPoint + "/" + "abcdef")
		.then()
		.statusCode(HttpStatus.NOT_FOUND.value());

		endPoint = "/posts/leaders";
		/*
		 * Total 3 posts posted by leaders of John. 
		 * Request for 1-st page with page size 2 return 
		 * array with size 2 and status code PARTIAL_CONTENT (206)
		 */		
		String json = given().contentType("application/json").header("Authentication", tokenOfJohn)
		.when()
		.get(domen + endPoint + "/1/2")
		.then()
		.statusCode(HttpStatus.PARTIAL_CONTENT.value())
		.extract().response().asString();
		
		List<HashMap<String, Object>> list1 = from(json).get();
		assertEquals(2, list1.size());
		
		/*
		 * Total 3 posts posted by leaders of John. 
		 * Request for 2-nd page with page size 2 return 
		 * array with size 1 and status code OK (200)
		 */		
		String json1 = given().contentType("application/json").header("Authentication", tokenOfJohn)
		.when()
		.get(domen + endPoint + "/2/2")
		.then()
		.statusCode(HttpStatus.OK.value())
		.extract().response().asString();
		
		List<HashMap<String, Object>> list2 = from(json1).get();
		assertEquals(1, list2.size());
		
		/*
		 * Check posts sorted in descending order by time. 
		 */		
		long[] sortedArr = list1.stream()
				.sorted((e1, e2) -> (int)((long)e2.get("time") - (long)e1.get("time")))
				.mapToLong(hm -> (long)hm.get("time"))
				.toArray();

		long[] unSortedArr = list1.stream()
				.mapToLong(hm -> (long)hm.get("time"))
				.toArray();

		assertArrayEquals(sortedArr, unSortedArr);
	}

}
