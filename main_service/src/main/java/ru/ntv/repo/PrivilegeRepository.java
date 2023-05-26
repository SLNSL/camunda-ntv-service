package ru.ntv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.users.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
}