package com.example.acpro.dotaresults;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.acpro.dotaresults.fragments.ListFragment;
import com.example.acpro.dotaresults.model.Team;
import com.example.acpro.dotaresults.model.Tournament;
import com.example.acpro.dotaresults.service.TeamAdapter;
import com.example.acpro.dotaresults.service.TournamentAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TeamListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListFragment.OnFragmentInteractionListener {

    public Elements content;

    public ArrayList<String> teamUrlList = new ArrayList<>();
    public ArrayList<Team> teams = new ArrayList<>();
    private TeamAdapter adapter;
    private ListView listView;
    ProgressBar progressBar;
    TextView textInfo;

    String globalUrl = "https://www.cybersport.ru/base/teams?sort=amount&disciplines=21&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textInfo = findViewById(R.id.textInfo);

        progressBar = findViewById(R.id.progressBarTable);
        listView = findViewById(R.id.listView);

        new TeamListActivity.NewThread().execute();
        adapter = new TeamAdapter(this, teams);

        final Intent intentTeam = new Intent(this, TournamentActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intentTeam.putExtra("urlTeam", teamUrlList.get(i));
                System.out.println("number of item " + i +"\n" + "url: " + teamUrlList.get(i));
                startActivity(intentTeam);
            }
        });
    }

    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document document;

            try {
                document = Jsoup.connect(globalUrl).userAgent("Mozilla").get();
                content = document.getElementsByClass("tables tables-base tables-base--teams").select("tr");

                teams.clear();
                teamUrlList.clear();

                for (Element content: content){
                    Team team = new Team();
                    team.setName(content.select(".team__title").text());
                    System.out.println("NAME " + content.select(".team__title").text());
                    team.setPrize(content.select(".tables-base__fund").text());
                    System.out.println("PRIZE " + content.select(".tables-base__fund").text());
                    team.setGold(content.select(".icon-booty--first").text());
                    System.out.println("GOLD " + content.select(".icon-booty--first").text());
                    team.setSilver(content.select(".icon-booty--second").text());
                    System.out.println("SILVER " + content.select(".icon-booty--second").text());
                    team.setBronze(content.select(".icon-booty--third").text());
                    System.out.println("BRONZE " + content.select(".icon-booty--third").text());
                    team.setLogo(content.select(".team__logo").select("img").attr("src"));
                    System.out.println("LOGO " + content.select(".team__logo").select("img").attr("src"));
                    String urlTeam = content.select(".tables-base__name").select("a").attr("href");
                    System.out.println("URLTOUR " + content.select(".tables-base__name").select("a").attr("href"));
                    if (!team.getName().equals("") && !team.getPrize().equals("") && !team.getGold().equals("")
                            && !team.getSilver().equals("") && !team.getBronze().equals("")) {
                        teams.add(team);
                        teamUrlList.add(urlTeam);
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
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            if (teams.size() == 0){
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

        if (id == R.id.nav_camera) {
            Intent matchesIntent = new Intent(this, MainActivity.class);
            startActivity(matchesIntent);
        } else if (id == R.id.nav_gallery) {
            Intent tournamentsIntent = new Intent(this, TournamentsListActivity.class);
            startActivity(tournamentsIntent);
        } else if (id == R.id.nav_slideshow) {
            Intent teamsIntent = new Intent(this, TeamListActivity.class);
            startActivity(teamsIntent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
