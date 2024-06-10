package at.ac.fhcampuswien.fhmdb.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import java.util.Comparator;
import java.util.List;

public class DescendingState implements SortState {
    @Override
    public void sort(List<Movie> movies) {
        System.out.println("SORTING DESC");
        movies.sort(Comparator.comparing(Movie::getTitle).reversed());
    }

    @Override
    public void setMovieList(List<Movie> movies) {}
}

