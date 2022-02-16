package org.bankmanagement.services;

import org.bankmanagement.data_transfer_objects.UserDetailsImpl;
import org.bankmanagement.data_transfer_objects.UserDto;
import org.bankmanagement.exceptions.UserNotFoundException;
import org.bankmanagement.mappers.UserMapper;
import org.bankmanagement.models.User;
import org.bankmanagement.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private final UserRepository userRepo = Mockito.mock(UserRepository.class);
    private final UserMapper userMapper = Mockito.mock(UserMapper.class);

    private final UserDetailsService service = new UserDetailsServiceImpl(userRepo, userMapper);

    @Test
    public void loadUserByUsernameShouldThrowUserNotFoundException() {
        when(userRepo.findByUsername(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.loadUserByUsername(any()));
        verify(userRepo).findByUsername(any());
        verifyNoInteractions(userMapper);
    }

    @Test
    public void loadUserByUsernameShouldSucceed() {
        String username = "Tumor";
        User user = new User().setUsername(username);
        UserDetails userDetails = new UserDetailsImpl();

        when(userRepo.findByUsername(username)).thenReturn(Optional.ofNullable(user));
        when(userMapper.mapToUserDetails(user)).thenReturn(userDetails);

        UserDetails response = service.loadUserByUsername(username);
        assertEquals(userDetails, response);

        verify(userRepo).findByUsername(username);
        verify(userMapper).mapToUserDetails(user);
    }

}