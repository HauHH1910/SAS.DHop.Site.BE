package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.constant.ArticleStatus;
import com.sas.dhop.site.dto.request.ArticleRequest;
import com.sas.dhop.site.dto.response.ArticleResponse;
import com.sas.dhop.site.dto.response.MediaResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.Article;
import com.sas.dhop.site.model.Status;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.repository.ArticleRepository;
import com.sas.dhop.site.service.ArticleService;
import com.sas.dhop.site.service.CloudStorageService;
import com.sas.dhop.site.service.StatusService;
import com.sas.dhop.site.service.UserService;
import com.sas.dhop.site.util.mapper.ArticleMapper;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j(topic = "[Article Service]")
public class ArticleServiceImpl implements ArticleService {

  private final CloudStorageService cloudStorageService;
  private final StatusService statusService;
  private final ArticleRepository articleRepository;
  private final ArticleMapper articleMapper;
  private final UserService userService;

  @Override
  @Transactional
  public ArticleResponse createArticle(ArticleRequest articleRequest) {

    // Upload thumbnail to cloud storage
    List<MediaResponse> mediaRespons = cloudStorageService.uploadImage(articleRequest.thumbnail());
    if (mediaRespons.isEmpty()) {
      throw new BusinessException(ErrorConstant.ARTICLE_NOT_FOUND);
    }

    String thumbnailUrl = mediaRespons.get(0).url();

    Status status = statusService.findStatusOrCreated(ArticleStatus.ACTIVATED_ARTICLE);

    Article article =
        Article.builder()
            .title(articleRequest.title())
            .content(articleRequest.content())
            .authorName(articleRequest.authorName())
            .thumbnail(thumbnailUrl)
            .status(status)
            .build();

    article = articleRepository.save(article);

    return articleMapper.mapToArticleResponse(article);
  }

  @Override
  public ArticleResponse getArticleById(Integer articleId) {
    Article article =
        articleRepository
            .findById(articleId)
            .orElseThrow(() -> new BusinessException(ErrorConstant.ARTICLE_NOT_FOUND));

    return articleMapper.mapToArticleResponse(article);
  }

  @Override
  public ArticleResponse updateAritcle(Integer id, ArticleRequest articleRequest) {
    Article article =
        articleRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException(ErrorConstant.ARTICLE_NOT_FOUND));

    User currentUser = userService.getLoginUser();
    boolean isStaff =
        currentUser.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.STAFF));

    if (!isStaff) {
      throw new BusinessException(ErrorConstant.ROLE_ACCESS_DENIED);
    }

    article.setTitle(articleRequest.title());
    article.setThumbnail(Arrays.toString(articleRequest.thumbnail()));
    article.setContent(articleRequest.content());
    article.setAuthorName(articleRequest.authorName());

    Article updateArticle = articleRepository.save(article);

    return articleMapper.mapToArticleResponse(updateArticle);
  }

  @Override
  public ArticleResponse deleteArticle(Integer id) {
    Article article =
        articleRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException(ErrorConstant.ARTICLE_NOT_FOUND));

    Status inactiveStatus = statusService.getStatus(ArticleStatus.INACTIVE_ARTICLE);
    article.setStatus(inactiveStatus);

    Article updateArticle = articleRepository.save(article);

    return articleMapper.mapToArticleResponse(updateArticle);
  }

  @Override
  public List<ArticleResponse> getAllArticle() {
    return articleRepository.findAll().stream().map(articleMapper::mapToArticleResponse).toList();
  }
}
