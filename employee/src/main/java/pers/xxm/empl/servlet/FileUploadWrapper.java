package pers.xxm.empl.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import pers.xxm.empl.common.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;

/**
 * Created by XuXuemin on 20/3/6
 */
public class FileUploadWrapper extends HttpServletRequestWrapper {
    /**
     * 构建支持FileUpload的HttpServletRequest对象
     *
     * @param request 根据包含数据的原HttpServletRequest去构建的对象
     * @throws IOException IO异常
     */
    public FileUploadWrapper(HttpServletRequest request) throws IOException {
        super(request);
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setFileSizeMax(5 * Constant.MB); // 设置单个文件大小
        upload.setSizeMax(20 * Constant.MB); // 设置总大小
        upload.setHeaderEncoding("UTF-8"); // 设置编码
        try {
            List<FileItem> fileItems = upload.parseRequest(request);
            toMap(fileItems);
        } catch (FileUploadException ex) {
            throw new IOException("Cannot parse underlying request: " + ex.toString());
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {
        // Enumeration和Iterator都是遍历的，Enumeration是早期的，还可vector.elements方式创建
        return Collections.enumeration(this.formParams.keySet());
    }

    @Override
    public String getParameter(String name) {
        List<String> values = this.formParams.get(name);
        return values == null ? null : values.get(PRIORITY);
    }

    @Override
    public String[] getParameterValues(String name) {
        List<String> values = this.formParams.get(name);
        if (values == null) {
            return null;
        }
        String[] paramValues = new String[values.size()];
        return values.toArray(paramValues);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        // Collections.unmodifiableMap(formParams)生成不能修改的Map视图
        Map<String, String[]> paramMap = new HashMap<>();
        this.formParams.forEach((key, value) -> {
            String[] paramValues = new String[value.size()];
            paramMap.put(key, value.toArray(paramValues));
        });
        return paramMap;
    }

    /**
     * 取得所有FileItem
     *
     * @return 所有FileItem
     */
    public List<FileItem> getFileItems() {
        return new ArrayList<>(this.fileParams.values());
    }

    /**
     * 取得对应参数名（FileName）的FileItem
     *
     * @param name 要取得FileItem的FieldName
     * @return 对应FileItem
     */
    public FileItem getFileItem(String name) {
        return this.fileParams.get(name);
    }

    // 表单（普通）参数
    private final Map<String, List<String>> formParams = new LinkedHashMap<>();
    // 文件参数
    private final Map<String, FileItem> fileParams = new LinkedHashMap<>();
    private static final int PRIORITY = 0; // 取得值时优先取得第一个

    // 将FileUpload中的所有项转为Key-Value形式，FileUpload中的数组会分裂成单个的FileItem
    private void toMap(List<FileItem> fileItems) {
        for (FileItem item : fileItems) {
            if (item.isFormField()) {
                String name = item.getFieldName();
                if (this.formParams.containsKey(name)) {
                    this.formParams.get(name).add(item.getString());
                } else {
                    List<String> paramValues = new ArrayList<>();
                    paramValues.add(item.getString()); // 如此，key对应value不为空
                    this.formParams.put(name, paramValues);
                }
            } else {
                fileParams.put(item.getFieldName(), item);
            }
        }
    }
}
