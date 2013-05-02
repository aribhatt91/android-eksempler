/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.grafik;

/**
 *
 * @author j
 */
class SensorObs {

  public long timestamp;
  public float[] values;

  public SensorObs(long timestamp1, float[] values1) {
    timestamp = timestamp1;
    values = values1;
  }

}
