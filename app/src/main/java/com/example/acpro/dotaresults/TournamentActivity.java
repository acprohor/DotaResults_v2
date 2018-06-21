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

import com.example.acpro.dotaresults.fragments.GroupTable;
import com.example.acpro.dotaresults.model.Match;
import com.example.acpro.dotaresults.model.TeamRate;
import com.example.acpro.dotaresults.service.MatchAdapter;
import com.example.acpro.dotaresults.service.TeamRateAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TournamentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GroupTable.OnFragmentInteractionListener {

    public Elements match_item, headerInfo, headerInfo2, tourSwitch, table_item, playoff_match_item, playoff_match_item2;

    public ArrayList<Match> matchList = new ArrayList<>();
    public ArrayList<TeamRate> tableItemsList = new ArrayList<>();
    public ArrayList<String> matchUrlList = new ArrayList<>();
    public ArrayList<String> updatedUrlList = new ArrayList<>();
    private ListView tourListView;
    private MatchAdapter matchAdapter;
    private TeamRateAdapter teamRateAdapter;
    TextView textTourName, textTourLocation, textTourPrize, textTourDate, textTourGroup, textTourOff, textInfo;
    String urlTour, contest, date, winner, headerInfoString;
    ProgressBar loading;
    boolean activeButton [] = new boolean[3];
    boolean ready = true;
    boolean playoff = true;
    ArrayList<String> temp = new ArrayList<>();
    ArrayList<String> temp2 = new ArrayList<>();
    ArrayList<String> temp3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        matchAdapter = new MatchAdapter(this, matchList);
        teamRateAdapter = new TeamRateAdapter(this, tableItemsList);
        Intent intent = getIntent();
        ArrayList<String> tempData = intent.getStringArrayListExtra("urlTour");
        urlTour = tempData.get(0);
        contest = tempData.get(1);
        urlTour = "https://www.cybersport.ru" + urlTour;

        textTourName = findViewById(R.id.textTourName);
        textTourLocation = findViewById(R.id.textTourLocation);
        textTourPrize = findViewById(R.id.textTourPrize);
        textTourDate = findViewById(R.id.textTourDate);
        textTourGroup = findViewById(R.id.textTourGroup);
        textTourOff = findViewById(R.id.textTourOff);
        textInfo = findViewById(R.id.textInfoTable);
        tourListView = findViewById(R.id.listViewTable);
        loading = findViewById(R.id.progressBarTable);

        final Button buttonTable = findViewById(R.id.buttonGroupTable);
        final Button buttonGroupMatches = findViewById(R.id.buttonGroupMatches);
        final Button buttonOffMatches = findViewById(R.id.buttonGroupOff);

        buttonGroupMatches.setEnabled(false);
        activeButton[0] = false;
        activeButton[1] = true;
        activeButton[2] = false;

        buttonTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTable.setEnabled(false);
                buttonGroupMatches.setEnabled(true);
                buttonOffMatches.setEnabled(true);
                activeButton[0] = true;
                activeButton[1] = false;
                activeButton[2] = false;
                updateGroupTable();
            }
        });

        buttonGroupMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonGroupMatches.setEnabled(false);
                buttonTable.setEnabled(true);
                buttonOffMatches.setEnabled(true);
                activeButton[0] = false;
                activeButton[1] = true;
                activeButton[2] = false;
                updateGroupMatch();
            }
        });

        buttonOffMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonOffMatches.setEnabled(false);
                buttonGroupMatches.setEnabled(true);
                buttonTable.setEnabled(true);
                activeButton[0] = false;
                activeButton[1] = false;
                activeButton[2] = true;
                updateOffMatch();
            }
        });

        new TournamentActivity.NewThreadn().execute();
        final Intent sendTour = new Intent(this, MatchActivity.class);
        tourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!activeButton[0]) {
                    sendTour.putExtra("num", matchUrlList.get(i));
                    startActivity(sendTour);
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class NewThreadn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            ready = true;
            playoff = true;
            Document document;
            try {
                document = Jsoup.connect(urlTour).userAgent("Mozilla").get();
                headerInfo = document.select(".report__information");
                headerInfo2 = headerInfo.select(".padding-left--15 padding-right--15");
                tourSwitch = document.select(".tabs__item");

                ArrayList<String> info = new ArrayList<>();
                for (Element content: headerInfo){
                    headerInfoString = content.select(".padding-right--15").text();
                }

                // date = headerInfo.select("div").text();

                updatedUrlList.clear();
                for (Element content: tourSwitch){
                    if (!content.attr("href").equals("") && !content.attr("href").equals("javascript:void(0)")) {
                        updatedUrlList.add(content.attr("href"));
                    }
                }

                if (updatedUrlList.size()>0) {

                    if (activeButton[1]) {
                        document = Jsoup.connect("https://www.cybersport.ru" + updatedUrlList.get(0)).userAgent("Mozilla").get();

                        match_item = document.select(".matche__entrant");

                        matchList.clear();
                        matchUrlList.clear();
                        // TODO тут я хотел сделать разделитель групп, но проблема что циклы работают асинхронно и разделители становятся не на свои места.
                /*for (Element headContent: group_item) {
                    Match divider = new Match();
                    divider.setScore(headContent.select("h4").text());
                    divider.setTeamL("--");
                    divider.setTeamR("--");
                    matchList.add(divider);
                    matchUrlList.add("divider");*/
                        for (Element content : match_item) {
                            Match match = new Match();
                            match.setTeamL(content.select(".matche__team--left .team__name").text());
                            match.setScore(content.getElementsByClass("matche__score").text());
                            match.setTeamR(content.select(".matche__team--right .team__name").text());
                            String urlMatch = content.select(".matche__score").select("a").attr("href");
                            if (!match.getScore().equals("") && !match.getTeamL().equals("") && !match.getTeamR().equals("") &&
                                    !match.getTeamL().equals("TBD") && !match.getTeamR().equals("TBD")) {
                                matchList.add(match);
                                matchUrlList.add(urlMatch);
                            }
                        }
                        //}
                    }
                    if (activeButton[0]) {
                        document = Jsoup.connect("https://www.cybersport.ru" + updatedUrlList.get(0)).userAgent("Mozilla").get();

                        table_item = document.select("tr");
                        tableItemsList.clear();
                        matchUrlList.clear();
                        for (Element content : table_item) {
                            TeamRate rate = new TeamRate();
                            rate.setTeamName(content.select(".team-standings__title").select("span").text());
                            rate.setMatches(content.select(".team-standings__i").text());
                            rate.setWins(content.select(".team-standings__v").text());
                            rate.setDraw(content.select(".team-standings__n").text());
                            rate.setLose(content.select(".team-standings__p").text());
                            rate.setPoints(content.select(".team-standings__o").text());
                            if (rate.getTeamName().equals("")) {
                                rate.setTeamName("<-- TEAM -->");
                                rate.setMatches("M");
                                rate.setWins("W");
                                rate.setDraw("D");
                                rate.setLose("L");
                                rate.setPoints("P");
                            }
                            tableItemsList.add(rate);
                        }
                    }
                    if (activeButton[2] && updatedUrlList.size() > 1) {
                        document = Jsoup.connect("https://www.cybersport.ru" + updatedUrlList.get(1)).userAgent("Mozilla").get();

                        // item teams-match__item team-match team-match--loser

                        playoff_match_item = document.select(".teams-match__wrapper");
                        playoff_match_item2 = document.select(".teams-match__item");

                        matchList.clear();
                        matchUrlList.clear();
                        temp.clear(); temp2.clear(); temp3.clear();
                        for (Element c1 : playoff_match_item2) {
                            temp2.add(c1.select(".team-match__title").text());
                            temp3.add(c1.select(".team-match__points").text());
                            temp.add(c1.select(".team-match__points").select("a").attr("href"));
                        }

                        for (int i = 0; i < temp2.size() - 1; i += 2) {
                            Match match = new Match();
                            match.setTeamL(temp2.get(i));
                            match.setScore(temp3.get(i) + ":" + temp3.get(i + 1));
                            match.setTeamR(temp2.get(i + 1));
                            matchList.add(match);
                            matchUrlList.add(temp.get(i + 1));
                            if (!match.getTeamL().equals("") && match.getTeamR().equals("")) {
                                winner = match.getTeamL();
                                i--;
                                matchList.remove(matchList.size() - 1);
                                matchUrlList.remove(matchUrlList.size() - 1);
                            }
                            if (match.getTeamL().equals("") && match.getTeamR().equals("")) {
                                matchList.remove(matchList.size() - 1);
                                matchUrlList.remove(matchUrlList.size() - 1);

                            }
                            if (match.getTeamL().equals("TBD") || match.getTeamR().equals("TBD")) {
                                matchList.remove(matchList.size() - 1);
                                matchUrlList.remove(matchUrlList.size() - 1);

                            }
                        }
                    }
                    else {
                        playoff = false;
                    }
                } else {
                    ready = false;
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                textInfo.setVisibility(View.GONE);
                textTourName.setText(contest);
                /*String dd[] = date.split("проведения");
                String dd1[] = dd[1].split("Серия");
                textTourDate.setText("date: " + dd1[0]);*/

                String dd[] = headerInfoString.split("проведения");
                textTourDate.setText("date: " + dd[1]);

                /*String loc[] = date.split("Локация");
                String loc1[] = loc[1].split("Формат");
                textTourLocation.setText("location: " + loc1[0]);*/

                String loc[] = headerInfoString.split("Сумма");
                String loc1[] = loc[0].split("Локация");
                textTourLocation.setText("location: " + loc1[1]);

                /*String pr[] = date.split("призовых");
                String pr1[] = pr[1].split("Дата");
                textTourPrize.setText("prize: " + pr1[0]);
*/

                String pr[] = headerInfoString.split("Дата");
                String pr1[] = pr[0].split("призовых");
                textTourPrize.setText("prize " + pr1[1]);

                if (!ready) {
                    textInfo.setVisibility(View.VISIBLE);
                    textInfo.setText("coming soon");
                }

                if (activeButton[0]) {
                    tourListView.setAdapter(teamRateAdapter);
                    loading.setVisibility(View.GONE);
                }

                if (activeButton[1]) {
                    tourListView.setAdapter(matchAdapter);
                    loading.setVisibility(View.GONE);
                }

                if (activeButton[2]) {
                    if (!playoff) {
                        textInfo.setVisibility(View.VISIBLE);
                        textInfo.setText("without playoff stage");
                        loading.setVisibility(View.GONE);
                        matchList.clear();
                        tourListView.setAdapter(matchAdapter);
                    } else {
                        if (matchList.size() > 0) {
                            tourListView.setAdapter(matchAdapter);
                            loading.setVisibility(View.GONE);
                        } else {
                            textInfo.setText("nothing to show");
                            textInfo.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            tourListView.setAdapter(matchAdapter);
                        }
                    }

                }
            } catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }

    public void updateGroupMatch(){
        new TournamentActivity.NewThreadn().execute();
        matchAdapter = new MatchAdapter(this, matchList);
        loading.setVisibility(View.VISIBLE);
    }

    public void updateGroupTable(){
        new TournamentActivity.NewThreadn().execute();
        teamRateAdapter = new TeamRateAdapter(this, tableItemsList);
        loading.setVisibility(View.VISIBLE);
    }

    public void updateOffMatch(){
        new TournamentActivity.NewThreadn().execute();
        matchAdapter = new MatchAdapter(this, matchList);
        loading.setVisibility(View.VISIBLE);
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
}
