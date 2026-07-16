package com.ebac.modulo62.controller;

import com.ebac.modulo62.dto.Phone;
import com.ebac.modulo62.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class PhoneController {
    @Autowired
    private PhoneService phoneService;

    @GetMapping ("/telefonos")
    public ResponseWrapper<List<Phone>> getPhoneList(){
        List<Phone> phoneList = phoneService.getPhoneList();
        ResponseEntity<List<Phone>> responseEntity = ResponseEntity.ok(phoneList);
        return new ResponseWrapper<>(true,"Lista de Teléfonos registrados:",responseEntity);

    }

    @GetMapping("/telefonos/{id}")
    public ResponseWrapper<Phone> getPhoneById(@PathVariable Long id){
        Optional<Phone> phoneOptional = phoneService.getPhoneById(id);
        ResponseEntity<Phone> responseEntity = phoneOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        return new ResponseWrapper<>(true, "Teléfonos registrados:", responseEntity );
    }

    @PostMapping("/telefonos")
    public ResponseWrapper<Phone> createPhoneEntry(@RequestBody Phone phone) throws Exception {

        try {
            Phone createdPhone = phoneService.newPhone(phone);
            ResponseEntity<Phone> responseEntity =ResponseEntity.created(new URI("http://localhost/telefonos")).body(createdPhone);

            return new ResponseWrapper<>(true,"Telefono agregado con exito",responseEntity);
        } catch (Exception e){
            ResponseEntity<Phone> responseEntity = ResponseEntity.badRequest().build();
            return new ResponseWrapper<>(false, e.getMessage(), responseEntity);
        }
    }

    @PutMapping("/telefonos/{id}")
    public ResponseWrapper<Phone> updatePhone(@PathVariable long id, @RequestBody Phone updatedPhone){
        Optional<Phone> phoneOptional = phoneService.getPhoneById(id);
        if(phoneOptional.isPresent()){
            updatedPhone.setIdPhone(phoneOptional.get().getIdPhone());
            phoneService.updatePhone(updatedPhone);
            ResponseEntity<Phone> responseEntity = ResponseEntity.ok(updatedPhone);

            return new ResponseWrapper<>(true,"Telefono Actualizado con exito",responseEntity);

        } else {
            ResponseEntity<Phone> responseEntity = ResponseEntity.notFound().build();
            return new ResponseWrapper<>(false, "Telefono no encontrado", responseEntity);

        }
    }

    @DeleteMapping("/telefonos/{id}")
    public ResponseWrapper<Void> deletePhone(@PathVariable long id) {
        if(phoneService.getPhoneById(id).isPresent()) {
            phoneService.deletePhone(id);
            ResponseEntity<Void> responseEntity =ResponseEntity.noContent().build();

            return new ResponseWrapper<>(true,"Telefono eliminado con exito",responseEntity);

        } else  {
            ResponseEntity<Void> responseEntity = ResponseEntity.notFound().build();
            return new ResponseWrapper<>(false, "Telefono no encontrado", responseEntity);

        }
    }
}
