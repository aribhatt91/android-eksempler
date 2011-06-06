package eks.livscyklus;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author Jacob Nordfalk
 */
public class LogFragment extends Fragment {

  /**
   * Denne klassevariabel vil tælle antallet af objekter (instanser af klassen)
   */
  static int instansNummer = 0;

  /**
   * I loggen skriver klassenavn#nummer foran hver log-sætniong.
   * F.eks LogAktivitet#1, LogAktivitet#2
   */
  String logNavn;

  public LogFragment() {
    instansNummer++;  // tæl nummeret op
    logNavn = "Fragment "+this.getClass().getSimpleName()+"#"+instansNummer;
  }


  /**
   * Når fragmentet skal oprettes
   */
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    Log.d(logNavn,"onAttach("+activity);    
  }
  
  
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(logNavn,"onCreate("+savedInstanceState);
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
    Log.d(logNavn,"onCreateView("+savedInstanceState);
    return super.onCreateView(inflater, container, savedInstanceState);      
  }

  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.d(logNavn,"onActivityCreated("+savedInstanceState);
  }
    
    /**
   * Når fragmentet er synligty
   */
  public void onStart() { Log.d(logNavn,"onStart()"); super.onStart(); }

  /**
   * Når fragmentet er i forgrunden
   */
  public void onResume() { Log.d(logNavn,"onResume()"); super.onResume(); }

  /**
   * Når fragmentet ikke mere er i forgrunden
   */
  public void onPause() { Log.d(logNavn,"onPause()"); super.onPause(); }

  /**
   * Når fragmentet ikke mere er synlig
   */
  public void onStop() { Log.d(logNavn,"onStop()"); super.onStop(); }

  public void onDestroyView() { Log.d(logNavn,"onDestroyView()"); super.onDestroyView(); }

  /**
   * Når fragmentet bliver nedlagt
   */
  public void onDestroy() { Log.d(logNavn,"onDestroy()"); super.onDestroy(); }

  public void onDetach() { Log.d(logNavn,"onDetach()"); super.onDetach(); }
}
