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

/**
 * This class is the ViewModel used for AddActivity and ActivityList fragment.
 * It stores the existing activities and retrieves activities from database and
 * update the database.
 *
 * @property _selected set by the ActivityList fragment indicating whether the AddActivity
 * fragment is used for adding new activity or editing activity. The value is set to -1 in
 * the former case and to the index of the edited activity in the latter.
 * @property selected expose the setValue and getValue methods of _selected.
 * @property db The database that the ViewModel gets data from
 * @property _loadFinished The indicator variable of whether the ViewModel has finished retrieving
 * data from the database. It is used to inform the adapter of the data change.
 * @property loadFinished expose the setValue and getValue methods of _loadFinished.
 * @property user the id of the current user.
 * @property _activities list of Activity retrieved from the database.
 * @property activities expose the setValue and getValue methods of _activities.
 * @author Wang Qiaochu
 */
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

    /**
     * The function to retrieve activities from the database.
     */
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

    /**
     * Initialisation of ViewModel
     */
    init{
        _loadFinished.value=false
        if(!user.isNullOrEmpty())
            retrieveActivities()
        _selected.value=-1
    }

    /**
     * Set the current user
     */
    fun setUser(s:String){
        user=s
    }

    /**
     * The function to check whether the user is set.
     * @return true if the user is not set.
     */
    fun emptyUser():Boolean{
        return user.isNullOrEmpty();
    }


    /**
     * The function to update an existing activity.
     *
     * @param id The position at which the activity is to be updated, if id=-1, then add
     * a new activity to activities.
     * @param newActivity The Activity object containing the new value of the activity.
     */
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

    /**
     * This function deletes an activity in activities and also delete it in database
     *
     * @param id the position of the target activity
     */
    fun deleteActivity(id: Int){
        val docid=_activities.value!![id].id
        _activities.value!!.removeAt(id)
        collectionRef.document(docid).delete()

    }

    /**
     * This function deletes an activity from activities and
     * change the completed value to true.
     *
     * @param id the position of the target activity
     */
    fun completeActivity(id:Int){

        _activities.value!![id].complete=true
        val docid=_activities.value!![id].id
        collectionRef.document(docid).update("completed",true)
        _activities.value!!.removeAt(id)
    }

    /**
     * This function returns the activity that the user selected to edit.
     *
     * @return Activity object to be edited.
     */
    fun getSelected():Activity{
        return _activities.value!![_selected.value!!]
    }

    /**
     * This function sets the selected value.
     */
    fun select(id:Int){
        _selected.value=id
        println(selected)
    }
    override fun onCleared() {
        super.onCleared()
    }

    /**
     * This function is used to convert activity to HashMap in order to store it in the database
     *
     * @param a the Activity to convert
     * @return a HashMap containing the attributes of the activity
     */
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

    /**
     * This function is used to convert HashMap retrieved from database to Activity.
     *
     * @param map a HashMap containing the attributes of the activity
     * @param id the auto-generated id of the activity
     * @return The activity corresponding to the HashMap
     */
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

    /**
     * This is used to convert the date time from database to DateTime
     * @see DateTime
     *
     * @param str The string representing date time
     * @return DateTime object corresponding to the string
     */
    private fun stringToDateTime(str:String):DateTime{
        val nums=str.split("/").map { it.toInt() }.toTypedArray();
        val dateTime=DateTime(nums[2],nums[1],nums[0],nums[3],nums[4])
        return dateTime;
    }

}