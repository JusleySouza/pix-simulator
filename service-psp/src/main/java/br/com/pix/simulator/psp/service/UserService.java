package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.config.LoggerConfig;
import br.com.pix.simulator.psp.dto.user.UserCreateRequest;
import br.com.pix.simulator.psp.dto.user.UserResponse;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.mapper.UserMapper;
import br.com.pix.simulator.psp.model.User;
import br.com.pix.simulator.psp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private User user;
    private final UserMapper mapper;
    private final UserRepository userRepository;

    public UserService(UserMapper mapper, UserRepository userRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.findByCpf(request.cpf()).isPresent()){
            throw new IllegalArgumentException("CPF already exists");
        }

        user = mapper.toEntity(request);
        userRepository.save(user);

        LoggerConfig.LOGGER_USER.info("User : " + user.getName() + " created successfully!");

        return mapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse searchUserById(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        LoggerConfig.LOGGER_USER.info("User : " + user.getName() + " successfully returned!");

        return mapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public User searchUserEntity(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

}
