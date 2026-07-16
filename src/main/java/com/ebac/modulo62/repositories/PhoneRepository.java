package com.ebac.modulo62.repositories;

import com.ebac.modulo62.dto.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
