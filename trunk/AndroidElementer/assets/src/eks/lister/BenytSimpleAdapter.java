/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Source: com.example.android.apis.ApiDemos in Android samples
 */

package eks.lister;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenytSimpleAdapter extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        List<Map<String,Object>> mineData = new ArrayList<Map<String,Object>>();

        PackageManager pm = getPackageManager();
        PackageInfo pi;
        try {
          pi=pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException ex) {
          ex.printStackTrace();
          return;
        }

        String thisName = getClass().getName();
        for (ActivityInfo ai : pi.activities) {
          if (thisName.equals(ai.name)) continue;
          Map<String, Object> temp = new HashMap<String, Object>();
          temp.put("title", ai.loadLabel(pm).toString());

          Intent intent = new Intent();
          intent.setClassName(ai.applicationInfo.packageName, ai.name);
          
          temp.put("intent", intent);
          mineData.add(temp);
        }


        setListAdapter(new SimpleAdapter(this, mineData,
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));

        getListView().setTextFilterEnabled(true);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map map = (Map) l.getItemAtPosition(position);
        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }
}
