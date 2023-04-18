package ru.ntv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ntv.dto.response.boss.JournalistResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.users.User;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.exception.NotRightRoleException;
import ru.ntv.repo.article.ArticleRepository;
import ru.ntv.repo.user.RoleRepository;
import ru.ntv.repo.user.UserRepository;


import java.io.IOException;
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


    @Transactional(transactionManager = "transactionManager")
    public String retireJournalist(int idJournalist) throws NotRightRoleException{

        final var journalist = userRepository.findById(idJournalist).orElseThrow(); //todo throw custom Exception if user is not found
        System.out.println(journalist.getLogin() + " " + journalist.getId() + " " + journalist.getRole().getRoleName());
        if (!Objects.equals(journalist.getRole().getRoleName(), DatabaseRole.ROLE_JOURNALIST.name())) throw new NotRightRoleException("Это не журналист"); //todo throw custom Exception that isn't journalist

        journalist.setRole(
                roleRepository.findRoleByRoleName(
                        DatabaseRole.ROLE_CLIENT.name()
                )
        );


        List<Article> articles = articleRepository.findAllByJournalistName(journalist.getLogin());

        articles.forEach(e -> System.out.println(e.getJournalistName()));
        articles.forEach( a -> a.setJournalistName(null));


        userRepository.save(journalist);
        articleRepository.saveAll(articles);

        return "уволен";


    }


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