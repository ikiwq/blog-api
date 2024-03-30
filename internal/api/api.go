package api

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"

	"github.com/ikiwq/blog-api/internal/domain"
	"github.com/ikiwq/blog-api/internal/repository"
	"github.com/jmoiron/sqlx"
)

var (
	DEFAULT_PAGE  = 1
	DEFAULT_TAKE  = 10
	MAX_TAKE      = 30
	MAX_SLUG_SIZE = 40
)

type api struct {
	apiAddress string
	apiPort    string

	httpClient   *http.Client
	dbConnection *sqlx.DB

	articleRepository  domain.ArticleRepository
	categoryRepository domain.CategoryRepository
}

func WriteJSON(w http.ResponseWriter, status int, v any) error {
	w.WriteHeader(status)
	w.Header().Set("Content-Type", "application/json")

	return json.NewEncoder(w).Encode(v)
}

func NewApi(apiAddress string, apiPort string, connectionString string) *api {
	client := &http.Client{}

	conn := repository.InitDB(connectionString)
	articleRepository := repository.NewMySqlArticle(conn)
	categoryRepository := repository.NewMySqlCategory(conn)

	return &api{
		apiAddress: apiAddress,
		apiPort:    apiPort,

		httpClient:   client,
		dbConnection: conn,

		articleRepository:  articleRepository,
		categoryRepository: categoryRepository,
	}
}

func (a *api) buildRoutes() *http.ServeMux {
	r := http.NewServeMux()

	// Articles
	r.HandleFunc("GET /api/v1/articles", a.getAllArticlesHandler)
	r.HandleFunc("GET /api/v1/articles/{slug}", a.getArticleBySlugHandler)
	r.HandleFunc("GET /api/v1/articles/{slug}/similar", a.getSimilarArticles)

	// Categories
	r.HandleFunc("GET /api/v1/categories", a.getAllCategories)
	r.HandleFunc("GET /api/v1/categories/{slug}", a.getCategoryBySlugHandler)
	r.HandleFunc("GET /api/v1/categories/{slug}/articles", a.getCategoryArticles)

	return r
}

func (a *api) Start() {
	r := a.buildRoutes()

	listenAddr := fmt.Sprintf("%s:%s", a.apiAddress, a.apiPort)
	http.ListenAndServe(listenAddr, r)
}

func (a *api) Exit() {
	a.dbConnection.Close()
}

func GetIntegerQueryParamOrDefault(r *http.Request, queryParam string, maxValue int, defaultValue int) int {
	value, err := strconv.Atoi(r.URL.Query().Get(queryParam))
	if err != nil || value > maxValue || value <= 0 {
		value = defaultValue
	}

	return value
}
