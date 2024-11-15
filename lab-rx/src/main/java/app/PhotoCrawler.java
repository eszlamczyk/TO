package app;

import driver.DuckDuckGoDriver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.Photo;
import model.PhotoSize;
import util.PhotoDownloader;
import util.PhotoProcessor;
import util.PhotoSerializer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoCrawler {

    private static final Logger log = Logger.getLogger(PhotoCrawler.class.getName());

    private final PhotoDownloader photoDownloader;

    private final PhotoSerializer photoSerializer;

    private final PhotoProcessor photoProcessor;

    public PhotoCrawler() throws IOException {
        this.photoDownloader = new PhotoDownloader();
        this.photoSerializer = new PhotoSerializer("./photos");
        this.photoProcessor = new PhotoProcessor();
    }

    public void resetLibrary() throws IOException {
        photoSerializer.deleteLibraryContents();
    }

    public void downloadPhotoExamples() {
            photoDownloader.getPhotoExamples()
                    .compose(this::processPhotos)
                    .subscribe(photoSerializer::savePhoto);
    }

    public void downloadPhotosForQuery(String query) throws IOException {
        photoDownloader.searchForPhotos(query)
                .compose(this::processPhotos)
                .subscribe(photoSerializer::savePhoto,
                    error -> log.log(Level.WARNING, "Could not download a photo", error) );
    }



    public void downloadPhotosForMultipleQueries(List<String> queries) {
        photoDownloader.searchForPhotos(queries)
                .compose(this::processPhotos)
                .subscribe(photoSerializer::savePhoto,
                    error -> log.log(Level.WARNING, "Could not download a photo", error));
    }

    public Observable<Photo> processPhotos(Observable<Photo> observablePhotos){
        return observablePhotos
                .groupBy(PhotoSize::resolve)
                .flatMap(groupedObservable -> switch (groupedObservable.getKey()) {
                    case SMALL -> Observable.empty();
                    case MEDIUM -> groupedObservable
                            .buffer(5, TimeUnit.SECONDS)
                            .flatMap(Observable::fromIterable);
                    case LARGE -> groupedObservable
                            .observeOn(Schedulers.computation())
                            .map(photoProcessor::convertToMiniature);
                });
    }

}
