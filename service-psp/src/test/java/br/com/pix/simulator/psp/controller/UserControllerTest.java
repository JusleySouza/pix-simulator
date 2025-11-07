package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.user.UserCreateRequest;
import br.com.pix.simulator.psp.dto.user.UserResponse;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.exception.ValidationException;
import br.com.pix.simulator.psp.mapper.UserMapper;
import br.com.pix.simulator.psp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private UserMapper mapper;

    @Test
    @DisplayName("You should successfully create a user and receive a status of 201 Created.")
    void createUser_WhenValidRequest_ShouldReturnCreated() throws Exception {

        UUID pspId = UUID.randomUUID();
        UserCreateRequest requestDto = new UserCreateRequest(
                "Pedro Lima", "385.049.820-41");

        UserResponse responseDto = new UserResponse(
                pspId, "Pedro Lima", "385.049.820-41");

        when(service.createUser(any(UserCreateRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(service, times(1)).createUser(any(UserCreateRequest.class));
    }

    @Test
    @DisplayName("Creating a User with an empty user name should fail and return a 400 Bad Request status.")
    void createUser_WhenUserNameIsEmpty_ShouldReturnBadRequest() throws Exception {

        UserCreateRequest invalidRequestDto = new UserCreateRequest(" ", "385.049.820-41");

        when(service.createUser(eq(invalidRequestDto)))
                .thenThrow(new ValidationException("User name is required."));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("User name is required."))
                .andExpect(jsonPath("$.message").value("Validation Error"));

        verify(service, never()).createUser(any());
    }

    @Test
    @DisplayName("Creating a User with an empty user cpf should fail and return a 400 Bad Request status.")
    void createUser_WhenUserCpfIsEmpty_ShouldReturnBadRequest() throws Exception {

        UserCreateRequest invalidRequestDto = new UserCreateRequest("Pedro Lima", " ");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'cpf')].message")
                        .value(hasItem("CPF is required.")))
                .andExpect(jsonPath("$.message").value("Validation Error"));

        verify(service, never()).createUser(any());
    }

    @Test
    @DisplayName("Creating a User with an invalid user cpf should fail and return a 400 Bad Request status.")
    void createUser_WhenUserCpfIsInvalid_ShouldReturnBadRequest() throws Exception {

        UserCreateRequest invalidRequestDto = new UserCreateRequest("Pedro Lima", "123.456.789-10");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("cpf"))
                .andExpect(jsonPath("$.errors[0].message").value("Invalid CPF."))
                .andExpect(jsonPath("$.message").value("Validation Error"));

        verify(service, never()).createUser(any());
    }

    @Test
    @DisplayName("You should be able to successfully query your user and receive a 200 OK status.")
    void searchingUserById_WhenUserExists_ShouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponse responseDto = new UserResponse(userId, "Pedro Lima", "123.456.789-10");

        when(service.searchUserById(userId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(service, times(1)).searchUserById(userId);
    }

    @Test
    @DisplayName("It may fail to check for a non-existent user and return a 404 Not Found error.")
    void searchingUserById_WhenUserNotFound_ShouldNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        when(service.searchUserById(userId))
                .thenThrow(new ResourceNotFoundException("User not found: " + userId));

        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isNotFound());

        verify(service, times(1)).searchUserById(userId);
    }

}
