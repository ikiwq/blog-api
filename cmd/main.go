package main

import (
	"fmt"
	"log"
	"os"

	"github.com/ikiwq/blog-api/internal/api"
	"github.com/joho/godotenv"
)

func main() {
	if err := godotenv.Load(); err != nil {
		log.Fatal("Error loading .env file")
	}

	connStr := os.Getenv("CONN_STR")

	api := api.NewApi("localhost", "8081", connStr)

	api.Start()
}
