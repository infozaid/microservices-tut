package com.PhotoApi.ws.users.service;

import com.PhotoApi.ws.users.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDetails);
}
