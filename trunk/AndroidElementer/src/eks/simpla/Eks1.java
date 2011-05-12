package eks.simpla;

import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import org.simpla.SimplaActivity;

/**
 * 
 * @author Jacob Nordfalk
 */
public class Eks1 extends SimplaActivity {


	public void simplaMain() {

		TextView tv;
    
    //tv =simpla.createTextView("Hvornår smager en Tuborg bedst?\n");

		tv = new TextView(this);
		tv.setText("Hvornår smager en Tuborg bedst?\n");

    tv.setTextSize(18);
    tv.setTextColor(Color.YELLOW);  // gul

		simpla.setContentView(tv); // Herefter er det forbudt at kalde f.eks. tv.setTextSize(18);
    simpla.sleep(5000);

    simpla.println(tv, "hej");

    String navnet;
    navnet = simpla.showEditTextDialog("Hvad hedder du?", "(skriv dit navn her)");
    simpla.println(tv, "Dit navn er: "+navnet);
    
    String svar;
		svar = simpla.showTwoButtonAlertDialog("Hvornår smager en Tuborg bedst?", "Hvergang!", "Altid!");

    simpla.sleep(1000);

    if (svar.equals("Hvergang!"))
    {
  		simpla.println(tv, "Ja, det er rigtigt!");
  		simpla.println(tv, "Hvergang!");
    } 
    else
    {
  		simpla.println(tv, "Nej, det er ikke "+svar);
  		simpla.println(tv, "Kender du ikke den gamle Tuborg-reklame?");
  		simpla.println(tv, "");
  		simpla.println(tv, "Tryk på en tast for at læse om Storm P");
  		simpla.println(tv, "(tryk <- / Esc når du vil tilbage)");

      simplaWaitForKeyPress();

  		simpla.openUrl("http://da.m.wikipedia.org/wiki/Storm_P");
    }

    simpla.sleep(1000);

    simpla.println(tv, "men... skal vi ikke fortælle det glade budskab til nogle andre?");

    simpla.sleep(3000);

    svar = simpla.showTwoButtonAlertDialog("Send SMS?", "Ja", "Nej, ikke nu.");
		simpla.showToast("Du svarede: "+svar);
    simpla.sleep(1000);
    
    if (svar.equals("Ja")) {
      Intent sms = simpla.createSmsIntent("26206512", tv.getText());
      simpla.startActivity(sms);
      simpla.waitForActivityToFinish();
      simpla.println(tv, "Velkommen tilbage fra SMS-programmet");
      simpla.sleep(1000);
    }

    simpla.println(tv, "Slut");
    simpla.sleep(3000);
    finish();  // Dette lukker aktiviteten
	}
}
