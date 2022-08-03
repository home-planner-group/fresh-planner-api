package com.freshplanner.api.service.user;

import com.freshplanner.api.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RoleRepo extends JpaRepository<Role, RoleName> {
}
