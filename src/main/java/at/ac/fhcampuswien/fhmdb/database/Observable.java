package at.ac.fhcampuswien.fhmdb.database;

import java.util.ArrayList;
import java.util.List;

public interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message);
}








