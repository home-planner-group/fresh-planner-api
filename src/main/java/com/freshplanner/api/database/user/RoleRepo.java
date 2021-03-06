package com.freshplanner.api.database.user;

import com.freshplanner.api.database.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RoleRepo extends JpaRepository<Role, RoleName> {
}
