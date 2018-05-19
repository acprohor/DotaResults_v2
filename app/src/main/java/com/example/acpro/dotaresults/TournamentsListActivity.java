package com.example.acpro.dotaresults;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.acpro.dotaresults.fragments.ButtonsBar;
import com.example.acpro.dotaresults.fragments.ListFragment;
import com.example.acpro.dotaresults.model.Tournament;
import com.example.acpro.dotaresults.service.TournamentAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TournamentsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ButtonsBar.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener{

    public Elements content;

    public ArrayList<String> tournamentUrlList = new ArrayList<>();
    public ArrayList<Tournament> tournaments = new ArrayList<>();
    ArrayList<String> transfer = new ArrayList<>();
    private TournamentAdapter myAdapter;
    private ListView listView;
    ProgressBar progressBar;
    TextView textInfo;

    String globalUrl = "https://www.cybersport.ru/base/tournaments/list/disciplines/dota2/order/end/auto/status/active/page/1/search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments_list);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textInfo = findViewById(R.id.textInfo);

        progressBar = findViewById(R.id.progressBarTable);
        listView = findViewById(R.id.listView);
        final Button buttonPresent = findViewById(R.id.buttonPresent);
        final Button buttonFuture = findViewById(R.id.buttonFuture);
        final Button buttonLast = findViewById(R.id.buttonLast);
        buttonPresent.setEnabled(false);
        buttonFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalUrl = "https://www.cybersport.ru/base/tournaments/list/disciplines/dota2/order/end/auto/status/future/page/1/search";
                update();
                view.setEnabled(false);
                buttonLast.setEnabled(true);
                buttonPresent.setEnabled(true);
            }
        });

        buttonPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalUrl = "https://www.cybersport.ru/base/tournaments/list/disciplines/dota2/order/end/auto/status/active/page/1/search";
                update();
                view.setEnabled(false);
                buttonFuture.setEnabled(true);
                buttonLast.setEnabled(true);
            }
        });
        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalUrl = "https://www.cybersport.ru/base/tournaments/list/disciplines/dota2/order/end/auto/status/past/page/1/search";
                update();
                view.setEnabled(false);
                buttonPresent.setEnabled(true);
                buttonFuture.setEnabled(true);
            }
        });

        new TournamentsListActivity.NewThread().execute();
        myAdapter = new TournamentAdapter(this, tournaments);

        final Intent intentTour = new Intent(this, TournamentActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                transfer.clear();
                transfer.add(tournamentUrlList.get(i));
                transfer.add(tournaments.get(i).getChampionship());
                intentTour.putStringArrayListExtra("urlTour", transfer);
                System.out.println("number of item " + i +"\n" + "url: " + tournamentUrlList.get(i));
                startActivity(intentTour);
            }
        });
    }

    public void update(){
        new TournamentsListActivity.NewThread().execute();
        myAdapter = new TournamentAdapter(this, tournaments);
        progressBar.setVisibility(View.VISIBLE);
    }

    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document document;

            try {
                document = Jsoup.connect(globalUrl).userAgent("Mozilla").get();
                content = document.getElementsByClass("tournaments__item tournament");
                System.out.println(globalUrl);

                tournaments.clear();
                tournamentUrlList.clear();
                String dateSpliter[];

                for (Element content: content){
                    Tournament tour = new Tournament();
                    tour.setChampionship(content.select(".tournament__name").text());
                    System.out.println("NAME " + content.select(".tournament__name").text());
                    dateSpliter = content.getElementsByClass("tournament__date").text().split("-");
                    tour.setDate(dateSpliter[0]);
                    System.out.println("DATE " + dateSpliter[0]);
                    tour.setPrize(content.select(".tournament__fund").text());
                    System.out.println("PRIZE " + content.select(".tournament__fund").text());
                    String urlTour = content.select(".tournament__name").select("a").attr("href");
                    System.out.println("URLTOUR " + content.select(".tournament__name").select("a").attr("href"));
                    if (!tour.getChampionship().equals("") && !tour.getDate().equals("") && !tour.getPrize().equals("")) {
                        tournaments.add(tour);
                        tournamentUrlList.add(urlTour);
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            listView.setAdapter(myAdapter);
            progressBar.setVisibility(View.GONE);
            if (tournaments.size() == 0){
                textInfo.setVisibility(View.VISIBLE);
                textInfo.setText("Noting to show");
            }else {
                textInfo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_matches) {
            Intent matchesIntent = new Intent(this, MainActivity.class);
            startActivity(matchesIntent);
        } else if (id == R.id.nav_tournaments) {
            Intent tournamentsIntent = new Intent(this, TournamentsListActivity.class);
            startActivity(tournamentsIntent);
        } else if (id == R.id.nav_teams) {
            Intent teamsIntent = new Intent(this, TeamListActivity.class);
            startActivity(teamsIntent);
        } else if (id == R.id.nav_players) {
            Intent playersRateIntent = new Intent(this, PlayerListActivity.class);
            startActivity(playersRateIntent);

        } else if (id == R.id.nav_pro_circuit) {

        } else if (id == R.id.nav_news) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
