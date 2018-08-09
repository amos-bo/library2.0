package com.xb.amosboutilslibrary.amosboutils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author : Amos_bo
 * @package: com.amosbo.maven.utis.file
 * @Created Time: 2018/3/28 11:39
 * @Changed Time: 2018/3/28 11:39
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: 文件工具
 */

public class FileUtils {

    /**
     * 打开文件
     *
     * @param context
     * @param filePath
     */
    public static void openFile(Context context, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filePath)), getMimeType
                    (filePath));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 过滤文件名
     *
     * @param file
     * @return
     */
    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    /**
     * 获取文件类型
     *
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath) {
        File file = new File(filePath);
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (!TextUtils.isEmpty(type)) {
            return type;
        }
        return "file/*";
    }

    /**
     * 是否能写入文件
     *
     * @param file File
     * @return boolean
     */
    private static boolean isWritable(@NonNull final File file) {
        boolean isExisting = file.exists();
        try {
            FileOutputStream output = new FileOutputStream(file, true);
            try {
                output.close();
            } catch (IOException e) {
                LogUtils.e(FileUtils.class.getName(), e.toString());
            }
        } catch (FileNotFoundException e) {
            LogUtils.e(FileUtils.class.getName(), e.toString());
            return false;
        }
        boolean result = file.canWrite();

        // Ensure that file is not created during this process.
        if (!isExisting) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
        return result;
    }

    /**
     * 是否有读写权限
     *
     * @param folder File
     * @return boolean
     */
    public static boolean isWritableNormalOrSaf(@Nullable final File folder) {
        // Verify that this is a directory.
        if (folder == null) {
            return false;
        }
        // Find a non-existing file in this directory.
        int i = 100000;
        File file;
        do {
            String fileName = "TestFile" + (++i);
            file = new File(folder, fileName);
        }
        while (file.exists());

        // First check regular writability
        if (isWritable(file)) {
            return true;
        }
        return false;
    }

    /**
     * 缓存文件夹
     *
     * @param context Context
     * @return File
     */
    public static File getCacheDir(Context context) {
        File file = null;
        try {
            file = context.getExternalCacheDir();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        boolean isSuccess = false;
        if (file != null && file.canRead() && file.canWrite()) {
            isSuccess = true;
        }
        if (!isSuccess) {
            file = context.getCacheDir();
        }
        return file;
    }


    /**
     * 是否有外存卡
     *
     * @return boolean
     */
    public static boolean isExistExternalStore() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 得到sd卡路径
     *
     * @return File
     */
    public static File getExternalStoreFile() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    /**
     * 得到sd卡路径
     *
     * @return String
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 得到sd卡下载路径
     *
     * @return File
     */
    public static File getDownloadCacheDirectory() {
        if (isExistExternalStore()) {
            return Environment.getDownloadCacheDirectory();
        }
        return null;
    }

    /**
     * 将字符串写入文件
     *
     * @param text     String
     * @param fileStr  String
     * @param isAppend boolean
     */
    public static void writeFile(String text, String fileStr, boolean isAppend) {
        try {
            File file = new File(fileStr);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream f = new FileOutputStream(fileStr, isAppend);
            f.write(text.getBytes());
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件 1 需要一个write权限 2 目录要同级别,这个很关键,你交换的两个文件夹要有相同的层数.
     *
     * @param srcFileName 源文件完整路径，需要文件名
     * @param destDirName 目的目录路径,不需要文件名
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcFileName, String destDirName, String moveName) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        File destDir = new File(destDirName);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File reNameFile = new File(destDirName + File.separator
                + moveName);
        return srcFile.renameTo(reNameFile);
    }

    /**
     * 重命名
     *
     * @param oldFilePath 旧文件的完整路径
     * @param newName     新的文件名
     * @return
     */
    public static boolean renameFile(String oldFilePath, String newName) {
        File srcFile = new File(oldFilePath);
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        File destDir = new File(srcFile.getParentFile(), newName);
        return srcFile.renameTo(destDir);
    }

    /**
     * 拷贝文件
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws IOException
     */
    public static boolean copyFileTo(File srcFile, File destFile) {
        try {
            if (!srcFile.exists() || srcFile.isDirectory()) {
                return false;
            }
            if (destFile.isDirectory()) {
                return false;
            }
            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(destFile);
            int readLen = 0;
            byte[] buf = new byte[1024];
            while ((readLen = fis.read(buf)) != -1) {
                fos.write(buf, 0, readLen);
            }
            fos.flush();
            fos.close();
            fis.close();
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /***
     * 拷贝文件
     * @param srcStr
     * @param destStr
     * @return
     */
    public static boolean copyFileTo(String srcStr, String destStr) {
        File srcFile = new File(srcStr);
        File destFile = new File(destStr);
        return copyFileTo(srcFile, destFile);
    }

    /**
     * 拷贝文件夹
     *
     * @param srcDir
     * @param destDir
     * @return
     */
    public static boolean copyFilesTo(File srcDir, File destDir) {
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        if (!srcDir.exists()) {
            return false;
        }
        if (!srcDir.isDirectory() || !destDir.isDirectory()) {
            return false;
        }
        File[] srcFiles = srcDir.listFiles();
        if (srcFiles == null || srcFiles.length == 0) {
            return false;
        }
        try {
            for (int i = 0; i < srcFiles.length; i++) {
                if (srcFiles[i].isFile()) {

                    File destFile = new File(destDir.getPath() + File.separator
                            + srcFiles[i].getName());
                    copyFileTo(srcFiles[i], destFile);
                } else if (srcFiles[i].isDirectory()) {
                    File theDestDir = new File(destDir.getPath() + File.separator
                            + srcFiles[i].getName());
                    copyFilesTo(srcFiles[i], theDestDir);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 拷贝文件夹
     *
     * @param srcStr
     * @param destStr
     * @return
     */
    public static boolean copyFilesTo(String srcStr, String destStr) {
        File srcFile = new File(srcStr);
        File destFile = new File(destStr);
        return copyFilesTo(srcFile, destFile);
    }

    /**
     * 移动文件
     *
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean moveFileTo(File srcFile, File destFile) {
        boolean iscopy = copyFileTo(srcFile, destFile);
        if (!iscopy) {
            return false;
        }
        delFile(srcFile.getAbsolutePath());
        return true;
    }

    /**
     * 移动文件
     *
     * @param srcStr
     * @param destStr
     * @return
     */
    public static boolean moveFileTo(String srcStr, String destStr) {
        File srcFile = new File(srcStr);
        File destFile = new File(destStr);

        return moveFileTo(srcFile, destFile);
    }

    /**
     * 删除文件或者文件夹
     *
     * @param filePath String
     * @return boolean
     */
    public boolean delFileOrDir(String filePath) {
        File file = new File(filePath);
        delFile(file);
        File parent = file.getParentFile();
        if (parent != null && parent.exists() && parent.isDirectory()) {
            String[] list = parent.list();
            if (list != null && list.length == 0) {
                delFile(parent);
            }
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @return
     */
    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
        boolean isSuccess = file.renameTo(to);
        if (isSuccess) {
            return to.delete();
        }
        return file.delete();
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        return delFile(file);
    }


    /**
     * 删除文件夹里的所有文件
     *
     * @param path String
     * @return boolean
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                delFile(temp);
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList[i]);
                delFolder(path + File.separator + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath String
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            File myFilePath = new File(folderPath);
            delFile(myFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件
     *
     * @param file
     */
    public static void createFile(File file) {
        try {
            if (file.exists()) {
                // file exists and *is* a directory
                if (file.isFile()) {
                    return;
                }
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static File createFile(String filePath) {
        File file = new File(filePath);
        createFile(file);
        return file;
    }

    /**
     * 创建文件夹
     *
     * @param mkdirsPath
     */
    public static void createFiles(@NonNull String mkdirsPath) {
        File file = new File(mkdirsPath);
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            file.mkdirs();
        }
    }

    /**
     * byte转换位M
     *
     * @param size
     * @return
     */
    public static String byteToMB(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format(Locale.CHINA, "%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(Locale.CHINA, f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size > kb) {
            float f = (float) size / kb;
            return String.format(Locale.CHINA, f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format(Locale.CHINA, "%d B", size);
        }
    }

    /**
     * 外存卡路径
     *
     * @param mContext
     * @param is_removale
     * @return
     */
    public static String getStoragePath(Context mContext, boolean is_removale) {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context
                .STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 外存卡集合
     *
     * @param mContext
     * @return
     */
    public static Set<String> getStoragePath(Context mContext) {
        Set<String> appReadableFolderList = new HashSet<>();
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context
                .STORAGE_SERVICE);
        try {
            String[] arrayOfString = (String[]) mStorageManager.getClass().getMethod
                    ("getVolumePaths", new Class[0]).invoke(mStorageManager, new Object[0]);
            int i = 0;
            while (i < arrayOfString.length) {
                if ((((String) mStorageManager.getClass().getMethod("getVolumeState", new
                        Class[]{String.class}).invoke(mStorageManager, new
                        Object[]{arrayOfString[i]})).equals("mounted")) && (new File
                        (arrayOfString[i]).canRead())) {
                    appReadableFolderList.add(arrayOfString[i]);
                }
                i += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appReadableFolderList;
    }

    /**
     * 文件加下文件数量
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    /**
     * 得到文件大小
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available() / 1024;
                return size;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * uri 得到filePath
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePathByUri(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new
                            String[]{MediaStore.Images.ImageColumns.DATA}, null,
                    null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images
                            .ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * @param String name
     * @return Object
     * @function 从本地读取保存的对象
     * @author D-light
     * @time 2014-07-23
     */
    public static Object getObject(Context context, String name) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            // 这里是读取文件产生异常
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 读取产生异常，返回null
        return null;
    }


    /**
     * @param String name
     * @return void
     * @function 将一个对象保存到本地
     * @author D-light
     * @time 2014-07-23
     */
    public static void saveObject(Context context, String name, Object object) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }
}
