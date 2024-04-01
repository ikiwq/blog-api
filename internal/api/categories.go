package api

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"log"
	"net/http"

	"github.com/ikiwq/blog-api/internal/domain"
)

func (a *api) getCategoryBySlugHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()

	slug := r.PathValue("slug")
	if slug == "" || len(slug) > MAX_SLUG_SIZE {
		a.errorResponse(w, r, http.StatusBadRequest, errors.New("invalid slug provided"))
		return
	}

	category, err := a.categoryRepository.GetBySlug(ctx, slug)
	switch {
	case errors.Is(err, sql.ErrNoRows):
		a.errorResponse(w, r, http.StatusNotFound, fmt.Errorf("no category found with slug %s", slug))
	case err != nil:
		a.errorResponse(w, r, http.StatusInternalServerError, errors.New("internal server error"))
	default:
		WriteJSON(w, http.StatusOK, category)
	}
}

func (a *api) getAllCategories(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()

	categories, count, err := a.categoryRepository.GetAll(ctx)
	if err != nil {
		a.errorResponse(w, r, http.StatusInternalServerError, errors.New("internal server error"))
		log.Print("Error while retrieving all categories: ", err)
		return
	}

	categoriesCollection := domain.CategoryCollection{
		Categories: categories,
		Count:      count,
	}

	WriteJSON(w, http.StatusOK, categoriesCollection)
}

func (a *api) getCategoryArticles(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()

	slug := r.PathValue("slug")
	if slug == "" || len(slug) > MAX_SLUG_SIZE {
		a.errorResponse(w, r, http.StatusBadRequest, errors.New("invalid slug provided"))
		return
	}

	take := GetIntegerQueryParamOrDefault(r, "take", MAX_TAKE, DEFAULT_TAKE)
	page := GetIntegerQueryParamOrDefault(r, "page", 100, DEFAULT_PAGE) - 1

	articles, count, err := a.articleRepository.GetByCategory(ctx, slug, take, page)
	switch {
	case errors.Is(err, sql.ErrNoRows):
		a.errorResponse(w, r, http.StatusNotFound, fmt.Errorf("no category found with slug %s", slug))
	case err != nil:
		a.errorResponse(w, r, http.StatusInternalServerError, errors.New("internal server error"))
	default:
		articlesCollection := domain.ArticleCollection{
			Articles: articles,
			Count:    count,
		}
		WriteJSON(w, http.StatusOK, articlesCollection)
	}
}
