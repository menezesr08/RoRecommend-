package com.example.android.top10downloadedappnew.data;
/**
 * Main class which contains the framework of the app and the main logic.
 *
 * @author Rohan Menezes
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.example.android.top10downloadedappnew.activities.HomescreenActivity;
import com.example.android.top10downloadedappnew.activities.MainActivity;
import com.example.android.top10downloadedappnew.models.HtmlApp;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.view.Menu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.top10downloadedappnew.R;
import com.example.android.top10downloadedappnew.fragments.AlbumFragment;
import com.example.android.top10downloadedappnew.fragments.FreeAppFragment;
import com.example.android.top10downloadedappnew.fragments.HomeFragment;
import com.example.android.top10downloadedappnew.fragments.PaidFragment;
import com.example.android.top10downloadedappnew.fragments.SongFragment;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class DataInitialization {
    private ActionBar actionBar;
    private View view;
    private TextView title_TextView;
    private TextView caption_TextView;
    private RecyclerView recycler_View;
    private FragmentActivity m_fragmentActivity;
    private Toolbar toolbar;
    private ImageView imageView;
    private int feedLimit = 10;
    private static final String TAG = "DataInitialization";
    private HTMLParser htmlParser;
    private HtmlAppAdapter htmlAppAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Document htmlDocument;
    private NavigationView navigationView;
    private static int navItemIndex = 0;
    private static final String TAG_HOME = "Home";
    private static final String TAG_FREE_APPS = "Top Free Apps";
    private static final String TAG_PAID_APPS = "Top Paid Apps";
    private static final String TAG_SONGS = "Top Songs";
    private static final String TAG_ALBUMS = "Top Albums";
    public static String CURRENT_TAG = TAG_HOME;
    private DrawerLayout drawer;
    private Handler mHandler;
    private String[] activityTitles;
    private ProgressBar progressBar;
    private ArrayList<String> favourites;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private static String userID;
    private WanderingCubes wanderingCubes;
    private FirebaseUser firebaseUser;
    private AlertDialog.Builder alertDialog;

    /** Setups the framework for the app
     *
     * @param view fragment view
     * @param fragmentActivity fragment context
     */
    public DataInitialization(View view, FragmentActivity fragmentActivity) {
        this.view = view;
        m_fragmentActivity = fragmentActivity;
        setupUI();
        setUpActionBar();
        setUpHeaderBackground();
        setUpNavigationView();
        setUpRecyclerView();
        //Retrieves a list of favourite apps which are saved in the system
        favourites = favouriteAppNames(activityTitles[navItemIndex]);

    }

    private void setupUI() {

        title_TextView = view.findViewById(R.id.love_music);
        caption_TextView = view.findViewById(R.id.top);
        recycler_View = view.findViewById(R.id.recycler_view);
        toolbar = view.findViewById(R.id.toolbar);
        imageView = view.findViewById(R.id.backdrop);
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        navigationView = view.findViewById(R.id.nav_view);
        drawer = view.findViewById(R.id.drawer_layout);
        mHandler = new Handler();
        progressBar = view.findViewById(R.id.spinner);
        wanderingCubes = new WanderingCubes();
        progressBar.setIndeterminateDrawable(wanderingCubes);
        progressBar.setVisibility(View.VISIBLE);
        activityTitles = m_fragmentActivity.getResources().getStringArray(R.array.nav_item_activity_titles);
        alertDialog = new AlertDialog.Builder(m_fragmentActivity);

    }

    private void setUpActionBar() {
        ((AppCompatActivity) m_fragmentActivity).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) m_fragmentActivity).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    //Uses the Glide library to set up the header background
    private void setUpHeaderBackground() {
        try {

            Glide.with(m_fragmentActivity).load(R.drawable.epic_2).into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Switching between different fragments
    void setUpNavigationView() {


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                database = FirebaseDatabase.getInstance();
                myRef = database.getReference(CURRENT_TAG);
                Log.d(TAG, "onNavigationItemSelected: myref is: " + myRef.toString());
                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(htmlAppAdapter.getFavourites());


                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with the selected Fragment.
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_free_apps:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_FREE_APPS;
                        break;
                    case R.id.nav_paid_apps:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PAID_APPS;
                        break;
                    case R.id.nav_songs:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SONGS;
                        break;
                    case R.id.nav_album:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ALBUMS;
                        break;
                    case R.id.signOut:
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

    }

    //Replaces the content frame with the selected fragment
    private void loadHomeFragment() {
        selectNavMenu();

        setToolbarTitle();

        if (m_fragmentActivity.getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                Log.d(TAG, "run: fragment is: " + fragment.toString());
                Log.d(TAG, "run: m_fragmentActivity is: " + m_fragmentActivity.toString());
                FragmentTransaction fragmentTransaction = m_fragmentActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();
        m_fragmentActivity.invalidateOptionsMenu();

    }

    private void setToolbarTitle() {
        actionBar.setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    //Selects the fragment depending on the item index
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // Free Apss
                FreeAppFragment freeAppFragment = new FreeAppFragment();
                return freeAppFragment;
            case 2:
                // Paid Apps
                PaidFragment paidFragment = new PaidFragment();
                return paidFragment;
            case 3:
                // Songs fragment
                SongFragment songFragment = new SongFragment();
                return songFragment;

            case 4:
                // Albums fragment
                AlbumFragment albumFragment = new AlbumFragment();
                return albumFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(m_fragmentActivity, 2);
        recycler_View.setLayoutManager(mLayoutManager);

        recycler_View.addItemDecoration(new GridSpacingItemDecoration(2, ((MainActivity) m_fragmentActivity).dpToPx(10), true));
        recycler_View.setItemAnimator(new DefaultItemAnimator());
    }

    //Downloads the requested URL through JSoup Library
    public void downloadURL(String url) {
        new JsoupAsyncTask().execute(url);
    }

    public TextView getTitleTextView() {
        return title_TextView;
    }

    public TextView getCaptionTextView() {
        return caption_TextView;
    }

    //Uses the JSoup Library to download the URL, parse the data and hook the data to the adapter
    private class JsoupAsyncTask extends AsyncTask<String, Void, Document> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Document d) {

            super.onPostExecute(d);

            htmlParser = new HTMLParser();
            htmlParser.parse(d);
            /**
             * Since we download the URL each time, the user clicks on a fragment, the data could change
             * regularly since Google play store updates their Top Apps page frequently.
             * We need to check if any apps from the favourites list (this is the list of favourite apps that were
             * saved in the system) are already contained in the new downloaded list of apps. If it is contained, we just
             * need to favourite these items again, so that it can be displayed for the user. Once the user favourites any item,
             * it should stay favourited unless the user de-selects that item.
             */
            if (favourites != null) {
                for (String favouriteApp : favourites) {
                    for (HtmlApp htmlApp : htmlParser.getApplications()) {
                        if (favouriteApp.equalsIgnoreCase(htmlApp.getName())) {
                            htmlApp.isFavourite();
                            System.out.println("App that is checked is: " + htmlApp.getName());
                            break;
                        }

                    }
                }
            }

            htmlAppAdapter = new HtmlAppAdapter(m_fragmentActivity, htmlParser.getApplications());
            progressBar.setVisibility(View.INVISIBLE);
            recycler_View.setAdapter(htmlAppAdapter);
        }

        @Override
        protected Document doInBackground(String... strings) {
            try {
                htmlDocument = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return htmlDocument;
        }
    }

    public void filter(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                htmlAppAdapter.filter(newText);
                return false;
            }
        });

    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    private ArrayList<String> favouriteAppNames(String key) {

        // gets current user firebase ID
        final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        final ArrayList<String> appNames = new ArrayList<>();
        // key: "home", "free App" etc etc
        ref.child(key).child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // loops through each user under the key
                for (DataSnapshot UID : dataSnapshot.getChildren()) {
                    appNames.add((String) UID.child("name").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return appNames;
    }

}
