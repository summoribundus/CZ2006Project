package com.example.tryonlysports.ui.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ActivityList : ViewModel (){
    private val _selected = MutableLiveData<Int>()
    private lateinit var db:FirebaseFirestore
    private val _loadFinished=MutableLiveData<Boolean>()
    private lateinit var collectionRef:CollectionReference
    val loadFinished:LiveData<Boolean>
        get()=_loadFinished
    val selected: LiveData<Int> = _selected
    private var user:String=""

    private val _activities=MutableLiveData<MutableList<Activity>>()
    val activities:LiveData<MutableList<Activity>> = _activities
    fun retrieveActivities(){
        _activities.value= mutableListOf<Activity>()
        db= Firebase.firestore
        collectionRef=db.collection("schedule")
                .document(user)
                .collection("activities")
        collectionRef
                .whereEqualTo("completed",false)
                .get()
                .addOnSuccessListener {d ->
                    if(d == null){
                        println("empty")

                    }
                    for(doc in d){
                        _activities.value!!.add(toActivity(doc.data,doc.id))

                    }
                    _loadFinished.value=true
                }
    }

    init{
        _loadFinished.value=false
        if(!user.isNullOrEmpty())
            retrieveActivities()
        _selected.value=-1
    }

    fun setUser(s:String){
        user=s
    }

    fun emptyUser():Boolean{
        return user.isNullOrEmpty();
    }
    fun getActivityData():MutableList<Activity>?{
        return _activities.value
    }

    fun updateActivities(id:Int, newActivity:Activity){
        if(id==-1) {

            val doc=collectionRef.document()
            doc.set(toHashMap(newActivity))
            doc.update("id",doc.id)
            newActivity.id=doc.id
            _activities.value?.add(newActivity)
        }
        else{
            _activities.value?.set(id,newActivity)
            collectionRef.document(newActivity.id).set(toHashMap(newActivity))
        }
    }

    fun deleteActivity(id: Int){
        val docid=_activities.value!![id].id
        _activities.value!!.removeAt(id)
        collectionRef.document(docid).delete()

    }

    fun completeActivity(id:Int){

        _activities.value!![id].complete=true
        val docid=_activities.value!![id].id
        collectionRef.document(docid).update("completed",true)
        _activities.value!!.removeAt(id)
    }

    fun getSelected():Activity{
        return _activities.value!![_selected.value!!]
    }

    fun select(id:Int){
        _selected.value=id
        println(selected)
    }
    override fun onCleared() {
        super.onCleared()
    }

    private fun toHashMap(a:Activity):HashMap<Any,Any>{
        return hashMapOf(
            "startTime" to a.start.toString(),
            "endTime" to a.end.toString(),
            "description" to a.description,
            "location" to a.location,
            "isOutdoor" to a.isOutdoor,
            "completed" to a.complete,
            "id" to a.id
        )
    }

    private fun toActivity(map:Map<String, Any>,id:String):Activity{
        val start=stringToDateTime(map.get("startTime") as String)
        val end=stringToDateTime(map.get("endTime") as String)
        val location=map.get("location") as String
        val description=map.get("description") as String
        val outdoor=map.get("isOutdoor") as Boolean
        val completed=map.get("completed") as Boolean
        val id=map.get("id") as String
        return Activity(description,location,start,end,outdoor,completed,id)

    }

    private fun stringToDateTime(str:String):DateTime{
        val nums=str.split("/").map { it.toInt() }.toTypedArray();
        val dateTime=DateTime(nums[2],nums[1],nums[0],nums[3],nums[4])
        return dateTime;
    }

}