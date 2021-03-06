package co.vector.itube;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.google.android.youtube.player.YouTubePlayer;
import com.splunk.mint.Mint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by hasanali on 27/08/14.
 */

public class BaseClass extends Application {
    /**
     * Init preferences
     */
    private  final int MAX_ATTEMPTS = 5;
    private  final int BACKOFF_MILLI_SECONDS = 2000;
    private  final Random random = new Random();
    //////////////////////////////////////////////////////////////////////////////
    Calendar newAlarmTime;
    AlarmManager alarmManager;Intent intentAlarm;
    YouTubePlayer youTubePlayer;
    public SharedPreferences appSharedPrefs;
    public SharedPreferences.Editor prefsEditor;
    private int Position = 0;
    public String SHARED_NAME = "co.vector.farseireader";
    public static final String KEY = "AIzaSyBTarcxurznZvnN9kZW9ekWe7JyQsLSLCo";
    //Shared preferences tags
    private String GCM_ID = "";
    private String IsFromFavorites = "IsFromFavorites";
    private String AUTH_TOKEN = "auth_token";
    private int DurationCounter = 0;
    private String Email = "Email";
    private String Category = "Category";
    private String NewCategory = "NewCategory";
    private String Duration = "Duration";
    private String Language = "English";
    private String VideoId = "VideoId";
    private String VideoTitle = "VideoTitle";
    private String VideoDuration = "VideoDuration";
    private String DataBy = "DataBy";
    private long CheckDuration = 0L;
    private String VideoUploadDate = "VideoUploadDate";
    private String VideoAuthor = "VideoAuthor";
    private String VideoPlayerlink = "VideoPlayerlink";
    private String VideoViewer = "VideoViewer";
    private String VideoThumbnail = "VideoThumbnail";
    private ArrayList<String> SongsList = new ArrayList<String>();
    private ArrayList<String> SaveList = new ArrayList<String>();
    ArrayList<SpecifiedKeyData> specifiedKeyData;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        /* initialize preferences for future use */
        this.appSharedPrefs = getSharedPreferences(SHARED_NAME,
                Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        specifiedKeyData = SetSpecifiedData();
        newAlarmTime = Calendar.getInstance();
        alarmManager = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        intentAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);

        alarmManager.set(AlarmManager.RTC_WAKEUP, newAlarmTime
                .getTimeInMillis() + 5*24*60*60*1000, PendingIntent
                .getBroadcast(getApplicationContext(), 1, intentAlarm,
                        PendingIntent.FLAG_UPDATE_CURRENT));
        Mint.initAndStartSession(this, "6aa8e202");

    }

    public static boolean isTabletDevice(Context activityContext) {
        boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);

        if (xlarge) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            }
        }
        return false;
    }
    public String TimeAgo (long time){

        List<Long> times = Arrays.asList(
                TimeUnit.DAYS.toMillis(365),
                TimeUnit.DAYS.toMillis(30),
                TimeUnit.DAYS.toMillis(1),
                TimeUnit.HOURS.toMillis(1),
                TimeUnit.MINUTES.toMillis(1),
                TimeUnit.SECONDS.toMillis(1));
        List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");
        return  toDuration(time,times,timesString);
    }
    public String toDuration(long duration,List<Long> times,List<String> timesString) {

        StringBuffer res = new StringBuffer();
        for (int i = 0; i < times.size(); i++) {
            Long current = times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                res.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if ("".equals(res.toString()))
            return "0 second ago";
        else
            return res.toString();
    }
    public static Long getTimeStampToMilli(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date obj = sdf.parse(date);
            return obj.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }
    public static int getDpValue(int val, Context ctx){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, val, ctx.getResources()
                        .getDisplayMetrics());
    }
    public void setDurationCounter(int checkDuration){
        DurationCounter= checkDuration;
    }
    public int getDurationCounter(){
        return DurationCounter;
    }

    public void setAUTH_TOKEN(String uuid){
        prefsEditor.putString(AUTH_TOKEN, uuid).commit();
    }
    public String getAUTH_TOKEN(){
        return appSharedPrefs.getString(AUTH_TOKEN, "");
    }

    public void setEmail(String email){
        prefsEditor.putString(Email, email).commit();
    }
    public String getEmail(){
        return appSharedPrefs.getString(Email, "");
    }

    public void setGCM_Key(String gcmKey){
        prefsEditor.putString(GCM_ID, gcmKey).commit();
    }
    public String getGCM_Key(){
        return appSharedPrefs.getString(GCM_ID, "");
    }

    public void setDuration(String duration){
        prefsEditor.putString(Duration, duration).commit();
    }
    public String getDuration(){
        return appSharedPrefs.getString(Duration, "");
    }

    public void setSaveList(ArrayList save){
        this.SaveList= save;
    }
    public ArrayList getSaveList(){
        return SaveList;
    }

    public void setArrayList(ArrayList list){
        this.SongsList= list;
    }
    public ArrayList getArrayList(){
        return SongsList;
    }

    public void setIsFromFavorites(String favorites){
        this.IsFromFavorites= favorites;
    }
    public String getIsFromFavorites(){
        return IsFromFavorites;
    }

    public void setDataBy(String dataBy){
        this.DataBy= dataBy;
    }
    public String getDataBy(){
        return DataBy;
    }

    public void setCheckDuration(long checkDuration){
        this.CheckDuration= checkDuration;
    }
    public long getCheckDuration(){
        return CheckDuration;
    }


    public void setLanguage(String language){
        prefsEditor.putString(Language, language).commit();
    }
    public String getLanguage(){
        return appSharedPrefs.getString(Language, "");
    }

    public void setCategory(String category){
        prefsEditor.putString(Category, category).commit();
    }
    public String getCategory(){
        return appSharedPrefs.getString(Category, "");
    }

    public void setVideoAuthor(String videoAuthor){
        prefsEditor.putString(VideoAuthor, videoAuthor).commit();
    }
    public String getVideoAuthor(){
        return appSharedPrefs.getString(VideoAuthor, "");
    }

    public void setNewCategory(String newcategory){
        prefsEditor.putString(NewCategory, newcategory).commit();
    }
    public String getNewCategory(){
        return appSharedPrefs.getString(NewCategory, "");
    }

    public void setVideoThumbnail(String thumbnail){
        prefsEditor.putString(VideoThumbnail, thumbnail).commit();
    }
    public String getVideoThumbnail(){
        return appSharedPrefs.getString(VideoThumbnail, "");
    }

    public void setVideoId(String videoid){
        prefsEditor.putString(VideoId, videoid).commit();
    }
    public String getVideoId(){
        return appSharedPrefs.getString(VideoId, "");
    }

    public void setVideoTitle(String videotitle){
        prefsEditor.putString(VideoTitle, videotitle).commit();
    }
    public String getVideoTitle(){
        return appSharedPrefs.getString(VideoTitle, "");
    }

    public void setVideoDuraion(String videoduration){
        prefsEditor.putString(VideoDuration, videoduration).commit();
    }
    public String getVideoDurtion(){
        return appSharedPrefs.getString(VideoDuration, "");
    }

    public void setVideoPlayerLink(String videolink){
        prefsEditor.putString(VideoPlayerlink, videolink).commit();
    }
    public String getVideoUploadDate(){
        return appSharedPrefs.getString(VideoUploadDate, "");
    }

    public void setVideoUploadDate(String videoUploadDate){
        prefsEditor.putString(VideoUploadDate, videoUploadDate).commit();
    }
    public String getVideoPlayerLink(){
        return appSharedPrefs.getString(VideoPlayerlink, "");
    }

    public void setVideoViewer(String videoviewer){
        prefsEditor.putString(VideoViewer, videoviewer).commit();
    }
    public String getVideoViewer(){
        return appSharedPrefs.getString(VideoViewer, "");
    }

    public void setPosition(int position){
        this.Position= position;
    }
    public int getPosition(){
        return Position;
    }

    public void setYOUTUBE(YouTubePlayer view){
        this.youTubePlayer= view;
    }
    public YouTubePlayer getYOUTUBE(){
        return youTubePlayer;
    }

    public void clearSharedPrefs(){
        prefsEditor.clear().commit();
    }

    public ArrayList<SpecifiedKeyData> SetSpecifiedData()
    {
        SpecifiedKeyData specifiedKeyData;
        ArrayList<SpecifiedKeyData> itemsArrayList = new ArrayList<SpecifiedKeyData>();

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("Iranian");
        specifiedKeyData.setMovie("فیلم سینمایی");
        specifiedKeyData.setCartons("Farsi Cartoons");
        specifiedKeyData.setMusic("موزيك ايرانى");
        specifiedKeyData.setDocumentries("مستند فارسى");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("Arabic");
        specifiedKeyData.setMovie("افلام");
        specifiedKeyData.setCartons("كارتون عربى");
        specifiedKeyData.setMusic(" موزيك عربى");
        specifiedKeyData.setDocumentries("فيلم وثائقي");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("Hindi");
        specifiedKeyData.setMovie("Hindi Movies");
        specifiedKeyData.setCartons("Hindi Cartoons");
        specifiedKeyData.setMusic("Hindi Songs");
        specifiedKeyData.setDocumentries("Hindi Documentary");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("Turkish");
        specifiedKeyData.setMovie("Türk Fılmı");
        specifiedKeyData.setCartons("çizgi film");
        specifiedKeyData.setMusic("Turkish Music");
        specifiedKeyData.setDocumentries("Belgesel");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("Spanish");
        specifiedKeyData.setMovie("películas");
        specifiedKeyData.setCartons("dibujos");
        specifiedKeyData.setMusic("Música");
        specifiedKeyData.setDocumentries("documental");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("English");
        specifiedKeyData.setMovie("English Movies");
        specifiedKeyData.setCartons("English Cartoons");
        specifiedKeyData.setMusic("English Music");
        specifiedKeyData.setDocumentries("English Documentary");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("German");
        specifiedKeyData.setMovie("Film auf deutsch");
        specifiedKeyData.setCartons("Zeichentrick Filme ");
        specifiedKeyData.setMusic("Musik");
        specifiedKeyData.setDocumentries("Dokumentation");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("French");
        specifiedKeyData.setMovie(" Film de action");
        specifiedKeyData.setCartons("Dessins animés");
        specifiedKeyData.setMusic("Musique");
        specifiedKeyData.setDocumentries("Documentaire");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("Italian");
        specifiedKeyData.setMovie("Film Italiano");
        specifiedKeyData.setCartons(" Cartoni animati");
        specifiedKeyData.setMusic("Musica");
        specifiedKeyData.setDocumentries("Documentario");
        itemsArrayList.add(specifiedKeyData);

        specifiedKeyData= new SpecifiedKeyData();
        specifiedKeyData.setLanguage("Russian");
        specifiedKeyData.setMovie("Российские фильмы");
        specifiedKeyData.setCartons("Мультфильмы");
        specifiedKeyData.setMusic("Russian folk music");
        specifiedKeyData.setDocumentries("Документальные фильмы");
        itemsArrayList.add(specifiedKeyData);
        return  itemsArrayList;
    }
    // Checking for all possible internet providers
    public boolean isConnectingToInternet(){

        ConnectivityManager connectivity =
                (ConnectivityManager) getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    // Notifies UI to display a message.
    void displayMessageOnScreen(Context context, String message) {

        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.EXTRA_MESSAGE, message);

        // Send Broadcast to Broadcast receiver with message
        context.sendBroadcast(intent);

    }


    //Function to display simple Alert Dialog
    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Set Dialog Title
        alertDialog.setTitle(title);

        // Set Dialog Message
        alertDialog.setMessage(message);

        if(status != null)
            // Set alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Set OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Show Alert Message
        alertDialog.show();
    }

    private PowerManager.WakeLock wakeLock;

    public  void acquireWakeLock(Context context) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock");

        wakeLock.acquire();
    }

    public  void releaseWakeLock() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }

}
