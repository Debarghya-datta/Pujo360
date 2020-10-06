package com.applex.utsav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.applex.utsav.models.BaseUserModel;
import com.applex.utsav.preferences.IntroPref;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    IntroPref introPref;
    private ImageButton back, search;
    private TextView searchKey;
    private ImageView nosearch;
    private ProgressBar progressMore, contentProgress;
    private RecyclerView mRecyclerView;
    private Button sName, sCity;
    private LinearLayoutManager layoutManager;
    int selected_button = 0;
    private String SEARCH;
    FirestorePagingAdapter adapter1;

    private ArrayList<BaseUserModel> userList;
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
        setContentView(R.layout.activity_search);

        back = findViewById(R.id.back);
        search = findViewById(R.id.searchButton);
        searchKey = findViewById(R.id.search);
        mRecyclerView = findViewById(R.id.search_recycler);
        sName = findViewById(R.id.Sfirstname);
        sCity = findViewById(R.id.Scity);
        nosearch = findViewById(R.id.no_search);
        progressMore = findViewById(R.id.content_progress_search);

        contentProgress = findViewById(R.id.content_progress);
        searchKey.setOnEditorActionListener(editorActionListener);
        userList = new ArrayList<>();

        back.setOnClickListener(v -> {
            super.onBackPressed();
        });

        sName.setOnClickListener(v -> {

            sName.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9800")));
            sName.setTextColor(Color.parseColor("#ffffff"));

            sCity.setBackgroundResource(R.drawable.add_tags_button_background);
            sCity.setBackgroundTintList(null);
            sCity.setTextColor(Color.parseColor("#000000"));

//            susername.setBackgroundResource(R.drawable.search_profile_button_background);
//            susername.setBackgroundTintList(null);
//            susername.setTextColor(Color.parseColor("#18357C"));
//
//            sinstitute.setBackgroundResource(R.drawable.search_profile_button_background);
//            sinstitute.setBackgroundTintList(null);
//            sinstitute.setTextColor(Color.parseColor("#18357C"));

            selected_button = 1;

            if (!(searchKey.getText().toString().isEmpty())){
                userList.clear();

                contentProgress.setVisibility(View.VISIBLE);
                SEARCH = searchKey.getText().toString();
                buildRecycler("name");

            }


            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SEARCH = searchKey.getText().toString();
                    if(!SEARCH.isEmpty()){
                        userList.clear();

                        buildRecycler("name");
                        contentProgress.setVisibility(View.VISIBLE);

                    }

                }
            });


        });

        sCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sCity.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9800")));
                sCity.setTextColor(Color.parseColor("#ffffff"));

                sName.setBackgroundResource(R.drawable.add_tags_button_background);
                sName.setBackgroundTintList(null);
                sName.setTextColor(Color.parseColor("#000000"));

//                susername.setBackgroundResource(R.drawable.search_profile_button_background);
//                susername.setBackgroundTintList(null);
//                susername.setTextColor(Color.parseColor("#18357C"));
//
//                sinstitute.setBackgroundResource(R.drawable.search_profile_button_background);
//                sinstitute.setBackgroundTintList(null);
//                sinstitute.setTextColor(Color.parseColor("#18357C"));

                selected_button = 2;

                if (!(searchKey.getText().toString().isEmpty())){
                    userList.clear();

                    contentProgress.setVisibility(View.VISIBLE);
                    SEARCH = searchKey.getText().toString();
                    buildRecycler("city");

                }


                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SEARCH = searchKey.getText().toString();
                        if(!SEARCH.isEmpty()){
                            contentProgress.setVisibility(View.VISIBLE);
                            userList.clear();

                            buildRecycler("city");

                        }

                    }
                });
            }
        });

        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemViewCacheSize(20);


    }


    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo.IME_ACTION_SEARCH:
                    if(selected_button==0 || selected_button==1){
                        SEARCH = searchKey.getText().toString();
                        if(!SEARCH.isEmpty()){

                            userList.clear();

                            contentProgress.setVisibility(View.VISIBLE);
                            buildRecycler("name");


                        }
                    }
                    else if(selected_button==2){
                        SEARCH = searchKey.getText().toString();
                        if(!SEARCH.isEmpty()){

                            userList.clear();

                            contentProgress.setVisibility(View.VISIBLE);
                            buildRecycler("city");

                        }
                    }
//                    else if(selected_button==3){
//                        SEARCH = searchKey.getText().toString();
//                        if(!SEARCH.isEmpty()){
//
//                            userList.clear();
//
//                            contentProgress.setVisibility(View.VISIBLE);
//                            buildRecycler("username");
//
//
//                        }
//                    }
//                    else if(selected_button==4){
//                        SEARCH = searchKey.getText().toString();
//                        if(!SEARCH.isEmpty()){
//
//                            userList.clear();
//
//                            contentProgress.setVisibility(View.VISIBLE);
//                            buildRecycler("institute");
//
//                        }
//                    }

            }
            return false;
        }
    };


    private void buildRecycler(String type) {

        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("type", "indi")
                .orderBy(type)
                .startAt(SEARCH);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        FirestorePagingOptions<BaseUserModel> options = new FirestorePagingOptions.Builder<BaseUserModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<BaseUserModel>() {
                    @NonNull
                    @Override
                    public BaseUserModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        BaseUserModel user = snapshot.toObject(BaseUserModel.class);
//                        userList = new ArrayList<>();
//                        if(user.getInstitute().toLowerCase().contains(SEARCH.toLowerCase())){
                        user.setUid(snapshot.getId());
//                            userList.add(user);
//                        }
                        return user;
                    }
                })
                .build();

        adapter1 = new FirestorePagingAdapter<BaseUserModel, ProgrammingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position, @NonNull BaseUserModel model) {
                holder.PName.setText(model.getName());

                if(model.getCity()!=null && model.getState()!=null && !model.getCity().isEmpty() && !model.getState().isEmpty()){
                    holder.Pcity.setText(model.getCity()+", "+model.getState());

                }
                else if(model.getCity()!=null &&  !model.getCity().isEmpty() && model.getState()==null && model.getState().isEmpty())
                    holder.Pcity.setText(model.getCity());

                else if(model.getCity()==null &&  model.getCity().isEmpty() && model.getState()!=null && !model.getState().isEmpty())
                    holder.Pcity.setText(model.getState());

                else
                    holder.Pcity.setVisibility(View.GONE);

//                if(model.get()!=null) {
//                    holder.PDescription.setText(model.getCourse()+ " "+model.getCoursestart()+"-"+model.getCourseend());
//                }
//                else {
//                    holder.PDescription.setText(model.getAbout());
//                }


                String userimage_url = model.getDp();
                if(userimage_url!=null){

                        Picasso.get().load(userimage_url).placeholder(R.drawable.ic_account_circle_black_24dp).into(holder.userimage);

                }
                else{
                    holder.userimage.setImageResource(R.drawable.ic_account_circle_black_24dp);
                }

                holder.card1.setOnClickListener(v -> {
                    Intent intent = new Intent(SearchActivity.this, ActivityProfileUser.class);
                    intent.putExtra("uid", model.getUid());
                    startActivity(intent);
                });

                holder.card2.setOnClickListener(v -> {
                    Intent intent = new Intent(SearchActivity.this, ActivityProfileUser.class);
                    intent.putExtra("uid", model.getUid());
                    startActivity(intent);
                });


            }

            @NonNull
            @Override
            public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
                View v = layoutInflater.inflate(R.layout.item_search_profile, viewGroup, false);
                return new ProgrammingViewHolder(v);
            }
        };
        contentProgress.setVisibility(View.GONE);
        nosearch.setVisibility(View.GONE);
        mRecyclerView.setAdapter(adapter1);
    }

    public static class ProgrammingViewHolder extends RecyclerView.ViewHolder{

        TextView PName,Pcity,PDescription;
        ImageView userimage;
        LinearLayout card2;
        CardView card1;

        ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);
            PName = itemView.findViewById(R.id.Profilename);
            Pcity = itemView.findViewById(R.id.Pcity);
            PDescription = itemView.findViewById(R.id.Pdescription);
            userimage = itemView.findViewById(R.id.Pdp);
            card2 = itemView.findViewById(R.id.profileCard2);
            card1 = itemView.findViewById(R.id.profileCard1);
        }
    }

    private String getAlphaNumericString (int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i=0; i<n; i++)
        {
            int index = (int) (AlphaNumericString.length()*Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}