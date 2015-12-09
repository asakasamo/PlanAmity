package data;

import java.time.LocalDate;
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
     * Creates a new DateTime at the current moment in time.
     */
    public DateTime() {
        super();
    }

    /**
     * Creates a DateTime out of a specified LocalDate object, using its month, day, and year.
     * @param date the LocalDate object
     */
    public DateTime(LocalDate date) {
        super(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * Creates a DateTime of the given month, day, and year. The parameters are formatted as a normal person would input
     * such a date.
     * @param month the month
     * @param day the day
     * @param year the year
     */
    public DateTime(int month, int day, int year) { super(year, month -1, day); }

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
	 * Returns a DateTime that is a specified number of minutes after this DateTime
	 * @param mins the number of minutes
	 * @return a DateTime mins minutes from this DateTime
	 */
	public DateTime getLaterDateTime(int mins){
		DateTime dt = (DateTime)this.clone();
		dt.add(DateTime.MINUTE, mins);
		return dt;
	}

    public String toString() {
        return (this.get(DateTime.MONTH) + 1) + "/" + this.get(DateTime.DAY_OF_MONTH) + "/" + this.get(DateTime.YEAR);
    }
}