package com.example.myway;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myway.Model.Model;
import com.example.myway.Model.RoomGraph;
import com.example.myway.Model.User;

import java.util.LinkedList;
import java.util.List;

public class favoritePlace extends Fragment {
    View view;
    RoomGraph.RoomRepresent r;
    View progBar;
    allRoomsHistoryViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    User currentUser;


    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(allRoomsHistoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.favorite_place_fragment, container, false);
        progBar = view.findViewById(R.id.fav_place_progressBar);
        progBar.setVisibility(View.VISIBLE);
        Model.instance.getCurrentUser(new Model.getCurrentUserListener() {
            @Override
            public void onComplete(User user) {
                progBar.setVisibility(View.INVISIBLE);
                currentUser=user;
                showFavRooms();
            }
        });

        return view;
    }

    public void showFavRooms() {
        RecyclerView list = view.findViewById(R.id.fav_place_reviewList);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        list.setAdapter(adapter);
        swipeRefresh = view.findViewById(R.id.fav_place_swiperefresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.reloadFavPlaceListByMail(currentUser.getEmail());
            }
        });

        if (viewModel.getData() == null) {
            refreshData();
        }
        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<RoomGraph.RoomRepresent>>() {
            @Override
            public void onChanged(List<RoomGraph.RoomRepresent> rooms) {
                adapter.notifyDataSetChanged();
            }

        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                RoomGraph.RoomRepresent room = viewModel.getData().getValue().get(position);
                Log.d("TAG", "room is clicked: " + room.getRoom());
            }
        });
        swipeRefresh.setRefreshing(Model.instance.getFavPlacesListForUserLoadingState().getValue() == Model.LoadingState.loading);
        Model.instance.getFavPlacesListForUserLoadingState().observe(getViewLifecycleOwner(), loadingState ->
                swipeRefresh.setRefreshing(loadingState == Model.LoadingState.loading));

    }

    private void refreshData(){
        Log.d("TAG", "refreshData:fav rooms");
    }

    interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameTV;
        CheckBox ifFavBtn;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            placeNameTV = itemView.findViewById(R.id.fav_place_row_name);
            ifFavBtn=itemView.findViewById(R.id.fav_place_row_cb);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(pos, v);
                    }
                }
            });
        }

        public void bind(RoomGraph.RoomRepresent r) {
            placeNameTV.setText(r.getRoom());
            ifFavBtn.setChecked(true);//always be true on this page
            ifFavBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ifFavBtn.isChecked() == true) {
                        ifFavBtn.setChecked(false);
                        Model.instance.removeRoomToFavPlacesByMail(currentUser.getEmail(), placeNameTV.getText().toString());
                    }
                }
            });
        }
    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.fav_place_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view,listener);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            RoomGraph.RoomRepresent room = viewModel.getData().getValue().get(position);
            holder.bind(room);
        }
        @Override
        public int getItemCount() {
            if (viewModel.getData().getValue() == null)
                return 0;
            return viewModel.getData().getValue().size();
        }


    }
}
