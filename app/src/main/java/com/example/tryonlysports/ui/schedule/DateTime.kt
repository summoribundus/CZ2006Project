package com.example.tryonlysports.ui.schedule


class DateTime (var year:Int,
                var month:Int,
                var day:Int,
                var hour:Int,
                var minute:Int){

    constructor(year:Int,month:Int,day:Int):this(year,month,day,0,0)

    override fun toString(): String {
        return day.toString()+"/"+month.toString()+"/"+
                year.toString()+"/"+hour.toString()+"/"+ minute.toString();
    }

    fun toDateString():String{
        return day.toString()+"/"+month.toString()+"/"+year.toString()
    }

    fun toTimeString():String{
        return hour.toString()+":"+minute.toString()
    }
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

    fun isEmpty():Boolean{
        return year==0 || month==0 || day==0
    }


}