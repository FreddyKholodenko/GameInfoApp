package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GamePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private GameSearchFragment searchFragment;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamePageFragment newInstance(String param1, String param2) {
        GamePageFragment fragment = new GamePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GamePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_page, container, false);
        loadGamePage(view,(((MainActivity)getActivity()).getGame()));
        return view;
    }

    private void loadGamePage(View view,Game game)
    {
        TextView nameTextView =(TextView) view.findViewById(R.id.NameTextView);
        TextView yearTextView=(TextView) view.findViewById(R.id.YearTextView);
        TextView genresTextView=(TextView) view.findViewById(R.id.GenresTextView);
        TextView platformTextView=(TextView) view.findViewById(R.id.PlatformTextView);
        TextView devTextView=(TextView) view.findViewById(R.id.DeveloperTextView);
        TextView descriptionTextView=(TextView) view.findViewById(R.id.DescriptionTextView10);
        WebView webView =(WebView) view.findViewById(R.id.webView);
        nameTextView.setText("Name:"+game.getName());
        yearTextView.setText("Release Year:"+Integer.toString(game.getReleaseYear()));
        platformTextView.setText("Platform:"+game.getPlatform());
        devTextView.setText("Developer:"+game.getDeveloper());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl("https://www.youtube-nocookie.com/embed/"+game.getTrailer());
        descriptionTextView.setText(game.getDescription());
        webView.setWebChromeClient(new WebChromeClient());
        genresTextView.setText("");
        genresTextView.setText("Genres:");
        for(String genre : game.getGenres())
        {
            genresTextView.append(genre+",");
        }
    }

}