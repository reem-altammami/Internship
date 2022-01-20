//package com.reem.internship.notification
//
//import android.app.Notification.DEFAULT_ALL
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.NotificationManager.IMPORTANCE_HIGH
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Context.NOTIFICATION_SERVICE
//import android.content.Intent
//import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
//import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
//import android.graphics.Color.RED
//import android.media.AudioAttributes
//import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
//import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
//import android.media.RingtoneManager.TYPE_NOTIFICATION
//import android.media.RingtoneManager.getDefaultUri
//import android.os.Build.VERSION.SDK_INT
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationCompat.PRIORITY_MAX
//import androidx.work.*
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.reem.internship.*
//import com.reem.internship.R
//import com.reem.internship.domainLayer.GetTrainingListWithBookMarksUseCase
//import kotlinx.coroutines.*
//import java.lang.Math.abs
//import java.lang.System.currentTimeMillis
//import java.text.SimpleDateFormat
//import java.time.Duration.between
//import java.time.Period.between
//import java.util.*
//import java.util.concurrent.TimeUnit
//import javax.xml.datatype.Duration
//
//object Id{
//    var i = 0
//    get() = field++
//}
//val sheardPrefrenceList = mutableListOf<List<Alerts>>()
//
//class AlertWorker(context : Context , params: WorkerParameters) : Worker(context , params) {
//
//    override fun doWork(): Result {
//        val currentTime = currentTimeMillis()
////
////        if ( 9000000 > currentTime)
////        Log.e("pref " , "${sheardPrefrenceList}")
//var id = Id.i
//       sendNotification(id)
//        return Result.success()
//    }
//
//    fun getallTrainingList():  List<TrainingItemUiState>{
//        val getProvider = GetTrainingListWithBookMarksUseCase(provideUserRepo(), provideCompaniesRepo())
//        var allTraining = emptyList<TrainingItemUiState>()
//        val getlist = GlobalScope.launch{
//            allTraining =  getProvider("","")}
//return allTraining
//    }
//
//    private fun getAlertListFromSharedPreference(context: Context): MutableList<Alerts> {
//        val sharedPreferences =
//             context.getSharedPreferences(SITTING_PREF, Context.MODE_PRIVATE)
//        val json = sharedPreferences?.getString(ALERTS_NAME, null)
//        val type = object : TypeToken<ArrayList<Alerts>>() {}.type
//        json?.let {
//            val alertList: ArrayList<Alerts> = Gson().fromJson(it, type)
//
//            return alertList
//        }
//
//        return arrayListOf()
//    }
//
//    fun getAlertList(){
//        val userList = getallTrainingList()
//        val prefList = getAlertListFromSharedPreference(context = context)
//        val newestTrainingList = mutableListOf<Alerts>()
//        for (item in userList){
//            if (isNewTraining(item.publishDate)){
//                newestTrainingList.add(Alerts(item.field,item.city))
//            }
//        }
//        for (newest in newestTrainingList){
//            for (prefItem in prefList)
//                if (newest.field == prefItem.field && newest.city == prefItem.city){}
//        }
//    }
//
//    fun isNewTraining (publishDate:String):Boolean{
//        val date = SimpleDateFormat("yyyy-MM-dd").parse(publishDate)
//        val formater = SimpleDateFormat("yyyy-MM-dd")
//        val currentDay = Date()
//        val difference: Long = abs(date.time - currentDay.time)
//        val differenceDates = difference / (24 * 60 * 60 * 1000)
//        val dayDifference = differenceDates.toInt()
//        return dayDifference >= 2
//    }
//
//
//    private fun sendNotification(id: Int) {
//        val intent = Intent(applicationContext , MainActivity::class.java)
//        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
//        intent.putExtra(NOTIFICATION_ID , id)
//
//        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        val titleNotification = applicationContext.getString(R.string.notiName)
//        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
//        val notification = NotificationCompat.Builder(applicationContext,NOTIFICATION_CHANNEL)
//            .setSmallIcon(R.mipmap.ic_launcher_foreground)
//            .setContentTitle(titleNotification)
//            .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)
//        notification.priority = PRIORITY_MAX
//
//        if (SDK_INT >= 26){
//            notification.setChannelId(NOTIFICATION_CHANNEL)
//
//            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
//            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
//                .setContentType(CONTENT_TYPE_SONIFICATION).build()
//
//            val channel =
//                NotificationChannel(NOTIFICATION_CHANNEL , NOTIFICATION_NAME , IMPORTANCE_HIGH)
//
//            channel.enableLights(true)
//            channel.lightColor = RED
//            channel.enableVibration(true)
//            channel.vibrationPattern = longArrayOf(100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400)
//            channel.setSound(ringtoneManager , audioAttributes)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(id , notification.build())
//
//    }
//
//
//    companion object {
//        const val NOTIFICATION_ID = "appName_notification_id"
//        const val NOTIFICATION_NAME = "appName"
//        const val NOTIFICATION_CHANNEL = "appName_channel_01"
//        const val NOTIFICATION_WORK = "appName_notification_work"
//    }
//
////    private fun sendNotification() {
////        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
////        val notification = NotificationCompat.Builder(applicationContext, channelID)
////            .setSmallIcon(R.drawable.ic_launcher_foreground)
////            .setContentTitle(titleExtra)
////            .setContentText(messageExtra)
////            .build()
////
////        notificationManager.notify(notificationID,notification)
////    }
//
//
//}
//
//
//const val notificationID = 1
//const val channelID = "channel1"
//const val titleExtra = "titleExtra"
//const val messageExtra = "messageExtra"
//const val NOTIFICATION_NAME = "appName"
//
//
//
//
