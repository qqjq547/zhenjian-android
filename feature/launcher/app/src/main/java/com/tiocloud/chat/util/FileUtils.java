package com.tiocloud.chat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import com.blankj.utilcode.util.ToastUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.graphics.Bitmap.createBitmap;

/**
 * @author lsy
 * <p>
 * 2022年3月26日 22点26分
 */
public class FileUtils {
    //混入的字节
    // 混入字节加密后文件
    public static String bytePath = Environment.getExternalStorageDirectory().getPath() + "/AAAAImage/";

    private static final byte keyNum = 99;

    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
// 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
// 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;

    }

    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind); //調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
        }
        return bitmap;
    }

    public static void saveBitmapFile(Bitmap bitmap) {
        File file = new File(bytePath + "videoImage.jpg");//将要保存图片的路径
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取视频首帧图片并保存到本地
    public static void getFirstframe(String videoPath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
        File file = new File(videoPath);//实例化File对象，文件路径为/storage/emulated/0/shipin.mp4 （手机根目录）
        if (!file.exists()) {
            ToastUtils.showLong("文件不存在");
        }
        mmr.setDataSource(videoPath);
        Bitmap bitmap = mmr.getFrameAtTime(0); //0表示首帧图片
        mmr.release(); //释放MediaMetadataRetriever对象
        if (bitmap != null) {
            //存储媒体已经挂载，并且挂载点可读/写。
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                bitmap.recycle(); //回收bitmap
                return;
            }
            try {
                String picture_Name = "videoImage"; //获取当前时间戳作为文件名称，避免同名
                String framePath = bytePath; //图片保存文件夹
                File frame_file = new File(framePath);
                if (!frame_file.exists()) { //// 如果路径不存在，就创建路径
                    frame_file.mkdirs();
                }
                File picture_file = new File(framePath, picture_Name + ".jpg"); // 创建路径和文件名的File对象
                FileOutputStream out = new FileOutputStream(picture_file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();  //注意关闭文件流
            } catch (Exception e) {
                ToastUtils.showLong("保存图片失败");
                e.printStackTrace();
            }
        } else {
            ToastUtils.showLong("获取视频缩略图失败");

        }
    }

    /**
     * 加密文件
     */
    public static void encrypt(File inFile, File outFile, int keyNum) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile))) {
            int b;
            while ((b = bis.read()) != -1) {
                bos.write(b ^ keyNum);
            }
        } catch (IOException e) {
            Log.e("图片加密错误!", e.getLocalizedMessage());
        }
    }

    /**
     * 加密byte数组
     */
    public static byte[] encryptByte(byte[] bs) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bs);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int b;
            while ((b = bis.read()) != -1) {
                bos.write(b ^ keyNum);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            Log.e("图片加密错误!", e.getLocalizedMessage());
            throw new IOException("图片加密错误!", e);
        }
    }
    public static Bitmap getBitmapIcon(Drawable drawable) {
        //BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        //return bitmapDrawable.getBitmap();
        Bitmap bmp = createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    public static byte[] getByteArrayIcon(Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = getBitmapIcon(drawable);
            if (bitmap != null) {
                //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //bitmap.compress(CompressFormat.PNG, 100, bytes);
                //return bytes.toByteArray();
                int byteCounts = bitmap.getByteCount();
                ByteBuffer byteBuffer = ByteBuffer.allocate(byteCounts);
                bitmap.copyPixelsToBuffer(byteBuffer);
                return byteBuffer.array();
            }
        }
        return null;
    }

    public static byte[] decrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = keyNum;
        for (int i = len - 1; i > 0; i--) {
            bytes[i] = (byte) (bytes[i] ^ bytes[i - 1]);
        }
        bytes[0] = (byte) (bytes[0] ^ key);
        return bytes;
    }

    /**
     * 将文件转换为 byte数组
     */
    public static byte[] toByteArray(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length())) {
            BufferedInputStream in;
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String base64Byte2String(byte[] bytes) {
        return Base64.encodeToString(bytes, 0);
    }

    public String byte2hex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        String tmp = null;

        for (byte b : bytes) {

            //将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制

            tmp = Integer.toHexString(0xFF & b);

            if (tmp.length() == 1) {

                tmp = "0" + tmp;

            }

            sb.append(tmp);

        }

        return sb.toString();

    }


    /**
     * 将文件转换成byte数组
     *
     * @param
     * @return
     */
    public static byte[] File2byte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * byte数组转File
     *
     * @param byteArray  字节数组
     * @param targetPath 目标路径
     */
    public static void byteArrayToFile(byte[] byteArray, String targetPath) {
        InputStream in = new ByteArrayInputStream(byteArray);
        File file = new File(targetPath);
        String path = targetPath.substring(0, targetPath.lastIndexOf("/"));
        if (!file.exists()) {
            new File(path).mkdir();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();
        httpURLconnection.setRequestMethod("GET");
        httpURLconnection.setReadTimeout(1000);
        InputStream in = null;
        if (httpURLconnection.getResponseCode() == 200) {
            in = httpURLconnection.getInputStream();
            byte[] result = readStream(in);
            in.close();
            return result;
        }
        return null;
    }

    public static byte[] readStream(InputStream in) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        in.close();
        //因为data字节数组是把输入流转换为字节内存输出流的字节数组格式，如果不进行outputStream进行转换，则返回结果会一直为null
        return outputStream.toByteArray();
    }

    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!filename.isEmpty() &&
                    Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }


}
