package com.iappstreat.ixigohackathon;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iappstreat.ixigohackathon.Utils.CShowProgress;
import com.iappstreat.ixigohackathon.Utils.Constants;
import com.iappstreat.ixigohackathon.Utils.URLConnector;
import com.iappstreat.ixigohackathon.adapters.HotelsAdapter;
import com.iappstreat.ixigohackathon.models.SearchedResult;
import com.iappstreat.ixigohackathon.models.VisitsDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by verma on 08-04-2017.
 */

public class HomeFragment   extends Fragment{
    AutoCompleteTextView searchField;
    RecyclerView recyclerView;
    public static  String LOG_TAG="IXIGOHackathon";
    private HotelsAdapter adapter;
    private List<VisitsDetails> hotelList;
    public static   List<SearchedResult> searchList;
    CShowProgress progressbar;
    String parameter,pageName="";
    LinearLayoutManager mLayoutManager;
    boolean isLoading=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.content_main, parent, false);

    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressbar = CShowProgress.getInstance();
        AppController.pagination=0;
        searchField=(AutoCompleteTextView)view.findViewById(R.id.searchField);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerview);
        hotelList = new ArrayList<>();
        searchList=new ArrayList<>();
        adapter = new HotelsAdapter(this.getActivity(), hotelList);
        mLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        /**/
    }
    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading ) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= AppController.NUMBER_OF_PHOTOS) {

                    if (URLConnector.isOnline(getActivity())) {
                        AppController.pagination+=1;
                        loadNextPage(AppController.pagination);
                    }


                }
            }
        }
    };
    public void loadNextPage(int pagesToSkip){
        isLoading=true;
        String tag_json_obj = "json_obj_req";
        String url = Constants.getInspirations;
        progressbar.showProgress(getActivity(),"Please wait!");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        isLoading=false;
                        progressbar.dismissDialog();
                        try {
                            JSONObject data=response.getJSONObject("data");
                            JSONArray resultArray=data.getJSONArray("flight");
                            for (int i=0;i<resultArray.length();i++ ){
                                JSONObject obj=resultArray.getJSONObject(i);
                                VisitsDetails res=new VisitsDetails();
                                res.setAddress(obj.getString("address"));
                                res.setCityId(obj.getString("cityId"));
                                res.setCityName(obj.getString("cityName"));
                                res.setShortDescription(obj.getString("shortDescription"));
                                res.setDescription(obj.getString("description"));
                                res.setMinimumPrice(obj.getString("minimumPrice"));
                                res.setKeyImageUrl(obj.getString("keyImageUrl"));
                                res.setId(obj.getString("id"));
                                hotelList.add(res);

                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("IXIGO", "Error: " + error.getMessage());
                progressbar.dismissDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
