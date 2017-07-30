package fightingpit.spacedrepetition.Adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fightingpit.spacedrepetition.Engine.ContextManager;
import fightingpit.spacedrepetition.Model.RepetitionPattern;
import fightingpit.spacedrepetition.R;


/**
 * Created by abhinavgarg on 31/07/17.
 */
public class PatternListAdapter extends BaseAdapter {

    List<RepetitionPattern> mPatternList;
    Context mContext;
    Fragment mFragment;
    OnPatternSelectedListener mOnPatternSelectedListener;

    public PatternListAdapter(Fragment fragment, List<RepetitionPattern> patternList) {
        mPatternList = patternList;
        mContext = ContextManager.getCurrentActivityContext();
        mFragment = fragment;
        try {
            mOnPatternSelectedListener = (OnPatternSelectedListener) mFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(mFragment.toString()
                    + " must implement OnPatternSelectedListener");
        }
    }

    @Override
    public int getCount() {
        return mPatternList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int aPos = i;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_single_text, viewGroup,
                    false);
        }

        TextView aPatternNameView = (TextView) view.findViewById(R.id.tv_pattern_name_pls);
        aPatternNameView.setText(mPatternList.get(i).getName());

        if (mPatternList.get(i).getId().equalsIgnoreCase(mContext.getString(R.string
                .AddPatternId))) {
            aPatternNameView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        aPatternNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnPatternSelectedListener.onPatternSelected(aPos, mPatternList.get(aPos));
            }
        });
        return view;
    }

    // This interface must be implemented by Fragment;
    public interface OnPatternSelectedListener {
        void onPatternSelected(int position, RepetitionPattern patternId);
    }
}
