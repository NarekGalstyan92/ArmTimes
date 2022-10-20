package com.armtimes.armtimes.service;


import com.armtimes.armtimes.entity.Article;
import com.armtimes.armtimes.entity.Role;
import com.armtimes.armtimes.entity.User;
import com.armtimes.armtimes.repository.ArticleRepository;
import com.armtimes.armtimes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;


    public void saveNewArticle(Article article) {
        if (article.getUser() != null && article.getUser().getId() == 0) {
            article.setUser(null);
        }
        articleRepository.save(article);
    }

/*  Keep it simple stupid (KISS)
don't write a code which can make problems for project.
It works, but if user call this method, server could blow up))

    public List<Task> findAll() {
        return taskRepository.findAll();
    }
*/

    public Page<Article> findArticleByUserRole(User user, Pageable pageable) {
        return user.getRole() == Role.USER ?
                articleRepository.findAllByUser_Id(user.getId(), pageable)
                : articleRepository.findAll(pageable);
    }

    public void changeArticleUser(int userId, int articleId) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (articleOptional.isPresent() && userOptional.isPresent()) {
            Article article = articleOptional.get();
            User user = userOptional.get();
            if (article.getUser() != user) {
                article.setUser(user);
                articleRepository.save(article);
            }
        } else if (articleOptional.isPresent() && userId == 0) {
            articleOptional.get().setUser(null);
            articleRepository.save(articleOptional.get());
        }
    }

}
