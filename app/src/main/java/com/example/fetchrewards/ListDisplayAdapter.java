package com.example.fetchrewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fetchrewards.Item;
import com.example.fetchrewards.R;

import java.util.List;

public class ListDisplayAdapter extends RecyclerView.Adapter<ListDisplayAdapter.ViewHolder> {
    private List<Item> items;

    public ListDisplayAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView field1TextView;
        private TextView field2TextView;
        private TextView field3TextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            field1TextView = itemView.findViewById(R.id.field1);
            field2TextView = itemView.findViewById(R.id.field2);
            field3TextView = itemView.findViewById(R.id.field3);
        }

        public void bind(Item item) {
            field1TextView.setText(String.format("Id: %s", item.getId()));
            field2TextView.setText(String.format("ListId: %s", String.valueOf(item.getListId())));
            field3TextView.setText(String.format("Name: %s", item.getName()));
        }
    }
}
