package com.rawcoders.hackaday.Blog;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.rawcoders.hackaday.Blog.BlogEntry.BlogEntry;
import com.rawcoders.hackaday.Global;
import com.rawcoders.hackaday.MainActivity;
import com.rawcoders.hackaday.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class BlogListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1 = "OLD";
    private String mParam2 = "OLD";

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    public static ProgressBar mProgressBar;


    // TODO: Rename and change types of parameters
    public static BlogListFragment newInstance(BlogEntry be) {
        BlogListFragment fragment = new BlogListFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BlogListFragment()   {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        // TODO : Load data at run time , write async loaders for loading data.
        //mAdapter = new ArrayAdapter<BlogEntry.BlogItem>(getActivity(),
        //        R.layout.blog_list_item, android.R.id.text1, BlogEntry.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bloglist, container, false);
        // Get the Drawable custom_progressbar
        //Drawable draw=Res.getDrawable(R.drawable.custom_progress_bar);

        // set the drawable as progress drawable
        //mProgressBar.setProgressDrawable(draw);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_spin);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#80DAEB"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        if(Global.mAdapter.ITEMS.size() >= 7) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
        // Set the adapter


        EndlessScrollListener elistener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Global.mAdapter.loadNext(page);
            }
        };
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(Global.mAdapter);
        mListView.setOnScrollListener(elistener);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        //view.setFocusableInTouchMode(true);
        //view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("ONKEY", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    Log.i("ONKEY CHECK", "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                } else {
                    return false;
                }
            }
        });

        return view;
    }

    public void refreshList()   {
        //TODO : Notify data set changed.k
        //mAdapter.notify();
        //Global.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            refreshList();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            Log.i("TEST", "TESTIN CLICK");
            mListener.onFragmentInteraction(Global.mAdapter.ITEMS.get(position).url);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
