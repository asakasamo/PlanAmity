package test

import data.DateTime
import data.Entry
import data.Project
import org.junit.Test

/**
 * @author Al-John
 */
class ProjectTest extends GroovyTestCase {

    @Test
    void testAddNewEntryToEmptyProject() {
        Project project = new Project("Test", new DateTime(1, 1, 2000), new DateTime(1, 31, 2000))
        Entry first = project.addNewEntry("TestEntry")

        assertSame project.getEntry(0), first
        assert first.getStart() == new DateTime(1, 16, 2000)
    }

    @Test
    void testAddEntryAfter() {
        Project project = new Project("Test", new DateTime(1, 1, 2000), new DateTime(1, 31, 2000))
        Entry first = project.addNewEntry("First")
        Entry second = project.addNewEntryAfter(first, "second")

        assert DateTime.minutesBetween(project.getStart(), first.getStart()) ==
                DateTime.minutesBetween(first.getStart(), second.getStart())
    }

    @Test
    void testAddABunchOfNewEntriesToEmptyProject() {
        Project project = new Project("Test", new DateTime(1, 1, 2000), new DateTime(1, 31, 2000))

        Entry first = project.addNewEntry("first")
        Entry second = project.addNewEntryAfter(first, "second")
        Entry third = project.addNewEntryAfter(second, "third")
        Entry fourth  = project.addNewEntryAfter(third, "fourth")
        Entry fifth = project.addNewEntryAfter(fourth, "fifth")

        int startTo1 = DateTime.minutesBetween(project.getStart(), first.getStart())
        int betw12 = DateTime.minutesBetween(first.getStart(), second.getStart())
        int betw23 = DateTime.minutesBetween(second.getStart(), third.getStart())
        int betw34 = DateTime.minutesBetween(third.getStart(), fourth.getStart())
        int betw45 = DateTime.minutesBetween(fourth.getStart(), fifth.getStart())
        int fifthToEnd = DateTime.minutesBetween(fifth.getStart(), project.getEnd())

        List<Integer> timesBetween = new ArrayList<>();

        timesBetween.addAll(0, startTo1, betw12, betw23, betw34, betw45, fifthToEnd)

        for(int i = 0; i < timesBetween.size() -1; i++)
            assert timesBetween.get(i) == timesBetween.get(i+1)
    }

    @Test
    void testAddEntryBetweenNonPointEntries() {
        Project project = new Project("Test", new DateTime(4,2,2015), new DateTime(4,20,2015))
        Entry longEntry = project.addNewEntry("Long Entry")
        longEntry.setDurationInDays(5)

        DateTime originalStart = longEntry.getStart().clone()

        Entry pointEntry = project.addNewEntryAfter(longEntry, "Point Entry")

        assert longEntry.getStart() == originalStart

        DateTime between = DateTime.getDateBetween(longEntry.getEnd(), project.getEnd())
        assert between == pointEntry.getStart()
    }

    @Test
    void testGetGroupOfSinglePointEntries() {
        Project project = new Project("Test", new DateTime(1,2,2013), new DateTime(1,2,2015))
        Entry point1 = project.addNewEntry("Point 1")
        Entry point2 = project.addNewEntry("Point 2")
        Entry point3 = project.addNewEntryAfter(point1, "Point 3")
        Entry point4 = project.addNewEntryAfter(point1, "Point 4")

        assert project.getGroupOfSinglePointEntries(point4).size() == 4
        assert project.getGroupOfSinglePointEntries(point2).size() == 4
        assert project.getGroupOfSinglePointEntries(point1).size() == 4
        assert project.getGroupOfSinglePointEntries(point3).size() == 4
    }

    @Test
    void testGetSinglePointEntriesBetweenTwoLongEntries() {
        Project project = new Project("Test", new DateTime(1,2,2013), new DateTime(1,2,2015))
        Entry longEntry1 = project.addNewEntry("Long Entry 1")
        Entry longEntry2 = project.addNewEntryAfter(longEntry1, "Long Entry 2")
        longEntry1.setDurationInDays(20)
        longEntry2.setDurationInDays(20)

        Entry point1 = project.addNewEntryAfter(longEntry1, "Point 1")
        Entry point2 = project.addNewEntryAfter(longEntry1, "Point 2")
        Entry point3 = project.addNewEntryAfter(longEntry1, "Point 3")
        Entry point4 = project.addNewEntryAfter(longEntry1, "Point 4")

        assert project.getGroupOfSinglePointEntries(point4).size() == 4
        assert project.getGroupOfSinglePointEntries(point2).size() == 4
        assert project.getGroupOfSinglePointEntries(point1).size() == 4
        assert project.getGroupOfSinglePointEntries(point3).size() == 4
        assert project.getGroupOfSinglePointEntries(longEntry1).size() == 4
        assert project.getGroupOfSinglePointEntries(longEntry2).size() == 4
    }

    @Test
    void testSequenceOfEvents() {
        Project project = new Project("Test", new DateTime(6,6,2016), new DateTime(8,8,2018))
        Entry first = project.addNewEntry("First")
        Entry second = project.addNewEntry("Second")
        Entry third = project.addNewEntry("Third")
        Entry fourth = project.addNewEntry("Fourth")

        /*

        START ----- first ----- second ----- third ----- fourth ----- END

        */

        first.setDurationInDays(1)
        third.setDurationInDays(1)

        /*

        START ----- [first] ----- second ----- [third] ----- fourth ----- END

        */

        int test5Dashes = DateTime.minutesBetween(project.start, first.getStart())

        Entry e1_2 = project.addNewEntryAfter(first, "1_2")
        Entry e1_1 = project.addNewEntryAfter(first, "1_1")

        /*

        START ----- [first] -- 1_1 -- 1_2 -- second -- [third] ----- fourth ----- END

        */

        Entry fifth = project.addNewEntryAfter(fourth, "fifth")
        Entry e4_1 = project.addNewEntryAfter(fourth, "4_1")

        /*

        START ----- [first] -- 1_1 -- 1_2 -- second -- [third] - fourth - 4_1 - fifth - END

        */

        fourth.setDurationInDays(1)
        fifth.setDurationInDays(1)

        /*

        START ----- [first] -- 1_1 -- 1_2 -- second -- [third] - [fourth] - 4_1 - [fifth] - END

        */

        Entry e4_2 = project.addNewEntryAfter(e4_1, "4_2")

        /*

        START ----- [first] -- 1_1 -- 1_2 -- second -- [third] - [fourth] ~ 4_1 ~ 4_2 ~ [fifth] - END

        Good Luck

        */

        assert DateTime.minutesBetween(project.getStart(), first.getStart()) == test5Dashes

        int test2Dashes = DateTime.minutesBetween(first.getEnd(), e1_1.getStart())
        assert DateTime.minutesBetween(e1_1.getStart(), e1_2.getStart()) == test2Dashes
        assert DateTime.minutesBetween(e1_2.getStart(), second.getStart()) == test2Dashes
        assert DateTime.minutesBetween(second.getStart(), third.getStart()) == test2Dashes

        int test1Dash = DateTime.minutesBetween(third.getEnd(), fourth.getStart())
        assert DateTime.minutesBetween(fifth.getStart(), project.getEnd()) == test1Dash

        int testTilde = DateTime.minutesBetween(fourth.getEnd(), e4_1.getStart())
        assert DateTime.minutesBetween(e4_1.getStart(), e4_2.getStart()) == testTilde
        assert DateTime.minutesBetween(e4_2.getStart(), fifth.getStart()) == testTilde
    }

    @Test
    void testSwapEntriesFarApart() {
        Project project = new Project("Test Project", new DateTime(3,6,2009), new DateTime(9,12,2018))
        Entry e1 = project.addNewEntry("First Entry")
        Entry e2 = project.addNewEntry("Second Entry")

        e1.setDurationInDays(100)
        e2.setDurationInDays(20)

        /**
         * Swaps two entries, as such:
         * - The gap between the two entries is preserved.
         *
         *   123456789ABCDEFGHI       123456789ABCDEFGHI
         *   --[-----]---{--}--  >>>  --{--}---[-----]--
         *     X     Y   x  y           x  y   X     Y
         *
         * - The start of the 2nd entry becomes the start of the first entry
         * - The start of the 1st entry becomes the end of the 2nd entry + the gap
         */

        //left side
        DateTime _3 = e1.getStart()
        DateTime _9 = e1.getEnd()
        DateTime _D = e2.getStart()
        DateTime _G = e2.getEnd()

        project.swapEntries(e1, e2)

        //right side
        DateTime x = e2.getStart()
        DateTime y = e2.getEnd()
        DateTime X = e1.getStart()
        DateTime Y = e1.getEnd()

        assert x == _3
        assert Y == _G
        assert DateTime.minutesBetween(_9, _D) == DateTime.minutesBetween(y, X)
    }

    void testSwapEntriesNoChange() {
        Project project = new Project("Test", new DateTime(), new DateTime())
        Entry entry = project.addNewEntry("One")

        DateTime start = entry.getStart()
        DateTime end = entry.getEnd()

        project.swapEntries(entry, entry)

        assert entry.getStart() == start
        assert entry.getEnd() == end
    }
}