package com.ebac.modulo62.controller;

import com.ebac.modulo62.dto.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @GetMapping("/usuarios")
    public List<User> getUsers() {
        return List.of(new User());
    }

    @GetMapping("/usuarios/{id}")
    public User getUserById(@PathVariable long id) {
        System.out.printf("Id obtenido: %s\n",id);
        return new User();
    }

    @PostMapping("/usuarios")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/usarios/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long Id, @RequestBody User updatedUser){
        System.out.printf("Usario con id [%s]: %s\n",Id, updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/usarios/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long Id) {
        return ResponseEntity.noContent().build();
    }
}
