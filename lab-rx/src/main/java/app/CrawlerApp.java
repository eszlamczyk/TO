package app;


import java.io.File;
import java.io.IOException;
import java.util.List;

public class CrawlerApp {

    public static final String SCRAPER_API_KEY = "d4f43716d7a62e1292b1383d55216baa";

    private static final List<String> TOPICS = List.of("Agent Cooper", "Sherlock", "Poirot", "Detective Monk");


    public static void main(String[] args) throws IOException, InterruptedException {
        PhotoCrawler photoCrawler = new PhotoCrawler();
        photoCrawler.resetLibrary();
        //photoCrawler.downloadPhotoExamples();
        //photoCrawler.downloadPhotosForQuery(TOPICS.get(0));
        photoCrawler.downloadPhotosForMultipleQueries(TOPICS);
        Thread.sleep(100_000);
    }
}