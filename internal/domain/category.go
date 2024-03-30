package domain

import (
	"context"
	"time"
)

type Category struct {
	ID          int64     `json:"id" db:"id"`
	Slug        string    `json:"slug" db:"slug"`
	Title       string    `json:"title" db:"title"`
	Description string    `json:"description" db:"description"`
	Img         string    `json:"img" db:"img"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
	PublishedAt time.Time `json:"published_at" db:"published_at"`
	CreatedByID int       `json:"created_by_id" db:"created_by_id"`
	UpdatedByID int       `json:"updated_by_id" db:"updated_by_id"`
}

type CategoryCollection struct {
	Categories []Category `json:"categories"`
	Count      int        `json:"count"`
}

type CategoryRepository interface {
	GetBySlug(context.Context, string) (Category, error)
	GetAll(context.Context) ([]Category, int, error)
}
