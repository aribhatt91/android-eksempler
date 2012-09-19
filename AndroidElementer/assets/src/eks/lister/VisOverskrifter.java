package eks.lister;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dk.nordfalk.android.elementer.R;

public class VisOverskrifter extends Activity {

	String[] landeOgOverskrifter = {
		"-Europas lande",
		"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien",
		"-Asien",
		"Nepal", "Kina",
		"*En overskrift",
		"-Flere lande i Europa",
		"*En overskrift mere",
		"Tyskland", "Finland", "Holland", "Italien",
		"-Flere lande i Asien",
		"Indien", "Nepal",
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView listView = new ListView(this);
		listView.setAdapter(new MinAdapterMedOverskrifter());
		listView.setDividerHeight(3);

		setContentView(listView);
	}

	public class MinAdapterMedOverskrifter extends BaseAdapter {
		Resources res = getResources();

		public int getCount() {
			return landeOgOverskrifter.length;
		}

		public Object getItem(int position) {
			return position;
		} // bruges ikke

		public long getItemId(int position) {
			return position;
		} // bruges ikke

		/** Skal give typen af elementet der skal vises.
		 0 er normale lande, 1 er kategorier og 2 er overskrifter*/
		@Override
    public int getItemViewType(int position) {
			String landEllerOverskrift = landeOgOverskrifter[position];
			if (landEllerOverskrift.startsWith("-")) return 1;
			if (landEllerOverskrift.startsWith("*")) return 2;
			return 0;
    }

		@Override
    public int getViewTypeCount() {
        return 3;
    }


		public View getView(final int position, View view, ViewGroup parent) {
			int typen = getItemViewType(position);

			if (view == null) {
				// Vi skal oprette et nyt view afhængig af typen
				if (typen==0) {
					view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
				} else if (typen==1) {
					view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
				} else {
					view = getLayoutInflater().inflate(R.layout.listeelement, null);
				}
			}

			String landEllerOverskrift = landeOgOverskrifter[position];
			// Sæt indholdet afhængig af typen
			if (typen==0) {
				TextView tv = (TextView) view.findViewById(android.R.id.text1);
				tv.setText(landEllerOverskrift);
			} else if (typen==1) {
				TextView tv = (TextView) view.findViewById(android.R.id.text2);
				tv.setText(landEllerOverskrift);
			} else {
				TextView tv = (TextView) view.findViewById(R.id.listeelem_overskrift);
				tv.setText(landEllerOverskrift);
			}

			return view;
		}
	}

}
