package com.sas.dhop.site.util.mapper;

import com.sas.dhop.site.dto.response.ArticleResponse;
import com.sas.dhop.site.model.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleResponse mapToArticleResponse(Article article);
}
