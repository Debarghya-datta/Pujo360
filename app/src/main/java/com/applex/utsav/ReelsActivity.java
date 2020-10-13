package com.applex.utsav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;
import android.content.res.Configuration;
import android.os.Bundle;
import com.applex.utsav.adapters.ReelsAdapter;
import com.applex.utsav.models.ReelsPostModel;
import com.applex.utsav.preferences.IntroPref;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ReelsActivity extends AppCompatActivity {

    private ViewPager2 reelsList;
    private String uid, bool;
    private Query query;
    private ReelsAdapter adapter;
    private ArrayList<ReelsPostModel> models;
    private String type, ts, pCom_ts, from;
    public static String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntroPref introPref = new IntroPref(this);
        String lang= introPref.getLanguage();
        Locale locale= new Locale(lang);
        Locale.setDefault(locale);
        Configuration config= new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

//        /////////////////DAY OR NIGHT MODE///////////////////
//        FirebaseFirestore.getInstance().document("Mode/night_mode").get()
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful()) {
//                        if(task.getResult().getBoolean("night_mode")) {
//                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                        } else {
//                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                        }
//                    } else {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    }
//                });
//        /////////////////DAY OR NIGHT MODE///////////////////

        setContentView(R.layout.activity_reels);

        models = new ArrayList<>();
        reelsList = findViewById(R.id.recyclerReelsViewAll);

        bool = Objects.requireNonNull(getIntent().getStringExtra("bool"));

        if(Objects.requireNonNull(getIntent().getExtras()).getString("type") != null) {
            type = getIntent().getExtras().getString("type");
        }

        if(Objects.requireNonNull(getIntent().getExtras()).getString("ts") != null) {
            ts = getIntent().getExtras().getString("ts");
        }

        if(Objects.requireNonNull(getIntent().getExtras()).getString("pCom_ts") != null) {
            pCom_ts = getIntent().getExtras().getString("pCom_ts");
        }

        if(getIntent().getStringExtra("uid") != null) {
            uid = getIntent().getStringExtra("uid");
        }

        if(getIntent().getStringExtra("from") != null) {
            from = getIntent().getStringExtra("from");
        }

        if(getIntent().getStringExtra("docID") != null) {
            String docID = getIntent().getStringExtra("docID");
            query = FirebaseFirestore.getInstance().collection("Reels").whereEqualTo("docID", docID);
        } else if(postID != null) {
            query = FirebaseFirestore.getInstance().collection("Reels").whereEqualTo("docID", postID);
        } else {
            query = FirebaseFirestore.getInstance().collection("Reels")
                    .orderBy("ts", Query.Direction.DESCENDING)
                    .limit(1);
        }
    }

    private void buildRecyclerView() {

        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = Objects.requireNonNull(task.getResult()).getDocuments().get(0);
                ReelsPostModel reelsPostModel = document.toObject(ReelsPostModel.class);
                Objects.requireNonNull(reelsPostModel).setDocID(document.getId());
                models.add(reelsPostModel);

                Query query_desc = null;

                if(bool.matches("1")) {
                    query_desc = FirebaseFirestore.getInstance()
                            .collection("Reels")
                            .orderBy("ts", Query.Direction.DESCENDING)
                            .whereEqualTo("type", from)
                            .limit(1)
                            .startAfter(document);
                }
                else if(bool.matches("2")) {
                    query_desc = FirebaseFirestore.getInstance()
                            .collection("Reels")
                            .whereEqualTo("uid", uid)
                            .orderBy("ts", Query.Direction.DESCENDING)
                            .limit(1)
                            .startAfter(document);
                }
                else if(bool.matches("3")) {
                    query_desc = FirebaseFirestore.getInstance()
                            .collection("Reels")
                            .orderBy("newTs", Query.Direction.DESCENDING)
                            .limit(1)
                            .startAfter(document);
                }

                Objects.requireNonNull(query_desc).get().addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful() && Objects.requireNonNull(task1.getResult()).getDocuments().size() != 0) {
                        DocumentSnapshot document1 = Objects.requireNonNull(task1.getResult()).getDocuments().get(0);
                        ReelsPostModel reelsPostModel1 = document1.toObject(ReelsPostModel.class);
                        Objects.requireNonNull(reelsPostModel1).setDocID(document1.getId());
                        models.add(models.size(), reelsPostModel1);
                    }
                });

                adapter = new ReelsAdapter(ReelsActivity.this, models, bool, uid, type, ts, pCom_ts, from);
                reelsList.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildRecyclerView();
    }
}