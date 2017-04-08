package com.iappstreat.ixigohackathon;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by verma on 08-04-2017.
 */

public class HotelsFragment  extends Fragment implements AdapterView.OnItemClickListener{
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

    public HotelsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        parameter=bundle.getString("parameter");
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
        searchField.setAdapter(new GooglePlacesAutocompleteAdapter(this.getActivity(), R.layout.list_item));
        searchField.setThreshold(1);
        searchField.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        final String str = (String) adapterView.getItemAtPosition(position);
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        pageName=searchList.get(position).get_id();
        String url = Constants.getPointsInterest1+pageName+Constants.getPointsInterest2+parameter+"&skip=1"+"&limit=20";
        progressbar.showProgress(getActivity(),"Please wait!");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressbar.dismissDialog();
                        try {
                            JSONObject data=response.getJSONObject("data");
                            JSONArray resultArray=data.getJSONArray(parameter);
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
        String url = Constants.getPointsInterest1+pageName+Constants.getPointsInterest2+parameter+"&skip="+pagesToSkip+"&limit=20";
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
                            JSONArray resultArray=data.getJSONArray(parameter);
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
    public static ArrayList autocomplete(String input) {
        URL url=null;
        String result="";
        ArrayList resultList = null;

        try {
            url = new URL(Constants.autoCompleteCitiesAPI+input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        result=Constants.getRequrstHit(url);
        try {
            // Create a JSON object hierarchy from the results
            JSONArray jsonAry = new JSONArray(result);

            // Extract the Place descriptions from the results
            resultList = new ArrayList(jsonAry.length());
            searchList= new ArrayList(jsonAry.length());
            for (int i = 0; i < jsonAry.length(); i++) {

                resultList.add(jsonAry.getJSONObject(i).getString("text"));
                SearchedResult res=new SearchedResult();
                res.set_id(jsonAry.getJSONObject(i).getString("_id"));
                res.setAddress(jsonAry.getJSONObject(i).getString("address"));
                //res.set(jsonAry.getJSONObject(i).getString("address"));
                searchList.add(res);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            if(resultList!=null)
                return resultList.size();
            else
                return 0;
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}
