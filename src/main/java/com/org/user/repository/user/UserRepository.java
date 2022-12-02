package com.org.user.repository.user;

import com.org.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String Email);
    User findByPhoneNumber(String phoneNumber);
}
