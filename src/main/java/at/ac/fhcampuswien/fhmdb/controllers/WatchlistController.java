package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable, Observer {

    @FXML
    public JFXListView<MovieEntity> watchlistView;

    protected ObservableList<MovieEntity> observableWatchlist = FXCollections.observableArrayList();

    private final ClickEventHandler onRemoveFromWatchlistClicked = (o) -> {
        if (o instanceof MovieEntity movieEntity) {
            try {
                WatchlistRepository watchlistRepository = WatchlistRepository.getInstance();
                int result = watchlistRepository.removeFromWatchlist(movieEntity.getApiId());
                if (result > 0) {
                    observableWatchlist.remove(movieEntity);
                }
            } catch (DataBaseException e) {
                UserDialog dialog = new UserDialog("Database Error", "Could not remove movie from watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            WatchlistRepository watchlistRepository = WatchlistRepository.getInstance();
            watchlistRepository.addObserver(this);
            List<WatchlistMovieEntity> watchlist = watchlistRepository.getWatchlist();

            MovieRepository movieRepository = MovieRepository.getInstance();
            List<MovieEntity> movies = new ArrayList<>();

            for (WatchlistMovieEntity movie : watchlist) {
                MovieEntity movieEntity = movieRepository.getMovie(movie.getApiId());
                if (movieEntity != null) {
                    movies.add(movieEntity);
                }
            }

            observableWatchlist.addAll(movies);
            watchlistView.setItems(observableWatchlist);
            watchlistView.setCellFactory(movieListView -> new WatchlistCell(onRemoveFromWatchlistClicked));

        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("Database Error", "Could not read movies from DB");
            dialog.show();
            e.printStackTrace();
        }

        if (observableWatchlist.isEmpty()) {
            watchlistView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }
        System.out.println("WatchlistController initialized");
    }

    @Override
    public void update(String message) {
        System.out.println("Update called with message: " + message);  // Debug output
        javafx.application.Platform.runLater(() -> {
            UserDialog dialog = new UserDialog("Watchlist Update", message);
            dialog.show();
        });
    }
}




