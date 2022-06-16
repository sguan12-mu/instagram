package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";
    private TextView tvUsername;
    private TextView tvUsername2;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTime;
    Post post;
    RecyclerView rvComments;
    private ImageButton ibComment;
    protected CommentAdapter adapter;
    protected List<Comment> allComments;
    private ImageButton ibLike;
    private TextView tvLike;

    @Override
    protected void onRestart(){
        super.onRestart();
        adapter.clear();
        queryComments();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername2 = findViewById(R.id.tvUsername2);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvTime = findViewById(R.id.tvTime);
        rvComments = findViewById(R.id.rvComments);

        post = (Post) getIntent().getParcelableExtra(Post.class.getSimpleName());
        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvUsername2.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }

        // setting timestamp
        Date createdAt = post.getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        tvTime.setText(timeAgo);

        // commenting on posts
        ibComment = findViewById(R.id.ibComment);
        ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, ComposeComment.class);
                i.putExtra("post", post);
                startActivity(i);
            }
        });

        allComments = new ArrayList<>();
        adapter = new CommentAdapter(this, allComments);
        // set the adapter on the recycler view
        rvComments.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        queryComments();

        ibLike = findViewById(R.id.ibLike);
        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getLiked()) {
                    post.setLike(post.getLike() - 1);
                    post.setLiked(false);
                }
                else {
                    post.setLike(post.getLike() + 1);
                    post.setLiked(true);
                }
                setLike();
                post.saveInBackground();
            }
        });
        tvLike = findViewById(R.id.tvLike);
        setLike();
    }

    void setLike() {
        if (post.getLiked()) {
            // set full heart
            ibLike.setImageDrawable(this.getDrawable(R.drawable.ufi_heart_active));
        }
        else {
            // set empty heart
            ibLike.setImageDrawable(this.getDrawable(R.drawable.ufi_heart));
        }

        // set text
        if (post.getLike() == 1) {
            tvLike.setText(String.valueOf(post.getLike())+" like");
        }
        else {
            tvLike.setText(String.valueOf(post.getLike())+" likes");
        }
    }

    private void queryComments() {
        adapter.clear();
        // specify what type of data we want to query - Post.class
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        // include data if post matches
        query.whereEqualTo(Comment.KEY_POST,post);
        query.include(Comment.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }

                // save received posts to list and notify adapter of new data
                adapter.clear();
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
            }
        });
    }
}