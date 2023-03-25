package com.PhotoApi.ws.users.repository;

import com.PhotoApi.ws.users.Entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,Long> {

}
