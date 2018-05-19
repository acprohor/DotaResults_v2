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
import com.example.acpro.dotaresults.model.Player;

import java.util.List;

public class PlayerAdapter extends BaseAdapter {

    private List<Player> list;
    private LayoutInflater layoutInflater;

    public PlayerAdapter(Context context, List<Player> list) {
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
            view1 = layoutInflater.inflate(R.layout.player_list_item, viewGroup, false);
        }
        Player player = getPlayer(i);

        ImageView photo = (ImageView) view1.findViewById(R.id.imagePlayer);
        TextView nickname = (TextView) view1.findViewById(R.id.nicknamePlayer);
        TextView info = (TextView) view1.findViewById(R.id.infoPlayer);
        TextView role = (TextView) view1.findViewById(R.id.rolePlayer);
        TextView winRate = (TextView) view1.findViewById(R.id.winRatePlayer);

        nickname.setText(player.getNickname());
        info.setText(player.getInfo());
        role.setText(player.getRole());
        winRate.setText(player.getWinRate());
        if (!player.getPhoto().contains("assets") && !player.getPhoto().equals("")) {
            Glide
                    .with(view1.getContext())
                    .load(player.getPhoto())
                    .into(photo);
        }else {
            photo.setImageResource(R.drawable.no_image);
        }

        return view1;
    }

    private Player getPlayer(int pos){
        return (Player) getItem(pos);
    }
}
