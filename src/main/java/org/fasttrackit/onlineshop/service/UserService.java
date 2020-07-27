package org.fasttrackit.onlineshop.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.fasttrackit.onlineshop.domain.User;
import org.fasttrackit.onlineshop.persistance.UserRepository;
import org.fasttrackit.onlineshop.transfer.user.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public User createUser(CreateUserRequest request) {
        LOGGER.info("Creating user {}", request);

        final User user = objectMapper.convertValue(request, User.class);

        return userRepository.save(user);

    }
}
