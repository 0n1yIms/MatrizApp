package com.ims.matrixcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.transition.MaterialSharedAxis;

import java.lang.reflect.Array;
import java.security.AlgorithmParameterGenerator;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    abstract class ItemInterface
    {
        public Drawable image;
        public ItemInterface(Drawable drawable) {
            image = drawable;
        }
        public abstract void onTouch();
    }
    
    private HandlerThread workThread = new HandlerThread("workThread");
    private Handler workHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        workThread.start();
        workHandler = new Handler(workThread.getLooper());

        workHandler.post(() ->
        {
            ArrayList<ItemInterface> items = new ArrayList<>(8);

            items.add(new ItemInterface(getDrawable(R.drawable.ic_sel)) {
                @Override
                public void onTouch() {
                    Intent startGauss = new Intent(MainActivity.this, GaussActivity.class);
                    startActivity(startGauss);
                }
            });
            items.add(new ItemInterface(getDrawable(R.drawable.ic_op_mat)) {
                @Override
                public void onTouch() {
                }
            });
            items.add(new ItemInterface(getDrawable(R.drawable.ic_mcm_mcd)) {
                @Override
                public void onTouch() {
                }
            });

            for (int i = 0; i < 7; i++) {
                items.add(new ItemInterface(getDrawable(R.drawable.item2)) {
                    @Override
                    public void onTouch() {
                    }
                });
            }

            RvAdapter rvAdapter = new RvAdapter(items);

            recyclerView = findViewById(R.id.recyclerview);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            runOnUiThread(()->
            {
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(rvAdapter);
            });
        });
    }

    @Override
    public void onBackPressed() {
    }

    class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvHolder>
    {
        ArrayList<ItemInterface> data;
        public RvAdapter(ArrayList<ItemInterface> data) {
            this.data = data;
        }
        @NonNull
        @Override
        public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout btn = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_btn, null, false);
            return new RvHolder(btn, parent.getWidth());
        }

        @Override
        public void onBindViewHolder(@NonNull RvHolder holder, int position) {
            holder.setBackground(data.get(position));
            if(position == 0 || position == 1)
            {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.layout.getLayoutParams();
                lp.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                        getResources().getDisplayMetrics());
                holder.layout.setLayoutParams(lp);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class RvHolder extends RecyclerView.ViewHolder
        {
            private LinearLayout layout;
            private ItemInterface itemInterface;
            public RvHolder(@NonNull View itemView, int width) {
                super(itemView);
                layout = (LinearLayout) itemView;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 2,(int)((float)width / 2f * 1.101052631f),0);
                layout.setLayoutParams(lp);

            }
            public void setBackground(ItemInterface item) {
                this.itemInterface = item;
                ((ImageView)layout.findViewById(R.id.btn)).setOnClickListener(view -> {
                    itemInterface.onTouch();
                });
                ((ImageView) layout.findViewById(R.id.btn)).setImageDrawable(itemInterface.image);
            }
        }
    }
}