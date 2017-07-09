package fightingpit.spacedrepetition.Adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import fightingpit.spacedrepetition.Engine.CommonUtils;
import fightingpit.spacedrepetition.Model.Task;
import fightingpit.spacedrepetition.R;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by abhinavgarg on 09/07/17.
 */
public class SectionedTaskAdapter extends StatelessSection {

    String mTitle;
    List<Task> mList;

    Drawable mItemDrawable;
    TreeSet<String> aSelectedItems = new TreeSet<>();


    public SectionedTaskAdapter(String title, List<Task> list) {

        super(new SectionParameters.Builder(R.layout.sectioned_task_item)
                .headerResourceId(R.layout.sectioned_task_header)
                .build());
        mTitle = title;
        mList = list;
    }

    @Override
    public int getContentItemsTotal() {
        return mList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;


        final String aItemName = mList.get(position).getName();
        final String aItemId = mList.get(position).getId();
        final String aItemDate = CommonUtils.getDateFromMillis(mList.get(position).getTime());

        itemHolder.mItemName.setText(aItemName);
        itemHolder.mItemName.setSelected(false);
        itemHolder.mItemDate.setText(aItemDate);

        if (aSelectedItems.contains(aItemId)) {
            itemHolder.mItemLayout.setBackgroundColor(Color.parseColor("#FFCCBC"));
        } else {
            itemHolder.mItemLayout.setBackground(mItemDrawable);
        }

        itemHolder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSelectedItems.contains(aItemId)) {
                    aSelectedItems.remove(aItemId);
                    itemHolder.mItemLayout.setBackground(mItemDrawable);
                } else {
                    aSelectedItems.add(aItemId);
                    itemHolder.mItemLayout.setBackgroundColor(Color.parseColor("#FFCCBC"));
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.mHeaderName.setText(mTitle);


    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_std)
        TextView mHeaderName;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_sti)
        TextView mItemName;
        @BindView(R.id.tv_sti_date)
        TextView mItemDate;
        @BindView(R.id.ll_sti)
        LinearLayout mItemLayout;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mItemDrawable = mItemLayout.getBackground();
        }
    }
}
