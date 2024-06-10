package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;

public class MovieAPIRequestBuilder {
    private final StringBuilder url;


    public MovieAPIRequestBuilder(String base) {
        System.out.println("Using API Request Builder!");
        this.url = new StringBuilder(base);
    }


    public MovieAPIRequestBuilder query(String query) {
        if (query != null && !query.isEmpty()) {
            appendParam("query", query);
        }
        return this;
    }


    public MovieAPIRequestBuilder genre(Genre genre) {
        if (genre != null) {
            appendParam("genre", genre.toString());
        }
        return this;
    }


    public MovieAPIRequestBuilder releaseYear(String releaseYear) {
        if (releaseYear != null) {
            appendParam("releaseYear", releaseYear);
        }
        return this;
    }


    public MovieAPIRequestBuilder ratingFrom(String ratingFrom) {
        if (ratingFrom != null) {
            appendParam("ratingFrom", ratingFrom);
        }
        return this;
    }


    private void appendParam(String key, String value) {
        if (url.toString().contains("?")) {
            url.append("&");
        } else {
            url.append("?");
        }
        url.append(key).append("=").append(value);
    }


    public String build() {
        return url.toString();
    }
}









