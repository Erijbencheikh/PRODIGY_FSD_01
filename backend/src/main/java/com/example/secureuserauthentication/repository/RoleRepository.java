package com.example.secureuserauthentication.repository;

import com.example.secureuserauthentication.models.ERole;
import com.example.secureuserauthentication.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
