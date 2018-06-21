package com.example.acpro.dotaresults;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.acpro.dotaresults.fragments.ButtonsBar;
import com.example.acpro.dotaresults.fragments.ListFragment;
import com.example.acpro.dotaresults.model.Match;
import com.example.acpro.dotaresults.service.MatchAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ButtonsBar.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener {

    public Elements content, content2;

    public ArrayList<String> matchUrlList = new ArrayList<>();
    public ArrayList<Match> matches = new ArrayList<>();
    private MatchAdapter myAdapter;
    private ListView listView;
    ProgressBar progressBar;
    TextView textInfo;

    String globalUrl = "https://www.cybersport.ru/base/match/list/disciplines/dota2/status/past/page/1/search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textInfo = findViewById(R.id.textInfo);

        progressBar = findViewById(R.id.progressBarTable);
        listView = findViewById(R.id.listView);
        final Button buttonPresent = findViewById(R.id.buttonPresent);
        final Button buttonFuture = findViewById(R.id.buttonFuture);
        final Button buttonLast = findViewById(R.id.buttonLast);
        buttonLast.setEnabled(false);
        buttonFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalUrl = "https://www.cybersport.ru/base/match/list/disciplines/dota2/status/future/page/1/search";
                update();
                view.setEnabled(false);
                buttonLast.setEnabled(true);
                buttonPresent.setEnabled(true);
            }
        });

        buttonPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalUrl = "https://www.cybersport.ru/base/match/list/disciplines/dota2/status/active/page/1/search";
                update();
                view.setEnabled(false);
                buttonFuture.setEnabled(true);
                buttonLast.setEnabled(true);
            }
        });
        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globalUrl = "https://www.cybersport.ru/base/match/list/disciplines/dota2/status/past/page/1/search";
                update();
                view.setEnabled(false);
                buttonPresent.setEnabled(true);
                buttonFuture.setEnabled(true);
            }
        });

        new MainActivity.NewThread().execute();
        myAdapter = new MatchAdapter(this, matches);

        final Intent intentMatch = new Intent(this, MatchActivity.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                intentMatch.putExtra("num", matchUrlList.get(i) );
                System.out.println("number of item " + i +"\n" + "url: " + matchUrlList.get(i));
                startActivity(intentMatch);
            }
        });

    }

    public void update(){
        new MainActivity.NewThread().execute();
        myAdapter = new MatchAdapter(this, matches);
        progressBar.setVisibility(View.VISIBLE);
    }

    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document document;
            try {
                document = Jsoup.connect(globalUrl).userAgent("Mozilla").get();
                content = document.select(".matche__entrant");

                matches.clear();
                matchUrlList.clear();

                for (Element content: content){
                    Match match = new Match();
                    match.setTeamL(content
                                    .select(".matche__team--left .team__name .hidden-xs--inline-block")
                                    .text());
                    match.setScore(content.getElementsByClass("matche__score")
                                    .text());
                    match.setTeamR(content
                                    .select(".matche__team--right .team__name .hidden-xs--inline-block")
                                    .text());
                    String urlMatch = content
                            .select(".matche__score").select("a").attr("href");
                    if (!match.getScore().equals("") && !match.getTeamL().equals("")
                            && !match.getTeamR().equals("") && !match.getTeamL().equals("TBD")
                            && !match.getTeamR().equals("TBD")) {
                        matches.add(match);
                        matchUrlList.add(urlMatch);
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
            if (matches.size() == 0){
                textInfo.setVisibility(View.VISIBLE);
                textInfo.setText(R.string.nothing_to_show_message);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_matches) {
            Intent matchesIntent =
                    new Intent(this, MainActivity.class);
            startActivity(matchesIntent);
        } else if (id == R.id.nav_tournaments) {
            Intent tournamentsIntent =
                    new Intent(this, TournamentsListActivity.class);
            startActivity(tournamentsIntent);
        } else if (id == R.id.nav_teams) {
            Intent teamsIntent =
                    new Intent(this, TeamListActivity.class);
            startActivity(teamsIntent);
        } else if (id == R.id.nav_players) {
            Intent playersRateIntent =
                    new Intent(this, PlayerListActivity.class);
            startActivity(playersRateIntent);

        } else if (id == R.id.nav_pro_circuit) {

        } else if (id == R.id.nav_news) {

        } else if (id == R.id.nav_info) {
            Intent info =
                    new Intent(this, AboutActivity.class);
            startActivity(info);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
