package org.bankmanagement.controllers;

import org.bankmanagement.data_transfer_objects.ErrorResponse;
import org.bankmanagement.data_transfer_objects.RegisterTicket;
import org.bankmanagement.data_transfer_objects.UserDto;
import org.bankmanagement.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegControllerIT extends AbstractIT {

    private final String uri = "/api/login/reg";

    @Test
    public void testRegByEmptyTicket() throws Exception {
        String response =
                mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(new RegisterTicket())))
                        .andExpect(status().isBadRequest())
                        .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        Set<String> expectedMessageSet = new HashSet<>();
        expectedMessageSet.add("Username field must be filled");
        expectedMessageSet.add("Password field must be filled");
        expectedMessageSet.add("Email field must be filled");

        Set<String> responseMessageSet = new HashSet<>();
        responseMessageSet.addAll(Arrays.asList(errorResponse.getMessage().split("\n")));

        assertAll(
                () -> assertEquals("BAD_REQUEST", errorResponse.getCode()),
                () -> assertEquals(400, errorResponse.getStatus()),
                () -> assertEquals(expectedMessageSet, responseMessageSet)
        );
    }

    @Test
    public void testRegValidationByWrongFormat() throws Exception {
        RegisterTicket ticket = new RegisterTicket().setEmail("changeadress@").setUsername("Di").setPassword("Lib");

        String response =
                mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(ticket)))
                        .andExpect(status().isBadRequest())
                        .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        Set<String> expectedMessageSet = new HashSet<>();
        expectedMessageSet.add("Username length shall be between 3 and 50 characters");
        expectedMessageSet.add("Password length shall be between 4 and 50 characters");
        expectedMessageSet.add("Email shall match pattern: \"chosenname@domain.org\"");

        Set<String> responseMessageSet = new HashSet<>();
        responseMessageSet.addAll(Arrays.asList(errorResponse.getMessage().split("\n")));

        assertAll(
                () -> assertEquals("BAD_REQUEST", errorResponse.getCode()),
                () -> assertEquals(400, errorResponse.getStatus()),
                () -> assertEquals(expectedMessageSet, responseMessageSet)
        );
    }

    @Test
    public void testRegByExistingEmail() throws Exception {
        RegisterTicket ticket = new RegisterTicket().setEmail("test@room.try").setUsername("Tutor").setPassword("Rial");

        String response =
                mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(ticket)))
                        .andExpect(status().isConflict())
                        .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals("CONFLICT", errorResponse.getCode()),
                () -> assertEquals(409, errorResponse.getStatus()),
                () -> assertEquals("User already exists by email: test@room.try", errorResponse.getMessage())
        );
    }

    @Test
    public void testRegByExistingUsername() throws Exception {
        RegisterTicket ticket = new RegisterTicket().setEmail("test3@room.try").setUsername("Tutor").setPassword("Rial");

        String response =
                mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(ticket)))
                        .andExpect(status().isConflict())
                        .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals("CONFLICT", errorResponse.getCode()),
                () -> assertEquals(409, errorResponse.getStatus()),
                () -> assertEquals("User already exists by username: Tutor", errorResponse.getMessage())
        );
    }

    @Test
    public void testRegisterNewClient() throws Exception {
        String email = "test3@room.try";
        String name = "Titor";
        String password = "Rial";

        RegisterTicket ticket = new RegisterTicket().setEmail(email).setUsername(name).setPassword(password);

        String response =
                mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(ticket)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        UserDto userDtoFromResponse = objectMapper.readValue(response, UserDto.class);
        User userFromDB = em.find(User.class, (long) 5);

        assertEquals(userMapper.mapToDto(userFromDB), userDtoFromResponse);
        assertAll(
                () -> assertEquals(email, userFromDB.getEmail()),
                () -> assertEquals(name, userFromDB.getUsername()),
                () -> assertTrue(encoder.matches(password, userFromDB.getPassword()))
        );

    }

}