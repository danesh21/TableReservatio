package com.example.tablereservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<Reservation> historyItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Reservation historyItem);
    }

    public HistoryAdapter(List<Reservation> historyItems, OnItemClickListener listener) {
        this.historyItems = historyItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reservation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation historyItem = historyItems.get(position);
        holder.bind(historyItem);
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView customerNameTextView;
        private TextView seatingAreaTextView;
        private TextView dateTextView;
        private TextView mealTimeTextView;
        private TextView tableSizeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            seatingAreaTextView = itemView.findViewById(R.id.seatingAreaTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            mealTimeTextView = itemView.findViewById(R.id.mealTimeTextView);
            tableSizeTextView = itemView.findViewById(R.id.tableSizeTextView);

            // Set click listener for the item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(historyItems.get(position));
                    }
                }
            });
        }

        public void bind(Reservation historyItem) {
            customerNameTextView.setText("Name: " + historyItem.getCustomerName());
            seatingAreaTextView.setText("Seating Area: " + historyItem.getSeatingArea());
            dateTextView.setText("Date: " + historyItem.getDate());
            mealTimeTextView.setText("Meal Time: " + historyItem.getMeal());
            tableSizeTextView.setText("Table Size: " + historyItem.getTableSize());
        }
    }
}
