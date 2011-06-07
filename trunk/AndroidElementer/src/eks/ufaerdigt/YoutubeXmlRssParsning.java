package eks.ufaerdigt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import dk.nordfalk.android.elementer.R;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

/**
 *
 * @author Jacob Nordfalk
 */
public class YoutubeXmlRssParsning extends Activity {

    // names of the XML tags
    static final String PUB_DATE = "pubDate";
    static final  String DESCRIPTION = "description";
    static final  String LINK = "link";
    static final  String TITLE = "title";
    static final  String ITEM = "item";

  private static class Message {

    public Message() {
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView tv=new TextView(this);
/*
http://www.ibm.com/developerworks/opensource/library/x-android/index.html
http://code.google.com/p/feedgoal/
     http://gdata.youtube.com/feeds/api/users/jn0101/uploads
http://stackoverflow.com/questions/5162088/video-view-not-playing-youtube-video

http://code.google.com/intl/da/apis/youtube/2.0/developers_guide_protocol_api_query_parameters.html#keysp
     */

    try {
      /*
      XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
      factory.setNamespaceAware(true);
      XmlPullParser xpp=factory.newPullParser();
       */
      XmlPullParser parser=android.util.Xml.newPullParser();

      InputStream is = new URL("http://gdata.youtube.com/feeds/api/users/jn0101/uploads").openStream();
              //=getResources().openRawResource(R.raw.data_xmleksempel);
      // Det kan være nødvendigt at hoppe over BOM mark - se http://android.forums.wordpress.org/topic/xml-pull-error?replies=2
      //is.read(); is.read(); is.read();
      parser.setInput(is, "UTF-8"); // evt "ISO-8859-1"
/*
            int eventType = parser.getEventType();
            Message currentMessage = null;
            boolean done = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        messages = new ArrayList<Message>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)){
                            currentMessage = new Message();
                        } else if (currentMessage != null){
                            if (name.equalsIgnoreCase(LINK)){
                                currentMessage.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)){
                                currentMessage.setDescription(parser.nextText());
                            } else if (name.equalsIgnoreCase(PUB_DATE)){
                                currentMessage.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)){
                                currentMessage.setTitle(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM) &&
currentMessage != null){
                            messages.add(currentMessage);
                        } else if (name.equalsIgnoreCase(CHANNEL)){
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
*/

      is.close();
      //tv.append("\n\nTotal kredit er "+totalKredit+" kr.");
    } catch (Exception ex) {
      ex.printStackTrace();
      tv.append("FEJL:"+ex.toString());
    }

    setContentView(tv);
  }


}
