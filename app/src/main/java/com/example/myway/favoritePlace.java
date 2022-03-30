package com.example.myway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myway.Model.Room;

public class favoritePlace extends Fragment {
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    View progBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.favorite_place_fragment, container, false);
        progBar = view.findViewById(R.id.fav_place_progressBar);
        //progBar.setVisibility(View.INVISIBLE);
        RecyclerView list = view.findViewById(R.id.fav_place_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new MyAdapter();
        list.setAdapter(adapter);

        return view;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameTV;
        Button ifFavBtn;//star

        public MyViewHolder(@NonNull View itemView, AdapterView.OnItemClickListener listener) {
            super(itemView);
            placeNameTV = itemView.findViewById(R.id.fav_place_row_name);
            ifFavBtn = itemView.findViewById(R.id.fav_place_row_chip);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        public void bind(Room r) {
            placeNameTV.setText(r.getDetails());
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        AdapterView.OnItemClickListener listener;

        public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.fav_place_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            // Room r = allRoomsList.get(position);
            // holder.bind(r);
        }


        @Override
        public int getItemCount() {
//            if (allRoomsList == null) return 0;
//            return allRoomsList.size();
        }


    }
}
