package com.wposs.appfinanciera.View.HomeActivity.Implementations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.ITransationAdapterListener;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private ArrayList<Transaction> transactions;
    private final Context context;
    private final ITransationAdapterListener listener;


    // Método para actualizar la lista de canciones
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Transaction> newTransactions) {
        this.transactions = newTransactions;
        notifyDataSetChanged();
    }

    public TransactionsAdapter(ArrayList<Transaction> transactions, ITransationAdapterListener listener, Context context) {
        this.transactions = transactions;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, null, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        );
        view.setLayoutParams(params);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setDetailTransation(transactions.get(position), context);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView descriptionTextView;
        private final TextView amountTextView;
        private final TextView dateTextView;
        private final LinearLayout containerItemDetail;
        private final ITransationAdapterListener mListener;

        public ViewHolder(View itemView, ITransationAdapterListener listener) {
            super(itemView);
            mListener = listener;
            descriptionTextView = itemView.findViewById(R.id.descriptionItemTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            containerItemDetail = itemView.findViewById(R.id.containerItemDetail);
        }
        @SuppressLint({"DefaultLocale", "ResourceAsColor"})
        public void setDetailTransation(Transaction transaction, Context context){
            descriptionTextView.setText(transaction.getDescription());
            amountTextView.setText(String.format("$%,.2f", transaction.getAmount()));
            dateTextView.setText(transaction.getDate());
            if (transaction.getType() == 2){
                amountTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            }
            containerItemDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) mListener.onPressTransaction(transaction);
                }
            });

        }
    }
}
