package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.dto.user.UserCreateRequest;
import br.com.pix.simulator.psp.dto.user.UserResponse;
import br.com.pix.simulator.psp.model.User;
import br.com.pix.simulator.psp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.findByCpf(request.cpf()).isPresent()){
            throw new IllegalArgumentException("CPF already exists");
        }

        User newUser = new User(
                null,
                request.name(),
                request.cpf()
        );

        User savedUser = userRepository.save(newUser);

        return new UserResponse(
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getCpf()
        );
    }

}
