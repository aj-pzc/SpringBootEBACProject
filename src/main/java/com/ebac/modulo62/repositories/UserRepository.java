package com.ebac.modulo62.repositories;

import com.ebac.modulo62.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
