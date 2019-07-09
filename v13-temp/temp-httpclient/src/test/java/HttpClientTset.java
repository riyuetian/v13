
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过HttpClient  来实现对web接口的调用  的测试
 *  1、引入httpclient的依赖
 *  2、测试类  引入  junit依赖
 *  3、
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1919:22
 */
public class HttpClientTset {


    /**
     * 测试方法
     *   这个方法可以抽取户公共类  HttpClientUtils
     */
    @Test
    public void grapHTMLTest() throws IOException {
        //1、打开流浪器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2、输入网址
        String url="http://localhost:9093/item/createHTMLById/10";
            //创建httpget对象
        HttpGet httpGet = new HttpGet(url);
        //3、敲回车
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //4、解析服务器的响应信息
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode==200){
            /*
            //请求成功
            //获取响应信息
            HttpEntity entity = response.getEntity();
            //获取输入流  得到远程服务器反馈给我们的信息
            InputStream inputStream = entity.getContent();
            //IO流获取
            byte[] bs = new byte[1024];
            int len;
            while ((len=inputStream.read(bs))!=-1){
                System.out.println(new String(bs,0,len));
            }
            */
            //上述如果只是获取内容，有简化的方式
            String s = EntityUtils.toString(response.getEntity());
            System.out.println(s);
        }else{
            //请求失败
            System.out.println(statusCode);
        }
    }

    /**
     * 测试 公共类
     */
    @Test
    public void utilsTest(){
        /*String s = HttpClientUtils.doGet("http://localhost:9093/item/createHTMLById/10");
        System.out.println(s);
*/
        Map<String, String> map = new HashMap<>();
        map.put("name","南瓜");
        map.put("password","999");
        String s1 = HttpClientUtils.doGet("http://localhost:9093/item/param", map);
        System.out.println(s1);
    }

}
