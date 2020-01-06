package com.abc.lib_utils;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Random;

/**
 * author       : frog
 * time         : 2019-09-23 17:51
 * email        : xxxxx
 * desc         : 文件操作
 * version      : 1.0.0
 */

public class FileUtils {

    private static final int BUFFER_LENGTH = 2048;
    private static final Random RANDOM = new Random();
    private static final int BOUND = 10_000;
    private static final char SEPARATION_CHAR = '-';
    private static final char DOT_CHAR = '.';

    /**
     * 文件是否存在
     *
     * @param file 文件
     * @return true：存在；false：不存在
     */
    public static boolean isFileExist(File file) {
        return file.exists();
    }

    // ----------------------------------- 文件夹创建 start------------------------------------------

    /**
     * 创建文件夹
     *
     * @param parentFolder 父类文件夹
     * @param folderName   子文件夹
     * @return 创建的文件夹，如果失败则返回null
     */
    public static File createFolder(File parentFolder, String folderName) {
        File folder = new File(parentFolder, folderName);
        boolean isSuccess = createFolder(folder);
        if (isSuccess) {
            return folder;
        } else {
            return null;
        }
    }

    /**
     * 创建文件夹
     *
     * @param folderPath 文件夹路径
     * @return 创建的文件夹，如果失败则返回null
     */
    public static File createFolder(String folderPath) {
        File folder = new File(folderPath);
        boolean isSuccess = createFolder(folder);
        if (isSuccess) {
            return folder;
        } else {
            return null;
        }
    }

    /**
     * 创建文件夹
     *
     * @param folder 文件夹路径
     * @return true：创建成功；false：创建失败
     */
    public static boolean createFolder(File folder) {
        if (!folder.exists()) {
            return folder.mkdirs();
        }

        return true;
    }
    // ----------------------------------- 文件夹创建 end--------------------------------------------

    // ------------------------------------ 文件创建 start-------------------------------------------

    /**
     * 在指定文件夹下创建一个文件
     *
     * @param folder 文件夹
     * @param prefix 文件前缀，可为null，则不增加前缀
     * @param suffix 文件后缀，可为null，则不增加后缀
     * @return 成功创建则返回file，否则返回null
     */
    public static File createFileViaAuto(File folder,
                                         String prefix,
                                         String suffix) {
        int random = RANDOM.nextInt(BOUND);
        long currentTimeMillis = System.currentTimeMillis();

        // 添加前缀
        StringBuilder fileName = new StringBuilder();
        if (prefix != null && prefix.length() > 0) {
            fileName.append(prefix)
                    .append("-");
        }

        // [0-9999]-[13时间戳]
        fileName.append(random)
                .append(SEPARATION_CHAR)
                .append(currentTimeMillis);

        // 添加后缀
        if (suffix != null && suffix.length() > 0) {
            fileName.append(DOT_CHAR)
                    .append(suffix);
        }

        File file = new File(folder, fileName.toString());
        boolean isSuc = createFile(file);
        return isSuc ? file : null;
    }

    /**
     * 创建文件
     *
     * @param filePath 文件路径
     * @return 创建的文件，失败则返回null
     */
    public static File createFile(String filePath) {
        File file = new File(filePath);
        boolean isSuc = createFile(file);
        return isSuc ? file : null;
    }

    /**
     * 创建文件
     *
     * @param file 文件
     * @return 创建的文件，失败则返回false
     */
    public static boolean createFile(File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean isSuccess = createFolder(parentFile);
            // 创建失败
            if (!isSuccess) {
                return false;
            }
        }

        if (!file.exists()) {
            boolean isMakeSuccess = false;

            try {
                isMakeSuccess = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return isMakeSuccess;
        }

        return true;
    }

    /**
     * 创建文件
     *
     * @param folder   文件夹
     * @param fileName 文件名
     * @return 创建的文件，失败则返回null
     */
    public static File createFile(File folder, String fileName) {
        if (!folder.exists()) {
            boolean isSuccess = createFolder(folder);
            // 创建失败
            if (!isSuccess) {
                return null;
            }
        }

        File file = new File(folder, fileName);

        if (!file.exists()) {
            boolean isMakeSuccess = false;

            try {
                isMakeSuccess = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!isMakeSuccess) {
                return null;
            }
        }

        return file;
    }
    // ------------------------------------ 文件创建 start-------------------------------------------

    /**
     * 将 流 保存至 文件
     *
     * @param file        保存的文件
     * @param inputStream 流
     * @return 保存成功返回true，否则返回false
     */
    public static boolean saveStreamToFile(File file, InputStream inputStream) {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            byte[] myByte = new byte[BUFFER_LENGTH];

            while (true) {
                int len = inputStream.read(myByte);
                if (len == -1) {
                    fileOutputStream.flush();
                    return true;
                }

                fileOutputStream.write(myByte, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.close(inputStream);
            CloseUtils.close(fileOutputStream);
        }
    }

    /**
     * 将 byte 保存至文件
     *
     * @param file  存储的文件
     * @param bytes 需要存储的byte数据
     * @return 保存成功则返回true，否则返回false
     */
    public static boolean saveByteToFile(File file, byte[] bytes) {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.close(fileOutputStream);
        }
    }

    /**
     * 删除文件
     *
     * @param file 需要删除的文件
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            return true;
        } else {
            return file.exists() ? file.delete() : true;
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 需要删除的文件夹路径
     * @return 删除成功返回true，否则false
     */
    public static boolean deleteFolder(String folderPath) {
        File dir = new File(folderPath);
        return deleteFolder(dir);
    }

    /**
     * 删除文件夹
     *
     * @param folder 需要删除的文件夹
     * @return 删除成功返回true，否则false
     */
    public static boolean deleteFolder(File folder) {
        boolean delete = true;

        // 不为空 且 存在 且为文件夹
        if (folder != null && folder.exists() && folder.isDirectory()) {

            File[] listFiles = folder.listFiles();
            int fileLength = listFiles.length;

            // 循环删除文件
            for (int fileIndex = 0; fileIndex < fileLength; ++fileIndex) {
                File file = listFiles[fileIndex];

                if (file.isFile()) {
                    if (!file.delete()) {
                        delete = false;
                    }
                } else if (file.isDirectory()) {
                    deleteFolder(file);
                }

            }

            if (!folder.delete()) {
                delete = false;
            }

            return delete;

        }

        return true;
    }

    public static boolean renameFileAtTheSameFolder(File oldFile, String newName) {
        return oldFile.renameTo(new File(oldFile.getParent(), newName));
    }

    public static boolean renameFileAtTheSameFolder(String filePath, String newName) {
        File oldFile = new File(filePath);
        return oldFile.renameTo(new File(oldFile.getParent(), newName));
    }

    public static boolean renameFile(File oldFile, String newPath) {
        return oldFile.renameTo(new File(newPath));
    }

    public static boolean renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        return oldFile.renameTo(new File(newPath));
    }

    /**
     * 将 String 写入文件
     *
     * @param file    存储文件
     * @param content 内容
     * @return 写入成功返回true，否则false
     */
    public static boolean saveStringToFile(File file, String content) {
        return saveByteToFile(file, content.getBytes());
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return 成功则返回内容，否则返回 ""
     */
    public static String readFromFile(File file) {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder stringBuilder = new StringBuilder();

            String tempLine;
            while ((tempLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(tempLine);
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(bufferedReader);
        }

        return "";
    }

    /**
     * 转接流，将 输入流 ==转给==> 输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param isCloseAuto  是否关闭流，会将 "输入流" 和 "输出流" 都关闭
     * @return 转送成功则返回true，否则返回false
     */
    public static boolean transmitStream(InputStream inputStream,
                                         OutputStream outputStream,
                                         boolean isCloseAuto) {

        try {
            byte[] tempByte = new byte[2048];

            while (true) {
                int len = inputStream.read(tempByte);
                if (len == -1) {
                    outputStream.flush();
                    return true;
                }

                outputStream.write(tempByte, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (isCloseAuto) {
                CloseUtils.close(inputStream);
                CloseUtils.close(outputStream);
            }
        }

    }

    /**
     * 转接流，将 输入流 ==转给==> 输出流
     *
     * @param inputStream   输入流
     * @param outputStream1 输出流
     * @param outputStream2 输出流
     * @param isCloseAuto   是否关闭流，会将 "输入流" 和 "输出流" 都关闭
     * @return 转送成功则返回true，否则返回false
     */
    public static boolean transmitStream(InputStream inputStream,
                                         OutputStream outputStream1,
                                         OutputStream outputStream2,
                                         boolean isCloseAuto) {

        try {
            byte[] tempByte = new byte[2048];

            while (true) {
                int len = inputStream.read(tempByte);
                if (len == -1) {
                    outputStream1.flush();
                    outputStream2.flush();
                    return true;
                }

                outputStream1.write(tempByte, 0, len);
                outputStream2.write(tempByte, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (isCloseAuto) {
                CloseUtils.close(inputStream);
                CloseUtils.close(outputStream1);
                CloseUtils.close(outputStream2);
            }
        }

    }

    /**
     * 获取扩展名
     *
     * @param fileName 文件名
     * @return 会返回最后 一个"." 之后的内容，否则返回 ""
     */
    public static String getExtensionName(String fileName) {

        if (fileName != null && fileName.length() > 0) {

            // 获取 "." 的下标
            int dot = fileName.lastIndexOf(46);

            // 如果 "." 的下标在合法范围，则裁剪
            if (dot > -1 && dot < fileName.length() - 1) {
                return fileName.substring(dot + 1);
            }
        }

        return "";
    }

    /**
     * 获取不带扩展名的 文件名
     *
     * @param fileName 文件名
     * @return 返回最后 一个"." 之前的内容，否则全部返回
     */
    public static String getFileWithoutExtName(String fileName) {
        // 文件名为 null 或者 为""
        if (fileName == null || fileName.equals("")) {
            return "";
        } else {
            int dot = fileName.lastIndexOf(".");
            return dot < 0 ? fileName : fileName.substring(0, dot);
        }
    }


    /**
     * 保存图片
     *
     * @param picFile 存储图片的文件
     * @param bitmap  需要保存的数据
     * @param quality 图像质量
     * @return 成功返回true，失败则返回false
     */
    public static boolean saveBitmap(File picFile, Bitmap bitmap, int quality) {

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(picFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.close(fos);
        }

        return true;
    }

}
