package eks.vejret;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import dk.nordfalk.android.elementer.R;

public class Indstillinger extends PreferenceActivity {
  
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.vejret_indstillinger);
   }

   public static boolean brugPosVedStart(Context ctx) {
     return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("brugPosVedStart", true);
   }

   public static boolean ventPåPos(Context ctx) {
     return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("ventPåPos", false);
   }
}
