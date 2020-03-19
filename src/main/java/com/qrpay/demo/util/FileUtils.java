package com.qrpay.demo.util;

import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by PC on 2019/3/29.
 */
public class FileUtils {
    /**
     *
     * @param file 文件
     * @param path 文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName) {

        // 生成新的文件名
        //String realPath = path + "/" + FileNameUtils.getFileName(fileName);


        String realPath =path + "/" +  fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            //保存文件
            file.transferTo(dest);
            System.out.println("本地路径:"+realPath);
            return true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * 简要说明:得到图片字节流 数组大小 <br>
     * 创建者：lcwen
     * 创建时间:2020年3月10日 下午2:12:06
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 简要说明:将文件转换成Byte数组 <br>
     * 创建者：lcwen
     * 创建时间:2020年3月10日 下午2:11:55
     * @param file
     * @return
     */
    public static byte[] getBytesByFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 简要说明:MultipartFile转File <br>
     * 创建者：lcwen
     * 创建时间:2020年3月10日 下午2:11:45
     * @param param
     * @return
     */
    public static File transfer(MultipartFile param) {
        if (!param.isEmpty()) {
            File file = null;
            try {
                InputStream in = param.getInputStream();
                file = new File(param.getOriginalFilename());
                OutputStream out = new FileOutputStream(file);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                in.close();
                out.close();
                return file;
            } catch (Exception e) {
                e.printStackTrace();
                return file;
            }
        }
        return null;
    }

    /**
     * 简要说明:获取指定文件的输入流 <br>
     * 创建者：lcwen
     * 创建时间:2020年3月10日 下午2:11:27
     * @param logoPath 文件的路径
     * @return
     */
    public static InputStream getResourceAsStream(String logoPath) {
        return FileUtils.class.getResourceAsStream(logoPath);
    }

    /**
     * 简要说明:将InputStream写入到File中
     * 创建者：lcwen
     * 创建时间:2020年3月10日 下午2:11:12
     * @param ins
     * @param file
     * @throws IOException
     */
    public void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
    
    /**
     * 简要说明:本地图片转换成base64字符串 <br>
     * 创建者：lcwen
     * 创建时间:2020年3月10日 下午2:55:25
     * @param realImgPath 真实路径
     * @return 图片Base64
     * @throws IOException
     */
    public static String ImageToBase64ByLocal(String realImgPath) throws IOException {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            //获取图片路径
            File file = new File(realImgPath);
            in = new FileInputStream(file.getPath());
 
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }


}
