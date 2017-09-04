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

public class PhracesFragment extends Fragment {


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

    public PhracesFragment() {
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
        words.add(new Word("Πού πας;", "minto wuksus", 0 , R.raw.phrase_where_are_you_going));
        words.add(new Word("Πώς σε λένε;", "\t\n" +
                "tinnә oyaase'nә", 0 , R.raw.phrase_what_is_your_name));
        words.add(new Word("Με λένε...", "\t\n" +
                "oyaaset...", 0 , R.raw.phrase_my_name_is));
        words.add(new Word("Πώς είσαι;", "\t\n" +
                "michәksәs?", 0 , R.raw.phrase_how_are_you_feeling));
        words.add(new Word("Είμαι καλά", "kuchi achit", 0 , R.raw.phrase_im_feeling_good));
        words.add(new Word("Έρχεσαι;", "\t\n" +
                "әәnәs'aa?",  0 , R.raw.phrase_are_you_coming));
        words.add(new Word("Ναι, έρχομαι", "\t\n" +
                "hәә’ әәnәm", 0 , R.raw.phrase_yes_im_coming));
        words.add(new Word("Έρχομαι", "әәnәm", 0 , R.raw.phrase_im_coming));
        words.add(new Word("Ας πάμε", "yoowutis", 0 , R.raw.phrase_lets_go));
        words.add(new Word("Έλα εδώ", "әnni'nem", 0 , R.raw.phrase_come_here));


        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);
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


