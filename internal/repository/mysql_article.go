package repository

import (
	"context"
	"fmt"
	"log"
	"sync"

	"github.com/ikiwq/blog-api/internal/domain"
	"github.com/jmoiron/sqlx"
)

type mysqlArticleRepository struct {
	db *sqlx.DB
}

func NewMySqlArticle(db *sqlx.DB) domain.ArticleRepository {
	return &mysqlArticleRepository{db: db}
}

func (p *mysqlArticleRepository) GetBySlug(ctx context.Context, slug string) (domain.Article, error) {
	var article domain.Article

	err := p.db.GetContext(ctx, &article, "SELECT * FROM articles WHERE slug = ?", slug)
	if err != nil {
		return domain.Article{}, err
	}

	p.populateArticleCategories(ctx, &article)

	return article, nil
}

func (p *mysqlArticleRepository) GetSimilarBySlug(ctx context.Context, slug string, take int) ([]domain.Article, int, error) {
	const query = `
		SELECT *
		FROM articles
		WHERE MATCH(title, content) AGAINST (
			(SELECT title FROM articles WHERE slug = ?) IN NATURAL LANGUAGE MODE
		) AND slug != ?
		LIMIT ?`

	var articles []domain.Article
	if err := p.db.SelectContext(ctx, &articles, query, slug, slug, take); err != nil {
		return nil, 0, err
	}

	p.populateArticlesCategories(ctx, articles)

	return articles, len(articles), nil
}

func (p *mysqlArticleRepository) GetByCategory(ctx context.Context, categorySlug string, take int, page int) ([]domain.Article, int, error) {
	offset := take * page

	const baseQuery = `
		FROM articles
		WHERE id IN (
			SELECT article_id
			FROM articles_categories_links
			WHERE category_id IN (
				SELECT id
				FROM categories
				WHERE slug = ?
			)
		)`

	var articles []domain.Article
	if err := p.db.SelectContext(ctx, &articles, fmt.Sprintf("SELECT * %s LIMIT ? OFFSET ? ", baseQuery), categorySlug, take, offset); err != nil {
		log.Print(err)
		return nil, 0, err
	}

	var count int
	if err := p.db.GetContext(ctx, &count, "SELECT COUNT(*) "+baseQuery, categorySlug); err != nil {
		log.Print(err)
		count = len(articles)
	}

	p.populateArticlesCategories(ctx, articles)

	return articles, count, nil
}

func (p *mysqlArticleRepository) GetAll(ctx context.Context, page int, take int, featured string) ([]domain.Article, int, error) {
	offset := take * page

	var baseQuery = `FROM articles`

	if featured != "" {
		baseQuery = fmt.Sprintf("%s WHERE featured = %s", baseQuery, featured)
	}

	var articles []domain.Article

	var articleQuery = fmt.Sprintf("SELECT * %s ORDER BY created_at DESC LIMIT ? OFFSET ?", baseQuery)

	if err := p.db.SelectContext(ctx, &articles, articleQuery, take, offset); err != nil {
		log.Print(err)
		return nil, 0, err
	}

	var count int

	var countQuery = fmt.Sprintf("SELECT COUNT(*) %s", baseQuery)
	if err := p.db.GetContext(ctx, &count, countQuery); err != nil {
		log.Print(err)
		return articles, 0, err
	}

	p.populateArticlesCategories(ctx, articles)

	return articles, count, nil
}

func (p *mysqlArticleRepository) populateArticlesCategories(ctx context.Context, articles []domain.Article) {
	var wg sync.WaitGroup
	for i := range articles {
		wg.Add(1)
		go func(article *domain.Article) {
			defer wg.Done()
			p.populateArticleCategories(ctx, article)
		}(&articles[i])
	}
	wg.Wait()
}

func (p *mysqlArticleRepository) populateArticleCategories(ctx context.Context, article *domain.Article) {
	var categories []domain.Category

	err := p.db.SelectContext(ctx, &categories, "SELECT * FROM categories WHERE id IN "+
		"(SELECT category_id FROM articles_categories_links WHERE article_id = ?)", article.ID)

	if err != nil {
		categories = make([]domain.Category, 0)
	}

	article.Categories = categories
}
