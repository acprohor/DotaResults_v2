package com.example.acpro.dotaresults;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.acpro.dotaresults.fragments.ListFragment;
import com.example.acpro.dotaresults.model.Match;
import com.example.acpro.dotaresults.model.Player;
import com.example.acpro.dotaresults.service.MatchAdapter;
import com.example.acpro.dotaresults.service.PlayerAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnFragmentInteractionListener{

    public Elements content, contentNick, contentName, contentFacts, contentWin, contentHonors, contentImg, contentListItem;

    public ArrayList<Match> matchList = new ArrayList<>();
    public ArrayList<String> matchUrlList = new ArrayList<>();
    private ListView listView;
    private MatchAdapter adapter;
    String urlPlayer, urlMatch, playerNick, playerName, playerDate, playerTeam, playerWinRate, gold, silver, bronze, photoUrl;
    TextView textNick, textName, textDate, textTeam, textGold, textSilver, textBronze, textWinRate, textInfo;
    ImageView photo;
    ProgressBar loading, winRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        urlPlayer = intent.getStringExtra("playerUrl");
        urlPlayer = "https://www.cybersport.ru" + urlPlayer;
        System.out.println("urltour in tourActivity " + urlPlayer);

        textNick = findViewById(R.id.playerProfileNick);
        textName = findViewById(R.id.playerProfileName);
        textDate = findViewById(R.id.playerProfileDate);
        textTeam = findViewById(R.id.playerProfileTeam);
        textWinRate = findViewById(R.id.playerProfileWR);
        textGold = findViewById(R.id.playerProfileGold);
        textSilver = findViewById(R.id.playerProfileSilver);
        textBronze = findViewById(R.id.playerProfileBronze);
        textInfo = findViewById(R.id.textInfo);
        listView = findViewById(R.id.listView);
        loading = findViewById(R.id.progressBarTable);
        winRate = findViewById(R.id.playerProfileWinRate);
        photo = findViewById(R.id.playerProfilePhoto);

        new PlayerActivity.NewThreadn().execute();
        adapter = new MatchAdapter(this, matchList);

        //final Intent gotoPlayer = new Intent(this, MatchActivity.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //sendTour.putExtra("num", matchUrlList.get(i));
                //startActivity(sendTour);

            }
        });
    }

    public class NewThreadn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document document;
            String temp[];
            try {
                document = Jsoup.connect(urlPlayer).userAgent("Mozilla").get();
                content = document.select(".facts__description");
                contentName = document.getElementsByClass("facts__name facts__name--desktop");
                contentFacts = document.getElementsByClass("facts__info");
                contentWin = document.getElementsByClass("facts__progress team-points");
                contentHonors = document.getElementsByClass("facts__honors");
                contentImg = document.getElementsByClass("facts__photo");
                contentListItem = document.getElementsByClass("matche__entrant");

                playerNick = contentName.select("h1").text();
                System.out.println("NICK" + playerNick);
                playerName = contentName.select("p").text();
                System.out.println(playerName);
                try {
                    temp = playerName.split(",");
                    playerDate = temp[temp.length-1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    playerDate = "";
                }

                try {
                    boolean flag = false;
                    if (playerName.contains(":")){
                        flag = true;
                    }
                    System.out.println("начинаем " + playerName + " поделим по запятой");
                    temp = playerName.split(",");
                    playerName = "";

                    if (flag) {
                        if (temp[temp.length-2].contains(":")){
                            System.out.println(" : " + temp[temp.length-2]);
                            temp = temp[temp.length-2].split(":");
                            temp = temp[1].split(" ");
                            for (int i = 2; i < temp.length; i++) {
                                System.out.println("i" + temp[i]);
                                playerName += " " + temp[i];
                            }
                        } else {
                            temp = temp[temp.length - 2].split(" ");
                            for (int i = 2; i < temp.length; i++) {
                                System.out.println("i" + temp[i]);
                                playerName += " " + temp[i];
                            }
                        }
                    } else {
                        playerName = temp[temp.length - 2];
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    playerName = "";
                }

                System.out.println("date " + playerDate);
                System.out.println("name " + playerName);


                playerTeam = contentFacts.select(".team-title").select("h3").text();
                System.out.println("prize " + playerTeam);
                playerWinRate = contentWin.select(".team-points__footer").select(".team-points__title--winner").text();
                temp = playerWinRate.split(" ");
                playerWinRate = temp[0];
                System.out.println("w " + playerWinRate);
                gold = contentHonors.select(".icon-booty--first").text();
                System.out.println("g " + gold);
                silver = contentHonors.select(".icon-booty--second").text();
                System.out.println("s " + silver);
                bronze = contentHonors.select(".icon-booty--third").text();
                System.out.println("br " + bronze);
                photoUrl = contentImg.select("img").attr("src");
                System.out.println("urlPhoto " + photoUrl);

                matchList.clear();
                matchUrlList.clear();
                for (Element content : contentListItem) {
                    Match match = new Match();

                    match.setTeamL(content.select(".matche__team--left .team__name .hidden-xs--inline-block").text());
                        System.out.println("L " + content.select(".matche__team--left .team__name .hidden-xs--inline-block").text());
                    match.setScore(content.select(".matche__score").text());
                        System.out.println(content.select(".matche__score").text());
                    match.setTeamR(content.select(".matche__team--right .team__name .hidden-xs--inline-block").text());
                        System.out.println("R " + content.select(".matche__team--right .team__name .hidden-xs--inline-block").text());
                    System.out.println("URLM " + content.select(".matche__score").select("a").attr("href"));
                    urlMatch = content.select(".matche__score").select("a").attr("href");

                    if (!match.getTeamL().equals("") && !match.getTeamR().equals("") && !match.getScore().equals("") && !urlMatch.equals("") ) {
                        matchList.add(match);
                        matchUrlList.add(urlMatch);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            String[] temp;
            //textInfo.setVisibility(View.GONE);
            textNick.setText(playerNick);
            textName.setText(playerName);
            textDate.setText(playerDate);
            textTeam.setText(playerTeam);
            textWinRate.setText(playerWinRate);
            textGold.setText(gold);
            textSilver.setText(silver);
            textBronze.setText(bronze);

            Context context = getApplicationContext();
            if (!photoUrl.contains("assets")) {
                Glide
                        .with(context)
                        .load(photoUrl)
                        .into(photo);
            }
            winRate.setMax(100);
            temp = playerWinRate.split("\\.");
            System.out.println(temp[0]);
            winRate.setProgress(Integer.parseInt(temp[0]));
            listView.setAdapter(adapter);
            loading.setVisibility(View.GONE);
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
