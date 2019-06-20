package com.bestom.videoplayer;

import com.bestom.videoplayer.utils.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import example.application.Settings;
import example.widget.media.AndroidMediaController;
import example.widget.media.IRenderView;
import example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private Activity mActivity;
    private Context mContext;

    android.support.v7.app.ActionBar mActionBar;

    private IjkVideoView mVideoView;
    private AndroidMediaController mMediaController;
    private TextView mTextView;
    private String mVideoPath;
    private TableLayout mHudView;

    private Settings mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initview();

        startVideo();
    }

    private void init(){
        mActivity=this;
        mContext=this;

        //        mVideoPath = "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8";
        mVideoPath = "http://vfx.mtime.cn/Video/2019/04/17/mp4/190417093801892920.mp4";

        //加载so库
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        //actionBar初始化
        mActionBar=getSupportActionBar();
        mActionBar.show();
        //返回键导航的显示与隐藏
        mActionBar.setDisplayHomeAsUpEnabled(true);
        //返回键导航的图标设置
        //mActionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        //log的显示与隐藏
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayUseLogoEnabled(true);
    }

    private void initview(){
        mVideoView = (IjkVideoView) findViewById(R.id.videoView);
        mHudView = (TableLayout) findViewById(R.id.hud_view);

    }

    private void startVideo() {
        mSettings = new Settings(this);


        mMediaController = new AndroidMediaController(this, false);



        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);

        if (TextUtils.isEmpty(mVideoPath)) {
            Toast.makeText(this,
                    "No Video Found! Press Back Button To Exit",
                    Toast.LENGTH_LONG).show();
        } else {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Toast.makeText(this,"点击了返回！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.up:
                Toast.makeText(this,"up！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.local_media:
//                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 1);

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,2);

                mVideoView.release(true);
                break;
            case R.id.zhibo_RTMP:
                //region RTMP直播流
                final String[] RTMPitems = new String[]{"香港卫视 "};//创建item
                AlertDialog RTMPalertDialog = new AlertDialog.Builder(this)
                        //.setTitle("选择您喜欢的老湿")
                        //.setIcon(R.mipmap.ic_launcher)
                        .setItems(RTMPitems, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:
                                        mVideoView.release(true);

                                        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                                        mVideoView.setMediaController(new AndroidMediaController(mContext, false));
                                        mVideoView.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
                                        mVideoView.start();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).create();
                RTMPalertDialog.show();
                //endregion
                break;
            case R.id.zhibo_RTSP:
                //region RTSP直播流
                final String[] RTSPitems = new String[]{"珠海过澳门大厅摄像头监控","大熊兔（点播）","rtsp://192.168.32.3/readsense"};//创建item
                AlertDialog RTSPalertDialog = new AlertDialog.Builder(this)
                        //.setTitle("选择您喜欢的老湿")
                        //.setIcon(R.mipmap.ic_launcher)
                        .setItems(RTSPitems, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:
                                        mVideoView.release(true);

                                        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                                        mVideoView.setMediaController(new AndroidMediaController(mContext, false));
                                        mVideoView.setVideoPath("rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp");
                                        mVideoView.start();
                                        break;
                                    case 1:
                                        mVideoView.release(true);

                                        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                                        mVideoView.setMediaController(new AndroidMediaController(mContext, false));
                                        mVideoView.setVideoPath("rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov");
                                        mVideoView.start();
                                        break;
                                    case 2:
                                        mVideoView.release(true);

                                        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                                        mVideoView.setMediaController(new AndroidMediaController(mContext, false));
                                        mVideoView.setVideoPath("rtsp://192.168.32.5/readsense");
                                        mVideoView.start();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).create();
                RTSPalertDialog.show();
                //endregion
                break;
            case R.id.zhibo_HTTPS:
                //region HTTPS直播流
                final String[] HTTPSitems = new String[]{"空","CCTV6高清"};//创建item
                AlertDialog HTTPSalertDialog = new AlertDialog.Builder(this)
                        //.setTitle("选择您喜欢的老湿")
                        //.setIcon(R.mipmap.ic_launcher)
                        .setItems(HTTPSitems, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:

                                        break;
                                    case 1:
                                        mVideoView.release(true);

                                        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                                        mVideoView.setMediaController(new AndroidMediaController(mContext, false));
                                        mVideoView.setVideoPath("http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8");
                                        mVideoView.start();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).create();
                HTTPSalertDialog.show();
                //endregion
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        mVideoView.stopPlayback();
        mVideoView.release(true);
        mVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //region case1
                case 1:
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    /** 数据库查询操作。
                     * 第一个参数 uri：为要查询的数据库+表的名称。
                     * 第二个参数 projection ： 要查询的列。
                     * 第三个参数 selection ： 查询的条件，相当于SQL where。
                     * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
                     * 第四个参数 sortOrder ： 结果排序。
                     */
                    Cursor cursor = cr.query(uri, null, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            // 视频ID:MediaStore.Audio.Media._ID
                            int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                            // 视频名称：MediaStore.Audio.Media.TITLE
                            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                            // 视频路径：MediaStore.Audio.Media.DATA
                            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            // 视频时长：MediaStore.Audio.Media.DURATION
                            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                            // 视频大小：MediaStore.Audio.Media.SIZE
                            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

//                        // 视频缩略图路径：MediaStore.Images.Media.DATA
//                        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//                        // 缩略图ID:MediaStore.Audio.Media._ID
//                        int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
//                        // 方法一 Thumbnails 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
//                        // 第一个参数为 ContentResolver，第二个参数为视频缩略图ID， 第三个参数kind有两种为：MICRO_KIND和MINI_KIND 字面意思理解为微型和迷你两种缩略模式，前者分辨率更低一些。
//                        Bitmap bitmap1 = MediaStore.Video.Thumbnails.getThumbnail(cr, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
//
//                        // 方法二 ThumbnailUtils 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
//                        // 第一个参数为 视频/缩略图的位置，第二个依旧是分辨率相关的kind
//                        Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND);
//                        // 如果追求更好的话可以利用 ThumbnailUtils.extractThumbnail 把缩略图转化为的制定大小
////                        ThumbnailUtils.extractThumbnail(bitmap, width,height ,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                            if (videoPath == null || videoPath.equals("")) {
                                mVideoView.start();
                            } else {
                                mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                                mVideoView.setMediaController(new AndroidMediaController(mContext, false));
                                mVideoView.setVideoPath(videoPath);
                                mVideoView.start();
                            }
                        }
                        cursor.close();
                    }
                    break;
                    //endregion
                //region case2
                case 2:
                    Uri uri0 = data.getData();
                    if (uri0 != null) {
                        String path = new baseUtil().getPath(this, uri0);
                        if (path != null) {
                            File file = new File(path);
                            if (file.exists()) {
                                String upLoadFilePath = file.toString();
                                String upLoadFileName = file.getName();

                                mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
                                mVideoView.setMediaController(new AndroidMediaController(mContext, false));
                                mVideoView.setVideoPath(upLoadFilePath);
                                mVideoView.start();
                            }
                        }
                    }
                    break;
                //endregion
                default:
                    break;
            }
        }
    }
}
