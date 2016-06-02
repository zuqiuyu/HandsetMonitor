package com.zuqiuyu.testtext;

/**
 * @Title: FileHelper.java
 * @Package com.tes.textsd
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Alex.Z
 * @date 2013-2-26 下午5:45:40
 * @version V1.0
 */

        import java.io.DataOutputStream;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.FileWriter;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import android.content.Context;
        import android.os.Environment;
        import android.util.Log;

public class FileHelper {
    private Context context;
    /** SD卡是否存在**/
    private boolean hasSD = false;
    /** SD卡的路径**/
    private String SDPATH;
    /** 当前程序包的路径**/
    private String FILESPATH;
    public FileHelper(Context context) {
        this.context = context;
        hasSD = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        FILESPATH = SDPATH+"Android/data/com.zuqiuyu.testtext/files/";//正确 正确 正确
       // FILESPATH = this.context.getFilesDir().getPath();
    }
    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
//    public File createSDFile(String fileName) throws IOException {
//        File file = new File(FILESPATH + "//" + fileName);
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        return file;
//    }
    public File createSDFile(String fileName) throws IOException {
        File file = new File(FILESPATH + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
    /**
     * 删除SD卡上的文件
     *
     * @param fileName
     */
//    public boolean deleteSDFile(String fileName) {
//        File file = new File(FILESPATH + "//" + fileName);
//        if (file == null || !file.exists() || file.isDirectory())
//            return false;
//        return file.delete();
//    }
    public boolean deleteSDFile(String fileName) {
        File file = new File(FILESPATH + fileName);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }
    /**
     * 写入内容到SD卡中的txt文本中
     * str为内容
     */
    public void writeSDFile(String str,String fileName)
    {
        try {
            FileWriter fw = new FileWriter(FILESPATH + "//" + fileName);
            File f = new File(SDPATH + "//" + fileName);
            fw.write(str);
            FileOutputStream os = new FileOutputStream(f);
            DataOutputStream out = new DataOutputStream(os);
            out.writeShort(2);
            out.writeUTF("");
            System.out.println(out);
            fw.flush();
            fw.close();
            System.out.println(fw);
        } catch (Exception e) {
        }
    }
    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public String readSDFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File file = new File(FILESPATH + "//" + fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public String getFILESPATH() {
        return FILESPATH;
    }
    public String getSDPATH() {
        return SDPATH;
    }
    public boolean hasSD() {
        return hasSD;
    }
    //创建文件夹
    public void makeRootDirectory() {
        File file = null;
        try {
            file = new File(FILESPATH);
            if (!file.exists()) {
                file.mkdirs();
                Log.e("error:", "创建成功");
            }
        } catch (Exception e) {
            Log.e("error:", "创建失败");
        }
    }
}