package com.example.android.top10downloadedappnew.data;
/**
 * Parses the HTML data and retrieves relevant information.
 *
 * @author Rohan Menezes
 */

import com.example.android.top10downloadedappnew.models.HtmlApp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class HTMLParser {
    private static final String TAG = "ParseApplications";

    /** Contains a list of Apps */
    private ArrayList<HtmlApp> applications;

    /** Parses the HTML data */
    public HTMLParser() {
        this.applications = new ArrayList<>();
    }

    /** Gets list of apps
     *
     * @return list of apps
     */
    public ArrayList<HtmlApp> getApplications() {
        return applications;
    }

    /** Parses the HTML document retrieved from the Jsoup AsyncTask
     *
     * @param d Document containing all HTML elements
     * @return parsing is successful so returns true
     */
    public boolean parse(Document d) {

        HtmlApp htmlApp;
        //Retrieves all <a> tag elements
        Elements aTags = d.getElementsByTag("a");
        //Retrieves all <img> tag elements
        Elements imgTags = d.getElementsByTag("img");
        //Stores all <a> tag that have the class "title"
        Elements titleLinks = new Elements();
        //Loops through all <a> tags retrieving the tags with class "title"
        for (Element link : aTags) {
            if (link.hasClass("title")) {
                titleLinks.add(link);

            }
        }
        /*
        Loops through all tags with class "title" and retrieves specific information
        such as the name, artist, appURL and imgURL of the app.
         */
        for (Element titleLink : titleLinks) {

            htmlApp = new HtmlApp();
            //Retrieves the name of the app
            String appName = titleLink.text();
            /* Trims all characters before the first " " (space)
            For example.
            appName: 1. Helix Jump
            trimmedAppName: Helix Jump
            */
            String trimmedAppName = appName.substring(appName.indexOf(" ") + 1);
            htmlApp.setName(trimmedAppName.trim());
            //Sets the URL of the app
            htmlApp.setAppURL("https://play.google.com" + titleLink.attr("href"));
            //Sets the artist of the app
            htmlApp.setArtist(titleLink.nextElementSibling().text());
            //Loops through the image tags and retrieves the correct image size
            for (Element img : imgTags) {
                if (img.attr("alt").equalsIgnoreCase(titleLink.attr("title"))) {
                    //Some images have no 'https:/' at the beginning of the URL
                    //so we can append this on if necessary
                    if (!img.attr("data-cover-small").contains("https:/")) {
                        htmlApp.setImageURL("https:/" + img.attr("data-cover-small"));
                    } else {
                        htmlApp.setImageURL(img.attr("data-cover-small"));
                    }

                    break;
                }
            }

            applications.add(htmlApp);
        }
        return true;
    }

}
