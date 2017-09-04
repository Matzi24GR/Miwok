package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Geomat Matzi on 21/6/2017.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int mcolor;

    public WordAdapter(Activity context, ArrayList<Word> words, int color) {
        super(context, 0, words);
        mcolor = color;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Word currentWord = getItem(position);
        TextView MiwokTextView = (TextView) listItemView.findViewById(R.id.MiwokWord);
        TextView DefaultTextView = (TextView) listItemView.findViewById(R.id.DefaultWord);
        ImageView ImageView = (ImageView) listItemView.findViewById(R.id.image);
        RelativeLayout RelativeLayout = (RelativeLayout) listItemView.findViewById(R.id.RelativeLayout);

        MiwokTextView.setText(currentWord.getMiwokTranslation());
        DefaultTextView.setText(currentWord.getDefaultTranslation());
        ImageView.setImageResource(currentWord.getImageResourceId());
        RelativeLayout.setBackgroundResource(mcolor);

        return listItemView;
    }
}


