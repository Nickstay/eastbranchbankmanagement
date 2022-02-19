package org.bankmanagement.services;

import lombok.RequiredArgsConstructor;
import org.bankmanagement.data_transfer_objects.RegisterTicket;
import org.bankmanagement.data_transfer_objects.UpdateTicket;
import org.bankmanagement.data_transfer_objects.UserDto;
import org.bankmanagement.exceptions.*;
import org.bankmanagement.mappers.UserMapper;
import org.bankmanagement.models.Role;
import org.bankmanagement.models.User;
import org.bankmanagement.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserDto regUser(RegisterTicket ticket) {
        String email = ticket.getEmail();
        String username = ticket.getUsername();

        checkForUniqueEmailAndUsername(email, username);

        User user = createNewUserInstance(email, username, ticket.getPassword());

        return userMapper.mapToDto(userRepo.save(user));
    }

    public UserDto getUser(String username) {
        User user = findActiveUser(username);
        return userMapper.mapToDto(user);
    }

    public UserDto updateUser(String username, UpdateTicket ticket) {
        User user = findActiveUser(username);
        boolean changed = false;

        String newEmail = ticket.getEmail();
        String newName = ticket.getUsername();
        String newPassword = ticket.getPassword();

        checkForUniqueEmailAndUsername(newEmail, newName);
        Predicate<String> nonNull = Objects::nonNull;

        if (nonNull.test(newEmail) && !newEmail.equals(user.getEmail())) {
            user.setEmail(newEmail);
            changed = true;
        }
        if (nonNull.test(newName) && !newName.equals(user.getUsername())) {
            user.setUsername(newName);
            changed = true;
        }
        if (nonNull.test(newPassword) && !encoder.matches(newPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            changed = true;
        }

        if (changed) {
            return userMapper.mapToDto(userRepo.save(user));
        } else throw new UpdateRequestException();
    }

    public UserDto deleteUser(String username) {
        User user = findActiveUser(username);
        user.setActive(false);
        return userMapper.mapToDto(userRepo.save(user));
    }

    private User createNewUserInstance(String email, String username, String rawPassword) {
        return new User()
                .setEmail(email)
                .setUsername(username)
                .setPassword(encoder.encode(rawPassword))
                .setRole(Role.ROLE_CLIENT)
                .setActive(true);
    }

    private User findActiveUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!user.isActive()) throw new UserIsDisabledException(username);
        return user;
    }

    private void checkForUniqueEmailAndUsername(String email, String username) {
        if (userRepo.existsByEmail(email)) throw new UserAlreadyExistsByEmailException(email);
        if (userRepo.existsByUsername(username)) throw new UserAlreadyExistsByUsernameException(username);
    }
}
