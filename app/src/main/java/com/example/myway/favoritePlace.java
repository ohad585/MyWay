package com.example.myway;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myway.Model.Model;
import com.example.myway.Model.Room;
import com.example.myway.Model.User;

import java.util.LinkedList;
import java.util.List;

public class favoritePlace extends Fragment{
        MyAdapter adapter;
        View progBar;
        User currentUser;
        List<Room> allFavoritePlacesList = new LinkedList<>();
        allFavoritePlacesViewModel viewModel;
        View view;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.favorite_place_fragment, container, false);
            progBar = view.findViewById(R.id.fav_place_progressBar);
            progBar.setVisibility(View.VISIBLE);

            Model.instance.getCurrentUser(new Model.getCurrentUserListener() {
                @Override
                public void onComplete(User user) {
                    currentUser=user;
                    progBar.setVisibility(View.INVISIBLE);
                    showFavoritePlaces();
                }
            });

            if(viewModel.getData()==null){refreshData();};
            viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Room>>() {
                @Override
                public void onChanged(List<Room> favoritePlaces) {
                    allFavoritePlacesList = favoritePlaces ;
                    Log.d("TAG", "onChanged: favoritePlaces list updated");
                    adapter.notifyDataSetChanged();
                }

            });
            return view;
        }


        public void showFavoritePlaces() {
            RecyclerView list = view.findViewById(R.id.fav_place_rv);
            list.setHasFixedSize(true);
            list.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new MyAdapter();
            list.setAdapter(adapter);
            if (viewModel.getData() == null) {
                refreshData();
            }

            viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Room>>() {
                @Override
                public void onChanged(List<Room> rooms) {
                    adapter.notifyDataSetChanged();
                }
            });
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    //progBar.setVisibility(View.VISIBLE);
                    Room room = viewModel.getData().getValue().get(position);
                }
            });

        }


        private void refreshData(){
            Log.d("TAG", "refreshData: watch favorite rooms");
        }




        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView placeNameTV;
            CheckBox ifFavBtn;

            public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
                super(itemView);
                placeNameTV = itemView.findViewById(R.id.fav_place_row_name);
                ifFavBtn = itemView.findViewById(R.id.fav_place_row_chip);
                ifFavBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ifFavBtn.isChecked()==true){
                            ifFavBtn.setChecked(false);
                            //TO DO: updateListOfFavorite();
                        }
                    }
                });
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
            public void bind(Room r) {
                placeNameTV.setText(r.getDetails());
                ifFavBtn.setChecked(true);
            }
        }

        interface OnItemClickListener {
            void onItemClick(int position, View v);
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
                MyViewHolder holder = new MyViewHolder(view, listener);
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                Room r  = viewModel.getData().getValue().get(position);
                if(r.isDeleted()==false) {
                    holder.bind(r);
                }
            }

            @Override
            public int getItemCount() {
                if (allFavoritePlacesList == null)
                    return 0;
                return allFavoritePlacesList.size();
            }
        }
}
