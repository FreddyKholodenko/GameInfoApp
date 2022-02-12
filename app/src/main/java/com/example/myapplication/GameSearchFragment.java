package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameSearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Game> gameList =new ArrayList<Game>();
    private ListView l1;
    private String status="all";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance("https://finalproj-18cc2-default-rtdb.firebaseio.com/").getReference();
    DatabaseReference mConditionRef = mRootRef.child("Games");


    public GameSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameSearchFragment newInstance(String param1, String param2) {
        GameSearchFragment fragment = new GameSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_game_search, container, false);
        gameList.clear();
        loadGames(view);
        return view;
    }


    private void loadGames(View view)
    {
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot g:snapshot.getChildren())
                {

                    String genres[]=new String[(int) g.child("Genres").getChildrenCount()];
                    int i=0;
                    for(DataSnapshot genre:g.child("Genres").getChildren())
                    {
                        genres[i]=(genre.getValue(String.class));
                        i++;
                    }
                    gameList.add(new Game(g.child("Name").getValue(String.class),g.child("Developer").getValue(String.class),g.child("Publisher").getValue(String.class),genres,g.child("Year").getValue(Integer.class),g.child("Series").getValue(String.class),g.child("Trailer").getValue(String.class),g.child("Platform").getValue(String.class),g.child("Description").getValue(String.class)));
                    loadList(view);
                    searchList(view);
                    loadCategories(view);
                    loadClickListItem(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadList(View view)
    {
        GameAdapter adapter=new GameAdapter(getActivity(),0, gameList);
        l1=(ListView) view.findViewById(R.id.GameListView);
        l1.setAdapter(adapter);
    }

    private void searchList(View view)
    {
        SearchView searchView =(SearchView) view.findViewById(R.id.GameSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Game> filter = new ArrayList<Game>();
                if(status=="all") {
                    for (Game g : gameList) {
                        if (g.getName().toLowerCase().contains(s.toLowerCase())) {
                            filter.add(g);
                        }
                        else if(g.getSeries().toLowerCase().contains(s.toLowerCase()))
                        {

                        }
                    }
                }
                else
                {
                    for (Game g : gameList) {
                        if ((g.getName().toLowerCase().contains(s.toLowerCase())&&g.getDeveloper().toLowerCase().contains(status))||g.getSeries().toLowerCase().contains(s.toLowerCase())&&g.getDeveloper().toLowerCase().contains(status)) {
                            filter.add(g);
                        }
                        else if((g.getName().toLowerCase().contains(s.toLowerCase()) && Integer.toString(g.getReleaseYear()).toLowerCase().contains(status))||(g.getSeries().toLowerCase().contains(s.toLowerCase()) && Integer.toString(g.getReleaseYear()).toLowerCase().contains(status)))
                        {
                            filter.add(g);
                        }
                        else
                        {
                            for(String genre:g.getGenres())
                            {
                                if (g.getName().toLowerCase().contains(s.toLowerCase())&&genre.toLowerCase().contains(status)) {
                                    filter.add(g);
                                    break;
                                }
                            }
                        }
                    }
                }
                GameAdapter adapterFilter = new GameAdapter(getActivity(),0, filter);
                l1.setAdapter(adapterFilter);
                return false;
            }
        });
    }

    private void loadCategories(View view)
    {
        List<String> CatName1=new ArrayList<String>();
        CatName1.add("Genre");
        for(Game g: gameList)
        {
            for(String genre:g.getGenres())
            {
                if(!CatName1.contains(genre))
                {
                    CatName1.add(genre);
                }
            }
        }
        Spinner spinner =(Spinner) view.findViewById(R.id.spinnerGenre);
        ArrayAdapter<String> adapterSpin=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,CatName1);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i)=="Genre")
                {
                    GameAdapter adapterFilter = new GameAdapter(getActivity(), 0, gameList);
                    l1.setAdapter(adapterFilter);
                    status = "all";
                }
                else {
                    List<Game> filter = new ArrayList<Game>();
                    for (Game g : gameList) {
                        for (String genre : g.getGenres()) {
                            if (genre.toLowerCase().contains(adapterView.getItemAtPosition(i).toString().toLowerCase())) {
                                filter.add(g);
                                break;
                            }
                        }
                    }
                    GameAdapter adapterFilter = new GameAdapter(getActivity(), 0, filter);
                    l1.setAdapter(adapterFilter);
                    status = adapterView.getItemAtPosition(i).toString().toLowerCase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> CatName2=new ArrayList<String>();
        CatName2.add("Release Year");
        for(Game g: gameList)
        {
            if(!CatName2.contains(Integer.toString(g.getReleaseYear())))
            {
                CatName2.add(Integer.toString(g.getReleaseYear()));
            }
        }
        spinner =(Spinner) view.findViewById(R.id.spinnerYear);
        adapterSpin=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,CatName2);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i)=="Release Year")
                {
                    GameAdapter adapterFilter = new GameAdapter(getActivity(), 0, gameList);
                    l1.setAdapter(adapterFilter);
                    status = "all";
                }
                else {
                    List<Game> filter = new ArrayList<Game>();
                        for (Game g : gameList) {
                            if (Integer.toString(g.getReleaseYear()).toLowerCase().contains(adapterView.getItemAtPosition(i).toString().toLowerCase())) {
                                filter.add(g);
                            }
                        }
                    GameAdapter adapterFilter = new GameAdapter(getActivity(), 0, filter);
                    l1.setAdapter(adapterFilter);
                    status = adapterView.getItemAtPosition(i).toString().toLowerCase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> CatName3=new ArrayList<String>();
        CatName3.add("Developer");
        for(Game g: gameList)
        {
            if(!CatName3.contains(g.getDeveloper())) {
                CatName3.add(g.getDeveloper());
            }
        }
        spinner =(Spinner) view.findViewById(R.id.spinnerDev);
        adapterSpin=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,CatName3);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i)=="Developer")
                {
                    GameAdapter adapterFilter = new GameAdapter(getActivity(), 0, gameList);
                    l1.setAdapter(adapterFilter);
                    status = "all";
                }
                else {
                    List<Game> filter = new ArrayList<Game>();
                    for (Game g : gameList) {
                        if (g.getDeveloper().toLowerCase().contains(adapterView.getItemAtPosition(i).toString().toLowerCase())) {
                            filter.add(g);
                        }
                    }
                    GameAdapter adapterFilter = new GameAdapter(getActivity(), 0, filter);
                    l1.setAdapter(adapterFilter);
                    status = adapterView.getItemAtPosition(i).toString().toLowerCase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadClickListItem(View fView)
    {
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).setGame((Game)adapterView.getItemAtPosition(i));
                Navigation.findNavController(fView).navigate(R.id.action_gameSearchFragment_to_gamePageFragment);
            }
        });
    }

}