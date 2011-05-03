package eks.livscyklus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 *
 * @author Jacob Nordfalk
 */
public class LogAktivitet extends Activity {

  /**
   * Denne klassevariabel vil tælle antallet af objekter (instanser af klassen)
   */
  static int instansNummer = 0;

  /**
   * I loggen skriver klassenavn#nummer foran hver log-sætniong.
   * F.eks LogAktivitet#1, LogAktivitet#2
   */
  String logNavn;

  public LogAktivitet() {
    instansNummer++;  // tæl nummeret op
    logNavn = this.getClass().getSimpleName()+"#"+instansNummer;
  }



  /**
   * Når aktiviteten skal oprettes
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(logNavn,"onCreate("+savedInstanceState);

    // Koden herunder er overflødig i nedarvinger, men forstørrer ikke,
    // så længe nedarvingerne også kalder setContentView()
    TextView tv1 = new TextView(this);
    tv1.setText( "Redigér i de to tekstfelter. Det med id vil få genskabt sine data når telefonen vendes (i emulatoren tryk Ctrl-F11)" );

    EditText tv2 = new EditText(this);
    tv2.setText("Et view uden id");

    EditText tv3 = new EditText(this);
    tv3.setText("Et view med id");
    // De Views der har et ID bliver gemt i onSaveInstanceState()
    // og genskabt i onRestoreInstanceState().
    // I layout-XML-filer sættes ID med attributten android:id="@+id/navn"
    tv3.setId(1000042); // bare et eller andet

    TableLayout tl = new TableLayout(this);
    tl.addView(tv1);
    tl.addView(tv2);
    tl.addView(tv3);
    setContentView(tl);
  }

  /**
   * Når aktiviteten er synlig
   */
  protected void onStart() { Log.d(logNavn,"onStart()"); super.onStart(); }

  /**
   * Når aktiviteten er i forgrunden
   */
  protected void onResume() { Log.d(logNavn,"onResume()"); super.onResume(); }

  /**
   * Når aktiviteten ikke mere er i forgrunden
   * Data der skal gemmes bør gemmes her - processen kan dræbes efter onPause()
   */
  protected void onPause() { Log.d(logNavn,"onPause()"); super.onPause(); }

  /**
   * Når aktiviteten ikke mere er synlig
   */
  protected void onStop() { Log.d(logNavn,"onStop()"); super.onStop(); }

  /**
   * Kaldes før onStart() hvis aktiviteten bliver genoptaget efter onStop()
   */
  protected void onRestart() { Log.d(logNavn,"onRestart()"); super.onRestart(); }

  /**
   * Når aktiviteten bliver nedlagt
   */
  protected void onDestroy() { Log.d(logNavn,"onDestroy()"); super.onDestroy(); }

  /**
   * Når skærmen vendes eller tastaturet bliver skubbet ind siger man der er sket
   * en ændring i telefonens konfiguration.
   * Så skal komponenterne have ændret størrelse
   * Evt skal et andet layout vises (layout-land)
   * Evt skærmtastatur skal vises/skjules
   * Systemet vil kalde denne metode så du kan give et enkelt objekt som indeholder
   * data der ikke er relateret til telefonens konfiguration (eonConfigurationInstance).
   * Dett bliver overført til det nye aktivitets-objekt oprettes med de nye skærmdimensioner etc.
   * I den nye instans kan du få objektet ved at kalde getLastNonConfigurationInstance()
   * @return Data der ikke er relateret til telefonens konfiguration
   */
  public Object onRetainNonConfigurationInstance() {
    Log.d(logNavn,"onRetainNonConfigurationInstance()");
    return super.onRetainNonConfigurationInstance();
  }
 
  /**
   * Når aktiviteten skal gemme sin tilstand, fordi den måske bliver dræbt herefter.
   * Kaldes ikke hvis aktiviteten afslutter normalt (bruger trykker tilbage-knap eller
   * finish() kaldes
   */
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(logNavn,"onSaveInstanceState("+outState);
  }

  /**
   * Når aktiviteten skal genskabe sin tilstand sådan som den så ud på et tidligere
   * tidspunkt. Du kan bruge onCreate() til det samme,
   * og det er det samme Bundle-objekt der overføres
   */
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    Log.d(logNavn,"onRestoreInstanceState("+savedInstanceState);
     // her genskabes indhold for alle views med id
    super.onRestoreInstanceState(savedInstanceState);
  }

}
