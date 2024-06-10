package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class WatchlistCell extends ListCell<MovieEntity> {
    private final Label title = new Label();
    private final Label description = new Label();
    private final Label genre = new Label();
    private final JFXButton detailBtn = new JFXButton("Show Details");
    private final JFXButton removeBtn = new JFXButton("Remove");
    private final HBox header = new HBox(title, detailBtn, removeBtn);
    private final VBox layout = new VBox(header, description, genre);
    private boolean collapsedDetails = true;

    public WatchlistCell(ClickEventHandler<MovieEntity> removeFromWatchlistClick) {
        super();
        styleComponents();
        setupButtonActions(removeFromWatchlistClick);
    }

    private void styleComponents() {
        // Styling components
        detailBtn.setStyle("-fx-background-color: #f5c518;");
        HBox.setMargin(detailBtn, new Insets(0, 10, 0, 10));
        removeBtn.setStyle("-fx-background-color: #f5c518;");
        title.getStyleClass().add("text-yellow");
        description.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        genre.setStyle("-fx-font-style: italic");
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setHgrow(title, Priority.ALWAYS);
        title.setMaxWidth(Double.MAX_VALUE);
        layout.setPadding(new Insets(10));
    }

    private void setupButtonActions(ClickEventHandler<MovieEntity> removeFromWatchlistClick) {
        detailBtn.setOnMouseClicked(mouseEvent -> toggleDetails());
        removeBtn.setOnMouseClicked(mouseEvent -> removeFromWatchlistClick.onClick(getItem()));
    }

    private void toggleDetails() {
        if (collapsedDetails) {
            layout.getChildren().add(getDetails());
            collapsedDetails = false;
            detailBtn.setText("Hide Details");
        } else {
            layout.getChildren().remove(3);
            collapsedDetails = true;
            detailBtn.setText("Show Details");
        }
        setGraphic(layout);
    }

    private VBox getDetails() {
        VBox details = new VBox();
        details.getChildren().addAll(
                new Label("Release Year: " + getItem().getReleaseYear()),
                new Label("Length: " + getItem().getLengthInMinutes() + " minutes"),
                new Label("Rating: " + getItem().getRating()),
                new Label("Genres: " + getItem().getGenres())
        );
        details.getChildren().forEach(label -> label.getStyleClass().add("text-white"));
        return details;
    }

    @Override
    protected void updateItem(MovieEntity movieEntity, boolean empty) {
        super.updateItem(movieEntity, empty);

        if (empty || movieEntity == null) {
            setGraphic(null);
            setText(null);
        } else {
            updateContent(movieEntity);
        }
    }

    private void updateContent(MovieEntity movieEntity) {
        title.setText(movieEntity.getTitle());
        description.setText(movieEntity.getDescription() != null ? movieEntity.getDescription() : "No description available");
        genre.setText(movieEntity.getGenres().stream().map(Enum::toString).collect(Collectors.joining(", ")));

        // Safely updating layout depending on scene availability
        if (getScene() != null) {
            updateLayout();
        } else {
            sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) updateLayout();
            });
        }
        setGraphic(layout);
    }

    private void updateLayout() {
        if (getScene() != null) {
            description.setMaxWidth(getScene().getWidth() - 30);
        }
    }
}
