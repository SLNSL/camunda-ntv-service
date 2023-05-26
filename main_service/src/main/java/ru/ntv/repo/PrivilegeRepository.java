package ru.ntv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.users.Privilege;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    Optional<Privilege> findByPrivilegeName(String name);
}