package com.example.acpro.dotaresults;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.acpro.dotaresults.fragments.GroupTable;
import com.example.acpro.dotaresults.fragments.ListFragment;
import com.example.acpro.dotaresults.model.Player;
import com.example.acpro.dotaresults.service.PlayerAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnFragmentInteractionListener{

    public Elements content, contentName, contentPrize, contentStat, contentWin, contentHonors, contentImg, contentTableItem;
    String teamName, teamFound, teamPrize, teamMatchCount, teamWinRate, gold, silver, bronze, logoUrl;
    //TODO класс игрока для списка игроков
    public ArrayList<Player> playerList = new ArrayList<>();
    public ArrayList<String> playerUrlList = new ArrayList<>();
    private ListView playersListView;
    private PlayerAdapter adapter;
    String urlTeam;
    TextView textName, textDate, textPrize, textMatchCount, textGold, textSilver, textBronze, textWinRate, textInfo;
    ImageView logo;
    ProgressBar loading, winRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        urlTeam = intent.getStringExtra("urlTeam");
        urlTeam = "https://www.cybersport.ru" + urlTeam;
        System.out.println("urltour in tourActivity " + urlTeam);

        textName = findViewById(R.id.profileName);
        textDate = findViewById(R.id.profileDate);
        textPrize = findViewById(R.id.profilePrize);
        textMatchCount = findViewById(R.id.profileMatchCount);
        textGold = findViewById(R.id.profileGold);
        textSilver = findViewById(R.id.profileSilver);
        textBronze = findViewById(R.id.profileBronze);
        textWinRate = findViewById(R.id.profileWR);
        textInfo = findViewById(R.id.textInfo);
        playersListView = findViewById(R.id.listView);
        loading = findViewById(R.id.progressBarTable);
        winRate = findViewById(R.id.profileWinRate);
        logo = findViewById(R.id.profileLogo);

        new TeamActivity.NewThreadn().execute();
        adapter = new PlayerAdapter(this, playerList);

        //final Intent gotoPlayer = new Intent(this, MatchActivity.class);
        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            String[] temp;
            try {
                document = Jsoup.connect(urlTeam).userAgent("Mozilla").get();
                content = document.select(".facts__description");
                contentName = document.getElementsByClass("facts__name facts__name--desktop");
                contentPrize = document.getElementsByClass("facts facts--player clearfix");
                contentStat = document.getElementsByClass("facts__info");
                contentWin = document.getElementsByClass("facts__progress team-points");
                contentHonors = document.getElementsByClass("facts__honors");
                contentImg = document.getElementsByClass("facts__photo");
                contentTableItem = document.getElementsByClass("gamers__item");

                teamName = contentName.select("h1").text();
                System.out.println("name " + teamName);
                teamFound = content.select(".facts__alias").text();
                try {
                    temp = teamFound.split("Образована в ");
                    temp = temp[1].split(" году");
                    teamFound = temp[0];
                } catch (ArrayIndexOutOfBoundsException e){
                    teamFound = "";
                }

                System.out.println("date " + teamFound);
                teamPrize = contentPrize.select(".facts__funds").text();
                System.out.println("prize " + teamPrize);
                teamMatchCount = contentStat.select(".team-points__total").text();
                System.out.println("c " + teamMatchCount);
                teamWinRate = contentWin.select(".team-points__title--winner").text();
                temp = teamWinRate.split("Победы");
                temp = temp[1].split(" ");
                teamWinRate = temp[1];
                System.out.println("w " + teamWinRate);
                gold = contentHonors.select(".icon-booty--first").text();
                System.out.println("g " + gold);
                silver = contentHonors.select(".icon-booty--second").text();
                System.out.println("s " + silver);
                bronze = contentHonors.select(".icon-booty--third").text();
                System.out.println("br " + bronze);
                logoUrl = contentImg.select("img").attr("src");

                playerUrlList.clear();
                playerList.clear();
                int ch = 0;
                String[] temp1;
                for (Element content: contentTableItem){
                    Player player = new Player();
                    if (content.select(".gamers__title").select("strong").text().equals("Игрок")){
                        ch++;
                    } else {
                        if (ch < 2) {
                            player.setPhoto(content.select(".gamers__title").select("img").attr("src"));
                            System.out.println("src" + content.select(".gamers__title").select(".gamer__photo").select("img").attr("src"));
                            player.setNickname(content.select(".gamers__title").select(".gamer__description").select("strong").text());
                            System.out.println(content.select(".gamers__title").select(".gamer__description").select("strong").text());
                            player.setInfo(content.select(".gamers__title").select(".gamer__description").select("p").text());
                            System.out.println(content.select(".gamers__title").select(".gamer__description").select("p").text());
                            player.setRole(content.select(".gamers__role").select("span").text());
                            System.out.println(content.select(".gamers__role").select("span").text());
                            temp1 = content.select(".gamers__points").select(".team-points__title--winner").text().split(" ");
                            player.setWinRate(temp1[0] + "%");
                            System.out.println(temp1[0]);
                            playerList.add(player);
                        } else {
                            break;
                        }
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
            //textInfo.setVisibility(View.GONE);
            textName.setText(teamName);
            if (!teamFound.equals("")) {
                textDate.setText("Основана в " + teamFound + " году");
            } else {
                textDate.setText(teamFound);
            }
            textPrize.setText(teamPrize);
            if (!gold.equals("")) {
                textGold.setText(gold);
            } else {
                textGold.setText("0");
            }
            if (!silver.equals("")) {
                textSilver.setText(silver);
            } else {
                textSilver.setText("0");
            }
            if (!bronze.equals("")) {
                textBronze.setText(bronze);
            } else {
                textBronze.setText("0");
            }
            textMatchCount.setText(teamMatchCount);
            textWinRate.setText(teamWinRate + "%");
            Context context = getApplicationContext();
            if (!logoUrl.contains("assets")){
                Glide
                        .with(context)
                        .load(logoUrl)
                        .into(logo);
            }
            winRate.setMax(100);
            winRate.setProgress(Integer.parseInt(teamWinRate));
            playersListView.setAdapter(adapter);
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
