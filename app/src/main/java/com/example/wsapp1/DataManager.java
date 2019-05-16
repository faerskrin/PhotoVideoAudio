package com.example.wsapp1;

import com.example.wsapp1.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private List<DataModel> dataModels;

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


}
