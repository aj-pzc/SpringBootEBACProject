package com.ebac.modulo62.controller;

import com.ebac.modulo62.dto.User;
import com.ebac.modulo62.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/usuarios")
    public ResponseWrapper<List<User>> getUsers() {
        log.info("Obteniendo Usuarios");
        List<User> userList =  userService.getUserList();

        ResponseEntity< List<User>> responseEntity = ResponseEntity.ok(userList);

         return new ResponseWrapper<>(true, "Lista de Usuarios: ", responseEntity);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseWrapper<User> getUserById(@PathVariable Long id) {

        Optional<User> userOptional = userService.getUserById(id);
        log.info("Obteniendo el usuario {}", id);

        ResponseEntity<User> userResponseEntity = userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        return new ResponseWrapper<>(true, "Usuario encontrado: "+id, userResponseEntity);
    }

    @PostMapping("/usuarios")
    public ResponseWrapper<User> createUser(@RequestBody User user) throws Exception {

        try {
            User createdUser = userService.newUser(user);
            ResponseEntity<User> responseEntity = ResponseEntity.created(new URI("http://localhost/usuarios")).build();
            return  new ResponseWrapper<>(true, "Usuario Creado con exito.", responseEntity);
        } catch (Exception e){
            ResponseEntity<User> responseEntity = ResponseEntity.badRequest().build();
            return new ResponseWrapper<>(false, e.getMessage(), responseEntity);
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseWrapper<User> updateUser(@PathVariable long id, @RequestBody User updatedUser){
        Optional<User> userOptional = userService.getUserById(id);
        if(userOptional.isPresent()){
            updatedUser.setIdUser(userOptional.get().getIdUser());
            userService.updateUser(updatedUser);

            ResponseEntity<User> responseEntity = ResponseEntity.ok(updatedUser);

            return new ResponseWrapper<>(true, "Usuario actualizado con exito.", responseEntity);

        } else {
            ResponseEntity<User> responseEntity = ResponseEntity.notFound().build();
            return new ResponseWrapper<>(false, "Usuario no encontrado", responseEntity);
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseWrapper<Void> deleteUser(@PathVariable long id) {
        if(userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
            return new ResponseWrapper<>(true, "Usuario Eliminado con Exito", responseEntity);

        } else  {
            ResponseEntity<Void> responseEntity = ResponseEntity.notFound().build();
            return new ResponseWrapper<>(false, "Usuario no encontrado", responseEntity);
        }
    }

    @GetMapping("/usuarios/username/{username}")
    public ResponseWrapper<User> getUserByUsername(@PathVariable String username) {
        log.info("Buscando el usuario por username: {}", username);

        Optional<User> userOptional = userService.getUserByUsername(username);

        if (userOptional.isPresent()) {
            ResponseEntity<User> responseEntity = ResponseEntity.ok(userOptional.get());
            return new ResponseWrapper<>(true, "Usuario encontrado", responseEntity);
        } else {
            ResponseEntity<User> responseEntity = ResponseEntity.notFound().build();
            return new ResponseWrapper<>(false, "Usuario no encontrado", responseEntity);
        }
    }
}
