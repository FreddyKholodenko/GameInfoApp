package com.example.myapplication;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GameAdapter extends ArrayAdapter<Game> {
    public GameAdapter(@NonNull Context context, int resource, @NonNull List<Game> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Game game = getItem(position);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.game_item,parent,false);
        }
        TextView textView1=(TextView) convertView.findViewById(R.id.textView9);
        TextView textView2=(TextView) convertView.findViewById(R.id.textView12);

        textView1.setText(game.getName());
        textView2.setText(Integer.toString(game.getReleaseYear()));

        return convertView;
    }
}
