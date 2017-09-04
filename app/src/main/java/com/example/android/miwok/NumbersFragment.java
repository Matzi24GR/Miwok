package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersFragment extends Fragment {


    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener ChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if ((focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) && mediaPlayer != null) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    private void releaseMediaPlayer(){
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            audioManager.abandonAudioFocus(ChangeListener);
        }
    }

    public NumbersFragment() {
        // Required empty public constructor

    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);


        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Ένα", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("Δύο", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("Τρία", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("Τέσσερα", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("Πέντε", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("Έξι", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("Εφτά", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("Οκτώ", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("Εννέα", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("Δέκα", "na’aacha", R.drawable.number_ten, R.raw.number_ten));


        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);
        final ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);
                Log.v("NumbersActivity", "Current word: " + word);
                int result = audioManager.requestAudioFocus(ChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    releaseMediaPlayer();
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getSoundResourceId() );
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            releaseMediaPlayer();
                            audioManager.abandonAudioFocus(ChangeListener);
                        }
                    });

                }
            }
        });
        listView.setAdapter(adapter);


        return rootView;
    }
}


