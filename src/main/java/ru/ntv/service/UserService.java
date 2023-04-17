package ru.ntv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntv.dto.response.boss.JournalistResponse;
import ru.ntv.entity.User;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.repo.RoleRepository;
import ru.ntv.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;


    public JournalistResponse getJournalistById(int id) {
        final var user = userRepository.findById(id).get(); //todo throw custom Exception if user is not found
        
        return convertUserToJournalist(user);
    }

    public List<JournalistResponse> getAllJournalists() {
        final var roleJournalist = roleRepository.findRoleByRoleName(DatabaseRole.ROLE_JOURNALIST.name());
        final List<User> users = userRepository.findAllByRole(roleJournalist);

        return users
                .stream()
                .map(this::convertUserToJournalist)
                .collect(Collectors.toList());
    }
    
    private JournalistResponse convertUserToJournalist(User user){
        return new JournalistResponse(
                user.getId(),
                user.getLogin()
        );
    }
}