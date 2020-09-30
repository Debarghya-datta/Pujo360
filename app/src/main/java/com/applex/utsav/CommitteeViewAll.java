package com.applex.utsav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.applex.utsav.models.BaseUserModel;
import com.applex.utsav.preferences.IntroPref;
import com.applex.utsav.utility.BasicUtility;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class CommitteeViewAll extends AppCompatActivity {

    private RecyclerView cRecyclerView;
    private ProgressBar progress;
    private ProgressBar progressMoreCom;
    private LinearLayout emptyLayout;

    private SwipeRefreshLayout swipeRefreshLayout;

    private FirestorePagingAdapter adapter;
    IntroPref introPref;

    ImageView search, back;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        introPref = new IntroPref(this);
        String lang= introPref.getLanguage();
        Locale locale= new Locale(lang);
        Locale.setDefault(locale);
        Configuration config= new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_committee_view_all);

        search = findViewById(R.id.search);
        back = findViewById(R.id.back);
        searchText = findViewById(R.id.search_text);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchText.getText().toString().isEmpty()){
                    buildRecyclerView(searchText.getText().toString());
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommitteeViewAll.super.onBackPressed();
            }
        });


        progress = findViewById(R.id.content_progress);
        progressMoreCom = findViewById(R.id.progress_more_comm);
        progress.setVisibility(View.VISIBLE);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        cRecyclerView = findViewById(R.id.community_view_all);
        cRecyclerView.setHasFixedSize(true);
        emptyLayout = findViewById(R.id.emptyLayout);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(CommitteeViewAll.this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cRecyclerView.setLayoutManager(gridLayoutManager);


        buildRecyclerView(null);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),getResources()
                .getColor(R.color.purple));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            buildRecyclerView(null);
        });

    }


    private void buildRecyclerView(String search) {

        Query query;
        if(search != null){
            query = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .whereEqualTo("type", "com")
                    .whereGreaterThanOrEqualTo("name", searchText.getText().toString())
                    .limit(10);
        }
        else {
            query = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .whereEqualTo("type", "com")
                    .limit(10);
        }


        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .setEnablePlaceholders(true)
                .build();

        FirestorePagingOptions<BaseUserModel> options = new FirestorePagingOptions.Builder<BaseUserModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, snapshot -> {
                    BaseUserModel committeeModel = new BaseUserModel();
                    if(snapshot.exists()) {
                        committeeModel = snapshot.toObject(BaseUserModel.class);
                    }
                    return committeeModel;
                })
                .build();

        adapter = new FirestorePagingAdapter<BaseUserModel, RecyclerView.ViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull BaseUserModel currentItem) {

                ProgrammingViewHolder programmingViewHolder = (ProgrammingViewHolder) holder;

                if(currentItem.getCoverpic() != null){
                    Picasso.get().load(currentItem.getCoverpic())
                            .error(R.drawable.image_background_grey)
                            .placeholder(R.drawable.image_background_grey)
                            .into(programmingViewHolder.committeeCover);
                }
                else {
                    programmingViewHolder.committeeCover.setImageResource(R.drawable.image_background_grey);
                }

                if(currentItem.getDp() != null){
                    Picasso.get().load(currentItem.getDp())
                            .error(R.drawable.image_background_grey)
                            .placeholder(R.drawable.image_background_grey)
                            .into(programmingViewHolder.committeeDp);
                }
                else {
                    programmingViewHolder.committeeDp.setImageResource(R.drawable.image_background_grey);
                }

                if(currentItem.getName() != null){
                    programmingViewHolder.committeeName.setText(currentItem.getName());
                }
                else {
                    programmingViewHolder.committeeName.setVisibility(View.GONE);
                }

                programmingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CommitteeViewAll.this, ActivityProfileCommittee.class);
                        intent.putExtra("uid",currentItem.getUid());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
                View v = layoutInflater.inflate(R.layout.item_committee_grid, viewGroup, false);
                return new ProgrammingViewHolder(v);

            }

            @Override
            public int getItemViewType(int position) { return position; }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {

                super.onLoadingStateChanged(state);
                switch (state) {
                    case ERROR:
                        BasicUtility.showToast(CommitteeViewAll.this, "Something went wrong...");
                        break;
                    case LOADING_MORE:
                        progressMoreCom.setVisibility(View.VISIBLE);
                        break;
                    case LOADED:
                        progressMoreCom.setVisibility(View.GONE);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case FINISHED:
                        progress.setVisibility(View.GONE);
                        progressMoreCom.setVisibility(View.GONE);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        if(adapter.getItemCount() == 0) {
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        };

        progress.setVisibility(View.GONE);
        progressMoreCom.setVisibility(View.GONE);
        cRecyclerView.setAdapter(adapter);

    }


    private static class ProgrammingViewHolder extends RecyclerView.ViewHolder{

        TextView committeeName;
        ImageView committeeCover, committeeDp;

        ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);

            committeeName = itemView.findViewById(R.id.committee_name);
            committeeDp = itemView.findViewById(R.id.committee_dp);
            committeeCover = itemView.findViewById(R.id.committee_cover);

        }
    }


//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            Toast.makeText(getApplicationContext(), "Go to Search Activity", Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }



}
