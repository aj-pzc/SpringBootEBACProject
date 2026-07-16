package com.ebac.modulo62.controller;

import com.ebac.modulo62.dto.User;
import com.ebac.modulo62.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUsers() {
        int users = 5;
        List<User> userListExpected = mockUserDbList(users);
        when(userService.getUserList()).thenReturn(userListExpected);
        ResponseWrapper<List<User>> wrapper = userController.getUsers();
        ResponseEntity<List<User>> responseEntity = wrapper.getResponseEntity();
        List<User> actualUserList = responseEntity.getBody();
        assertTrue(wrapper.isSuccess());
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(actualUserList);
        assertEquals(5, actualUserList.size());
        verify(userService, times(1)).getUserList();
    }

    @Test
    void getUserWhenEmpty() {
        when(userService.getUserList()).thenReturn(List.of());

        ResponseWrapper<List<User>> wrapper = userController.getUsers();
        ResponseEntity<List<User>> responseEntity = wrapper.getResponseEntity();
        List<User> actualUserList = responseEntity.getBody();

        assertTrue(wrapper.isSuccess());
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(actualUserList);
        assertTrue(actualUserList.isEmpty());
        verify(userService, times(1)).getUserList();
    }

    @Test
    void getUserById() {
        long userId = 1;
        Optional<User> expectedUser = Optional.of(mockUserDbList(1).get(0));
        when(userService.getUserById(userId)).thenReturn(expectedUser);

        ResponseWrapper<User> wrapper = userController.getUserById(userId);
        ResponseEntity<User> responseEntity = wrapper.getResponseEntity();
        User actualUser = responseEntity.getBody();

        assertTrue(wrapper.isSuccess());
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(actualUser);
        assertEquals("name1", actualUser.getName());
    }

    @Test
    void getUserByIdWhenEmpty() {
        long userId = 1;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseWrapper<User> wrapper = userController.getUserById(userId);
        ResponseEntity<User> responseEntity = wrapper.getResponseEntity();
        User actualUser = responseEntity.getBody();

        assertTrue(wrapper.isSuccess()); // Tu endpoint retorna true incluso si no lo encuentra
        assertEquals(404, responseEntity.getStatusCode().value());
        assertNull(actualUser);
    }

    @Test
    void createUser() throws Exception {
        User expectedUser = mockUserDbList(1).get(0);
        when(userService.newUser(expectedUser)).thenReturn(expectedUser);

        ResponseWrapper<User> wrapper = userController.createUser(expectedUser);
        ResponseEntity<User> responseEntity = wrapper.getResponseEntity();
        User actualUser = responseEntity.getBody();

        assertTrue(wrapper.isSuccess());
        assertEquals(201, responseEntity.getStatusCode().value());
        assertNull(actualUser); // Es correcto, .build() en ResponseEntity.created() no lleva body
    }

    @Test
    void createUnderageUser() throws Exception {
        User user = new User();
        user.setIdUser(1);
        user.setName("NombreUser");
        user.setAge(15);

        doThrow(new RuntimeException("Usuario menor de edad")).when(userService).newUser(user);

        ResponseWrapper<User> wrapper = userController.createUser(user);
        ResponseEntity<User> responseEntity = wrapper.getResponseEntity();
        User actualUser = responseEntity.getBody();

        assertFalse(wrapper.isSuccess()); // Esperamos false por el catch del controlador
        assertEquals("Usuario menor de edad", wrapper.getMessage());
        assertEquals(400, responseEntity.getStatusCode().value());
        assertNull(actualUser);
    }

    @Test
    void updateUser() {
        int userId = 5;
        String updatedName = "Aldo";
        int updatedAge = 27;

        User oldUser = new User();
        oldUser.setIdUser(userId);
        oldUser.setName("David");
        oldUser.setAge(21);

        User updatedUser = new User();
        updatedUser.setName(updatedName);
        updatedUser.setAge(updatedAge);

        when(userService.getUserById((long) userId)).thenReturn(Optional.of(oldUser));
        doNothing().when(userService).updateUser(updatedUser);

        ResponseWrapper<User> wrapper = userController.updateUser((long) userId, updatedUser);
        ResponseEntity<User> responseEntity = wrapper.getResponseEntity();
        User actualUser = responseEntity.getBody();

        assertTrue(wrapper.isSuccess());
        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(actualUser);
        assertEquals(updatedName, actualUser.getName());
        assertEquals(updatedAge, actualUser.getAge());
    }

    @Test
    void updateUserNoUser() {
        long userId = 5;
        String updatedName = "Aldo";
        int updatedAge = 27;

        User updatedUser = new User();
        updatedUser.setName(updatedName);
        updatedUser.setAge(updatedAge);
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseWrapper<User> wrapper = userController.updateUser(userId, updatedUser);
        ResponseEntity<User> responseEntity = wrapper.getResponseEntity();
        User actualUser = responseEntity.getBody();

        assertFalse(wrapper.isSuccess());
        assertEquals(404, responseEntity.getStatusCode().value());
        assertNull(actualUser);
        verify(userService, never()).updateUser(updatedUser);
    }

    @Test
    void deleteUser() {
        long userId = 5;
        User expectedUser = mockUserDbList(1).get(0);

        when(userService.getUserById(userId)).thenReturn(Optional.of(expectedUser));
        doNothing().when(userService).deleteUser(userId);

        ResponseWrapper<Void> wrapper = userController.deleteUser(userId);
        ResponseEntity<Void> responseEntity = wrapper.getResponseEntity();

        assertTrue(wrapper.isSuccess());
        assertEquals(20, responseEntity.getStatusCode().value() / 10);
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUserNotFound() {
        long userId = 5;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseWrapper<Void> wrapper = userController.deleteUser(userId);
        ResponseEntity<Void> responseEntity = wrapper.getResponseEntity();

        assertFalse(wrapper.isSuccess());
        assertEquals(404, responseEntity.getStatusCode().value());
        verify(userService, never()).deleteUser(userId);
    }

    private List<User> mockUserDbList(int listCount){
        return IntStream.range(1, listCount+1)
                .mapToObj(i -> {
                    User user = new User();
                    user.setIdUser(i);
                    user.setName("name" + i);
                    user.setAge(10+i);
                    return user;
                })
                .collect(Collectors.toList()
        );
    }
}