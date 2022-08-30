package com.ims.matrixcalc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHoder> {

    ArrayList<Integer> datos;

    public RecyclerAdapter(ArrayList<Integer> datos)
    {

        this.datos = datos;
    }

    @NonNull
    @Override
    public RecyclerHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = new EditText(parent.getContext());
//        View item = new TextView(parent.getContext());
        return new RecyclerHoder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHoder holder, int position) {
        holder.set(" " + datos.get(position));
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    class RecyclerHoder extends RecyclerView.ViewHolder
    {
//        TextView tv;
        EditText et;
        public RecyclerHoder(@NonNull View itemView) {
            super(itemView);
//            tv = (TextView) itemView;
//            tv.setWidth(300);
            et = (EditText) itemView;
            et.setWidth(300);
        }
        void set(String text)
        {
//            tv.setText(text);
            et.setText(text);
        }
    }

}
