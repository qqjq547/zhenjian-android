package com.watayouxiang.androidutils.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author lsy
 * <p>
 * 2022年3月26日 22点26分
 */
public class FileUtils {
    //混入的字节
    // 混入字节加密后文件
    public static  String bytePath = Environment.getExternalStorageDirectory().getPath()+ "/AAAAImage/";

    private static final byte keyNum = 99;

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
    /**
     * 将文件转换成byte数组
     * @param
     * @return
     */
    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
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
            if (!dir.exists() && dir.isDirectory())
            {
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
     * @param byteArray 字节数组
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

    public static byte[] getImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection httpURLconnection = (HttpURLConnection)url.openConnection();
        httpURLconnection.setRequestMethod("GET");
        httpURLconnection.setReadTimeout(1000);
        InputStream in = null;
        if(httpURLconnection.getResponseCode() == 200){
            in = httpURLconnection.getInputStream();
            byte[] result = readStream(in);
            in.close();
            return result;
        }
        return null;
    }
    public static byte[] readStream(InputStream in) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = in.read(buffer)) !=-1){
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
