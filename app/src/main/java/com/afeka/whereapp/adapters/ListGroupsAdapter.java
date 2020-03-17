package com.afeka.whereapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.afeka.whereapp.R;
import com.afeka.whereapp.data.Group;

import java.util.List;

public class ListGroupsAdapter extends RecyclerView.Adapter<ListGroupsAdapter.MyViewHolder> {
    private List<Group> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView linearLayout;
        public MyViewHolder(CardView v) {
            super(v);
            linearLayout = v;
        }
    }

    public ListGroupsAdapter(List<Group> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ListGroupsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_group_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView name = holder.linearLayout.findViewById(R.id.group_name);
        TextView description = holder.linearLayout.findViewById(R.id.group_description);
        name.setText(mDataset.get(position).getName());
        description.setText(mDataset.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
