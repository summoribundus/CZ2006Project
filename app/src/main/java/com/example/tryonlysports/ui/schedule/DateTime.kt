package com.example.tryonlysports.ui.schedule

/**
 * This class is used to record date and time related to an Activity
 * @see Activity
 * @author Wang Qiaochu
 */
class DateTime (var year:Int,
                var month:Int,
                var day:Int,
                var hour:Int,
                var minute:Int){

    constructor(year:Int,month:Int,day:Int):this(year,month,day,0,0)

    /**
     * This function is used when storing an activity into the database.
     */
    override fun toString(): String {
        return day.toString()+"/"+month.toString()+"/"+
                year.toString()+"/"+hour.toString()+"/"+ minute.toString();
    }

    /**
     * This function is used in AddActivity fragment to display the selected start date
     * or end date.
     */
    fun toDateString():String{
        return day.toString()+"/"+month.toString()+"/"+year.toString()
    }

    /**
     * This function is used in AddActivity fragment to display the selected start time
     * or end time.
     */
    fun toTimeString():String{
        return hour.toString()+":"+minute.toString()
    }

    /**
     * This function is used to compare two DateTime objects
     *
     * @param other the other DateTime to be compared with this.
     * @return True if this is later than other, false if they are the same or this is
     * earlier than other.
     *
     */
    fun biggerThan(other:DateTime):Boolean{
        if(year<other.year) return false
        else if(year>other.year) return true
        else if(month<other.month) return false
        else if(month>other.month) return true
        else if(day<other.day) return false
        else if(day>other.day) return true
        else if(hour< other.hour) return false
        else if(hour>other.hour) return true
        return minute>other.minute
    }

    /**
     * This function is used to check that the fields related to date time selection is filled.
     * @return true if date is not selected.
     */
    fun isEmpty():Boolean{
        return year==0 || month==0 || day==0
    }


}