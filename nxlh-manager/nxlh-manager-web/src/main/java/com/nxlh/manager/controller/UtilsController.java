package com.nxlh.manager.controller;

import com.alibaba.fastjson.JSON;
import com.nxlh.common.annotation.ApiController;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApiController("/api/utils")
public class UtilsController {

    /**
     * 正式文件存储地址(合并之后的文件)
     */
    private static String tofile = "/tmp/video/";

//    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    /**
     * 临时文件存储地址(M3U8视频段)
     */
    private static final String path = File.separator + "tmp" + File.separator + "video" + File.separator + "temp";


    @GetMapping("/m3u8")
    public Object m3u8(@RequestParam String urlStr, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //通过剪切板获取复制的url
        String url = urlStr;

        if ((url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))) {
            /** m3u8地址 */
            System.out.println("获取到链接地址：" + url);
        } else {
            System.out.println("不合法链接地址！");
            return null;
        }
        //设置好初始路径
        String folderpath = path + UUID.randomUUID();

        File dir = new File(folderpath);
        //防止文件夹里有其他文件，做好分类
        if (dir.exists()) {
            System.out.println("文件夹：" + folderpath + "已存在！");
            return null;
        }
        //获取到地址里面的m3u8文件名称
        final String m3u8name = url.substring(url.lastIndexOf("/"), url.toLowerCase().indexOf(".m3u8", url.lastIndexOf("/"))) + ".m3u8";

        //先将m3u8文件保存到本地，以便不用合成也能播放对应的视频
        saveM3u8File(folderpath, url, m3u8name);
        //解析M3U8地址为对象
        M3U8 m3u8 = parseIndex(folderpath, m3u8name, url);

        //根据M3U8对象获取时长
        float duration = getDuration(m3u8);
        System.out.println("时长: " + ((int) duration / 60) + "分" + (int) duration % 60 + "秒");

        //根据M3U8对象下载视频段
        download(m3u8, executor);

        //关闭线程池
        executor.shutdown();
        System.out.println("等待下载中...");
        while (!executor.isTerminated()) {
            Thread.sleep(500);
        }

        //合并文件
        merge(m3u8, tofile);
        //合并完成后删除缓存文件
        delAllFile(m3u8.getFpath());
        System.out.println("下载完成，文件在: " + folderpath);
//        return null;

        //读取本地录音输入流
        FileInputStream inputStream = new FileInputStream(tofile + "1.mp4");
        int i = inputStream.available();
        //byte数组用于存放图片字节数据
        byte[] buff = new byte[i];
        inputStream.read(buff);
        //记得关闭输入流
        inputStream.close();
        //设置发送到客户端的响应内容类型
        response.setContentType("video/mpeg4");
        OutputStream out = response.getOutputStream();
        out.write(buff);
        //关闭响应输出流
        out.close();
        delAllFile(folderpath);
        return null;
    }


    /**
     * 获取剪切板里面的文字数据
     *
     * @return
     * @Title: getSysClipboardText
     * @version v1.0.0
     * @author guojin
     * @date 2019年2月15日上午11:24:34
     */
    public static String getSysClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * @param
     * @return Modification History:
     * Date         Author          Version            Description
     * ---------------------------------------------------------*
     * 2019年2月19日     guojin           v1.0.0               修改原因
     * @ClassName: M3U8Downloader.java
     * @Description: 该类的功能描述
     * @Title:saveM3u8File
     * @version: v1.0.0
     * @author: guojin
     * @date: 2019年2月19日 下午12:54:19
     */
    private static void saveM3u8File(String folderpath, String url, String m3u8name) throws MalformedURLException, IOException {

        InputStream ireader = new URL(url).openStream();

        final File dir = new File(folderpath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileOutputStream writer = new FileOutputStream(new File(dir, m3u8name));

        IOUtils.copyLarge(ireader, writer);

        ireader.close();
        writer.close();

    }

    /**
     * 根据M3U8对象获取时长
     *
     * @param m3u8
     * @return
     */
    private static float getDuration(M3U8 m3u8) {
        float duration = 0;
        for (M3U8Ts ts : m3u8.getTsList()) {
            duration += ts.getSeconds();
        }
        return duration;
    }

    /**
     * 合并文件
     *
     * @param m3u8
     * @param tofile
     * @throws IOException
     */
    public static void merge(M3U8 m3u8, String tofile) throws IOException {
//        String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".ts";
        String tofilename = tofile + "1.mp4";
        File file = new File(tofilename);
        FileOutputStream fos = new FileOutputStream(file);

        for (M3U8 m : m3u8.getM3u8List()) {
            merge(m, tofile);

        }

        for (M3U8Ts ts : m3u8.getTsList()) {
            File fileTemp = new File(m3u8.getFpath(), ts.getFile().substring(ts.getFile().indexOf("=") + 1));
            if (fileTemp.exists()) {
                IOUtils.copyLarge(new FileInputStream(fileTemp), fos);
            } else {
                throw new RuntimeException(ts.getFile() + "文件不存在，合成失败！");
            }
        }

        fos.close();

        System.out.println("已合成：" + tofilename);
    }

    /**
     * 根据M3U8对象下载视频段
     *
     * @param m3u8
     * @param tofile
     * @throws IOException
     */
    public static void download(final M3U8 m3u8, ExecutorService executor) throws IOException {
        final File dir = new File(m3u8.getFpath());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (final M3U8 m : m3u8.getM3u8List()) {
            //下载对应的m3u8
            download(m, executor);
        }

        for (final M3U8Ts ts : m3u8.getTsList()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        FileOutputStream writer = new FileOutputStream(new File(dir, ts.getFile().substring(ts.getFile().indexOf("=") + 1)));
                        IOUtils.copyLarge(new URL(m3u8.getBasepath() + ts.getFile()).openStream(), writer);
                        writer.close();
                        System.out.println("视频段: " + ts + "下载完成");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    /**
     * 删除文件夹里面的所有数据
     *
     * @param folderPath
     * @return void
     * @Title: delFolder
     * @version v1.0.0
     * @author guojin
     * @date 2019年2月27日上午11:57:25
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
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
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }


    /**
     * 解析M3U8地址为对象
     *
     * @param
     * @return Modification History:
     * Date         Author          Version            Description
     * ---------------------------------------------------------*
     * 2019年2月20日     guojin           v1.0.0               修改原因
     * @ClassName: M3U8Downloader.java
     * @Description: 该类的功能描述
     * @Title:parseIndex
     * @version: v1.0.0
     * @author: guojin
     * @date: 2019年2月20日 下午3:43:49
     */
    static M3U8 parseIndex(String folderpath, String m3u8name, String url) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(folderpath, m3u8name))));
        //解析请求的相关路径
        String basepath = url.substring(0, url.lastIndexOf("/") + 1);

        M3U8 ret = new M3U8();
        //基本url路径
        ret.setBasepath(basepath);
        //基本存放文件夹地址
        ret.setFpath(folderpath);

        String line;
        float seconds = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                if (line.startsWith("#EXTINF:")) {
                    line = line.substring(8);
                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    if (line.contains(",")) {
                        line = line.substring(0, line.indexOf(","));
                    }
                    //解析每个分段的长度
                    seconds = Float.parseFloat(line);
                }
                continue;
            }
            if (line.endsWith("m3u8")) {//文件包含另一个m3u8文件
                if (line.toLowerCase().startsWith("http://") || line.toLowerCase().startsWith("https://")) {
                    String linetag = line.substring(line.lastIndexOf("/"), line.toLowerCase().indexOf(".m3u8", line.lastIndexOf("/")));
                    String linename = linetag + ".m3u8";
                    int tp = basepath.indexOf(line);
                    String nfpath = folderpath;
                    if (tp != -1) {
                        if ((line.lastIndexOf("/") + 1) > (tp + basepath.length())) {//判断路径是否重复
                            line.substring(tp + basepath.length(), line.lastIndexOf("/") + 1);
                        } else {
                            nfpath = folderpath;
                        }
                    } else {
                        nfpath = folderpath + linetag + File.separator;
                    }
                    //获取远程文件
                    saveM3u8File(nfpath, line, linename);
                    //进行递归获取数据
                    ret.addM3u8(parseIndex(nfpath, linename, line));
                } else {//不是使用http协议的 TODO
//            		String nurl=basepath + line;
//            		//传入新的文件夹地址
//            		String nfpath=folderpath;
//            		if(line.lastIndexOf("/")!=-1) {
//            			nfpath+=line.substring(0, line.lastIndexOf("/") + 1);
//            		}
//            		//获取远程文件
//            		saveM3u8File(nfpath, nurl, line);
//            		ret.addM3u8(parseIndex(nfpath,line, nurl));
                }
            } else {
                ret.addTs(new M3U8Ts(line, seconds));
                seconds = 0;
            }
        }
        reader.close();

        return ret;
    }

    static class M3U8 {
        private String basepath;
        private String fpath;
        private java.util.List<M3U8Ts> tsList = new ArrayList<M3U8Ts>();
        private java.util.List<M3U8> m3u8List = new ArrayList<M3U8>();


        public String getFpath() {
            return fpath;
        }

        public void setFpath(String fpath) {
            this.fpath = fpath;
        }

        public java.util.List<M3U8> getM3u8List() {
            return m3u8List;
        }

        public void addM3u8(M3U8 m3u8) {
            this.m3u8List.add(m3u8);
        }

        public String getBasepath() {
            return basepath;
        }

        public void setBasepath(String basepath) {
            this.basepath = basepath;
        }

        public java.util.List<M3U8Ts> getTsList() {
            return tsList;
        }

        public void setTsList(List<M3U8Ts> tsList) {
            this.tsList = tsList;
        }

        public void addTs(M3U8Ts ts) {
            this.tsList.add(ts);
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

    }

    static class M3U8Ts {
        private String file;
        private float seconds;

        public M3U8Ts(String file, float seconds) {
            this.file = file;
            this.seconds = seconds;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public float getSeconds() {
            return seconds;
        }

        public void setSeconds(float seconds) {
            this.seconds = seconds;
        }

        @Override
        public String toString() {
            return file + " (" + seconds + "sec)";
        }
    }
}
