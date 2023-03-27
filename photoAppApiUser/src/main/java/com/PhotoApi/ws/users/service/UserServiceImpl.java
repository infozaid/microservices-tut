package com.PhotoApi.ws.users.service;

import com.PhotoApi.ws.users.Entity.UserEntity;
import com.PhotoApi.ws.users.dto.UserDto;
import com.PhotoApi.ws.users.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
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

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity= userRepository.findByEmail(email);
        if (userEntity==null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(userEntity,UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity= userRepository.findByEmail(userName);
        if (userEntity==null) throw new UsernameNotFoundException(userName);
        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),true,true,true,true,new ArrayList<>());
    }
}
