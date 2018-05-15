package sss.spade.spadei.inspectorspade.Test.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.Test.GetterSetter.GetterSetter;

/**
 * Created by hp on 08-03-2018.
 */

public class FarmVerifyAdapter extends RecyclerView.Adapter<FarmVerifyAdapter.MyViewHolder> {

    private List<GetterSetter> listfarmers;
    private Context context;

    public FarmVerifyAdapter(List<GetterSetter> listfarmers, Context context)
    {
        this.context=context;
        this.listfarmers=listfarmers;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.verify_farm_view_for_recycler,null);
        MyViewHolder layoutViewHolder=new MyViewHolder(v);
        return layoutViewHolder;    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetterSetter getterSetter=listfarmers.get(position);

        if(getterSetter.getFarmerName()!=null){
            holder.farmersName.setText(getterSetter.getFarmerName());
        }

    }

    @Override
    public int getItemCount() {
        return listfarmers.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

       TextView farmersName;


        public MyViewHolder(View itemView) {
            super(itemView);
            farmersName=(TextView)itemView.findViewById(R.id.farmers_name_to_verify);

        }
    }

}
