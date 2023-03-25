package com.PhotoApi.ws.users.service;

import com.PhotoApi.ws.users.Entity.UserEntity;
import com.PhotoApi.ws.users.dto.UserDto;
import com.PhotoApi.ws.users.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
       try {
           userDetails.setUserId(UUID.randomUUID().toString());
           userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
           ModelMapper modelMapper = new ModelMapper();
           modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
           UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

           userRepository.save(userEntity);

       }catch (Exception e){
           e.printStackTrace();
           userDetails.setMessage(e.getCause().getCause().getMessage());
           System.out.println(e.getCause().getCause().getMessage());
       }
        return userDetails;
    }
}
