package com.example.findme.adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 2014/12/26.
 */
public class CaseInfoAdapter extends SimpleAdapter {

    public CaseInfoAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }
}
