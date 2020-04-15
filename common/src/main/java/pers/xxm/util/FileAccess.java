package pers.xxm.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by XuXuemin on 18/10/19
 */
public class FileAccess {

    /**
     * 判断文件是否存在
     * @param path 文件路径
     * @return 存在true，否则false
     */
    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 读取目录下的文件列表，去掉隐藏和目录类型，不包含子目录中的文件
     * @param path 文件目录
     * @return 文件列表
     */
    public static List<String> getFileList(String path) {
        List<String> fileList = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return fileList;
        }
        for (File item: files) {
            if (item.isFile() && !item.isHidden()) {
                fileList.add(item.getPath());
            }
        }
        return fileList;
    }

    /**
     * 按行读取文件，直到读完
     * @param fileName 文件路径名称
     * @return 读取结果
     */
    public static String readByLine(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) return null;
        StringBuilder result = new StringBuilder();
        // FileReader继承于InputStreamReader，创建时同时创建了FileInputStream，使用默认编码
        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
            }
        }
        return result.toString();
    }

    /**
     * 按行读取文件，直到读完
     * @param fileName 文件路径名称
     * @return 读取结果
     */
    public static String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) return null;
        StringBuilder result = new StringBuilder();
        // FileReader继承于InputStreamReader，创建时同时创建了FileInputStream，使用默认编码
        try (FileReader reader = new FileReader(file)) {
            char[] buffer = new char[1024];
            int len;
            while ((len = reader.read(buffer)) > 0) {
                result.append(buffer, 0, len);
            }
        }
        return result.toString();
    }

    /**
     * 写入到目标文件
     * @param file 写入的文件路径名称
     * @param items 写入的内容
     * @throws IOException IO异常
     */
    public static void writeFile(String file, String items) throws IOException {
        File writFile = new File(file);
        try (Writer writer = new FileWriter(writFile, true)) {
            writer.write(items);
        }
    }

    /**
     * 写入到目标文件
     * @param file 写入的文件路径名称
     * @param yield 写入内容生成器，每次调用产生一次要写入的内容
     * @throws IOException IO异常
     */
    public static void writeFile(String file, Supplier<String> yield) throws IOException {
        Writer writer = new FileWriter(file);
        try (Writer bufferedWriter = new BufferedWriter(writer)) {
            String buffer = yield.get();
            bufferedWriter.write(buffer);
        }
    }

}
