package org.ncgw.noron.utils;
import com.baidu.aip.nlp.AipNlp;
import org.json.JSONObject;

/**
 * User: WYN
 * Date: 2020/6/5
 * Time: 11:04
 */
public class TextToKeywordUtil {
  public static final String APP_ID = "20136395";
  public static final String API_KEY = "1jAZ3741hlz9TlP8tLOnTyBb";
  public static final String SECRET_KEY = "1QESFwgYL1DbhkarHGvBxM99HlEqpxUD";

  public JSONObject text2keywords(String text){
    // 初始化一个AipNlp
    AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

    // 可选：设置网络连接参数
    client.setConnectionTimeoutInMillis(2000);
    client.setSocketTimeoutInMillis(60000);

    // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//    client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//    client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

    // 调用接口
    JSONObject res = client.lexerCustom(text, null);
//    System.out.println(res.toString(2));
    return res;

  }

  public static void main(String[] args) {

    TextToKeywordUtil demo = new TextToKeywordUtil();

    String text = "6月2日上午10点至11点开会";
    JSONObject r = demo.text2keywords(text);
    System.out.println(r);

  }
}
