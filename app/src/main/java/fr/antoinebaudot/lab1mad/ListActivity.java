package fr.antoinebaudot.lab1mad;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        int size = 40;
        dataSet = new String[size];
        for (int i = 0; i < size; i++) {
            dataSet[i] = "Data number " + i;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        int marginBottom = 40;
        int marginRight = 20;
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(marginBottom, marginRight,this));

        //The purpose of these lines is to know the number of column to display
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int nbColumn = (int) metrics.xdpi / 120;
        Integer nbC = nbColumn;
        String nbCs = nbC.toString();
        Log.i("Columns", nbCs);
        mLayoutManager = new GridLayoutManager(this, nbColumn);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new Adapter(getApplicationContext(), dataSet);
        mRecyclerView.setAdapter(mAdapter);
    }


    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int itemMarginTop;
        private int ItemMarginRight;
        private Context context;

        public ItemOffsetDecoration(int itemT, int itemR, Context c) {
            itemMarginTop = itemT;
            ItemMarginRight = itemR;
            context = c;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent != null && view != null) {

                int itemPosition = parent.getChildAdapterPosition(view);
                int totalCount = parent.getAdapter().getItemCount();

                if (itemPosition >= 0 && itemPosition < totalCount - 1) {
                    outRect.bottom = itemMarginTop;
                    outRect.right = ItemMarginRight;
                }
            }
        }
    }
}

