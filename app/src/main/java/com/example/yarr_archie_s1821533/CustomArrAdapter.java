//Archie Yarr S1821533



package com.example.yarr_archie_s1821533;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomArrAdapter extends ArrayAdapter<TsItem> implements Filterable {
    public ArrayList<TsItem> origItems;
    public ArrayList<TsItem> Items;
    private Filter filter;
    private Context conx;
    private Boolean inc = false;


    private static LayoutInflater inflater = null;

    public CustomArrAdapter(Context context, ArrayList<TsItem> _items) {
        super(context, 0, _items);
        conx = context;
        this.origItems = _items;
        this.Items = _items;

    }


    @Override
    public TsItem getItem(int position) {
        return Items.get(position);
    }

    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public long getItemId(int position) {
        return Items.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TsItem it = getItem(position);

            ItemHolder holder = new ItemHolder();

            if (convertView == null) {
                Log.d("MyTag", "No Filter");
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
                TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
                TextView tvPeriod = (TextView) convertView.findViewById(R.id.tvPeriod);

                LinearLayout LlClick= (LinearLayout) convertView.findViewById(R.id.LlClick);

                holder.clickable = LlClick  ;
                holder.itemTitleView = tvName;
                holder.itemPeriodView = tvPeriod;


                tvName.setText(it.getTitle());
                tvPeriod.setText(it.getPeriod());


                inc = it.getInc();
                if (inc == false){
                TextView tvDuration= (TextView) convertView.findViewById(R.id.tvDuration);
                tvDuration.setText(Long.toString(it.getDuration())+" hrs");
                    tvDuration.setBackgroundColor(Color.parseColor(it.getColour()));
                holder.itemDurationView = tvDuration;
                }
                convertView.setTag(holder);
            }else{
                holder = (ItemHolder) convertView.getTag();
            }

            final TsItem t = Items.get(position);
                    Log.e("MyTag","___TRAFFIC ITEM IS_____"+t);

           TextView tv =  holder.itemTitleView;
                   tv.setText(t.getTitle());
            if (inc == false) {
                holder.itemPeriodView.setText(t.getPeriod());
                holder.itemDurationView.setText(Long.toString(t.getDuration()) + " hrs");
                holder.itemDurationView.setBackgroundColor(Color.parseColor(it.getColour()));
            }else {
                holder.itemPeriodView.setText("");
            }
            holder.clickable.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Log.e("MyTag","CLICK CLICK CLICK! ");
                    Intent i = new Intent(conx, ItemDescActivity.class);

                    i.putExtra("title", t.getTitle());
                    i.putExtra("desc", t.getDesc());
                    i.putExtra("desc0", t.getDesc0());
                    i.putExtra("inc", t.getInc());
                    i.putExtra("pub", t.getPubDate());
                    i.putExtra("geo", t.getGeoRss());

                    if(inc == false) {
                        i.putExtra("dur", Long.toString(t.getDuration()) + " hrs");
                        i.putExtra("period", t.getPeriod2());
                        i.putExtra("col", t.getColour());
                    }
                    conx.startActivity(i);
                }
            });
            return convertView;
    }


    public void resetData() {
        Items = origItems;
    }

    private static class ItemHolder {
        public TextView itemTitleView;
        public TextView itemPeriodView;
        public TextView itemDurationView;
        public LinearLayout clickable;
    }

    @Override
    public Filter getFilter() {

        Log.e("MyTag"," ** FILTER ** ");
        if (filter == null)
            filter = new myFilter();
            Log.d("MyTag"," ** ");
        Log.d("MyTag"," * ");

        return filter;

    }

private class myFilter extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        Log.e("MyTag"," *PERFOMING FILTERING* ");
        FilterResults results = new FilterResults();


        // filtering logic begins
        if (constraint == null || constraint.length() == 0) {
            Log.e("MyTag"," *NO CONSTRAINTS* ");
            // No filter implemented, return unfiltered list

            ArrayList<TsItem> ar = new ArrayList<TsItem>(origItems);

            results.values = ar;
            results.count = ar.size();

//            Log.e("MyTag"," 1.results.count "+results.count);
//            Log.e("MyTag"," ");
        }
        else {
            Log.e("MyTag"," CONSTRAINTS = "+constraint);
            // perform filter as filter is found
            ArrayList<TsItem> nItemList = new ArrayList<TsItem>();
//            Log.e("MyTag"," 3. results.count "+results.count);
//            Log.e("MyTag"," 3. nitem  " +nItemList.size());
            for (TsItem p : origItems) {
                if ((p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) | (p.getPeriod().toUpperCase().contains(constraint.toString().toUpperCase()))) {
                        Log.e("MyTag","match: "+p.toString());
                        nItemList.add(p);
                }
            }
        Log.e("MyTag"," 2. nitem  " +nItemList.size());
            results.values = nItemList;
            results.count = nItemList.size();
        }
//        Log.e("MyTag"," 2. results.count "+results.count);
//            Log.e("MyTag"," ");



        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint,
                                  FilterResults results) {
        if (results.count == 0)
            notifyDataSetInvalidated();
        else {
            Items = (ArrayList<TsItem>) results.values;
            notifyDataSetChanged();
        }
    }
}

}




