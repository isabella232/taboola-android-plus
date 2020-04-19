package com.taboola.samples.endlessfeed.sampleUtil;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taboola.android.api.TBImageView;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.samples.endlessfeed.R;

import java.util.List;
import java.util.Random;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_DEMO = 0;
    private final int TYPE_TABOOLA = 1;

    private final List<Object> mData;

    public FeedAdapter(List<Object> data) {
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_DEMO: {
                LinearLayout linearLayout = (LinearLayout) inflater
                        .inflate(R.layout.feed_demo_item, parent, false);
                return new DemoItemViewHolder(linearLayout);
            }
            case TYPE_TABOOLA: {
                LinearLayout linearLayout = (LinearLayout) inflater
                        .inflate(R.layout.feed_taboola_item, parent, false);
                return new TaboolaItemViewHolder(linearLayout);
            }
            default: {
                throw new IllegalStateException("Unknown view type: " + viewType);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_DEMO: {
                DemoItemViewHolder demoHolder = (DemoItemViewHolder) holder;
                DemoItem dm = (DemoItem) mData.get(position);
                demoHolder.mImageView.setImageResource(dm.getImageResourceId());
                demoHolder.mTextView.setText(dm.getText());
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                demoHolder.itemView.setBackgroundColor(color);
                demoHolder.itemView.getBackground().setAlpha(51);
                break;
            }

            case TYPE_TABOOLA: {
                TBRecommendationItem item = (TBRecommendationItem) mData.get(position);
                LinearLayout adContainer = ((TaboolaItemViewHolder) holder).mAdContainer;

                // remove all views except attribution view (which has index 0)
                for (int i = 0; i <= 3; i++) {
                    if (adContainer.getChildAt(1) != null) {
                        adContainer.removeViewAt(1);
                    }
                }

                TBImageView thumbnailView = item.getThumbnailView(adContainer.getContext());
                adContainer.addView(thumbnailView);
                adContainer.addView(item.getTitleView(adContainer.getContext()));
                adContainer.addView(item.getBrandingView(adContainer.getContext()));
                break;
            }

            default: {
                throw new IllegalStateException("Unknown view type: " + holder.getItemViewType());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof DemoItem) {
            return TYPE_DEMO;
        } else if (mData.get(position) instanceof TBRecommendationItem) {
            return TYPE_TABOOLA;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class DemoItemViewHolder extends RecyclerView.ViewHolder {

        final ImageView mImageView;
        final TextView mTextView;

        DemoItemViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
            mImageView = linearLayout.findViewById(R.id.demo_iv);
            mTextView = linearLayout.findViewById(R.id.demo_tv);
        }
    }

    static class TaboolaItemViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout mAdContainer;

        TaboolaItemViewHolder(LinearLayout adContainer) {
            super(adContainer);
            mAdContainer = adContainer;
        }
    }
}
