package com.afeka.whereapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afeka.whereapp.adapters.ListGroupsAdapter;
import com.afeka.whereapp.R;
import com.afeka.whereapp.data.Group;

import java.util.List;


public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = v.findViewById(R.id.groups);
        recyclerView.setHasFixedSize(true);

        rvLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(rvLayoutManager);
        if (rvAdapter != null) {
            recyclerView.setAdapter(rvAdapter);
        }

        return v;
    }

    public void addGroupsToList(List<Group> groups) {
        rvAdapter = new ListGroupsAdapter(groups);
        if (recyclerView != null) {
            recyclerView.setAdapter(rvAdapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (rvAdapter != null) {
            recyclerView.setAdapter(rvAdapter);
        }
    }
}
