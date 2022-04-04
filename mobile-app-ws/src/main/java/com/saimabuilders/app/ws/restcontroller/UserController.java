package com.saimabuilders.app.ws.restcontroller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saimabuilders.app.ws.entity.User;
import com.saimabuilders.app.ws.exception.UserServiceException;

@RestController
@RequestMapping("/users")
public class UserController {

	
	Map<String, User> users;
	
	@GetMapping
	public String getUsers(@RequestParam(value="page",defaultValue = "1")int page,
			@RequestParam(value ="limit",defaultValue = "2")int limit,
			@RequestParam(value ="sort",defaultValue = "desc",required=false)String sort) {
		return "we are getting users page= "+page+" and limit= "+limit+" and sort in "+sort;
	}
	
	@GetMapping(path="/{userId}",
			produces= {MediaType.APPLICATION_JSON_VALUE,
					   MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<User> getUser(@PathVariable String userId) {
		
		/*
		 * String Name=null; int namelength=Name.length();
		 */
		
		if(true) throw new UserServiceException("A User Service Exception  is thrown");
		
		if(users.containsKey(userId)) {
			return new ResponseEntity<User>(users.get(userId),HttpStatus.OK);
			}
		else {
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
		
		
	}
	
	
	@PostMapping(
			produces= {MediaType.APPLICATION_JSON_VALUE,
					   MediaType.APPLICATION_XML_VALUE},
			consumes= {MediaType.APPLICATION_JSON_VALUE,
					   MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<User> createUser(@RequestBody User user) {
		user.setEmail(user.getEmail());
		user.setFirstName(user.getFirstName());
		user.setLastName(user.getLastName());
		
		String userId=UUID.randomUUID().toString();
		user.setId(userId);
		
		if(users==null) { users=new HashMap<>();}
		users.put(userId, user);
		
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@PutMapping(path="/{userId}",
			produces= {MediaType.APPLICATION_JSON_VALUE,
					   MediaType.APPLICATION_XML_VALUE},
			consumes= {MediaType.APPLICATION_JSON_VALUE,
					   MediaType.APPLICATION_XML_VALUE})
	public User updateUser(@PathVariable String userId, @RequestBody User user){
		
		User user1=users.get(userId);
		user1.setFirstName(user.getFirstName());
		user1.setLastName(user.getLastName());
		users.put(userId, user1);
		return user1;
	}
}
