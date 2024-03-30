package repository

import (
	"context"

	"github.com/ikiwq/blog-api/internal/domain"
	"github.com/jmoiron/sqlx"
)

type mysqlCategoryRepository struct {
	db *sqlx.DB
}

func NewMySqlCategory(db *sqlx.DB) domain.CategoryRepository {
	return &mysqlCategoryRepository{db: db}
}

func (p *mysqlCategoryRepository) GetBySlug(ctx context.Context, slug string) (domain.Category, error) {
	var category domain.Category

	err := p.db.GetContext(ctx, &category, "SELECT * FROM categories WHERE slug = ?", slug)
	if err != nil {
		return domain.Category{}, err
	}

	return category, nil
}

func (p *mysqlCategoryRepository) GetAll(ctx context.Context) ([]domain.Category, int, error) {
	var categories []domain.Category

	err := p.db.SelectContext(ctx, &categories, `
		SELECT c.*
		FROM categories c
		ORDER BY (
			SELECT COUNT(*)
			FROM articles_categories_links acl
			WHERE acl.category_id = c.id
		) DESC
	`)

	if err != nil {
		return nil, 0, err
	}

	var count int
	if err := p.db.GetContext(ctx, &count, "SELECT COUNT(*) FROM categories"); err != nil {
		count = len(categories)
	}

	return categories, count, nil
}
