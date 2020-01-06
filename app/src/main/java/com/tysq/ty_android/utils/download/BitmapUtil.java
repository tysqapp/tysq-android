package com.tysq.ty_android.utils.download;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class BitmapUtil {

    private static final int SIZE_MB = 1024 * 1024;

    /**
     * convert Bitmap to byte array
     *
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public static Bitmap getBytesFromBase64(String content) {

        if (TextUtils.isEmpty(content)) {
            return null;
        }

        byte[] bytes = Base64.decode(content, Base64.NO_WRAP);
        return byteToBitmap(bytes);

    }

    /**
     * 把bitmap转换成Base64编码String
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        return Base64.encodeToString(bitmapToByte(bitmap), Base64.DEFAULT);
    }

    /**
     * convert Drawable to Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     * scale image
     *
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * scale image
     *
     * @param src
     * @param scaleWidth
     * @param scaleHeight
     * @return
     */
    public static Bitmap scaleImage(Bitmap src, float scaleWidth, float scaleHeight) {
        if (src == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    /**
     * 圆bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 生成bitmap缩略图
     *
     * @param bitmap
     * @param needRecycle 是否释放bitmap原图
     * @param newHeight   目标宽度
     * @param newWidth    目标高度
     * @return
     */
    public static Bitmap createBitmapThumbnail(Bitmap bitmap, boolean needRecycle, int newHeight, int newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (needRecycle) {
            bitmap.recycle();
        }
        return newBitMap;
    }

    /**
     * 保存Bitmap到文件
     *
     * @param bitmap
     * @param target
     */
    public static void saveBitmap(Bitmap bitmap, File target) {
        saveBitmap2JPG(bitmap, 80, target);
    }

    /**
     * 保存Bitmap到JPG文件
     *
     * @param bitmap
     * @param quality 保存质量 0..100
     * @param target
     */
    public static void saveBitmap2JPG(Bitmap bitmap, int quality, File target) {
        if (target.exists()) {
            target.delete();
        }
        try {
            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            OutputStream stream = new FileOutputStream(target);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            stream.flush();
            stream.close();
            newBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存bitmap到png文件
     *
     * @param bitmap
     * @param quality
     * @param target
     */
    public static void saveBitmap2PNG(Bitmap bitmap, int quality, File target) {
        if (target.exists()) {
            target.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(target);
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 压缩bitmp到目标大小（质量压缩）
     *
     * @param bitmap
     * @param needRecycle
     * @param maxSize
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, boolean needRecycle, long maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length > maxSize) {
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bm = BitmapFactory.decodeStream(isBm, null, null);
        if (needRecycle) {
            bitmap.recycle();
        }
        bitmap = bm;
        return bitmap;
    }

    /**
     * 等比压缩（宽高等比缩放）
     *
     * @param bitmap
     * @param needRecycle
     * @param targetWidth
     * @param targeHeight
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, boolean needRecycle, int targetWidth, int targeHeight) {
        float sourceWidth = bitmap.getWidth();
        float sourceHeight = bitmap.getHeight();

        float scaleWidth = targetWidth / sourceWidth;
        float scaleHeight = targeHeight / sourceHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight); //长和宽放大缩小的比例
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (needRecycle) {
            bitmap.recycle();
        }
        bitmap = bm;
        return bitmap;
    }

    public static Bitmap compressBitmap(String imageFile, boolean qualityCompress, long maxSize, int targetWidth, int targeHeight) {
        return compress(imageFile, null, false, qualityCompress, maxSize, targetWidth, targeHeight);
    }

    private static Bitmap compress(String imageFile, String targetFile, boolean isSave, boolean qualityCompress, long maxSize, int targetWidth, int targeHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options); //加载图片信息
        int sourceWidth = options.outWidth;
        int sourceHeight = options.outHeight;
        options.inJustDecodeBounds = false;
        //计算inSampleSize
        int inSampleSize = 1;
        //先根据宽度进行缩小
        while (sourceWidth / inSampleSize > targetWidth) {
            inSampleSize++;
        }
        //然后根据高度进行缩小
        while (sourceHeight / inSampleSize > targeHeight) {
            inSampleSize++;
        }

        if (inSampleSize <= 0) {
            inSampleSize = 1;
        }
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile, options);//加载真正bitmap

        bitmap = compressBitmap(bitmap, false, targetWidth, targeHeight); //等比缩放
        if (qualityCompress) {
            bitmap = compressBitmap(bitmap, true, maxSize); //压缩质量
        }

        if (isSave) {
            String savePath = imageFile;
            if (!TextUtils.isEmpty(targetFile)) {
                savePath = targetFile;
            }

            saveBitmap(bitmap, new File(savePath));//保存图片
        }

        return bitmap;
    }

    /**
     * 压缩某张图片(执行步骤sampleSize压缩->等比压缩->质量压缩)
     *
     * @param imageFile
     * @param targetFile      保存目标，为空表示源地址保存
     * @param qualityCompress 是否做质量压缩
     * @param maxSize         目标图片大小
     * @param targetWidth
     * @param targeHeight
     */
    public static void compressImage(String imageFile, String targetFile, boolean qualityCompress, long maxSize, int targetWidth, int targeHeight) {
        Bitmap bitmap = compress(imageFile, targetFile, true, qualityCompress, maxSize, targetWidth, targeHeight);
        bitmap.recycle();
    }

    public static void compressImage(String imageFile, boolean qualityCompress, long maxSize, int targetWidth, int targeHeight) {
        compressImage(imageFile, null, qualityCompress, maxSize, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param targetWidth
     * @param targeHeight
     */
    public static void compressImage(String imageFile, int targetWidth, int targeHeight) {
        compressImage(imageFile, null, false, 0L, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param targetWidth
     * @param targeHeight
     * @return
     */
    public static Bitmap compressBitmap(String imageFile, int targetWidth, int targeHeight) {
        return compressBitmap(imageFile, false, 0L, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param scale     图片缩小倍速
     */
    public static void compressImageSmall(String imageFile, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth / scale;
        int targeHeight = options.outHeight / scale;
        compressImage(imageFile, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param scale     图片缩小倍速
     * @return
     */
    public static Bitmap compressBitmapSmall(String imageFile, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth / scale;
        int targeHeight = options.outHeight / scale;
        return compressBitmap(imageFile, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param scale     图片放大倍速
     */
    public static void compressImageBig(String imageFile, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth * scale;
        int targeHeight = options.outHeight * scale;
        compressImage(imageFile, targetWidth, targeHeight);
    }

    /**
     * 图片缩放-尺寸缩放
     *
     * @param imageFile
     * @param scale     图片放大倍速
     * @return
     */
    public static Bitmap compressBitmapBig(String imageFile, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth * scale;
        int targeHeight = options.outHeight * scale;
        return compressBitmap(imageFile, targetWidth, targeHeight);
    }

    /**
     * 质量压缩图片
     *
     * @param imageFile
     * @param targetFile
     * @param qualityCompress
     * @param maxSize
     */
    public static void compressImage(String imageFile, String targetFile, boolean qualityCompress, long maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth / 2;
        int targeHeight = options.outHeight / 2;
        compressImage(imageFile, targetFile, qualityCompress, maxSize, targetWidth, targeHeight);
    }

    /**
     * 质量压缩图片
     *
     * @param imageFile
     * @param qualityCompress
     * @param maxSize
     */
    public static void compressImage(String imageFile, boolean qualityCompress, long maxSize) {
        compressImage(imageFile, null, qualityCompress, maxSize);
    }

    /**
     * 质量压缩图片
     *
     * @param imageFile
     * @param qualityCompress
     * @param maxSize
     * @return
     */
    public static Bitmap compressBitmap(String imageFile, boolean qualityCompress, long maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth / 2;
        int targeHeight = options.outHeight / 2;
        return compressBitmap(imageFile, qualityCompress, maxSize, targetWidth, targeHeight);
    }

    /**
     * 质量压缩图片-压缩在maxSize以内
     *
     * @param imageFile
     * @param maxSize
     */
    public static void compressImage(String imageFile, long maxSize) {
        compressImage(imageFile, true, maxSize);
    }

    /**
     * 质量压缩图片-压缩在maxSize以内
     *
     * @param imageFile
     * @param maxSize
     * @return
     */
    public static Bitmap compressBimap(String imageFile, long maxSize) {
        return compressBitmap(imageFile, true, maxSize);
    }

    /**
     * 质量压缩图片-压缩在1M以内
     *
     * @param imageFile
     */
    public static void compressImage(String imageFile) {
        compressImage(imageFile, true, (long) (1 * SIZE_MB));
    }

    /**
     * 质量压缩图片-压缩在1M以内
     *
     * @param imageFile
     * @return
     */
    public static Bitmap compressBitmap(String imageFile) {
        return compressBitmap(imageFile, true, (long) (1 * SIZE_MB));
    }

    /**
     * 旋转bitmap
     *
     * @param bitmap
     * @param degress     旋转角度
     * @param needRecycle
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress, boolean needRecycle) {
        Matrix m = new Matrix();
        m.postRotate(degress);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        if (needRecycle) {
            bitmap.recycle();
        }
        return bm;
    }

    /**
     * 根据path
     *
     * @param path
     * @return
     */
    public final static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将彩色图转换为灰度图
     *
     * @param img 源Bitmap
     * @return 返回转换好的位图
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth(); // 获取位图的宽
        int height = img.getHeight(); // 获取位图的高

        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    /**
     * 合并bitmap，左右合并
     *
     * @param bitmaps
     * @return
     */
    public static Bitmap mergeBitmapL2R(List<Bitmap> bitmaps) {

        if (bitmaps.isEmpty()) {
            Log.e("Bitmap", "bitmaps is empty");
            return null;
        }

        //新bitmap的宽高
        int width = 0;
        int height = 0;
        for (Bitmap bitmap : bitmaps) {
            if (bitmap == null || bitmap.isRecycled()) {
                bitmaps.remove(bitmap);
            } else {
                width += bitmap.getWidth();
                height = Math.max(height, bitmap.getHeight());
            }
        }

        if (bitmaps.isEmpty()) {
            Log.e("Bitmap", "bitmaps is empty, by bitmap == null or bitmap.isRecycled");
            return null;
        }

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        int bWidth = 0;
        for (Bitmap bitmap : bitmaps) {
            canvas.drawBitmap(bitmap, bWidth, 0, null);
            bWidth += bitmap.getWidth();
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }

    /**
     * 合并bitmap，上下合并
     *
     * @param bitmaps
     * @return
     */
    public static Bitmap mergeBitmapT2B(List<Bitmap> bitmaps) {

        if (bitmaps.isEmpty()) {
            Log.e("Bitmap", "bitmaps is empty");
            return null;
        }

        //新bitmap的宽高
        int width = 0;
        int height = 0;
        for (Bitmap bitmap : bitmaps) {
            if (bitmap == null || bitmap.isRecycled()) {
                bitmaps.remove(bitmap);
            } else {
                height += bitmap.getHeight();
                width = Math.max(width, bitmap.getWidth());
            }
        }

        if (bitmaps.isEmpty()) {
            Log.e("Bitmap", "bitmaps is empty, by bitmap == null or bitmap.isRecycled");
            return null;
        }

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        int bHeight = 0;
        for (Bitmap bitmap : bitmaps) {
            canvas.drawBitmap(bitmap, 0, bHeight, null);
            bHeight += bitmap.getHeight();
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }

}
