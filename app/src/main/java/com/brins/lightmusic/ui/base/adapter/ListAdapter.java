package com.brins.lightmusic.ui.base.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.brins.lightmusic.utils.ShowShadowUtil;
import com.brins.lightmusic.R;
import com.brins.lightmusic.model.loaclmusic.LocalMusic;

import java.util.List;

import static com.brins.lightmusic.utils.UtilsKt.string2Bitmap;


public class ListAdapter<T> extends RecyclerView.Adapter<ListAdapter.viewHolder> {

    private Context mContext;
    private List<T> mData;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private int mLastItemClickPosition = RecyclerView.NO_POSITION;

    public ListAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }


    @Override
    public ListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_local_music, parent, false);
        final ListAdapter.viewHolder holder = new ListAdapter.viewHolder(itemView);
        if (mItemClickListener != null) {
            itemView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mLastItemClickPosition = position;
                    mItemClickListener.onItemClick(position);
                }
            });
        }
        if (mItemLongClickListener != null) {
            itemView.setOnLongClickListener(v -> {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mItemLongClickListener.onItemClick(position);
                }
                return false;
            });
        }
        return holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(ListAdapter.viewHolder holder, int position) {
        LocalMusic music = (LocalMusic) getItem(position);
        holder.textViewName.setText(music.getName());
        holder.textViewArtist.setText(music.getSinger());
        if (music.getCoverBitmap() == null) {
            music.setCoverBitmap(string2Bitmap(music.getCover()));
        }
        holder.imgCover.setImageBitmap(music.getCoverBitmap());
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public void addData(List<T> data) {
        if (mData == null) {
            mData = data;
        } else {
            mData.addAll(data);
        }
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public int getLastItemClickPosition() {
        return mLastItemClickPosition;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public OnItemLongClickListener getItemLongClickListener() {
        return mItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView textViewName;
        TextView textViewArtist;
        ImageView imgCover;
        public viewHolder(@NonNull View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewName);
            textViewArtist = view.findViewById(R.id.textViewArtist);
            imgCover = view.findViewById(R.id.imgCover);

        }
    }
}
