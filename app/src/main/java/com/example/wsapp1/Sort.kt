package com.example.wsapp1

import android.provider.ContactsContract
import com.example.wsapp1.model.DataModel
import com.example.wsapp1.model.DataX

class Sort {
    companion object {
        fun sort(list: ArrayList<DataX>): List<DataX> {
            list.sortBy { it.name }
            return list;
        }  //.sortBy { it.Model }
    }

    //TODO dasdas

}