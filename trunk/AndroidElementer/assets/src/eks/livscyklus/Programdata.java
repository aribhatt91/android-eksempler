package eks.livscyklus;
import java.io.*;
import java.util.ArrayList;

public class Programdata implements Serializable
{
	public int etTal;
	public int etAndetTal;

  public ArrayList<String> liste = new ArrayList<String>();

	// Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
	private static final long serialVersionUID = 12345; // bare et eller andet nr.

	public String toString()
	{
		return "Data: etTal="+etTal+" liste="+liste;
	}
}