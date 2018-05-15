package sss.spade.spadei.inspectorspade.LandingActivity.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.FarmLocation.GetFarmLocationActivity;
import sss.spade.spadei.inspectorspade.FarmerInformation.FarmerInfoActivity;
import sss.spade.spadei.inspectorspade.Interface.RecyclerViewClickListener;
import sss.spade.spadei.inspectorspade.LandingActivity.ShowFarmData.ShowFarmDataActivity;
import sss.spade.spadei.inspectorspade.LandingActivity.models.Farmers;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.SharedPref.SharedPreferencesMethod;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyFarmActivity;
import sss.spade.spadei.inspectorspade.VerifyFarm.VerifyaFarm.VerifySingleFarmActivity;


public class LandingAdapter extends RecyclerView.Adapter<LandingAdapter.MyViewHolder> {

    private List<Farmers> farmersList;
    private Context context;
    ProgressDialog progressDialog;
    String from;
    public static final String KEY_FARM_NUM = "farm_num";
    private static final String REGISTER_URL_CHECK_GPS = "http://spade.farm/app/index.php/inspectorApp/is_location_tracked";
    private RecyclerViewClickListener clicklistener = null;
    String output;
    final  String KEY_TOKEN="token1";
    String ct1="";

    @Override
    public void onBindViewHolder(LandingAdapter.MyViewHolder holder, int position) {

        final Farmers farmers = farmersList.get(position);



       /* if (farmers.getDate_of_harvesting() != null) {
            holder.harvestDate.setText(farmers.getDate_of_harvesting());
        }

        if (farmers.getDate_of_sowing() != null) {
            holder.sowingDate.setText(farmers.getDate_of_sowing());
        }

        if (farmers.getIrrigationtype() != null) {
            holder.irrigationType.setText(farmers.getIrrigationtype());

        }

        if (farmers.getSoiltype() != null) {
            holder.soilType.setText(farmers.getSoiltype());
        }


       */

       if(from.equals("from_verify_activity")){
          /* RelativeLayout.LayoutParams layoutParams =
                   (RelativeLayout.LayoutParams) holder.farmname.getLayoutParams();
           layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
           holder.farmname.setLayoutParams(layoutParams);
*/
          /* output = farmers.getFarmname().substring(0,1).toUpperCase();
           holder.rounded_text_forverify.setText(output);*/
           if (farmers.getFarmname() != null) {
               holder.farm_name_verify.setText(farmers.getFarmname());
           }

           if (farmers.getFarmername() != null) {
               holder.farmer_name_verify.setText(farmers.getFarmername());
           }
           if (farmers.getAddress_farm() != null) {
               holder.address.setText(farmers.getAddress_farm());
           }

           if(farmers.getProfile_img()!=null) {
               if (farmers.getProfile_img().equals("null")) {
                   holder.rounded_view_for_verify.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_light_grey_24dp));
               } else {
                   Uri uriprofile = Uri.parse(farmers.getProfile_img());
                   Picasso.with(holder.rounded_view_for_verify.getContext()).load(uriprofile).into(holder.rounded_view_for_verify);
                   holder.rounded_view_for_verify.setPadding(0, 0, 0, 0);

               }
           }

       }else{

           if(farmers.getLatest_farm_image()!=null){
               if(farmers.getLatest_farm_image().equals("null")){
                   holder.farm_latest_image.setImageDrawable(context.getResources().getDrawable(R.drawable.soil2));
               }else{
                   Uri uri = Uri.parse(farmers.getLatest_farm_image());
                   Picasso.with(holder.farm_latest_image.getContext()).load(uri).into(holder.farm_latest_image);
                   holder.farm_latest_image.setPadding(0,0,0,0);

               }
           }


//            Context context = holder.task_recycler_single_view_relative.getContext();
//            Picasso.with(context).load(uri).into(holder.taskLinearLayout);



          // Picasso.with(holder.farm_latest_image.getContext()).load(uri).into(holder.farm_latest_image);

           if (farmers.getFarmname() != null) {
               holder.farmname.setText(farmers.getFarmname());
           }
           if (farmers.getFarmername() != null) {
               holder.farmername.setText(farmers.getFarmername());
           }

           if(farmers.getProfile_img()!=null){
               if(farmers.getProfile_img().equals("null")){
                   holder.imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_light_grey_24dp));
               }else{
                   Uri uriprofile = Uri.parse(farmers.getProfile_img());
                   Picasso.with(holder.imageButton.getContext()).load(uriprofile).into(holder.imageButton);
                   holder.imageButton.setPadding(0,0,0,0);

               }
           }


           if (farmers.getMobno() != null) {
               // holder.mobNo.setText(farmers.getMobno());
/*
            holder.mobNo.setPaintFlags(holder.mobNo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
*/
            /*holder.mobNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(farmers.getMobno()+""));
                    context.startActivity(intent);

                }
            });*/
           }
           if(farmers.getCropName()!=null) {
               holder.cropName.setText(farmers.getCropName());
           }




       }



        /*if(farmers.getFarmimgbacklink()!=null){
            String image_link = farmers.getFarmimgbacklink();
            Uri uri = Uri.parse(image_link);
            Picasso.with(holder.farmback.getContext()).load(uri).into(holder.farmback);
        }*/
    }

    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads)
    {
        super.onBindViewHolder(holder,position,payloads);
    }

    public LandingAdapter(List<Farmers> farmerlist, Context context, String from) {
        this.farmersList = farmerlist;
        this.context=context;
        this.from=from;
        Log.d("TAG", "LandingAdapter: "+farmerlist);

    }

    @Override
    public LandingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder layoutViewHolder = null;
        View itemView;

        if(from.equals("from_verify_activity")){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_for_verify_farm,  null);
            layoutViewHolder=new MyViewHolder(itemView);
        }
        else{
            itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_for_landing,  null);
            layoutViewHolder=new MyViewHolder(itemView);
        }
        return layoutViewHolder;
    }

    @Override
    public int getItemCount() {
        return farmersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView farmname,farmername,farmer_name_verify,farm_name_verify,irrigationType,mobNo,cropName,sowingDate,harvestDate,address;
        ImageView farm_latest_image;
        CircleImageView imageButton,rounded_view_for_verify;
        LinearLayout lin_parenta_lay,linear_call_layout;
        RelativeLayout rel_lay_farm_info;


        public MyViewHolder(View view) {
            super(view);
            farmname = (TextView) view.findViewById(R.id.tvfarmname);
            farm_name_verify=(TextView)view.findViewById(R.id.tv_farm_name_verify);
            farmername = (TextView) view.findViewById(R.id.tvfarmername);
            rounded_view_for_verify=(CircleImageView) view.findViewById(R.id.rounded_test_for_verify);
            address=(TextView)view.findViewById(R.id.add_for_verify_farm);
            farmer_name_verify=(TextView)view.findViewById(R.id.tvfarmername_verify);
            ct1=SharedPreferencesMethod.getString(context,"cctt");
//            address.setVisibility(View.GONE);
            //rounded_text_forverify.setVisibility(View.GONE);

            //layout.addView(farmname);

           /* soilType = (TextView) view.findViewById(R.id.soil_type);
            irrigationType = (TextView) view.findViewById(R.id.irrigation_type);
           */ //mobNo = (TextView) view.findViewById(R.id.farmer_mob_no);
            cropName = (TextView) view.findViewById(R.id.farm_crop_name);
            imageButton=(CircleImageView) view.findViewById(R.id.user_profile_photo);

            farm_latest_image=(ImageView)view.findViewById(R.id.latest_farm_image);
            lin_parenta_lay=(LinearLayout)view.findViewById(R.id.lin_parent_lay_for_landing);
            rel_lay_farm_info=(RelativeLayout)view.findViewById(R.id.rel_lay_recy);
            linear_call_layout=(LinearLayout)view.findViewById(R.id.linear_call_layout);





            if(from.equals("from_verify_activity")){
                address.setVisibility(View.VISIBLE);




                linear_call_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position  =   getAdapterPosition();
                        final Farmers farmers = farmersList.get(position);
                        String strnumber = farmers.getMobno();
                        Uri number = Uri.parse("tel:" + strnumber);
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(number);
                        view.getContext().startActivity(intent);

                    }
                });

                //mobNo.setVisibility(View.GONE);
/*
                farmername.setVisibility(View.VISIBLE);
                cropName.setVisibility(View.GONE);
                rounded_text_forverify.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.GONE);
                farm_latest_image.setVisibility(View.GONE);
                address.setVisibility(View.VISIBLE);
*/

                /*RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) holder.txtGuestName.getLayoutParams();
                farmname.*/
                //farmname.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);


                lin_parenta_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        int position  =   getAdapterPosition();
                        final Farmers farmers = farmersList.get(position);
                        try {
                            progressDialog = ProgressDialog.show(context,
                                    context.getString(R.string.dialog_please_wait), "");
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_CHECK_GPS,

                                    new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("true")){
                                                Intent intent = new Intent(context, VerifySingleFarmActivity.class);
                                                DataHandler.newInstance().setFarmnum(farmers.getFarmnum());
                                                intent.putExtra("from","verify");
                                                //intent.putExtra("farm_num",farmers.getFarmnum());
                                                progressDialog.dismiss();
                                                view.getContext().startActivity(intent);
                                                ((Activity)view.getContext()).finish();
                                            }else{
                                                Intent intent = new Intent(context, GetFarmLocationActivity.class);
                                                DataHandler.newInstance().setFarmnum(farmers.getFarmnum());
                                                //intent.putExtra("farm_num",farmers.getFarmnum());
                                                progressDialog.dismiss();
                                                view.getContext().startActivity(intent);
                                                ((Activity)view.getContext()).finish();// Toast.makeText(context, "Coordinat Not Submitted"+response, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put(KEY_FARM_NUM,farmers.getFarmnum());
                                    params.put(KEY_TOKEN,ct1);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        }catch (Exception e){
                            progressDialog.dismiss();
                        }



                        //DataHandler.newInstance().setFarmnum(farmers.getFarmnum());

                    }
                });
            }else{
                lin_parenta_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position  =   getAdapterPosition();
                        final Farmers farmers = farmersList.get(position);
                        Intent intent = new Intent(context, ShowFarmDataActivity.class);
                        intent.putExtra("farm_num",farmers.getFarmnum());
                        SharedPreferencesMethod.setString(context,"GPSC1",farmers.getGpsc1());
                        SharedPreferencesMethod.setString(context,"GPSC2",farmers.getGpsc2());
                        SharedPreferencesMethod.setString(context,"GPSC3",farmers.getGpsc3());
                        SharedPreferencesMethod.setString(context,"GPSC4",farmers.getGpsc4());
                        SharedPreferencesMethod.setString(context,"GPSC5",farmers.getGpsc5());
                        SharedPreferencesMethod.setString(context,"GPSC6",farmers.getGpsc6());
                        //intent.putExtra("farm_pet_name",farmers.getFarmname());
                        DataHandler.newInstance().setLanding_farm_pet_name(farmers.getFarmname());
                        DataHandler.newInstance().setFarmnum(farmers.getFarmnum());
                        view.getContext().startActivity(intent);
                        ((Activity)view.getContext()).finish();
                    }
                });


                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position  =   getAdapterPosition();
                        final Farmers farmers = farmersList.get(position);
                        //Toast.makeText(context, "--->>>>>>"+farmers.getFarmername()+position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, FarmerInfoActivity.class);
                        intent.putExtra("farm_num",farmers.getFarmnum());
                        DataHandler.newInstance().setUser_num(farmers.getUser_num());
                        DataHandler.newInstance().setPerson_num(farmers.getPerson_num());
                        //intent.putExtra("task_date",taskdata.getTaskDate());
                        Log.e("My Tag :",""+position);
                        view.getContext().startActivity(intent);
                        ((Activity)view.getContext()).finish();
                    }
                });

            }
            /*sowingDate = (TextView) view.findViewById(R.id.sowing_date);
            harvestDate = (TextView) view.findViewById(R.id.harvest_date);
*/

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
