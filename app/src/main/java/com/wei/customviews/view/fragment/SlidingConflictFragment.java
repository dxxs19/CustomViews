package com.wei.customviews.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wei.customviews.R;
import com.wei.customviews.view.widget.slidingConflic.HorizontalEx;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SlidingConflictFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SlidingConflictFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//@EFragment(R.layout.fragment_sliding_conflict)
public class SlidingConflictFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    HorizontalEx mHorizontalEx;

    public SlidingConflictFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SlidingConflictFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SlidingConflictFragment newInstance(String param1, String param2) {
        SlidingConflictFragment fragment = new SlidingConflictFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sliding_conflict, container, false);
        initView(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    private void initView(View view)
    {
        mHorizontalEx = (HorizontalEx) view.findViewById(R.id.horizontalEx);
        addViewsToViewGroup();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    void addViewsToViewGroup()
    {
        List<String> data1 = new ArrayList<>(), data2 = new ArrayList<>(), data3 = new ArrayList<>();
        for (int i = 0; i < 100; i ++)
        {
            if (i % 2 == 1)
            {
                data1.add(i + "");
            }
            else if (i % 2 == 0 && i >= 2)
            {
                data2.add(i + "");
            }
            else if (i % 3 == 0 && i >= 3)
            {
                data3.add(i + "");
            }
        }

        ListView listView1 = new ListView(getContext());
        ArrayAdapter adapter1 = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, data1);
        listView1.setAdapter(adapter1);

        ListView listView2 = new ListView(getContext());
        ArrayAdapter adapter2 = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, data2);
        listView2.setAdapter(adapter2);

        ListView listView3 = new ListView(getContext());
        ArrayAdapter adapter3 = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, data3);
        listView3.setAdapter(adapter3);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mHorizontalEx.addView(listView1, params);
        mHorizontalEx.addView(listView2, params);
        mHorizontalEx.addView(listView3, params);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
