package sss.spade.spadei.inspectorspade.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import sss.spade.spadei.inspectorspade.FarmActivities.Adapter.TaskRecyclerViewAdapter;
import sss.spade.spadei.inspectorspade.FarmActivities.FarmActionReplyActivity;
import sss.spade.spadei.inspectorspade.FarmActivities.GetterSetter.Taskdata;
import sss.spade.spadei.inspectorspade.LandingActivity.Adapter.RecyclerTouchListener;
import sss.spade.spadei.inspectorspade.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    String dateall,idall,activityall,activitydescriptionall,activity_imgall,is_doneall;

    public static final String ARG_DATE = "ARG_DATE";
    private static final String ARG_ID = "ARG_ID";
    private static final String ARG_ACTIVITY = "ARG_ACTIVITY";
    private static final String ARG_ACTIVITY_DESCRIPTION = "ARG_ACTIVITY_DESCRIPTION";
    private static final String ARG_ACTIVITY_IMG = "ARG_ACTIVITY_IMG";
    private static final String ARG_ISDONE = "ARG_ISDONE";
    private static final String ARG_FARM_DWORK_NUM = "ARG_FARM_DWORK_NUM";
    public static final String ARG_PAGE = "ARG_PAGE";


    private RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    TaskRecyclerViewAdapter mAdapter;
    Context context;


    // TODO: Rename and change types of parameters
    private String[] mdate;
    private String[] mid;
    private String[] mactivity;
    private String[] mactivitydescription;
    private String[] mactivityimg;
    private String[] misdone;
    private String[] farm_dwork_num;
    private int mPage;



    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment Wheather_thirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String[] mdate, String[] mid, String[] mactivity,
                                             String[] mactivitydescription, String[] mactivityimg, String[] mis_done,String[] farm_dwork_num,int page) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_DATE,mdate);
        args.putStringArray(ARG_ID,mid);
        args.putStringArray(ARG_ACTIVITY,mactivity);
        args.putStringArray(ARG_ACTIVITY_DESCRIPTION,mactivitydescription);
        args.putStringArray(ARG_ACTIVITY_IMG,mactivityimg);
        args.putStringArray(ARG_ISDONE,mis_done);
        args.putStringArray(ARG_FARM_DWORK_NUM,farm_dwork_num);
        args.putInt(ARG_PAGE, page);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mdate=getArguments().getStringArray(ARG_DATE);
            mid=getArguments().getStringArray(ARG_ID);
            mactivity=getArguments().getStringArray(ARG_ACTIVITY);
            mactivitydescription=getArguments().getStringArray(ARG_ACTIVITY_DESCRIPTION);
            mactivityimg=getArguments().getStringArray(ARG_ACTIVITY_IMG);
            misdone=getArguments().getStringArray(ARG_ISDONE);
            farm_dwork_num=getArguments().getStringArray(ARG_FARM_DWORK_NUM);
            mPage = getArguments().getInt(ARG_PAGE);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_second, container, false);
        final List<Taskdata> taskdatums=new ArrayList<>();
        Taskdata taskdatum=new Taskdata();
        context=getActivity();

        if(mdate!=null) {
            for (int i = 0; i < mdate.length; i++) {
                taskdatum = new Taskdata();
                taskdatum.setTaskDate(mdate[i]);
                taskdatum.setTaskTitle(mactivity[i]);
                taskdatum.setTaskDescription(mactivitydescription[i]);
                taskdatum.setImgBgLink(mactivityimg[i]);
                taskdatum.setIsDone(misdone[i]);
                taskdatum.setTaskId(mid[i]);
                taskdatum.setFarm_dwork_num(farm_dwork_num[i]);
                taskdatums.add(taskdatum);
            }
        }
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_pending);
        //mprogressDialog.dismiss();
        mAdapter = new TaskRecyclerViewAdapter(taskdatums,context,"n");
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(getApplicationContext()," "+taskdatums.get(position), Toast.LENGTH_SHORT).show();
                Taskdata taskdata=taskdatums.get(position);
//                                        Toast.makeText(getApplicationContext(),"Description ->"+taskdata.getTaskDescription()+", Id ->"+taskdata.getTaskId(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, FarmActionReplyActivity.class);
                intent.putExtra("id",taskdata.getFarm_dwork_num());
                intent.putExtra("task_date",taskdata.getTaskDate());
                intent.putExtra("fromactivity","pager");
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
