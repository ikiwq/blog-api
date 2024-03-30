package domain

import (
	"context"
	"time"
)

type Article struct {
	ID            int64     `json:"id" db:"id"`
	Slug          string    `json:"slug" db:"slug"`
	Title         string    `json:"title" db:"title"`
	Excerpt       string    `json:"excerpt" db:"excerpt"`
	Img           string    `json:"img" db:"img"`
	ReadingTime   string    `json:"reading_time" db:"reading_time"`
	Featured      bool      `json:"featured" db:"featured"`
	EditorsChoice bool      `json:"editors_choice" db:"editors_choice"`
	CreatedAt     time.Time `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time `json:"updated_at" db:"updated_at"`
	PublishedAt   time.Time `json:"published_at" db:"published_at"`
	CreatedByID   int       `json:"created_by_id" db:"created_by_id"`
	UpdatedByID   int       `json:"updated_by_id" db:"updated_by_id"`
	Content       string    `json:"content" db:"content"`

	Categories []Category `json:"categories" db:"-"`
}

type ArticleCollection struct {
	Articles []Article `json:"articles"`
	Count    int       `json:"count"`
}

type ArticleRepository interface {
	GetBySlug(context.Context, string) (Article, error)
	GetAll(context.Context, int, int, string) ([]Article, int, error)
	GetByCategory(context.Context, string, int, int) ([]Article, int, error)
	GetSimilarBySlug(context.Context, string, int) ([]Article, int, error)
}
