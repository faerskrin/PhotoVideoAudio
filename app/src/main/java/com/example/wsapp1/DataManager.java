package com.example.wsapp1;

import com.example.wsapp1.model.DataModel;
import com.example.wsapp1.model.DataX;
import com.example.wsapp1.model.GetAutoFromUser;
import com.example.wsapp1.model.GetToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class DataManager {

    private List<DataModel> dataModels;
    private GetToken mGetToken;
    private GetAutoFromUser mGetAutoFrom;
    private List<DataX> mListAuto;

    public DataManager() {
        setDataModels();
    }

    public List<DataModel> getDataModels() {
        return dataModels;
    }

    public void setDataModels() {
        dataModels = new ArrayList<>();
        dataModels.add(new DataModel("321312"));
        dataModels.add(new DataModel("dsadas"));
        dataModels.add(new DataModel("zxczxc"));
        dataModels.add(new DataModel("czz"));
        dataModels.add(new DataModel("vxcvxc"));
        dataModels.add(new DataModel("0000"));
        dataModels.add(new DataModel("https://toster.ru/q/356543"));
    }


    public void setUserInfo(GetToken o) {
        mGetToken = o;
    }

    public GetToken getmGetToken() {
        return mGetToken;
    }

    public void setCarInfoForUser(GetAutoFromUser o) {

        mGetAutoFrom = o;
    }

    public List<DataX> getmListAuto() {
        return mListAuto;
    }

    public GetAutoFromUser getmGetAutoFrom() {
        return mGetAutoFrom;
    }
}
