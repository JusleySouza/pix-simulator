package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.dto.user.UserCreateRequest;
import br.com.pix.simulator.psp.dto.user.UserResponse;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.mapper.UserMapper;
import br.com.pix.simulator.psp.model.User;
import br.com.pix.simulator.psp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private User user;
    @Mock
    private UserMapper mapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("You should be able to successfully create a user if the CPF still exists in the database.")
    void createUser_shouldSucceed_whenDataIsValid() {
        UserCreateRequest request = new UserCreateRequest("Pablo Silva", "385.049.820-41");

        when(userRepository.findByCpf("385.049.820-41")).thenReturn(Optional.empty());

        when(mapper.toEntity(request)).thenReturn(user);

        UserResponse expectedResponse = new UserResponse(UUID.randomUUID(), "Pablo Silva", "385.049.820-41");
        when(mapper.toResponse(user)).thenReturn(expectedResponse);

        when(userRepository.save(user)).thenReturn(user);

        UserResponse result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(expectedResponse.userId(), result.userId());
        assertEquals(expectedResponse.name(), result.name());
        assertEquals(expectedResponse.cpf(), result.cpf());

        verify(mapper, times(1)).toEntity(request);
        verify(mapper, times(1)).toResponse(user);
    }

    @Test
    @DisplayName("The createUser command should throw an IllegalArgumentException if the user cpf already exists.")
    void createUser_shouldThrowIllegalArgumentException_whenCpfExisting() {
        UserCreateRequest request = new UserCreateRequest("Pablo Silva", "385.049.820-41");

        when(userRepository.findByCpf("385.049.820-41")).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(request)
        );

        assertTrue(exception.getMessage().contains("CPF already exists"));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("The system should return the user's data when the user ID exists.")
    void searchUserById_shouldSucceed_whenUserExists() {
        UUID userId = UUID.randomUUID();
        UserResponse expectedResponse = new UserResponse(userId, "Pablo Silva", "385.049.820-41");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.toResponse(user)).thenReturn(expectedResponse);

        UserResponse result = userService.searchUserById(userId);

        assertNotNull(result);
        assertEquals(expectedResponse.userId(), result.userId());
        assertEquals(expectedResponse.name(), result.name());
        assertEquals(expectedResponse.cpf(), result.cpf());
        verify(userRepository, times(1)).findById(userId);
        verify(mapper, times(1)).toResponse(user);
    }

    @Test
    @DisplayName("The searchUserById command should throw an ResourceNotFoundException if the id user is not found.")
    void searchUserById_shouldResourceNotFoundException_whenIdNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.searchUserById(userId)
        );

        assertTrue(exception.getMessage().contains("User not found with ID: " + userId));
    }

    @Test
    @DisplayName("The system should return the user's data when the user exists.")
    void searchUserEntity_shouldSucceed_whenUserExists() {
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(userId, "Pablo Silva", "385.049.820-41");

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User result = userService.searchUserEntity(userId);

        assertNotNull(result);
        assertEquals(expectedUser.getUserId(), result.getUserId());
        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getCpf(), result.getCpf());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("The searchUserEntity command should throw an ResourceNotFoundException if the user is not found.")
    void searchUserEntity_shouldResourceNotFoundException_whenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.searchUserEntity(userId)
        );

        assertTrue(exception.getMessage().contains("User not found with ID: " + userId));
    }

}
