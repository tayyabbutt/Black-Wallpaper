package com.novalapps.blackwallpapers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.novalapps.blackwallpapers.R;
import com.novalapps.blackwallpapers.interfaces.OnImageClickListner;

import java.util.ArrayList;
import java.util.List;

public class MostViewedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> mGallerylist2 = new ArrayList<>();
    Context mContext2;
    OnImageClickListner imageClickListner;

    public MostViewedAdapter(Context context, List<String> mGallerylist1, OnImageClickListner imageClickListner) {
        this.mContext2 = context;
        this.mGallerylist2 = mGallerylist1;
        this.imageClickListner = imageClickListner;
    }

    private class MyViewHolder2 extends RecyclerView.ViewHolder {
        ImageView thumnail;

        public MyViewHolder2(View itemView) {
            super(itemView);

            thumnail = itemView.findViewById(R.id.thumb2);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_adapter_layout, parent, false);
        return new MostViewedAdapter.MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MostViewedAdapter.MyViewHolder2 bodyViewHolder = (MostViewedAdapter.MyViewHolder2) holder;
        bindBodyComponent(bodyViewHolder, position);
    }

    private void bindBodyComponent(MostViewedAdapter.MyViewHolder2 holder, final int position) {

        Glide.with(mContext2).load(mGallerylist2.get(position)).apply(new RequestOptions().placeholder(R.drawable.loading))
                .into(holder.thumnail);

        holder.thumnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClickListner.OnImageClick(mGallerylist2.get(position));
            /*    Intent intent = new Intent(mContext2, FullViewImage.class);
                Bundle bundle = new Bundle();
//Add your data from getFactualResults method to bundle
                bundle.putSerializable("image_Object", mGallerylist2.get(position));
//Add the bundle to the intent
                intent.putExtras(bundle);
                mContext2.startActivity(intent);*/

            }
        });

        /* Glide.with(mContext2).load(mGallerylist2.get(position)).into(holder.thumnail);
         *//*  Glide.with(mContext2).load(mGallerylist2.get(position)).apply(new RequestOptions().placeholder(R.drawable.loading))
                .into(holder.thumnail);*//*

        holder.thumnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext2, SlideshowDialogFragment.class);
                intent.putExtra("categoryTitle", mGallerylist2.get(position));


                *//* Intent intent = new Intent(mContext2, SlideshowDialogFragment.class);
                Bundle bundle = new Bundle();
//Add your data from getFactualResults method to bundle
                bundle.putSerializable("image_Object", mGallerylist2.get(position));
//Add the bundle to the intent
                intent.putExtras(bundle);
                mContext2.startActivity(intent);*//*

            }
        }
        );*/

    }

    @Override
    public int getItemCount() {
        return mGallerylist2.size();
    }/*
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {



        private GestureDetector gestureDetector;
        private MostViewedAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MostViewedAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }*/
}

