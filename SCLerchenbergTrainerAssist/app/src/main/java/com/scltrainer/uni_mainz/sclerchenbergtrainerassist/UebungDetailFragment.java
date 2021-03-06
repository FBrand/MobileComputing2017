package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Dies ist ein Platzhalter für die Detailansicht einer Übung!
 * TODO: Diesen Hinweis löschen wenn die Klasse benutzt wird!
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UebungDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UebungDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UebungDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //TAG
    //private static final String TAG = UebungDetail.class.getSimpleName();

    // wird für die Listenansicht benötigt
    private Cursor dbCursor;
    // bildet den Cursor auf die ListView ab
    private UebungDetailAdapter listAdapter;
    // Schnittstelle zur Datenbank
    private DBConnection dbConnection;

    private int entryID;

    public UebungDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UebungDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UebungDetailFragment newInstance(int entryID) {
        UebungDetailFragment fragment = new UebungDetailFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
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

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras == null) {
            return;
        }
        entryID = extras.getInt("_id");
        Log.i("UebungDetailFragment", "ID: " + entryID);

        //Log.i(TAG, "onCreate");
        dbConnection= DBHelper.getConnection(this.getContext());
        dbCursor = selectCursorUebungDetail(entryID);
        //Log.i(TAG, "Cursor wurde initiiert");
        getActivity().startManagingCursor(dbCursor);
        //Log.i(TAG, "startManagingCursor");
        listAdapter = new UebungDetailAdapter(this.getActivity(), dbCursor);
        listAdapter.bindView(this.getView(), this.getContext(), dbCursor);
        //Log.i(TAG, "listAdapter inintiiert");
        //getActivity().setListAdapter(listAdapter);
        Log.i("UebungDetailFragment", "setListAdapter");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uebung_detail, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public Cursor selectCursorUebungDetail(int entryID){

        String s = DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ? ";
        String[] sArgs = {"" + entryID};
        Log.i("UebungDetailFragment", "entryID in cursor: " + entryID);
        String[] sArr = {"_id", DBInfo.EXERCISE_COLUMN_NAME_NAME, DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME,
                DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION, DBInfo.EXERCISE_COLUMN_NAME_TECHNIC,
                DBInfo.EXERCISE_COLUMN_NAME_TACTIC, DBInfo.EXERCISE_COLUMN_NAME_PHYSIS,
                DBInfo.EXERCISE_COLUMN_NAME_DURATION};
        return dbConnection.select(DBInfo.EXERCISE_TABLE_NAME, sArr, s, sArgs);
    }
}
