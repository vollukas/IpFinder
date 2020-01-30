package com.example.ipfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IpAdapter extends RecyclerView.Adapter<IpAdapter.IpViewHolder> {
    private ArrayList<IpItem> _ipList;
    private OnItemClickListener _listener;


    public IpAdapter(ArrayList<IpItem> ipList){
        _ipList = ipList;
    }

    @NonNull
    @Override
    public IpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ip_item, parent, false);
        IpViewHolder viewHolder = new IpViewHolder(v, _listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IpViewHolder holder, int position) {
        IpItem currentItem = _ipList.get(position);

        holder._imageView.setImageResource(currentItem.getImageResource());
        holder._textView1.setText(currentItem.getText1());
        holder._textView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return _ipList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        _listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public static class IpViewHolder extends RecyclerView.ViewHolder {
        public ImageView _imageView;
        public TextView _textView1;
        public TextView _textView2;
        public ImageView _delete;
        public IpViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            _imageView = itemView.findViewById(R.id.imageView);
            _textView1 = itemView.findViewById(R.id.textView1);
            _textView2 = itemView.findViewById(R.id.textView2);
            _delete = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            _delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }


}
