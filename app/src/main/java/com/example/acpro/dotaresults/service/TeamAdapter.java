package com.example.acpro.dotaresults.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.acpro.dotaresults.R;
import com.example.acpro.dotaresults.model.Team;

import java.util.List;

public class TeamAdapter extends BaseAdapter{

    private List<Team> list;
    private LayoutInflater layoutInflater;

    public TeamAdapter(Context context, List<Team> list) {
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
            view1 = layoutInflater.inflate(R.layout.team_list_item, viewGroup, false);
        }
        Team team = getTeam(i);

        TextView name = (TextView) view1.findViewById(R.id.teamName);
        TextView prize = (TextView) view1.findViewById(R.id.teamPrize);
        TextView gold = (TextView) view1.findViewById(R.id.teamGold);
        TextView silver = (TextView) view1.findViewById(R.id.teamSilver);
        TextView bronze = (TextView) view1.findViewById(R.id.teamBronze);
        ImageView logo = (ImageView) view1.findViewById(R.id.teamLogo);

        name.setText(team.getName());
        prize.setText(team.getPrize());
        gold.setText(team.getGold());
        silver.setText(team.getSilver());
        bronze.setText(team.getBronze());
        if (!team.getLogo().contains("assets")) {
            Glide
                    .with(view1.getContext())
                    .load(team.getLogo())
                    .into(logo);
        }else {
            logo.setImageResource(R.drawable.no_image);
        }


        return view1;
    }

    private Team getTeam(int pos){
        return (Team) getItem(pos);
    }
}
