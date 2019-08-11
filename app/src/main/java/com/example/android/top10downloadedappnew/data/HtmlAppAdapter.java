package com.example.android.top10downloadedappnew.main_classes;
/**
 * Displays HtmlApp Data
 * @author Rohan Menezes
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.top10downloadedappnew.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HtmlAppAdapter extends RecyclerView.Adapter<HtmlAppAdapter.AppViewHolder> {
    private static final String TAG = "HtmlAppAdapter";
    private List<HtmlApp> applications;
    private List<HtmlApp> applications_copy;
    private List<HtmlApp> favourites;
    private Context mContext;
    private Document htmlDocument;
    private String genre;

    /** Creates the Adapter to display content
     *
     * @param context current state of application
     * @param mApplications list of apps downloaded from the google play store
     */
    public HtmlAppAdapter(Context context, List<HtmlApp> mApplications) {
        applications = mApplications;
        mContext = context;
        applications_copy = new ArrayList<>();
        applications_copy.addAll(applications);
        favourites = new ArrayList<>();
    }


    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_record, parent, false);
        return new AppViewHolder(view);
    }

    /** Binds the data to the layout objects
     */
    @Override
    public void onBindViewHolder(final AppViewHolder holder, int position) {
        //Retrieving which item user clicked
        final HtmlApp currentApp = applications.get(position);
        holder.tvName.setText(currentApp.getName());
        holder.tvArtist.setText(currentApp.getArtist());
        Glide.with(mContext).load(currentApp.getImageURL()).into(holder.thumbnail);

        /*
            Add favourite apps to the favourite list
         */
        if (currentApp.getFavourite()) {
            if(!favourites.contains(currentApp)){
                Log.d(TAG, "onBindViewHolder: Adding retrieved app to favourites list");
                favourites.add(currentApp);
            }else{
                Log.d(TAG, "onBindViewHolder: Retrieved app is already in favourite list");
            }
            holder.favourite.setActivated(true);

        } else {
            Log.d(TAG, "onBindViewHolder: current app is unchecked");
            holder.favourite.setActivated(false);
        }

        /*
            - When user selects favourite icon, we add the app to the favourites list.
            - When user de-selects favourite icon, we remove the app from the favourites list.
         */
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.favourite.setActivated(!holder.favourite.isActivated());
                currentApp.isFavourite();
                Log.d(TAG, "onClick: currentApp checked value is: " + currentApp.getFavourite());
                /*
                    For all favourite apps, we retrieve the genre using an async task.
                 */
                if (currentApp.getFavourite()) {
                    GetGenre getGenre = new GetGenre();
                    try{
                        genre = getGenre.execute(currentApp.getAppURL()).get();
                        currentApp.setGenre(genre);
                        Log.d(TAG, "onClick: Retrieved the genre for: " + currentApp.getName());
                    }catch(Exception e){
                        Log.d(TAG, "onClick: " + e.getMessage());
                    }
                    Log.d(TAG, "onClick: The app to be added to favourites is: " + currentApp.getName());
                    favourites.add(currentApp);
                } else {
                    Log.d(TAG, "onClick: removing current app: " + currentApp.getName());
                        favourites.remove(currentApp);
                }


                }
        });
        /*
            If user clicks on the card containing all the app info, a browser will be opened directing
            the user to the official app page on Google play store.
         */
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentApp.getAppURL()));
                mContext.startActivity(browserIntent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return applications == null ? 0 : applications.size();
    }

    public void filter(String text) {
        applications.clear();
        if (text.isEmpty()) {
            applications.addAll(applications_copy);
        } else {
            text = text.toLowerCase();
            for (HtmlApp htmlApp : applications_copy) {
                if (htmlApp.getName().toLowerCase().contains(text)) {
                    applications.add(htmlApp);
                }
            }
        }

        notifyDataSetChanged();

    }


    static class AppViewHolder extends RecyclerView.ViewHolder {

        final TextView tvName, tvArtist;
        final ImageView thumbnail, favourite;
        final CardView card;


        public AppViewHolder(View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvArtist = itemView.findViewById(R.id.tvArtist);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);
            this.favourite = itemView.findViewById(R.id.favourite);
            this.card = itemView.findViewById(R.id.cv);
//
        }
    }

    public List<HtmlApp> getFavourites() {
        return favourites;
    }

    public void setApplications(List<HtmlApp> applications) {
        this.applications = applications;
    }
    /*
        Retrieves the genre for each app
     */
    public class GetGenre extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{

                htmlDocument = Jsoup.connect(strings[0]).get();

                Elements links = htmlDocument.getElementsByTag("a");
                Elements spanLinks = htmlDocument.getElementsByTag("span");

                for(Element link:links){
                    if(link.attr("itemprop").equalsIgnoreCase("genre")){
                        genre = link.text();
                        System.out.println(genre);
                        break;
                    }else{

                    }
                }

                if(genre == null) {
                    for (Element link : spanLinks) {
                        if (link.attr("itemprop").equalsIgnoreCase("genre")) {
                            genre = link.text();
                            System.out.println(genre);
                            break;
                        }
                    }
                }

            }catch(IOException e){
                e.printStackTrace();
            }
            return genre;
        }
    }

}















