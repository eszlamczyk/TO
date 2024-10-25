package controller;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Gallery;
import model.Photo;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import util.PhotoDownloader;

public class GalleryController {


    private Gallery galleryModel;

    @FXML
    public TextField searchTextField;

    @FXML
    public ListView<Photo> imagesListView;

    @FXML
    private TextField imageNameField;

    @FXML
    public ImageView imageView;

    @FXML
    public void initialize() {
        imagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Photo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView photoIcon = new ImageView(item.getPhotoData());
                    photoIcon.setPreserveRatio(true);
                    photoIcon.setFitHeight(50);
                    setGraphic(photoIcon);
                }
            }
        });

        imagesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                bindSelectedPhoto(newValue);
            }
        });
    }

    public void setModel(Gallery gallery) {
        this.galleryModel = gallery;

        imagesListView.setItems(gallery.getPhotos());

        if (!gallery.getPhotos().isEmpty()) {
            imagesListView.getSelectionModel().select(0);
        }
    }


    private void bindSelectedPhoto(Photo selectedPhoto) {
        imageNameField.textProperty().bindBidirectional(selectedPhoto.nameProperty());
        imageView.imageProperty().bind(selectedPhoto.photoDataProperty());
    }

    public void searchButtonClicked(ActionEvent _event) {
        //System.out.println("Started");
        PhotoDownloader photoDownloader = new PhotoDownloader();
        //System.out.println("Downloader done");

        Platform.runLater(() -> galleryModel.clear());

        //System.out.println("Clear done");

        Observable<Photo> photoObservable = photoDownloader.searchForPhotos(searchTextField.getText())
                .subscribeOn(Schedulers.io());

        photoObservable
                .observeOn(JavaFxScheduler.platform())
                .doOnNext(photo -> Platform.runLater(() -> galleryModel.addPhoto(photo)))
                .doOnComplete(() -> System.out.println("Download and UI update complete"))
                .subscribe();

        //System.out.println("Subscribed");
    }
}

