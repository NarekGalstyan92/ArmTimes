package com.armtimes.armtimes.controller;


import com.armtimes.armtimes.entity.Article;
import com.armtimes.armtimes.entity.User;
import com.armtimes.armtimes.security.CurrentUser;
import com.armtimes.armtimes.service.ArticleService;
import com.armtimes.armtimes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/articles/add")
    public String addTaskPage(ModelMap modelMap) {
        List<User> users = userService.findAllUsers();
        modelMap.addAttribute("users", users);
        return "addArticle";
    }

    @PostMapping("/articles/add")
    public String addArticle(@ModelAttribute Article article) {
        articleService.saveNewArticle(article);
        return "redirect:/articles";
    }

    @GetMapping("/articles")
    public String articlePage(@RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           ModelMap modelMap,
                           @AuthenticationPrincipal CurrentUser currentUser) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Article> taskByUserRole = articleService.findArticleByUserRole(currentUser.getUser(),
                PageRequest.of(currentPage - 1, pageSize));

        modelMap.addAttribute("articles", taskByUserRole);
        int totalPages = taskByUserRole.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("users", userService.findAllUsers());
        return "articles";
    }

    @PostMapping("/articles/changeUser")
    public String changeUser(@RequestParam("userId") int userId, @RequestParam("articleId") int taskId) {
        articleService.changeArticleUser(userId, taskId);
        return "redirect:/articles";
    }
}
