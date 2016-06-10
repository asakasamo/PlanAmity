package test

import data.DateTime
import org.junit.Test

/**
 * @author Al-John
 */
class DateTimeTest extends GroovyTestCase {
    @Test
    void testCreateDateBetween() {
        DateTime start = new DateTime(1, 1, 2015)
        DateTime finish = new DateTime(1, 31, 2015)
        int minsBetween = DateTime.minutesBetween(start, finish)

        DateTime middle = DateTime.getDateBetween(start, finish)
        int startToMiddle = DateTime.minutesBetween(start, middle)
        int middleToEnd = DateTime.minutesBetween(middle, finish)

        assertEquals(startToMiddle, middleToEnd)
        assertEquals(startToMiddle, minsBetween/2)
        assertEquals(middleToEnd, minsBetween/2)
    }

    @Test
    void testCreateDateBetweenWithWrongOrderParameters() {
        DateTime start = new DateTime(5, 1, 2015)
        DateTime finish = new DateTime(1, 31, 2015)
        int minsBetween = DateTime.minutesBetween(start, finish)

        DateTime middle = DateTime.getDateBetween(start, finish)
        int startToMiddle = DateTime.minutesBetween(start, middle)
        int middleToEnd = DateTime.minutesBetween(middle, finish)

        assertEquals(startToMiddle, middleToEnd)
        assertEquals(startToMiddle, minsBetween/2)
        assertEquals(middleToEnd, minsBetween/2)
    }

    @Test
    void testMinutesBetween() {
        DateTime d1 = new DateTime(1,1,2000)
        DateTime d2 = new DateTime(1,2,2000)
        int minsBetween = DateTime.minutesBetween(d1, d2)

        assertEquals(minsBetween, 1440)
    }

    @Test
    void testDistributeDatesEvenlyOneDate() {
        DateTime start = new DateTime(5,5,2005)
        DateTime middle = new DateTime(5,6,2005)
        DateTime end = new DateTime(5,7,2005)

        List<DateTime> distributed = DateTime.getDatesDistributedEvenlyBetween(start, end, 1)

        assert distributed.size() == 1
        assert middle == distributed.get(0)
    }

    @Test
    void testDistributeDatesEvenlyMultipleDates() {
        DateTime start = new DateTime(6,5,2005)
        DateTime end = new DateTime(6,25,2005)

        List<DateTime> distributed = DateTime.getDatesDistributedEvenlyBetween(start, end, 3)

        DateTime ten = new DateTime(6,10,2005)
        DateTime fifteen = new DateTime(6,15,2005)
        DateTime twenty = new DateTime(6,20,2005)

        assert distributed.size() == 3
        assert ten == distributed.get(0)
        assert fifteen == distributed.get(1)
        assert twenty == distributed.get(2)
    }
    
    @Test
    void testDistributeDatesEvenlySameDate() {
        DateTime one = new DateTime()
        List<DateTime> allTheSame = DateTime.getDatesDistributedEvenlyBetween(one, one, 10)
        
        assert one == allTheSame.get(8)
        assert one == allTheSame.get(2)
        assert one == allTheSame.get(7)
        assert one == allTheSame.get(0)
        assert one == allTheSame.get(9)
    }

    @Test
    void testDifferentMethodSameResults() {
        DateTime start = new DateTime(12, 10, 2020)
        DateTime end = new DateTime(11, 16, 2525)

        DateTime between1 = DateTime.getDateBetween(start, end)
        DateTime between2 = DateTime.getDatesDistributedEvenlyBetween(start, end, 1).get(0)

        assert between1 == between2
    }
}