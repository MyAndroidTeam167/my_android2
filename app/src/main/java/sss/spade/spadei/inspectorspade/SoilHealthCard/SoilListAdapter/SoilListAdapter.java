package sss.spade.spadei.inspectorspade.SoilHealthCard.SoilListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.Interface.RecyclerViewClickListener;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SoilHealthCard.GetterSetterForSoil.GetterSetterForSoil;
import sss.spade.spadei.inspectorspade.SoilHealthCard.InspectorSoilCardInputActivity;
import sss.spade.spadei.inspectorspade.TypeFacedTextView.TypefacedTextView;

/**
 * Created by hp on 29-03-2018.
 */

public class SoilListAdapter extends RecyclerView.Adapter<SoilListAdapter.MyViewHolder> {

    private List<GetterSetterForSoil> SoilTestList;
    private Context context;
    String from;
    private RecyclerViewClickListener clicklistener = null;




    @Override
    public void onBindViewHolder(SoilListAdapter.MyViewHolder holder, int position) {

        final GetterSetterForSoil soillist = SoilTestList.get(position);
        if(soillist.getSoil_test_farm_pet_name()!=null){
            holder.farmname.setText(soillist.getSoil_test_farm_pet_name());
        }
        if(soillist.getSoil_test_soil_type()!=null){
            holder.soil_type.setText("Soil : "+soillist.getSoil_test_soil_type());
        }
       /* if(soillist.getSoil_test_crop_assigned_status()!=null){
            if(soillist.getSoil_test_crop_assigned_status().equals("Y")){
                holder.soil_crop_status.setText("Crop Status : Assigned");
            }else{
                holder.soil_crop_status.setText("Crop Status : Not Assigned");
            }
        }*/
       /* if(soillist.getSoil_test_irri_type()!=null){
            holder.irrigation_type.setText("Irrigation Method : "+soillist.getSoil_test_irri_type());
        }*/
        if(soillist.getSoil_test_address()!=null){
            holder.address.setText("Address : "+soillist.getSoil_test_address());
        }
        if(soillist.getSoil_test_count()!=0){
            holder.back_image.setImageResource(R.drawable.soil2);
/*
            if(soillist.getSoil_test_count()%2==0){
                holder.back_image.setImageResource(R.drawable.soil1);
            }else{
                holder.back_image.setImageResource(R.drawable.soil2);
            }*/
        }


    }

    public void onBindViewHolder(SoilListAdapter.MyViewHolder holder, int position, List<Object> payloads)
    {
        super.onBindViewHolder(holder,position,payloads);
    }

    public SoilListAdapter(List<GetterSetterForSoil> soilTestList, Context context, String from) {
        this.SoilTestList = soilTestList;
        this.context=context;
        this.from=from;
        Log.d("TAG", "LandingAdapter: "+soilTestList);

    }

    @Override
    public SoilListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.soil_list_contents,  null);
        MyViewHolder layoutViewHolder=new MyViewHolder(itemView);
        return layoutViewHolder;
    }

    @Override
    public int getItemCount() {
        return SoilTestList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.farm_soil_pet_name)
        public TypefacedTextView farmname;
        @BindView(R.id.soil_type_soil_test)
        public TypefacedTextView soil_type;
        /*@BindView(R.id.soil_irrigration)
        public  TypefacedTextView irrigation_type;*/
       /* @BindView(R.id.soil_crop_status)
        public  TypefacedTextView soil_crop_status;*/
        @BindView(R.id.soil_address)
        public TypefacedTextView address;
        @BindView(R.id.soil_test_image)
        public ImageView back_image;
        @BindView(R.id.recy_for_soil_list)
        public RelativeLayout recy_for_soil_list;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

            recy_for_soil_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  =   getAdapterPosition();
                    final GetterSetterForSoil soillists = SoilTestList.get(position);
                    Intent intent =new Intent(context,InspectorSoilCardInputActivity.class);
                    intent.putExtra("from","soilnotdone");
                    DataHandler.newInstance().setFarmnum(soillists.getSoil_test_farm_num());
                    //intent.putExtra("soil_farm_num",soillists.getSoil_test_farm_num());
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).finish();
                }
            });
            //view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clicklistener != null) {
                clicklistener.recyclerViewListClicked(view, getAdapterPosition());

            }
        }
    }
    public void setClickListener(RecyclerViewClickListener clicklistener) {
        this.clicklistener = clicklistener;
    }


}
