package ru.ntv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntv.dto.response.boss.JournalistResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.users.User;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.repo.article.ArticleRepository;
import ru.ntv.repo.user.RoleRepository;
import ru.ntv.repo.user.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ArticleRepository articleRepository;



//    public void retireJournalist(int idBoss, int idJournalist){
//
//        final var boss = userRepository.findById(idBoss).get(); //todo throw custom Exception if user is not found
//        if (!Objects.equals(boss.getRole().getRoleName(), DatabaseRole.ROLE_BOSS.name())) throw new RuntimeException(); //todo throw custom Exception that isn't boss
//
//
//        final var user = userRepository.findById(idJournalist).get(); //todo throw custom Exception if user is not found
//        if (!Objects.equals(user.getRole().getRoleName(), DatabaseRole.ROLE_JOURNALIST.name())) throw new RuntimeException(); //todo throw custom Exception that isn't journalist
//
//        user.setRole(
//                roleRepository.findRoleByRoleName(
//                        DatabaseRole.ROLE_CLIENT.name()
//                )
//        );
//        userRepository.save(user);
//
//        List<Article> articles = articleRepository.findAllB
//
//
//
//    }


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