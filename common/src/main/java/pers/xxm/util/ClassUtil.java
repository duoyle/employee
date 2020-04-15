package pers.xxm.util;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by XuXuemin on 20/3/25
 */
public class ClassUtil {
    /**
     * 获取某包下所有类
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static Set<String> getClassName(String packageName) {
        Set<String> classNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");

        URL url = loader.getResource(packagePath);
        if (url != null) {
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                classNames = getClassNameFromDir(url.getPath(), packageName);
            }
        }
        return classNames;
    }

    /**
     * 从项目文件获取某包下所有类
     * @param filePath 文件路径
     * @param packageName 包名
     * @return 类的完整名称
     */
    private static Set<String> getClassNameFromDir(String filePath, String packageName) {
        Set<String> classNames = new HashSet<>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files == null) {
            return classNames;
        }
        for (File childFile : files) {
            if (childFile.isFile()) {
                String fileName = childFile.getName();
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    classNames.add(packageName+ "." + fileName.replace(".class", ""));
                }
            }
        }

        return classNames;
    }
}
