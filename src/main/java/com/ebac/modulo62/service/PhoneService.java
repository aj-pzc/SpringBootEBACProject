package com.ebac.modulo62.service;

import com.ebac.modulo62.dto.Phone;
import com.ebac.modulo62.dto.User;
import com.ebac.modulo62.repositories.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneService {
    @Autowired
    private PhoneRepository phoneRepository;

    public Phone newPhone(Phone phone) throws Exception {
        if(phone.getNumber().length() <= 15){
            return phoneRepository.save(phone);
        } throw new Exception("Telefono Invalido");
    }

    public Optional<Phone> getPhoneById(Long phoneId){
        return phoneRepository.findById(phoneId);
    }

    public List<Phone> getPhoneList(){
        return phoneRepository.findAll();
    }

    public void updatePhone(Phone phoneId){
        phoneRepository.save(phoneId);
    }

    public void deletePhone(Long phoneId){
        phoneRepository.deleteById(phoneId);
    }
}
