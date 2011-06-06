/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.loaders;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Definition of the contract for the main table of our provider.
 */
/**
 *
 * @author j
 */
public final class MainTable implements BaseColumns {

  private MainTable() {
  }
  /**
   * The table name offered by this provider
   */
  public static final String TABLE_NAME = "main";
  /**
   * The content:// style URL for this table
   */
  public static final Uri CONTENT_URI = Uri.parse("content://" + LoaderThrottle.AUTHORITY + "/main");
  /**
   * The content URI base for a single row of data. Callers must
   * append a numeric row id to this Uri to retrieve a row
   */
  public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + LoaderThrottle.AUTHORITY + "/main/");
  /**
   * The MIME type of {@link #CONTENT_URI}.
   */
  public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.api-demos-throttle";
  /**
   * The MIME type of a {@link #CONTENT_URI} sub-directory of a single row.
   */
  public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.api-demos-throttle";
  /**
   * The default sort order for this table
   */
  public static final String DEFAULT_SORT_ORDER = "data COLLATE LOCALIZED ASC";
  /**
   * Column name for the single column holding our data.
   * <P>Type: TEXT</P>
   */
  public static final String COLUMN_NAME_DATA = "data";

}
