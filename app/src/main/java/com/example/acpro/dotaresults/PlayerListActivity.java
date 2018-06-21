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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.acpro.dotaresults.fragments.ListFragment;
import com.example.acpro.dotaresults.model.PlayerRate;
import com.example.acpro.dotaresults.model.Team;
import com.example.acpro.dotaresults.service.PlayerAdapter;
import com.example.acpro.dotaresults.service.PlayerRateAdapter;
import com.example.acpro.dotaresults.service.TeamAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnFragmentInteractionListener{

    public Elements content;

    public ArrayList<String> playerUrlList = new ArrayList<>();
    public ArrayList<PlayerRate> playersList = new ArrayList<>();
    private PlayerRateAdapter adapter;
    private ListView listView;
    ProgressBar progressBar;
    TextView textInfo;

    String globalUrl = "https://www.cybersport.ru/base/gamers?disciplines=21&page=1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textInfo = findViewById(R.id.textInfo);

        progressBar = findViewById(R.id.progressBarTable);
        listView = findViewById(R.id.listView);

        new PlayerListActivity.NewThread().execute();
        adapter = new PlayerRateAdapter(this, playersList);

        // final Intent intentTeam = new Intent(this, .class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*intentTeam.putExtra("urlTeam", teamUrlList.get(i));
                System.out.println("number of item " + i +"\n" + "url: " + teamUrlList.get(i));
                startActivity(intentTeam);*/
            }
        });
    }

    public class NewThread extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document document;

            try {
                document = Jsoup.connect(globalUrl).userAgent("Mozilla").get();
                content = document.getElementsByClass("tables tables-base tables-base--players").select("tr");

                playersList.clear();
                playerUrlList.clear();

                for (Element content: content){
                    PlayerRate player = new PlayerRate();
                    player.setNick(content.select(".gamer__title").select("p").text());
                        System.out.println("Nick " + content.select(".gamer__title").select("p").text());
                    player.setName(content.select(".gamer__title").select("span").text());
                        System.out.println("NAME " + content.select(".gamer__title").select("span").text());
                    player.setPrize(content.select(".tables-base__fund").text());
                        System.out.println("PRIZE " + content.select(".tables-base__fund").text());
                    player.setGold(content.select(".icon-booty--first").text());
                        System.out.println("GOLD " + content.select(".icon-booty--first").text());
                    player.setSilver(content.select(".icon-booty--second").text());
                        System.out.println("SILVER " + content.select(".icon-booty--second").text());
                    player.setBronze(content.select(".icon-booty--third").text());
                        System.out.println("BRONZE " + content.select(".icon-booty--third").text());
                    player.setPhoto(content.select(".gamer__photo").select("img").attr("src"));
                        System.out.println("PHOTO " + content.select(".gamer__photo").select("img").attr("src"));
                    String urlTeam = content.select(".gamer__title").select("a").attr("href");
                        System.out.println("URL " + content.select(".gamer__title").select("a").attr("href"));
                    if (!player.getName().equals("") && !player.getPrize().equals("") && !player.getGold().equals("")
                            && !player.getSilver().equals("") && !player.getBronze().equals("")) {
                        playersList.add(player);
                        playerUrlList.add(urlTeam);
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
            if (playersList.size() == 0){
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
