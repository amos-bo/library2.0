package com.xb.amosboutilslibrary.amosboutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author : Amos_bo
 * @package: com.amosbo.maven.utis.bitmap
 * @Created Time: 2018/3/28 11:37
 * @Changed Time: 2018/3/28 11:37
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: //图片工具
 */

public class BitmapUtils {

    /**
     * view 映射成bitmap
     *
     * @param view
     * @return Bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View
                .MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        if (bitmap != null) {
            return bitmap;
        }
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        view.draw(new Canvas(bitmap));

        return bitmap;
    }


    /**
     * 将bitmap存为本地图片
     *
     * @param bitmap
     * @param path
     * @return
     */
    public boolean saveBitmapForSdCard(Bitmap bitmap, String path) {
        File f = new File(path);
        File parent = new File(f.getParent());
        FileOutputStream out = null;
        if (!parent.exists()) {
            parent.mkdirs();
        }
        try {
            f.createNewFile();
            out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Compress by quality,  and generate image to the path specified
     * 图片质量压缩
     *
     * @param image
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(kb)
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath, int maxSize) throws
            IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int options = 100;
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        LogUtils.e("图片压缩前大大小=", os.toByteArray().length / 1024 + "kb");
        while (os.toByteArray().length / 1024 > maxSize) {
            os.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
            LogUtils.e("图片压缩后大大小=", os.toByteArray().length / 1024 + "kb");
        }
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }

    /**
     * 图片压缩
     *
     * @param image
     * @return
     */
    public static Bitmap getCompressBitmap(Bitmap image, int minSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > minSize) {
            options -= 10;
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 得到指定大小本地缩略图
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getCompressBitmapByLocal(String srcPath, int ww, int hh) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return getCompressBitmap(bitmap, 100);
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap getZoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                    true);
        }
        return newbmp;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param context Activity
     * @param uri     Uri
     * @param out     Uri
     */
    public static void cropPicture(Activity context, Uri uri, Uri out) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 1);
        intent.putExtra("outputY", 1);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, out);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        context.startActivity(intent);
    }

    /**
     * 保存图片到sdcard
     *
     * @param context Context
     * @param bitmap  Bitmap
     * @param quality 图片质量
     * @param path    String
     * @return boolean
     */
    public static boolean saveBitmapToSdCard(Context context, Bitmap bitmap, int quality, String
            path) {
        boolean save = false;
        FileOutputStream out = null;
        File f = null;
        try {
            f = new File(path);
            if (!f.exists()) {
                boolean createNewFile = f.createNewFile();
                if (!createNewFile) {
                    return false;
                }
            }
            out = new FileOutputStream(f);
            save = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (save) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(f);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
        return save;
    }

    /**
     * 保存图片到sdcard
     *
     * @param bitmap Bitmap
     * @return boolean
     */
    public static boolean saveBitmapToSdCard(Context context, Bitmap bitmap, File parent) {
        if (bitmap == null) {
            return false;
        }
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return false;
        }
        long millis = Calendar.getInstance().getTimeInMillis();
        File f = new File(parent, millis + ".jpg");
        return saveBitmapToSdCard(context, bitmap, 100, f.getAbsolutePath());
    }

    /**
     * 将bitmap转为base64
     *
     * @param bitmap
     * @param quality   图片质量
     * @param imgFormat
     * @return
     */
    @SuppressLint("NewApi")
    public static String bitmapToBase64(Bitmap bitmap, int quality, String imgFormat) {
        if (null == bitmap) {
            return null;
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            if (imgFormat.equalsIgnoreCase("png")) {
                compressFormat = Bitmap.CompressFormat.PNG;
            }
            bitmap.compress(compressFormat, quality, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将图片转为base64
     *
     * @param imgPath
     * @param imgFormat
     * @return
     */
    @SuppressLint("NewApi")
    public String urlToBase64(String imgPath, String imgFormat) {
        Bitmap bitmap = null;
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        return bitmapToBase64(bitmap, 100, imgFormat);
    }

    /**
     * 读取bitmap
     *
     * @param imgPath
     * @return
     */
    public static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到圆角的bitmap
     *
     * @param bitmap
     * @param corner 以长或宽的比例为半径，2表示二分之一，8表示八分之一
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int corner) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / corner;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / corner;
            float clip = (width - height) / corner;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 设置Bitmap圆角
     *
     * @param image          传入的Bitmap
     * @param outerRadiusRat 圆角半径
     * @return 返回处理后的Bitmap
     */
    public static Bitmap getRoundedBitmap(Bitmap image, int outerRadiusRat) {
        int x = image.getWidth();
        int y = image.getHeight();
        // 根据源文件新建一个darwable对象
        Drawable imageDrawable = new BitmapDrawable(image);
        // 新建一个新的输出图片
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        // 新建一个矩形
        RectF outerRect = new RectF(0, 0, x, y);
        // 产生一个红色的圆角矩形
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);
        // 将源图片绘制到这个圆角矩形上
        // 详解见http://lipeng88213.iteye.com/blog/1189452
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();
        return output;
    }

    /**
     * Bitmap转化为drawable
     *
     * @param bitmap
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * Drawable 转 bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 精确压缩
     *
     * @param filePath
     * @param w
     * @param h
     * @return
     */
    public static Bitmap getBitmapThumbnailByAuto(String filePath, int w, int h) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //true那么将不返回实际的bitmap对象,不给其分配内存空间但是可以得到一些解码边界信息即图片大小等信息
        options.inJustDecodeBounds = true;
        //此时rawBitmap为null
        Bitmap rawBitmap = BitmapFactory.decodeFile(filePath, options);
        if (rawBitmap == null) {
            System.out.println("此时rawBitmap为null");
        }
        //inSampleSize表示缩略图大小为原始图片大小的几分之一,若该值为3
        //则取出的缩略图的宽和高都是原始图片的1/3,图片大小就为原始大小的1/9
        //计算sampleSize
        int sampleSize = computeSampleSize(options, -1, w * h);
        //为了读到图片,必须把options.inJustDecodeBounds设回false
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        Bitmap thumbnailBitmap = BitmapFactory.decodeFile(filePath, options);
        return thumbnailBitmap;
    }


    /**
     * 为了得到恰当的inSampleSize，Android提供了一种动态计算的方法
     *
     * @param options        原本Bitmap的options
     * @param minSideLength  希望生成的缩略图的宽高中的较小的值
     * @param maxNumOfPixels 希望生成的缩量图的总像素
     * @return
     */

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */
    public static Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;
        try {
            String status = Environment.getExternalStorageState();
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
            long time = System.currentTimeMillis();
            String imageName = timeFormatter.format(new Date(time));
            // ContentValues是我们希望这条记录被创建时包含的数据信息
            ContentValues values = new ContentValues(3);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
            values.put(MediaStore.Images.Media.DATE_TAKEN, time);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
                imageFilePath = context.getContentResolver().insert(MediaStore.Images.Media
                                .EXTERNAL_CONTENT_URI,
                        values);
            } else {
                imageFilePath = context.getContentResolver().insert(MediaStore.Images.Media
                                .INTERNAL_CONTENT_URI,
                        values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFilePath;
    }

    /**
     * 得到灰度图
     *
     * @param bmp
     * @return
     */
    public static Bitmap getGreyImage(Bitmap bmp) {
        if (bmp != null) {
            int width, height;
            Paint paint = new Paint();
            height = bmp.getHeight();
            width = bmp.getWidth();
            Bitmap bm = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            Canvas c = new Canvas(bm);
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(bmp, 0, 0, paint);
            return bm;
        } else {
            return bmp;
        }
    }

}
