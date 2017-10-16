package com.sample.inferentdemo.imain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.inferentdemo.R;
import com.sample.inferentdemo.adapter.RecycleKeyboardAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class KeyBoardActivity extends BaseActivity {
    @InjectView(R.id.input_txt)
    TextView mTxtInput;

    @InjectView(R.id.recycle_keybord)
    RecyclerView mRecycleKeyboard;

    @InjectView(R.id.img_close)
    ImageView mImgClose;


    private Context mContext;
    private GridLayoutManager mLayoutManager;
    private RecycleKeyboardAdapter mAdapter;
    private String[] nums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_keyboard_view);
        ButterKnife.inject(this);
        mContext= this.getBaseContext();

        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                KeyBoardActivity.this.overridePendingTransition(R.anim.no_anim,R.anim.translate_up_to_down);
            }
        });
        mTxtInput.setText("");
        mLayoutManager = new GridLayoutManager(mContext,3);
        mRecycleKeyboard.setLayoutManager(mLayoutManager);
        mRecycleKeyboard.setHasFixedSize(true);

        nums = mContext.getResources().getStringArray(R.array.keyboard_num_array);
        mAdapter = new RecycleKeyboardAdapter(mContext,nums);
        mRecycleKeyboard.setAdapter(mAdapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.keyboard_spacing);
        mRecycleKeyboard.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        mAdapter.setOnItemClickListener(new RecycleKeyboardAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position < 9) {
                    mTxtInput.append(nums[position]);
                }else if(position == 9){
                    mTxtInput.setText("");
                }else if(position == 10){
                    mTxtInput.append(nums[position]);
                }else if(position == 11){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("NUMBER", mTxtInput.getText().toString());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                    KeyBoardActivity.this.overridePendingTransition(R.anim.no_anim,R.anim.translate_up_to_down);
                }
            }
        });
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) >8){
                outRect.bottom = 0;
            }
        }
    }
}
