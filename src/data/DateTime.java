package data;

import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Data structure representing a single moment in time, expressed in the standard Gregorian Calendar.
 * 
 * @author Al-John
 *
 */
public class DateTime extends GregorianCalendar {

	private static final long serialVersionUID = 1L;

	/**
	 * Returns the amount of time between two specified DateTimes, in minutes.
	 * @param t1 DateTime a
	 * @param t2 DateTime b
	 * @return the amount of time between a and b, in minutes
	 */
	public static int minutesBetween(DateTime t1, DateTime t2) {
		//time between start and end, in minutes
		return (int)(Math.abs(TimeUnit.MILLISECONDS.toMinutes(t1.getTime().getTime() - t2.getTime().getTime())));
	}
	
	/**
	 * Returns a DateTime that is a specified number of minutes after a specified DateTime.
	 * @param hrs the number of minutes
	 * @param d the DateTime
	 * @return a DateTime mins minutes from d
	 */
	public static DateTime getLaterDateTime(int mins, DateTime from){
		DateTime dt = (DateTime)from.clone();
		dt.add(DateTime.MINUTE, mins);
		return dt;
	}
}