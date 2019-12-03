package com.example.androidimagefilter;

//import android.content.Context;
import java.lang.Math;
import android.content.Context;
import android.graphics.Bitmap;
//import android.graphics.Point;
import com.zomato.photofilters.geometry.Point;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidimagefilter.Adapter.ThumbnailAdapter;
import com.example.androidimagefilter.Interface.FiltersListFragmentListner;
import com.example.androidimagefilter.Utils.BitmapUtils;
import com.example.androidimagefilter.Utils.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.androidimagefilter.R.string.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersListFragment extends Fragment implements FiltersListFragmentListner{

    RecyclerView recyclerView;
    ThumbnailAdapter adapter;
    List<ThumbnailItem> thumbnailItems;

    FiltersListFragmentListner listner;

    static FiltersListFragment instance;
    static Bitmap bitmap;

    public static FiltersListFragment getInstance(Bitmap bitmapSave) {
        if(instance==null){
            instance = new FiltersListFragment();
            bitmap = bitmapSave;
        }

        return instance;
    }

    public void setListner(FiltersListFragmentListner listner) {
        this.listner = listner;
    }

    public FiltersListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_filters_list, container, false);

        thumbnailItems = new ArrayList<>();
        adapter = new ThumbnailAdapter(thumbnailItems, this, getActivity());

        recyclerView = (RecyclerView)itemView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(adapter);

        displayThumbnail(bitmap);

        return itemView;
    }

    public void displayThumbnail(final Bitmap bitmap) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Bitmap thumbImg;
                if(bitmap == null)
                    thumbImg = BitmapUtils.getBitmapFromAssets(getActivity(), MainActivity.pictureName, 100, 100);
                else
                    thumbImg = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

                if(thumbImg == null)
                    return;

                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                //normal bitmap
                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbImg;
                thumbnailItem.filterName = "Normal";
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(getActivity());

                for (Filter filter:filters){
                    ThumbnailItem tI = new ThumbnailItem();
                    tI.image = thumbImg;
                    tI.filter = filter;
                    tI.filterName = filter.getName();
                    ThumbnailsManager.addThumb(tI);
                }

                List<Filter> customfilters = getCustomFilters(getActivity());
                List<String> FilterName = Arrays.asList("Red", "Green", "Blue", "RedGreen", "GreenBlue", "RedBlue", "Grayscale",
                        "Negative", "NegativeGray", "NegativeRed", "NegativeGreen", "NegativeBlue",
                        "Log","Gamma0.5", "Gamma0.8", "Gamma2", "Gamma4", "Gamma8",
                        "Sine", "Cos", "Tan", "Contrast", "NegContrast", "Slice1Low", "Slice1Mid", "Slice2Low","Slice2Mid", "Tomb", "InvertTomb");
                int i=0;
                for (Filter filter:customfilters){
                    ThumbnailItem tI = new ThumbnailItem();
                    tI.image = thumbImg;
                    tI.filter = filter;
                    tI.filterName = FilterName.get(i);
                    ThumbnailsManager.addThumb(tI);
                    i = i+1;
                }

                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(r).start();
    }

    public static List<Filter> getCustomFilters(Context context) {
        List<Filter> filters = new ArrayList<>();
        filters.add(getRedFilter(context));
        filters.add(getGreenFilter(context));
        filters.add(getBlueFilter(context));
        filters.add(getRedGreenFilter(context));
        filters.add(getGreenBlueFilter(context));
        filters.add(getRedBlueFilter(context));
        filters.add(getGrayscaleFilter(context));
        filters.add(getNegativeFilter(context));
        filters.add(getGrayscaleNegativeFilter(context));
        filters.add(getNegativeRedFilter(context));
        filters.add(getNegativeGreenFilter(context));
        filters.add(getNegativeBlueFilter(context));
        filters.add(getLogFilter(context));
        filters.add(getGamma05Filter(context));
        filters.add(getGamma08Filter(context));
        filters.add(getGamma2Filter(context));
        filters.add(getGamma4Filter(context));
        filters.add(getGamma8Filter(context));
        filters.add(getSineFilter(context));
        filters.add(getCosFilter(context));
        filters.add(getTanFilter(context));
        filters.add(getContrastFilter(context));
        filters.add(getNegContrastFilter(context));
        filters.add(getSlice1LowFilter(context));
        filters.add(getSlice1MidFilter(context));
        filters.add(getSlice2LowFilter(context));
        filters.add(getSlice2MidFilter(context));
        filters.add(getTombFilter(context));
        filters.add(getInvertTombFilter(context));
        return filters;
    }

    public static Filter getRedFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,0);
        redKnots[1] = new Point(255,255);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,0);
        greenKnots[1] = new Point(255,0);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,0);
        blueKnots[1] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getGreenFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,0);
        redKnots[1] = new Point(255,0);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,0);
        greenKnots[1] = new Point(255,255);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,0);
        blueKnots[1] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getBlueFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,0);
        redKnots[1] = new Point(255,0);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,0);
        greenKnots[1] = new Point(255,0);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,0);
        blueKnots[1] = new Point(255,255);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getRedGreenFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,0);
        redKnots[1] = new Point(255,255);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,0);
        greenKnots[1] = new Point(255,255);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,0);
        blueKnots[1] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getGreenBlueFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,0);
        redKnots[1] = new Point(255,0);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,0);
        greenKnots[1] = new Point(255,255);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,0);
        blueKnots[1] = new Point(255,255);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getRedBlueFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,0);
        redKnots[1] = new Point(255,255);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,0);
        greenKnots[1] = new Point(255,0);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,0);
        blueKnots[1] = new Point(255,255);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getGrayscaleFilter(Context context) {
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(0f));
        return filter;
    }

    public static Filter getNegativeFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[2];
        rgbKnots[0] = new Point(0,255);
        rgbKnots[1] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getGrayscaleNegativeFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[2];
        rgbKnots[0] = new Point(0,255);
        rgbKnots[1] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        filter.addSubFilter(new SaturationSubfilter(0f));
        return filter;
    }

    public static Filter getNegativeRedFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,255);
        redKnots[1] = new Point(255,0);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,0);
        greenKnots[1] = new Point(255,0);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,0);
        blueKnots[1] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getNegativeGreenFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,0);
        redKnots[1] = new Point(255,0);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,255);
        greenKnots[1] = new Point(255,0);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,0);
        blueKnots[1] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getNegativeBlueFilter(Context context) {
        Point[] redKnots;
        redKnots = new Point[2];
        redKnots[0] = new Point(0,0);
        redKnots[1] = new Point(255,0);

        Point[] greenKnots;
        greenKnots = new Point[2];
        greenKnots[0] = new Point(0,0);
        greenKnots[1] = new Point(255,0);

        Point[] blueKnots;
        blueKnots = new Point[2];
        blueKnots[0] = new Point(0,255);
        blueKnots[1] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }

    public static Filter getLogFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        for(int i=0;i<256;i++){
            rgbKnots[i] = new Point(i, (int) ((255/(Math.log(1+255)))*Math.log(1+i)));
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getGamma05Filter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        for(int i=0;i<256;i++){
            rgbKnots[i] = new Point(i, (int) ((255/(Math.pow(255,0.5)))*Math.pow(i, 0.5)));
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getGamma08Filter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        for(int i=0;i<256;i++){
            rgbKnots[i] = new Point(i, (int) ((255/(Math.pow(255,0.8)))*Math.pow(i, 0.8)));
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getGamma2Filter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        for(int i=0;i<256;i++){
            rgbKnots[i] = new Point(i, (int) ((1/(Math.pow(255,1)))*Math.pow(i, 2)));
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getGamma4Filter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        for(int i=0;i<256;i++){
            rgbKnots[i] = new Point(i, (int) ((1/(Math.pow(255,3)))*Math.pow(i, 4)));
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getGamma8Filter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        for(int i=0;i<256;i++){
            rgbKnots[i] = new Point(i, (int) ((1/(Math.pow(255,7)))*Math.pow(i, 8)));
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getSineFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        int temp;
        for(int i=0;i<256;i++){
            temp = 127 + ((int) (127*Math.sin(2*(Math.PI)*(1f/256f)*i)));
            rgbKnots[i] = new Point(i, temp);
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getCosFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        int temp;
        for(int i=0;i<256;i++){
            temp = 127 + ((int) (127*Math.cos(2*(Math.PI)*(1f/256f)*i)));
            rgbKnots[i] = new Point(i, temp);
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getTanFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        int temp;
        for(int i=0;i<256;i++){
            temp = 127 + ((int) (12.7*Math.tan(2*(Math.PI)*(1f/256f)*i)));
            if(temp>256) temp=255;
            if(temp<0) temp=0;
            rgbKnots[i] = new Point(i, temp);
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getContrastFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        for(int i=0;i<128;i++){
            rgbKnots[i] = new Point(i, 127 - (int) ((127/(Math.pow(127,0.4)))*Math.pow(127-i, 0.4)));
        }

        for(int i=128;i<256;i++){
            rgbKnots[i] = new Point(i, 127 + (int) ((127/(Math.pow(127, 0.4)))*Math.pow(i-127, 0.4)));
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getNegContrastFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[256];
        for(int i=0;i<128;i++){
            rgbKnots[i] = new Point(i, 127 - (int) ((127/(Math.pow(127,2.5)))*Math.pow(127-i, 2.5)));
        }

        for(int i=128;i<256;i++){
            rgbKnots[i] = new Point(i, 127 + (int) ((127/(Math.pow(127, 2.5)))*Math.pow(i-127, 2.5)));
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getSlice1LowFilter(Context context){
        Point[] rgbKnots;
        rgbKnots = new Point[256];

        for(int i=0;i<256;i++) {
            rgbKnots[i] = new Point(i, 64);
        }

        for(int i=0;i<50;i++){
            rgbKnots[i] = new Point(i, 196);
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        filter.addSubFilter(new SaturationSubfilter(0f));
        return filter;
    }

    public static Filter getSlice1MidFilter(Context context){
        Point[] rgbKnots;
        rgbKnots = new Point[256];

        for(int i=0;i<256;i++) {
            rgbKnots[i] = new Point(i, 64);
        }

        for(int i=100;i<147;i++){
            rgbKnots[i] = new Point(i, 196);
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        filter.addSubFilter(new SaturationSubfilter(0f));
        return filter;
    }

    public static Filter getSlice2LowFilter(Context context){
        Point[] rgbKnots;
        rgbKnots = new Point[256];

        for(int i=0;i<256;i++) {
            rgbKnots[i] = new Point(i, i);
        }

        for(int i=0;i<50;i++){
            rgbKnots[i] = new Point(i, 210);
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        filter.addSubFilter(new SaturationSubfilter(0f));
        return filter;
    }

    public static Filter getSlice2MidFilter(Context context){
        Point[] rgbKnots;
        rgbKnots = new Point[256];

        for(int i=0;i<256;i++) {
            rgbKnots[i] = new Point(i, i);
        }

        for(int i=100;i<147;i++){
            rgbKnots[i] = new Point(i, 210);
        }

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        filter.addSubFilter(new SaturationSubfilter(0f));
        return filter;
    }

    public static Filter getTombFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[3];
        rgbKnots[0] = new Point(0,0);
        rgbKnots[1] = new Point(127,255);
        rgbKnots[2] = new Point(255,0);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }

    public static Filter getInvertTombFilter(Context context) {
        Point[] rgbKnots;
        rgbKnots = new Point[3];
        rgbKnots[0] = new Point(0,255);
        rgbKnots[1] = new Point(127,0);
        rgbKnots[2] = new Point(255,255);

        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }



    @Override
    public void onFilterSelected(Filter filter) {
        if(listner != null)
            listner.onFilterSelected(filter);
    }
}
