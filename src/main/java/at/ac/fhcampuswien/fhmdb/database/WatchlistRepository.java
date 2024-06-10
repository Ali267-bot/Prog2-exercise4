package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable {

    private static WatchlistRepository instance;
    private List<Observer> observers = new ArrayList<>();
    private Dao<WatchlistMovieEntity, Long> dao;

    // Private constructor to prevent external instantiation
    private WatchlistRepository() throws DataBaseException {
        try {
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DataBaseException("Failed to initialize DAO: " + e.getMessage());
        }
    }

    public static synchronized WatchlistRepository getInstance() throws DataBaseException {
        if (instance == null) {
            System.out.println("New Instance of WatchlistRepository created!");
            instance = new WatchlistRepository();
        } else {
            System.out.println("Existing Instance of WatchlistRepository used!");
        }
        return instance;
    }

    @Override
    public void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        System.out.println("Notifying observers with: " + message);
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while reading watchlist");
        }
    }

    public int addToWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            long count = dao.queryBuilder().where().eq("apiId", movie.getApiId()).countOf();
            if (count == 0) {
                dao.create(movie);
                notifyObservers("Movie successfully added to watchlist.");
                return 1;
            } else {
                notifyObservers("Movie already in the watchlist.");
                return 0;
            }
        } catch (Exception e) {
            notifyObservers("Error while adding movie to watchlist: " + e.getMessage());
            return -1;
        }
    }

    public int removeFromWatchlist(String apiId) throws DataBaseException {
        try {
            int rowsAffected = dao.delete(dao.queryBuilder().where().eq("apiId", apiId).query());
            if (rowsAffected > 0) {
                notifyObservers("Movie successfully removed from watchlist: " + apiId);
                return rowsAffected;
            } else {
                notifyObservers("Movie not found in watchlist: " + apiId);
                return 0;
            }
        } catch (Exception e) {
            notifyObservers("Error while removing movie from watchlist: " + e.getMessage());
            throw new DataBaseException("Error while removing from watchlist");
        }
    }
}















