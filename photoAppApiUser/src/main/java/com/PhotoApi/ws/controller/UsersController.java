package com.PhotoApi.ws.controller;

import com.PhotoApi.ws.users.dto.UserDto;
import com.PhotoApi.ws.users.model.UserResponseModel;
import com.PhotoApi.ws.users.service.UserService;
import com.PhotoApi.ws.users.ui.payload.CreateUserRequestModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private Environment env;

	@Autowired
	private UserService userService;

	@GetMapping("/status/check")
	public String status() {
		return "Working on port "+env.getProperty("local.server.port");
	}

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
	             produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<UserResponseModel> createUser(@RequestBody CreateUserRequestModel userDetails){
		ResponseEntity response=null;
		try {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			UserDto userDto = modelMapper.map(userDetails, UserDto.class);
			UserDto createdUser= userService.createUser(userDto);
			UserResponseModel returnResponse=modelMapper.map(createdUser,UserResponseModel.class);
			if(userDto.getMessage()==null){
				returnResponse.setMessage(HttpStatus.CREATED.series().name()+" "+HttpStatus.CREATED.getReasonPhrase()+" "+HttpStatus.CREATED.value());
				response=  ResponseEntity.status(HttpStatus.CREATED).body(returnResponse);
			}else{
				response=  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto.getMessage());
				returnResponse.setMessage(HttpStatus.BAD_REQUEST.series().name()+" "+HttpStatus.BAD_REQUEST.getReasonPhrase()+" "+HttpStatus.BAD_REQUEST.value());
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return response;
	}
}
