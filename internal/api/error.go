package api

import "net/http"

func (a *api) errorResponse(w http.ResponseWriter, _ *http.Request, status int, err error) {
	w.Header().Set("X-Error-Info", err.Error())
	http.Error(w, err.Error(), status)
}
