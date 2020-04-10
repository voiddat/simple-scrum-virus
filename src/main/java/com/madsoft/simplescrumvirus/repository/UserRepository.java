package com.madsoft.simplescrumvirus.repository;

import com.madsoft.simplescrumvirus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
