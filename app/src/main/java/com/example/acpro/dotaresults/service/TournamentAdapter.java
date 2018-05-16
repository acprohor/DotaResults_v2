package com.example.acpro.dotaresults.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.acpro.dotaresults.R;
import com.example.acpro.dotaresults.model.Tournament;

import java.util.List;

public class TournamentAdapter extends BaseAdapter {
    private List<Tournament> list;
    private LayoutInflater layoutInflater;

    public TournamentAdapter(Context context, List<Tournament> list) {
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
            view1 = layoutInflater.inflate(R.layout.tournament_list_item, viewGroup, false);
        }
        Tournament tournament = getTournament(i);

        TextView textChampionship = (TextView) view1.findViewById(R.id.textChampionship);
        TextView date = (TextView) view1.findViewById(R.id.textDateChampionship);
        TextView prize = (TextView) view1.findViewById(R.id.textPrizeChampionship);

        textChampionship.setText(tournament.getChampionship());
        date.setText(tournament.getDate());
        prize.setText(tournament.getPrize());

        return view1;
    }

    private Tournament getTournament(int pos){
        return (Tournament) getItem(pos);
    }
}
