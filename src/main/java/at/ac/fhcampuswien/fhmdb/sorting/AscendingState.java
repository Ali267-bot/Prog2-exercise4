package at.ac.fhcampuswien.fhmdb.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import java.util.Comparator;
import java.util.List;

public class AscendingState implements SortState {
    @Override
    public void sort(List<Movie> movies) {
        System.out.println("SORTING ASC");
        movies.sort(Comparator.comparing(Movie::getTitle));
    }

    @Override
    public void setMovieList(List<Movie> movies) {}
}

