package com.example.acpro.dotaresults.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.acpro.dotaresults.R;
import com.example.acpro.dotaresults.model.TeamRate;

import java.util.List;

public class TeamRateAdapter extends BaseAdapter {
    private List<TeamRate> list;
    private LayoutInflater layoutInflater;

    public TeamRateAdapter(Context context, List<TeamRate> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        if (view1 == null){
            view1 = layoutInflater.inflate(R.layout.table_item, viewGroup, false);
        }
        TeamRate teamRate = getTeamRate(i);

        TextView teamName = (TextView) view1.findViewById(R.id.textTeamName);
        TextView matches = (TextView) view1.findViewById(R.id.textMatchSum);
        TextView wins = (TextView) view1.findViewById(R.id.textWinSum);
        TextView draw = (TextView) view1.findViewById(R.id.textDrawSum);
        TextView lose = (TextView) view1.findViewById(R.id.textLostSum);
        TextView points = (TextView) view1.findViewById(R.id.textPoints);

        teamName.setText(teamRate.getTeamName());
        matches.setText(teamRate.getMatches());
        wins.setText(teamRate.getWins());
        draw.setText(teamRate.getDraw());
        lose.setText(teamRate.getLose());
        points.setText(teamRate.getPoints());

        return view1;
    }

    private TeamRate getTeamRate(int pos){
        return (TeamRate) getItem(pos);
    }
}
