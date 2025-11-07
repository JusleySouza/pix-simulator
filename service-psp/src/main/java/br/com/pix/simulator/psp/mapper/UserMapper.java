package br.com.pix.simulator.psp.mapper;

import br.com.pix.simulator.psp.dto.user.UserCreateRequest;
import br.com.pix.simulator.psp.dto.user.UserResponse;
import br.com.pix.simulator.psp.model.User;
import lombok.Generated;
import org.springframework.stereotype.Component;

@Component
@Generated
public class UserMapper {

    public User toEntity(UserCreateRequest request) {

        if (request == null) {
            return null;
        }

        User user = new User();
        user.setName(request.name());
        user.setCpf(request.cpf());
        return user;
    }


    public UserResponse toResponse(User user) {

        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getCpf()
        );
    }

}
