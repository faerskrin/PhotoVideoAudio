package com.example.wsapp1

import android.provider.ContactsContract
import com.example.wsapp1.model.DataModel

class Sort {
    companion object {
        fun sort(list: ArrayList<DataModel>): List<DataModel> {
            list.sortBy { it.Model }
            return list;
        }  //.sortBy { it.Model }
    }

    //TODO dasdas

}