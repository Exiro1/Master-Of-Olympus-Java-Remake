package com.exiro.systemCore;

import com.exiro.utils.Time;

public class TimeManager {


    int year, month, day, yearoffset = -984;
    double t;

    String[] months = {"JAN", "FEV", "MAR", "AVR", "MAI", "JUN", "JUL", "AOU", "SEP", "OCT", "NOV", "DEC"};

    public TimeManager(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.t = 0;
    }

    public int daysSince(Time t) {
        int tot1 = t.getYear() * 12 * 30 + t.getMonth() * 30 + t.getDay();
        int tot2 = this.year * 12 * 30 + this.month * 30 + this.day;
        return tot2 - tot1;
    }

    public boolean timeHasPassed(int year, int month, int day) {
        int tot1 = year * 12 * 30 + month * 30 + day;
        int tot2 = this.year * 12 * 30 + this.month * 30 + this.day;
        return tot1 < tot2;
    }

    public boolean timeHasPassed(Time time) {
        return timeHasPassed(time.getYear(), time.getMonth(), time.getDay());
    }

    public Time getTime() {
        return new Time(year, month, day);
    }

    public Time getFutureTime(int year, int month, int day) {
        int newday = 0, newmonth = 0, newyear = year + this.year;
        if (day + this.day >= 30) {
            newday = this.day + day - 30;
            newmonth++;
        } else {
            newday = this.day + day;
        }
        if (newmonth + month + this.month >= 12) {
            newmonth = newmonth + month + this.month - 12;
            newyear++;
        } else {
            newmonth = month + newmonth + this.month;
        }
        return new Time(newyear, newmonth, newday);
    }

    public int updateTime(double delta) {
        int ds = 0;
        t += delta;
        while (t > 1) {
            t = t - 1;
            day++;
            ds++;
            if (day >= 30) {
                day = 0;
                month++;
                if (month >= 12) {
                    month = 0;
                    year++;
                }
            }
        }
        return ds;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @Override
    public String toString() {
        if (year + yearoffset < 0)
            return months[month] + "  " + (-(year + yearoffset)) + " AV. J.-C.";
        if (year + yearoffset >= 0)
            return months[month] + "  " + (year + yearoffset) + " AP. J.-C.";
        return "";
    }
}
