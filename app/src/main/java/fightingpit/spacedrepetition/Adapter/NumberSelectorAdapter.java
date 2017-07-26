package fightingpit.spacedrepetition.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fightingpit.spacedrepetition.Engine.ContextManager;
import fightingpit.spacedrepetition.R;

/**
 * Created by abhinavgarg on 26/07/17.
 */
public class NumberSelectorAdapter extends BaseAdapter {

    ArrayList<String> mNumberList;
    ArrayList<Boolean> mSelected;
    private Drawable mItemDrawable;

    public NumberSelectorAdapter(ArrayList<String> numberList, ArrayList<Boolean> selected) {
        mNumberList = numberList;
        mSelected = selected;
    }

    @Override
    public int getCount() {
        return mNumberList.size();
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
        final Integer aItemNumber = i;
        LayoutInflater aLayoutInflater = (LayoutInflater) ContextManager.getCurrentActivityContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(view == null){
            view = aLayoutInflater.inflate(R.layout.number_selector_single_number,null);
        }


        final TextView  aNumberTextView = view.findViewById(R.id.tv_number_nssn);
        aNumberTextView.setText(mNumberList.get(i));

        if(mSelected.get(i)){
            aNumberTextView.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
        else{
            aNumberTextView.setBackgroundColor(Color.parseColor("#FFCCBC"));
        }

        aNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelected.set(aItemNumber,!mSelected.get(aItemNumber));
                if(mSelected.get(aItemNumber)){
                    aNumberTextView.setBackgroundColor(Color.parseColor("#3F51B5"));
                }
                else{
                    aNumberTextView.setBackgroundColor(Color.parseColor("#FFCCBC"));
                }
            }
        });

        return view;
    }
}
