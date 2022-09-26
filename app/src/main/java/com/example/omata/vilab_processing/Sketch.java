package com.example.omata.vilab_processing;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import ketai.ui.KetaiGesture;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Sketch extends PApplet {
    Activity activity;
    PImage pimg = null;
    String [] requestedPermissions = null;
    int listnum=0;//画像をちょっとずつ表示を下げたい
    KetaiGesture gesture;
    int x=0,y=0;
    float b1 = 0;//1:x
    float b2 = 0;//1:y
    float rectSize = 100;

    float rotX, rotY, rotAngle;
    PVector[] points = new PVector[10];
    PVector[] points2 = new PVector[10];
    //ArrayList points;
//PVector point;
    int npoints = 0;

    PackageManager pm;
    List<AppData> dataList;


    public void settings() {//Startみたいなもの？
        size(displayWidth,displayHeight);

    }

    public void setup() {//lateStartみたいな？
        background(255);
        frameRate(60);

        /** Settings for overlay view */
        private var layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Overlay レイヤに表示
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  // フォーカスを奪わない
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,  // 画面外への拡張を許可
                PixelFormat.TRANSLUCENT
        )

        fun showAsOverlay() {
            windowManager.addView(myView, layoutParams)
        }


        for (int i = 0; i < points.length; i++) {
            points[i] = new PVector();
            points2[i] = new PVector();
        }
        gesture = new KetaiGesture(this);
        //ここからアプリの読み出し作業
        //
        // 端末にインストール済のアプリケーション一覧情報を取得
        activity = getActivity();
        pm = activity.getPackageManager();
        final int flags = PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS;
        final List<ApplicationInfo> installedAppList = pm.getInstalledApplications(0);
        dataList = new ArrayList<AppData>();

        // リストに一覧データを格納する

        for (ApplicationInfo app : installedAppList) {
            AppData data = new AppData();
            data.label = app.loadLabel(pm).toString();
            data.icon = app.loadIcon(pm);
            data.pname = app.packageName;
            dataList.add(data);
        }
        //System.out.println("Listサイズは"+dataList.size());

    }

    public void draw() {//Updateみたいなもの？
        for (int i = 0; i < npoints; i++) {
            PVector p = points[i];
            stroke(0, 255, 0);
            noFill();
            ellipse(p.x, p.y, 100, 100);
            text(p.x + "," + p.y, p.x, p.y);
        }

        pushMatrix();
        int i=0;
        while(i<dataList.size()){
//System.out.println("pname:"+dataList.get(i).pname+"  GooglePlayストア："+MarketApp(dataList.get(i).pname));
            System.out.println("y:"+y);
            if (dataList.get(i).icon instanceof BitmapDrawable){//ある限りのアプリ画像を表示
                BitmapDrawable bd = (BitmapDrawable) dataList.get(i).icon;
                if(bd.getBitmap() != null)pimg = new PImage(bd.getBitmap());
                pimg.resize(144, 144);
                image(pimg,10,(200*listnum)+b2);
                textSize(42);
                fill(0);
                text(dataList.get(i).label, 300, (200*listnum+50)+b2);//アプリ名
                PackageInfo packageInfo = null;
                try {
                    packageInfo = pm.getPackageInfo(dataList.get(i).pname, PackageManager.GET_PERMISSIONS);
                    //Get Permissions
                    requestedPermissions = packageInfo.requestedPermissions;
                    if(requestedPermissions != null) {
                        for(String str:requestedPermissions) {//配列にいれられているパーミッションを読みだす
                            if(str.contains("android.permission"))System.out.println("i=" + i + " " + str);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                listnum++;//次のアプリへ
            }
            i++;
        }

        popMatrix();

    }

    public void readApp(){
        //
        background(255);
        // 端末にインストール済のアプリケーション一覧情報を取得
        final PackageManager pm = activity.getPackageManager();
        final int flags = PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS;
        final List<ApplicationInfo> installedAppList = pm.getInstalledApplications(0);
        // リストに一覧データを格納する
        final List<AppData> dataList = new ArrayList<AppData>();
        for (ApplicationInfo app : installedAppList) {
            AppData data = new AppData();
            data.label = app.loadLabel(pm).toString();
            data.icon = app.loadIcon(pm);
            data.pname = app.packageName;
            dataList.add(data);
        }
        //System.out.println("Listサイズは"+dataList.size());
        for(int i=0;i<dataList.size();i++){
                //System.out.println("pname:"+dataList.get(i).pname+"  GooglePlayストア："+MarketApp(dataList.get(i).pname));
            System.out.println("y:"+y);
                if (dataList.get(i).icon instanceof BitmapDrawable){//ある限りのアプリ画像を表示
                    BitmapDrawable bd = (BitmapDrawable) dataList.get(i).icon;
                    if(bd.getBitmap() != null)pimg = new PImage(bd.getBitmap());
                    pimg.resize(144, 144);
                    image(pimg,10,(200*listnum)-y);
                    textSize(42);
                    fill(0);
                    text(dataList.get(i).label, 300, (200*listnum+50)-y);//アプリ名
                    PackageInfo packageInfo = null;
                    try {
                        packageInfo = pm.getPackageInfo(dataList.get(i).pname, PackageManager.GET_PERMISSIONS);
                        //Get Permissions
                        requestedPermissions = packageInfo.requestedPermissions;
                        if(requestedPermissions != null) {
                            for(String str:requestedPermissions) {//配列にいれられているパーミッションを読みだす
                                //if(str.contains("android.permission"))System.out.println("i=" + i + " " + str);
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    listnum++;//次のアプリへ
                }
        }

    }

    // アプリケーションデータ格納クラス
    private static class AppData {
        String label;
        Drawable icon;
        String pname;
    }

    public boolean MarketApp(String pname){//GooglePlayにあるアプリか？
        if(!pname.equals("com.android.vending")){
            try{
                ApplicationInfo appInfo=activity.getPackageManager().getApplicationInfo(pname,0);
                if((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
public int getApp(String pname){
    // アプリケーションパッケージの情報を取得
    try {
        PackageInfo info = activity.getPackageManager().getPackageInfo(pname, PackageManager.GET_META_DATA);
        return info.installLocation;
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    }
    return 0;
}


//スワイプ
public boolean surfaceTouchEvent(MotionEvent event) {

    if (event.getActionMasked() == MotionEvent.ACTION_UP) {
        npoints = 0;
    } else {
        for (int i = 0; i < event.getPointerCount(); i++) {
            points2[i].x = event.getX(i);
            points2[i].y = event.getY(i);
        }
        PVector[] tmp = points;
        points = points2;
        points2 = tmp;
        npoints = event.getPointerCount();
    }

    //call to keep mouseX, mouseY, etc updated
    super.surfaceTouchEvent(event);

    //forward event to class for processing
    return gesture.surfaceTouchEvent(event);
}

}
