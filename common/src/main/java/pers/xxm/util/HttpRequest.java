package pers.xxm.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**`
 * Created by XuXuemin on 18/11/15
 */
public class HttpRequest {
    private static HttpClient httpClient;
    static {
        // 设置连接池管理，包括连接池限制等
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(1000);
        connManager.setDefaultMaxPerRoute(100);
        // 使用以上的配置和管理设置HttpClient
        httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    /**
     * 通过GET获取指定地址的数据
     * @param path
     * @return
     */
    public static String get(String path) throws IOException {
        String data = null;
        // 根据URL创建HttpGet实例
        HttpGet get = new HttpGet(path);
        // 执行get请求，得到返回体
        HttpResponse response = httpClient.execute(get);
        // 判断是否正常返回
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 解析数据
            data = EntityUtils.toString(response.getEntity());
        }
        return data;
    }

    /**
     * 通过POST获取指定地址的数据
     * @param path
     * @param args
     * @return
     * @throws Exception
     */
    public static String post(String path, List<NameValuePair> args) throws IOException {
        String data = null;
        // 根据URL创建HttpPost实例
        HttpPost post = new HttpPost(path);
        // 传入请求体
        post.setEntity(new UrlEncodedFormEntity(args));
        // 发送请求，得到响应体
        HttpResponse response = httpClient.execute(post);
        // 判断是否正常返回
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 解析数据
            HttpEntity resEntity = response.getEntity();
            data = EntityUtils.toString(resEntity);
        }
        return data;
    }

}