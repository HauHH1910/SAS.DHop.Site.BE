package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.ArticleRequest;
import com.sas.dhop.site.dto.response.ArticleResponse;

import java.util.List;

public interface ArticleService {

    ArticleResponse createArticle(ArticleRequest articleRequest);

    ArticleResponse getArticleById(Integer id);

    ArticleResponse updateAritcle(Integer id, ArticleRequest articleRequest);

    ArticleResponse deleteArticle(Integer id);

    List<ArticleResponse> getAllArticle();


}
