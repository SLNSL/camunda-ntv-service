package ru.ntv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ntv.dto.response.boss.JournalistResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.users.User;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.exception.NotRightRoleException;
import ru.ntv.repo.ArticleRepository;
import ru.ntv.repo.RoleRepository;
import ru.ntv.repo.UserRepository;

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
    public String dismissJournalist(int idJournalist){
        final var journalist = userRepository.findById(idJournalist).orElseThrow();
        if (!Objects.equals(journalist.getRole().getRoleName(), DatabaseRole.ROLE_JOURNALIST.name()))
            throw new NotRightRoleException("Это не журналист");

        journalist.setRole(
            roleRepository.findRoleByRoleName(
                DatabaseRole.ROLE_CLIENT.name()
            )
        );

        List<Article> articles = articleRepository.findAllByJournalistName(journalist.getLogin());
        articles.forEach(article -> article.setJournalistName(null));
        
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
    
    public User getCurrentUser(){
        final var userName = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByLogin(userName).get();
    }
}