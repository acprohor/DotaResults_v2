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
import com.example.acpro.dotaresults.model.PlayerRate;
import com.example.acpro.dotaresults.model.Team;

import java.util.List;

public class PlayerRateAdapter extends BaseAdapter {

    private List<PlayerRate> list;
    private LayoutInflater layoutInflater;

    public PlayerRateAdapter(Context context, List<PlayerRate> list) {
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
            view1 = layoutInflater.inflate(R.layout.player_rate_list_item, viewGroup, false);
        }
        PlayerRate playerRate = getPlayerRate(i);

        ImageView photo = (ImageView) view1.findViewById(R.id.imagePlayerR);
        TextView nick = (TextView) view1.findViewById(R.id.nicknamePlayerR);
        TextView name = (TextView) view1.findViewById(R.id.namePlayerR);
        TextView prize = (TextView) view1.findViewById(R.id.prizePlayerR);
        TextView gold = (TextView) view1.findViewById(R.id.goldPlayerR);
        TextView silver = (TextView) view1.findViewById(R.id.silverPlayerR);
        TextView bronze = (TextView) view1.findViewById(R.id.bronzePlayerR);

        nick.setText(playerRate.getNick());
        name.setText(playerRate.getName());
        prize.setText(playerRate.getPrize());
        gold.setText(playerRate.getGold());
        silver.setText(playerRate.getSilver());
        bronze.setText(playerRate.getBronze());
        if (!playerRate.getPhoto().contains("assets")) {
            Glide
                    .with(view1.getContext())
                    .load(playerRate.getPhoto())
                    .into(photo);
        }else {
            photo.setImageResource(R.drawable.no_image);
        }


        return view1;
    }

    private PlayerRate getPlayerRate (int pos){
        return (PlayerRate) getItem(pos);
    }
}
