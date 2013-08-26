/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lekt03_net;

import android.content.res.Resources;
import android.location.Location;

import java.io.DataInputStream;
import java.io.IOException;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class GpsDataPostnrBy {

  static int[] postNr;
  static int[] gpsX;
  static int[] gpsY;
  static String[] byNavn;
  static GpsDataPostnrBy instans = new GpsDataPostnrBy();

  /**
   * Indlæs postnumre, byer og deres GPS-koordinater
   *
   * @param resources Applikationens resurser
   */
  static void init(Resources resources) throws IOException {
    if (postNr != null) {
      return;
    }
    DataInputStream ins = new DataInputStream(resources.openRawResource(R.raw.vejret_postnr_gps_by));
    int antal = ins.readInt();
    postNr = new int[antal];
    gpsX = new int[antal];
    gpsY = new int[antal];
    byNavn = new String[antal];

    for (int i = 0; i < antal; i++) {
      postNr[i] = ins.readInt();
    }
    for (int i = 0; i < antal; i++) {
      gpsX[i] = ins.readInt();
    }
    for (int i = 0; i < antal; i++) {
      gpsY[i] = ins.readInt();
    }
    String byer = ins.readUTF();
    System.err.println("byer = " + byer);
    // byer = ,Viborg,Rødby,Vojens,Slagelse,Hadsund,Læsø/Byrum,Ølgod,Ebeltoft,Haderslev,Sorø,Fejø,Blåvand,Hasle,Lemvig,Hedehusene,Århus,Svendborg,Støvring,Ishøj
    // del op i array
    int n = byer.length();
    int n0 = n;
    while (--n >= 0) {
      if (byer.charAt(n) == ',') {
        byNavn[--antal] = byer.substring(n + 1, n0);
        System.err.println("byNavn[" + antal + "] = " + byNavn[antal]);
        n0 = n;
      }
    }
  }

  static GpsDataPostnrBy instans() {
    return instans;
  }

  static final int maxDist = (int) 5E5;

  int findNærmestePunkt(Location pos) {

    System.err.println("findNærmestePunkt pos = " + pos); // mLatitude=55.663655774999995,mLongitude=12.502774549999998
    int y = (int) (pos.getLatitude() * 1E6); // "latitude=y","longitude=x"
    int x = (int) (pos.getLongitude() * 1E6);

    int dist = Integer.MAX_VALUE;
    int bedsteN = -1;

    for (int n = gpsX.length - 1; n >= 0; n--) {
      int dx = gpsX[n] - x;
      if (dx > maxDist || dx < -maxDist) {
        continue;
      }
      int dy = gpsY[n] - y;
      if (dy > maxDist || dy < -maxDist) {
        continue;
      }

      int dist2 = dx * dx + dy * dy;
      if (dist > dist2) {
        dist = dist2;
        bedsteN = n;
        System.err.println(x + " - dx = " + dx);
        System.err.println("dist2 = " + dist2 + "   for " + n);
      }
    }
    return bedsteN;
  }
}
