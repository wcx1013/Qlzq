package com.juguo.gushici.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.juguo.gushici.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.jessyan.autosize.utils.LogUtils;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

public class Util {

    /**
     * 判断是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断qq是否安装QQ
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 显示网络图片
     *
     * @param context
     * @param view
     * @param imgUrl
     * @param img_dt
     */
    public static void displayBlendImgView(Context context, ImageView view, String imgUrl, int img_dt) {
        Glide.with(context)
                .load(imgUrl)
                .placeholder(img_dt)
                .into(view);
    }

    /**
     * 显示圆角网络图片
     *
     * @param context
     * @param view
     * @param imgUrl
     * @param img_dt
     */
    public static void displayRoundedImgView(Context context, ImageView view, String imgUrl, int img_dt) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(10);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);

        Glide.with(context)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//磁盘缓存
                .apply(options)
                .placeholder(img_dt)
                .into(view);
    }

    /**
     * 显示圆形图片
     *
     * @param context
     * @param view
     * @param imgUrl
     * @param img_dt
     */
    public static void displayCircleCropImgView(Context context, ImageView view, String imgUrl, int img_dt) {
        Glide.with(context)
                .load(imgUrl)
                .apply(RequestOptions.circleCropTransform())
                .centerCrop()
                .placeholder(img_dt)
                .into(view);
    }

    public static void displayCircleCropImgView(Context context, ImageView view, Object imgUrl, int img_dt) {
        Glide.with(context)
                .load(imgUrl)
                .apply(RequestOptions.circleCropTransform())
                .centerCrop()
                .placeholder(img_dt)
                .into(view);
    }

    /**
     * 获取本地分享图标
     *
     * @param context
     * @param imgName shareicon.png
     * @param imgId   R.raw.shareicon
     * @return
     */
    public static String getAssetsResource(Context context, String imgName, int imgId) {
        if (!new File(Constants.JUGUO_CACHE_DIR + Constants.CACHE_FILE + imgName).exists()) {
            InputStream is = context.getResources().openRawResource(imgId);
            Bitmap bmpBitmap = BitmapFactory.decodeStream(is);
            if(bmpBitmap!=null){
                storeInSD(bmpBitmap, GetCacheFilePath(), imgName);
            }
        }
        return Constants.JUGUO_CACHE_DIR + Constants.CACHE_FILE + imgName;
    }

    /**
     * 将图片保存至sdcard目录下
     *
     * @param bitmap   图片源
     * @param path     sdcard文件夹
     * @param fileName 文件名
     */
    private static void storeInSD(Bitmap bitmap, String path, String fileName) {
        LogUtils.e("保存图片");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File imageFile = new File(file, fileName);
        try {
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            LogUtils.e("图片保存成功");
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取图片缓存在sdcard的路径
     *
     * @return
     */
    private static String GetCacheFilePath() {
        String filePath = "";
        String rootpath = GetSdcardSystemPath();
        filePath = rootpath + Constants.CACHE_FILE;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }

    /**
     * 获取sdcard系统路径
     *
     * @return
     */
    public static String GetSdcardSystemPath() {
        String rootpath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            rootpath = Constants.JUGUO_CACHE_DIR;
        }
        return rootpath;
    }

    //获取当前时间前后几天的时间
    public static String beforeAfterDate(int days) {

        long nowTime = System.currentTimeMillis();

        long changeTimes = days * 24L * 60 * 60 * 1000;

        return getStrTime(String.valueOf(nowTime + changeTimes), "yyyy-MM-dd");

    }

    //时间戳转字符串
    public static String getStrTime(String timeStamp, String format) {

        String timeString = null;

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        long l = Long.valueOf(timeStamp);

        timeString = sdf.format(new Date(l));//单位秒

        return timeString;

    }

    /**
     * 获取当前日期
     *
     * @param format yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());

        return simpleDateFormat.format(date);
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String authorization() {
        MySharedPreferences mySharedPreferences = new MySharedPreferences(MyApplication.getApp(), "Shared");
        return (String) mySharedPreferences.getValue("token", "");
    }

    /**
     * 10以内的随机数
     *
     * @return
     */
    public static int randomNum() {
        //10以内的随机数
        int num = 0;
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            num = random.nextInt(9) + 1;
        }
        return num;
    }

    /**
     * context 上下文
     * uri 视频地址
     * imageView 设置image
     * frameTimeMicros 获取某一时间帧
     */
    public static void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }

    /**
     * 将元为单位的转换为分 （乘100）
     *
     * @param amount
     * @return
     */
    public static String changeY2F(Double amount) {
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).toString();
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(Context context, int amount) {
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).toString();
    }

    /**
     * 计算会员剩余多少天
     *
     * @return
     */
    public static long timeCompare(String time2) {
        long between = 0;
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date begin = dfs.parse(getCurrentTime("yyyy-MM-dd HH:mm:ss"));
            Date end = dfs.parse(time2);
            between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
        } catch (Exception e) {
            e.printStackTrace();
        }

        return between / (24 * 3600);
    }

    public static String requestGet(String orderId, boolean b) {
        String baseUrl = null;
        if (b) {
            baseUrl = Constants.BASE_URL + "order/" + orderId;
        } else {
            baseUrl = Constants.BASE_URL + "user/me/";
        }
        try {
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(6 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(6 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Authorization", Util.authorization());
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            String result = null;
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = streamToString(urlConn.getInputStream());
            } else {
                Log.e("", "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e("", e.toString());
            return null;
        }
    }

    //key为渠道名的key，对应友盟的 UMENG_CHANNEL
    public static String getChannel(Context context) {
        String channel = "";
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String umeng_channel = appInfo.metaData.getString("UMENG_CHANNEL");
            if ("xiaomi".equalsIgnoreCase(umeng_channel)) {
                channel = "XM";
            } else if ("oppo".equalsIgnoreCase(umeng_channel)) {
                channel = "OPPO";
            } else if ("vivo".equalsIgnoreCase(umeng_channel)) {
                channel = "VIVO";
            } else if ("huawei".equalsIgnoreCase(umeng_channel)) {
                channel = "HW";
            } else if ("meizu".equalsIgnoreCase(umeng_channel)) {
                channel = "MZ";
            } else if ("sanxing".equalsIgnoreCase(umeng_channel)) {
                channel = "SX";
            } else if ("yingyongbao".equalsIgnoreCase(umeng_channel)) {
                channel = "YYB";
            } else if ("none".equalsIgnoreCase(umeng_channel)) {
                channel = "NONE";
            }
        } catch (Exception e) {
            channel = "NONE";
        }
        return channel;
    }

    //定义GB的计算常量
    private static final int GB = 1024 * 1024 * 1024;
    //定义MB的计算常量
    private static final int MB = 1024 * 1024;
    //定义KB的计算常量
    private static final int KB = 1024;

    public static String bytes2kb(long bytes) {
        //格式化小数
        DecimalFormat format = new DecimalFormat("###.0");
        if (bytes / GB >= 1) {
            return format.format(bytes / GB) + "GB";
        } else if (bytes / MB >= 1) {
            return format.format(bytes / MB) + "MB";
        } else if (bytes / KB >= 1) {
            return format.format(bytes / KB) + "KB";
        } else {
            return bytes + "B";
        }
    }

    /** 获取SD可用容量 */
    public static long getAvailableStorage() {
        String root = Environment.getExternalStorageDirectory().getPath();
        StatFs statFs = new StatFs(root);
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     *  校验是否开通会员或者购买课程
     * @param mContext
     * @return
     */
    public static boolean pageTo(Context mContext) {
        MySharedPreferences mySharedPreferences = new MySharedPreferences(mContext, "Shared");
        String isOpenMember = (String) mySharedPreferences.getValue("isOpenMember", "");
        if ("1".equals(isOpenMember)) {
            String level = (String) mySharedPreferences.getValue("level", "");
            String dueTime = (String) mySharedPreferences.getValue("dueTime", "");
            boolean isYg = (boolean) mySharedPreferences.getValue("isYg", false);
            if (TextUtils.isEmpty(level)) {
                if (isYg){
                    return true;
                }
                return false;
            } else {
                if (!TextUtils.isEmpty(dueTime)) {
                    long count = Util.timeCompare(dueTime);
                    if (count <= 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean page2To(Context mContext) {
        MySharedPreferences mySharedPreferences = new MySharedPreferences(mContext, "Shared");
        String isOpenMember = (String) mySharedPreferences.getValue("isOpenMember", "");
        if ("1".equals(isOpenMember)) {
            String level = (String) mySharedPreferences.getValue("level", "");
            String dueTime = (String) mySharedPreferences.getValue("dueTime", "");
            if (TextUtils.isEmpty(level)) {
                return false;
            } else {
                if (!TextUtils.isEmpty(dueTime)) {
                    long count = Util.timeCompare(dueTime);
                    if (count <= 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 保存图片至本地
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片 创建文件夹
        File appDir = new File(Environment.getExternalStorageDirectory(), "fdq");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //图片文件名称
        String fileName = "fdq_" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        String path = file.getAbsolutePath();
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), path, fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        ToastUtils.shortShowStr(context, "保存成功");
    }
}
