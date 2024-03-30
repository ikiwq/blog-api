package api

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"net/http"

	"github.com/ikiwq/blog-api/internal/domain"
)

func (a *api) getArticleBySlugHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()

	slug := r.PathValue("slug")
	if slug == "" || len(slug) > MAX_SLUG_SIZE {
		a.errorResponse(w, r, http.StatusBadRequest, errors.New("invalid slug provided"))
		return
	}

	article, err := a.articleRepository.GetBySlug(ctx, slug)

	switch {
	case errors.Is(err, sql.ErrNoRows):
		a.errorResponse(w, r, http.StatusNotFound, fmt.Errorf("no article found with slug %s", slug))
	case err != nil:
		a.errorResponse(w, r, http.StatusInternalServerError, errors.New("internal server error"))
	default:
		WriteJSON(w, http.StatusOK, article)
	}
}

func (a *api) getAllArticlesHandler(w http.ResponseWriter, r *http.Request) {
	context, cancel := context.WithCancel(r.Context())
	defer cancel()

	slug := r.PathValue("slug")
	if slug == "" || len(slug) > MAX_SLUG_SIZE {
		a.errorResponse(w, r, http.StatusBadRequest, errors.New("invalid slug provided"))
		return
	}

	take := GetIntegerQueryParamOrDefault(r, "take", MAX_TAKE, DEFAULT_TAKE)
	page := GetIntegerQueryParamOrDefault(r, "take", 100, DEFAULT_PAGE) - 1

	articles, _, _ := a.articleRepository.GetAll(context, take, page, "")

	WriteJSON(w, 200, articles)
}

func (a *api) getSimilarArticles(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()

	slug := r.PathValue("slug")
	if slug == "" || len(slug) > MAX_SLUG_SIZE {
		a.errorResponse(w, r, http.StatusBadRequest, errors.New("invalid slug provided"))
		return
	}

	take := GetIntegerQueryParamOrDefault(r, "take", MAX_TAKE, DEFAULT_TAKE)

	articles, count, err := a.articleRepository.GetSimilarBySlug(ctx, slug, take)
	switch {
	case errors.Is(err, sql.ErrNoRows):
		a.errorResponse(w, r, http.StatusNotFound, fmt.Errorf("no articles found with slug %s", slug))
	case err != nil:
		a.errorResponse(w, r, http.StatusInternalServerError, errors.New("internal server error"))
	default:
		articleCollection := domain.ArticleCollection{
			Articles: articles,
			Count:    count,
		}
		WriteJSON(w, http.StatusOK, articleCollection)
	}
}
