package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.ArticleRequest;
import com.sas.dhop.site.dto.response.ArticleResponse;
import com.sas.dhop.site.service.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Article")
@Tag(name = "[Article Controller]")
@Slf4j(topic = "[Article Controller]")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/get-all-article")
    public ResponseData<List<ArticleResponse>> getAllArticle(){
        return ResponseData.<List<ArticleResponse>>builder()
                .message(ResponseMessage.GET_ALL_ARTICLE_SUCCESSFULLY)
                .data(articleService.getAllArticle())
                .build();
    }

    @DeleteMapping("/delete-article/{articleId}")
    public ResponseData<ArticleResponse> removeArticle(@PathVariable Integer articleId){
        return ResponseData.<ArticleResponse>builder()
                .message(ResponseMessage.DELETE_ARTICLE_SUCCESSFULLY)
                .data(articleService.deleteArticle(articleId))
                .build();
    }

    @GetMapping("/get-article-by-id/{articleId}")
    public ResponseData<ArticleResponse> getArticleById(@PathVariable Integer articleId){
        return ResponseData.<ArticleResponse>builder()
                .message(ResponseMessage.GET_ARTICLE_BY_ID)
                .data(articleService.getArticleById(articleId))
                .build();
    }

    @PostMapping("/create-article")
    public ResponseData<ArticleResponse> creatArticle(@RequestBody ArticleRequest articleRequest) {
        return ResponseData.<ArticleResponse>builder()
                .message(ResponseMessage.CREATE_ARTICLE_SUCCESSFULLY)
                .data(articleService.createArticle(articleRequest))
                .build();
    }

    @PostMapping("/update-article/{articleId}")
    public ResponseData<ArticleResponse> updateArticle(@PathVariable Integer articleId, @RequestBody ArticleRequest articleRequest) {
        return ResponseData.<ArticleResponse>builder()
                .message(ResponseMessage.UPDATE_ARTICLE_SUCCESSFULLY)
                .data(articleService.updateAritcle(articleId, articleRequest))
                .build();
    }


}
