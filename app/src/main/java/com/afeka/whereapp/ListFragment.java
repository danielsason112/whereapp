package com.afeka.whereapp;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afeka.whereapp.dao.GroupDao;
import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.dao.firebase.FirebaseGroupDao;
import com.afeka.whereapp.data.Group;
import com.afeka.whereapp.data.util.EntityFactory;
import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Stream;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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

        View v = inflater.inflate(R.layout.fragment_list, container, false);



        recyclerView = v.findViewById(R.id.groups);
        recyclerView.setHasFixedSize(true);

        rvLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(rvLayoutManager);



        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        GroupDao groupDao = new FirebaseGroupDao();
        OnResponse res = new OnResponse<DataSnapshot>() {
            @Override
            public void onData(DataSnapshot data) {
                ArrayList<Group> groups = new ArrayList<Group>();
                for (DataSnapshot d:
                        data.getChildren()) {
                    Group group = d.getValue(Group.class);
                    group.setId(d.getKey());
                    groups.add(group);
                }

                String[] names = new String[groups.size()];
                for (int i = 0; i < names.length; i++) {
                    names[i] = groups.get(i).getName();
                }

                rvAdapter = new ListGroupsAdapter(names);
                recyclerView.setAdapter(rvAdapter);
            }

            @Override
            public void onError(String msg) {

            }
        };
        groupDao.readAll(res);
    }
}
