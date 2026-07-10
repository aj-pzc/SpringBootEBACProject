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

        List<User> actualUserList = userController.getUsers();
        assertEquals(users, actualUserList.size());

        assertEquals(userListExpected, actualUserList);
    }

    @Test
    void getUserWhenEmpty() {
        when(userService.getUserList()).thenReturn(List.of());

        List<User> actualUserList = userController.getUsers();

        assertTrue(actualUserList.isEmpty());

        verify(userService, times(1)).getUserList();
    }

    @Test
    void getUserById() {
        long userId = 1;
        Optional<User>expectedUser = Optional.of(mockUserDbList(1).get(0));
        when(userService.getUserById(userId)).thenReturn(expectedUser);

        ResponseEntity<User> userResponseEntity = userController.getUserById(userId);
        User actualUser = userResponseEntity.getBody();

        assertEquals(200, userResponseEntity.getStatusCode().value());
        assertNotNull(actualUser);
        assertEquals("name1", actualUser.getName());
    }

    @Test
    void getUserByIdWhenEmpty() {
        long userId = 1;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> userResponseEntity = userController.getUserById(userId);
        User actualUser = userResponseEntity.getBody();

        assertEquals(404, userResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(actualUser));
    }

    @Test
    void createUser() throws Exception {
        User expectedUser = mockUserDbList(1).get(0);

        when(userService.newUser(expectedUser)).thenReturn(expectedUser);

        ResponseEntity<User> userResponseEntity = userController.createUser(expectedUser);
        User actualUser = userResponseEntity.getBody();

        assertEquals(201, userResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(actualUser));
    }

    @Test
    void updateUser() {
        int userId = 5;
        String upatedName ="Aldo";
        int updatedAge = 27;

        User oldUser = new User();
        oldUser.setIdUser(userId);
        oldUser.setName("David");
        oldUser.setAge(21);

        User updatedUser = new User();
        updatedUser.setName(upatedName);
        updatedUser.setAge(updatedAge);

        when(userService.getUserById((long) userId)).thenReturn(Optional.of(oldUser));
        doNothing().when(userService).updateUser(updatedUser);

        ResponseEntity<User> userResponseEntity = userController.updateUser((long) userId, updatedUser);
        User actualUser = userResponseEntity.getBody();

        assertEquals(200, userResponseEntity.getStatusCode().value());
        assertNotNull(actualUser);
        assertEquals(upatedName, actualUser.getName());
        assertEquals(updatedAge, actualUser.getAge());
    }

    @Test
    void updateUserNoUser() {
        long userId = 5;
        String upatedName ="Aldo";
        int updatedAge = 27;

        User updatedUser = new User();
        updatedUser.setName(upatedName);
        updatedUser.setAge(updatedAge);
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> userResponseEntity = userController.updateUser(userId, updatedUser);
        User actualUser = userResponseEntity.getBody();

        assertEquals(404, userResponseEntity.getStatusCode().value());
        assertNull(actualUser);
        verify(userService, never()).updateUser(updatedUser);
    }

    @Test
    void deleteUser() {
        long userId = 5;

        User expectedUser = mockUserDbList(1).get(0);
        when(userService.getUserById((long) userId)).thenReturn(Optional.of(expectedUser));

        doNothing().when(userService).deleteUser(userId);
        ResponseEntity<Void> userResponseEntity = userController.deleteUser(userId);

        assertEquals(204, userResponseEntity.getStatusCode().value());
        verify(userService, atLeast(1)).deleteUser(userId);
    }

    @Test
    void createUnderageUser() throws Exception {
        User user = new User();
        user.setIdUser(1);
        user.setName("NombreUser");
        user.setAge(15);

        doThrow(Exception.class).when(userService).newUser(user);

        ResponseEntity<User> userResponseEntity = userController.createUser(user);
        User actualUser = userResponseEntity.getBody();

        assertEquals(400, userResponseEntity.getStatusCode().value());
        assertNull(actualUser); 
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