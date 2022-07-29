package com.hsck.tzapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

public class MainActivity extends AppCompatActivity {
    static String interneturl="http://207.148.17.218:8096/gethost?webname=itdknet";
    static String internetip="";
    static boolean intergetfinish=false;
    static String tryapi="";//用于尝试看网址是否可以连接
    static String getnetcontent="";
    private int time = 1000 * 5;//周期时间
    private String localip="192.168.3.66";
    private Timer timer = new Timer();
    Boolean timerdoing=true;
    private Button btn_open;
    private Button btn_savefile;
    private WebView webView;
    private TextView label_wordcount;
    Handler handler;
    long timeri=0;//计时器，计算定时器已经运行的时间，同时用于计算定时任务的间隔
    String toshowmsg="";
    String finaurl="";
    MediaPlayer mediaPlayer = null;
    Vibrator vibrator =null;
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //权限判断，如果没有权限就请求权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        label_wordcount = findViewById(R.id.label_wordcount);
        intergetfinish=false;
        //网络连接的必须要放在线程里面，否则getInputStream会报null
        new Thread(new Runnable() {
            @Override
            public void run() {
                internetip = new webdo().gethtmlfromurl(interneturl, "GET");
                intergetfinish=true;
            }
        }).start();
        while(!intergetfinish){

        }
        //Log.i("tzapp","获得的网址："+internetip+","+internetip.length());
        /*AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("确认" ) ;
        builder.setMessage("获得的网址："+internetip+","+internetip.length() ) ;
        builder.setPositiveButton("是" ,  null );
        builder.show();*/
        if(internetip.length()>0){
            //internetip=internetip.substring(0,internetip.indexOf(":"));
            internetip=internetip.trim();
            tryapi="";
            intergetfinish=false;
            //网络连接的必须要放在线程里面，否则getInputStream会报null
            new Thread(new Runnable() {
                @Override
                public void run() {
                    tryapi = new webdo().gethtmlfromurl("http://"+internetip+":8095/gethost?webname=itdknet", "GET");
                    intergetfinish=true;
                }
            }).start();
            while(!intergetfinish){

            }
            if(tryapi.length()==0){
                internetip=localip;
            }
        }else{
            internetip=localip;
        }
        if(internetip.equals(localip)){
            tryapi="";
            intergetfinish=false;
            //网络连接的必须要放在线程里面，否则getInputStream会报null
            new Thread(new Runnable() {
                @Override
                public void run() {
                    tryapi = new webdo().gethtmlfromurl("http://"+internetip+":8095/gethost?webname=itdknet", "GET");
                    intergetfinish=true;
                }
            }).start();
            while(!intergetfinish){

            }
            if(tryapi.length()==0){
                AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("确认" ) ;
                builder.setMessage("网上"+internetip+"和本地"+localip+"的服务都没有办法连上") ;
                builder.setPositiveButton("是" ,  null );
                builder.show();
                internetip="";
            }
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    if(toshowmsg.indexOf("触发临界值警报")>-1) {
                        //开启音乐
                        //File file=new File(Environment.getExternalStorageDirectory()+File.separator+"zhwhcjljz.mp3");
                        File file = new File(Environment.getExternalStorageDirectory(), "zhwhcjljz.mp3");
                        if (file.exists()) {

                            try {
                                Log.v("tzapp", "play");
                                mediaPlayer = null;
                                vibrator = null;
                                vibrator = (Vibrator)getBaseContext().getSystemService(getBaseContext().VIBRATOR_SERVICE);
                                if(vibrator.hasVibrator()) {
                                    vibrator.vibrate(1000);
                                }
                                //这里使用的是一个长整型数组，数组的a[0]表示静止的时间，a[1]代表的是震动的时间，然后数组的a[2]表示静止的时间，a[3]代表的是震动的时间……依次类推下去
                                //long[] patter = {1000, 500, 1000, 50};
                                //vibrator.vibrate(patter, 0);
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.pause();
                                mediaPlayer.setDataSource(file.getPath());//指定音频文件路径
                                //mediaPlayer.setLooping(true);//设置为循环播放
                                mediaPlayer.prepare();//初始化播放器MediaPlayer
                                mediaPlayer.start();
                                //vibrator.cancel();
                            } catch (Exception e) {

                            }
                        }

                    }
                    if (label_wordcount != null) {
                        label_wordcount.setMovementMethod(ScrollingMovementMethod.getInstance());
                        label_wordcount.setText(toshowmsg);
                    }
                }
            }
        };

        if(internetip.length()>0) {
            setTitle ("投资工具："+internetip);


            //定时任务
            timerdoing=false;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!timerdoing) {
                        //Log.v("ust", "timework");
                        timerdoing = true;
                        timeri = timeri + 1;

                        //去拉取汇率数据进行计算
                        getnetcontent="";
                        getnetcontent = new webdo().gethtmlfromurl("http://" + internetip + ":8095/gethlandala", "GET");
                        //if(getnetcontent.length()>0) {
                            //internetip=internetip.substring(0,internetip.indexOf(":"));
                            getnetcontent = getnetcontent.trim();
                            //label_wordcount.setText(getnetcontent);
                            toshowmsg=getnetcontent;
                            Message msg=new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        //}
                        timerdoing=false;
                    }
                }
            };
            timer.schedule(timerTask, 0, time);//周期时间


            finaurl = "http://" + internetip + ":8095/whtzjk";

            /*//Log.i("tzapp","最终网址："+finaurl);

            //获得控件
            WebView webView = (WebView) findViewById(R.id.wv_webview);
            //访问网页
            webView.loadUrl(finaurl);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);//允许自动播放音乐，而不需要用户进行交互

            webView.getSettings().setAllowContentAccess(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setOffscreenPreRaster (true);//当WebView切换到后台但仍然与窗口关联时是否raster tiles，打开它可以避免在WebView从后台切换到前台时重新绘制，默认值false

            //webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //使用WebView加载显示url
                    view.loadUrl(url);
                    //返回true
                    return true;
                }
            });*/
        }
        btn_open = findViewById(R.id.btn_loadfile);
        btn_savefile = findViewById(R.id.btn_savefile);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label_wordcount.setVisibility(View.GONE);
                //label_wordcount=null;
                //Log.i("tzapp","最终网址："+finaurl);

                //获得控件
                webView = (WebView) findViewById(R.id.wv_webview);
                webView.setVisibility(View.VISIBLE);
                //访问网页
                webView.loadUrl(finaurl);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setSupportZoom(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDisplayZoomControls(false);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setDatabaseEnabled(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setMediaPlaybackRequiresUserGesture(false);//允许自动播放音乐，而不需要用户进行交互

                webView.getSettings().setAllowContentAccess(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setAllowFileAccessFromFileURLs(true);
                webView.getSettings().setOffscreenPreRaster (true);//当WebView切换到后台但仍然与窗口关联时是否raster tiles，打开它可以避免在WebView从后台切换到前台时重新绘制，默认值false

                //webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //使用WebView加载显示url
                        view.loadUrl(url);
                        //返回true
                        return true;
                    }
                });
            }
        });
        btn_savefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.setVisibility(View.GONE);
                //webView.destroy();
                //webView = null;
                label_wordcount = findViewById(R.id.label_wordcount);
                label_wordcount.setVisibility(View.VISIBLE);
            }
        });
    }
}
