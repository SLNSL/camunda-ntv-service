package ru.ntv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.users.Role;
import ru.ntv.entity.users.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);

    List<User> findAllByRole(Role roleJournalist);
}