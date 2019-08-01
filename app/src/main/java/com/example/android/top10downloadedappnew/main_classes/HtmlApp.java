package com.example.android.top10downloadedappnew.main_classes;

/**
 * Represents an App based on HTML data
 *
 * @author Rohan Menezes
 */
public class HtmlApp {

    private String name;
    private String artist;
    private String imageURL;
    private String appURL;
    public String genre;
    //Toggles True/False if the user favourites an App
    private boolean isFavourite = false;

    /**
     * Creates an HtmlApp Object
     */
    public HtmlApp() {
    }

    /**
     * Gets the URL of the app
     *{@link HtmlApp#appURL}
     * @return A string representing the URL of the app
     */
    public String getAppURL() {
        return appURL;
    }

    /**
     * Sets the URL for the App
     *
     * @param appURL A string containing the URL for the app
     */
    public void setAppURL(String appURL) {
        this.appURL = appURL;
    }

    /**
     * Gets the name of the App
     *
     * @return A string representing the name of the app
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the App
     *
     * @param name: A string containing the name of the app
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the Artist
     *
     * @return A string representing the name of the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the name of the Artist
     *
     * @param artist A string containing the name of the artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Gets the URL of an image
     *
     * @return A string representing the URL of the image
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Sets the URL of the image
     *
     * @param imageURL A string containing the URL of the image
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Toggles False if the user favourites the app
     * Toggles True if the user does not favourite the app
     */
    public void isFavourite() {
        isFavourite = !isFavourite;
    }

    /**
     * Gets a value depending if the user favourites the app or not
     *
     * @return A boolean value representing if the user favourites the app or not
     */
    public boolean getFavourite() {
        return isFavourite;
    }

    /**
     * Gets the genre of the app
     *
     * @return A string representing the genre of the app
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the app
     *
     * @param genre A string containing the genre of the app
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /*This toString method makes it easy to convert class info into a more visual
    representation i.e String. So rather than reading the class info in the form of
    hexadecimals or some unreadable format, we can immediately convert this to a string.
     */
    @Override
    public String toString() {
        return "HtmlApp{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", appURL='" + appURL + '\'' +
                ", genre='" + genre + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
