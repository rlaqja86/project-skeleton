package com.org.user.service;

import com.org.user.entity.User;
import com.org.user.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
       return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseGet(this::getDefault);
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User getDefault() {
        return User.builder().build();
    }
}

