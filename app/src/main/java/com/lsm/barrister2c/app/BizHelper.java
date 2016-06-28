package com.lsm.barrister2c.app;

import android.content.Context;

import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.BusinessArea;
import com.lsm.barrister2c.data.entity.BusinessType;
import com.lsm.barrister2c.data.entity.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvshimin on 16/6/25.
 */
public class BizHelper {

    private static BizHelper ourInstance = new BizHelper();

    public static BizHelper getInstance() {
        return ourInstance;
    }

    private BizHelper() {

    }

    List<BusinessArea> bizAreas ;
    List<BusinessType> bizTypes ;
    List<Filter> years = new ArrayList<>();
    List<Filter> areas = new ArrayList<>();

    boolean isInited = false;

    public void init(final Context context){

        years.clear();
        areas.clear();

        String[] yearArray = context.getResources().getStringArray(R.array.filter_year);
        for(int i= 0 ;i<yearArray.length;i++){
            Filter filter = new Filter();
            filter.setName(yearArray[i]);
            if(i==0){
                //1-3
                filter.setId(String.valueOf(3));
            }else if(i==1){//3-5
                filter.setId(String.valueOf(5));
            }else if(i==2){//5-10
                filter.setId(String.valueOf(10));
            }else {//10+
                filter.setId(String.valueOf(11));
            }
            years.add(filter);
        }

        String[] areaArray = context.getResources().getStringArray(R.array.filter_area);
        for(int i= 0 ;i<areaArray.length;i++){
            Filter filter = new Filter();
            filter.setName(areaArray[i]);
            filter.setId(areaArray[i]);
            areas.add(filter);
        }

//        new GetBizTypeAreaListReq(context).execute(new Action.Callback<IO.GetBizTypeAreaListResult>() {
//
//            @Override
//            public void progress() {
//
//            }
//
//            @Override
//            public void onError(int errorCode, String msg) {
//
//            }
//
//            @Override
//            public void onCompleted(IO.GetBizTypeAreaListResult result) {
//                isInited = true;
//
//                BizHelper.this.bizAreas = result.bizAreas;
//                BizHelper.this.bizTypes = result.bizTypes;
//
//                years.clear();
//                areas.clear();
//
//                String[] yearArray = context.getResources().getStringArray(R.array.filter_year);
//                for(int i= 0 ;i<yearArray.length;i++){
//                    Filter filter = new Filter();
//                    filter.setName(yearArray[i]);
//                    if(i==0){
//                        //1-3
//                        filter.setId(String.valueOf(3));
//                    }else if(i==1){//3-5
//                        filter.setId(String.valueOf(5));
//                    }else if(i==2){//5-10
//                        filter.setId(String.valueOf(10));
//                    }else {//10+
//                        filter.setId(String.valueOf(11));
//                    }
//                    years.add(filter);
//                }
//
//                String[] areaArray = context.getResources().getStringArray(R.array.filter_area);
//                for(int i= 0 ;i<areaArray.length;i++){
//                    Filter filter = new Filter();
//                    filter.setName(areaArray[i]);
//                    filter.setId(areaArray[i]);
//                    areas.add(filter);
//                }
//            }
//        });
    }


    public List<BusinessArea> getBizAreas() {
        return bizAreas;
    }

    public void setBizAreas(List<BusinessArea> bizAreas) {
        this.bizAreas = bizAreas;
    }

    public List<BusinessType> getBizTypes() {
        return bizTypes;
    }

    public void setBizTypes(List<BusinessType> bizTypes) {
        this.bizTypes = bizTypes;
    }

    public boolean isInited() {
        return isInited;
    }

    public List<Filter> getYears() {
        return years;
    }

    public void setYears(List<Filter> years) {
        this.years = years;
    }

    public List<Filter> getAreas() {
        return areas;
    }

    public void setAreas(List<Filter> areas) {
        this.areas = areas;
    }
}
