import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

/**\
 * 提供过HttpClient  来实现对web接口的调用  的 公共类
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1920:50
 */
public class HttpClientUtils {


    /**
     *带有参数 需要拼接
     */
    public static String doGet(String url,Map<String,String> params){
        //1、打开流浪器
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            //
           //
            URIBuilder uriBuilder = new URIBuilder(url);//?????????????????????
            //判断params参数是否为null
            if(params!=null) {
                Set<Map.Entry<String, String>> entries = params.entrySet();//?????????????????????
                for (Map.Entry<String, String> entry : entries) {
                    //拼接
                   uriBuilder.addParameter(entry.getKey(),entry.getValue());
                }
            }
            //2、输入网址
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            //3、敲回车
            CloseableHttpResponse response = null;
            response = httpClient.execute(httpGet);
            //4、解析服务器的响应信息
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==200){
                //上述如果只是获取内容，有简化的方式
                return EntityUtils.toString(response.getEntity());
            }else{
                //请求失败
                return  statusCode+"";
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "error";
        } finally {
            if(httpClient!=null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String doGet(String url){
        //有了上述方法 因为做了null值判断  这里可以直接调用
        return doGet(url,null);

       /* //1、打开流浪器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2、输入网址
        //创建httpget对象
        HttpGet httpGet = new HttpGet(url);
        try {
            //3、敲回车
            CloseableHttpResponse response = null;
            response = httpClient.execute(httpGet);
            //4、解析服务器的响应信息
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==200){
                //上述如果只是获取内容，有简化的方式
                return EntityUtils.toString(response.getEntity());
            }else{
                //请求失败
                return  statusCode+"";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }finally {
            if(httpClient!=null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

}
