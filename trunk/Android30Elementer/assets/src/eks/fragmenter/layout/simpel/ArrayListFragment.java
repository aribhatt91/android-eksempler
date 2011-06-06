/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.fragmenter.layout.simpel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import eks.fragmenter.layout.ShakespeareData;
import eks.livscyklus.LogListFragment;

/**
 *
 * @author j
 */
public class ArrayListFragment extends LogListFragment {

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ShakespeareData.TITLES));
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Log.i("ArrayListFragment", "Item clicked: " + id);
  }

}
