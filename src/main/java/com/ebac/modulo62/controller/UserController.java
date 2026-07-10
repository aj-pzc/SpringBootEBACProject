package com.ebac.modulo62.controller;

import com.ebac.modulo62.dto.User;
import com.ebac.modulo62.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/usuarios")
    public List<User> getUsers() {
        return userService.getUserList();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/usuarios")
    public ResponseEntity<User> createUser(@RequestBody User user) throws Exception {

        try {
            userService.newUser(user);
            return ResponseEntity.created(new URI("http://localhost/usuarios")).build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User updatedUser){
        Optional<User> userOptional = userService.getUserById(id);
        if(userOptional.isPresent()){
            updatedUser.setIdUser(userOptional.get().getIdUser());
            userService.updateUser(updatedUser);

            return ResponseEntity.ok(updatedUser);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        if(userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();

        } else  {
            return ResponseEntity.notFound().build();
        }
    }
}
