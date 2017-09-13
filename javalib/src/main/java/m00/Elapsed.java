/*
 * elap.java
 *
 * Created on 18 de Março de 2007, 14:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package m00;

import java.util.*;

/**
 *
 * @author consul
 */
public class Elapsed {
    private GregorianCalendar gc_first = null;
    private GregorianCalendar gc_last = null;
    private int fh12, fh24, fmin, fsec, fmls, faph,
                lh12, lh24, lmin, lsec, lmls, laph;
    private long fmillis, lmillis;
    /** Creates a new instance of elap */
    public Elapsed() {
    }
    
    public void setFirstEvent() {
        gc_first = new GregorianCalendar();
        fh12 = gc_first.get(Calendar.HOUR);         // 0..11
        fh24 = gc_first.get(Calendar.HOUR_OF_DAY);  // 0..23
        fmin = gc_first.get(Calendar.MINUTE);       // 0..59
        fsec = gc_first.get(Calendar.SECOND);       // 0..59
        fmls = gc_first.get(Calendar.MILLISECOND);  // 0..999
        faph = gc_first.get(Calendar.AM_PM);        // 0=AM, 1=PM    
        fmillis = gc_first.getTimeInMillis();
    }

    public void setLastEvent() {
        gc_last = new GregorianCalendar();
        lh12 = gc_last.get(Calendar.HOUR);         // 0..11
        lh24 = gc_last.get(Calendar.HOUR_OF_DAY);  // 0..23
        lmin = gc_last.get(Calendar.MINUTE);       // 0..59
        lsec = gc_last.get(Calendar.SECOND);       // 0..59
        lmls = gc_last.get(Calendar.MILLISECOND);  // 0..999
        laph = gc_last.get(Calendar.AM_PM);        // 0=AM, 1=PM    
        lmillis = gc_last.getTimeInMillis();
    }
    
    public long getElapInMillis() {
/*
        System.out.println("first="+fmillis);
        System.out.println("last="+lmillis);
*/
        return(lmillis - fmillis);
    }
    
    public long getElapInSeconds() {
        return((lmillis - fmillis)/1000);
    }
    
    public String getElapInHMS() {
        long timeInSeconds = getElapInSeconds();
        long hours, minutes, seconds;
        hours = (timeInSeconds / 3600);
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = (timeInSeconds / 60);
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;
        return(Long.toString(hours) + " hour(s) " + Long.toString(minutes) + " minute(s) " + Long.toString(seconds) + " second(s)");
   }   

    public static void main(String[] args) {

      Elapsed elap = new Elapsed();
      elap.setFirstEvent();
      long cont = 0;
      for (long i=0; i<10000000; i++)  cont++;
      elap.setLastEvent();
      System.out.println(elap.getElapInHMS());
      System.out.println(Long.toString(elap.getElapInMillis())+" milliseconds");
   }

}
    

