package org.ncgw.noron.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.apache.tools.ant.types.FileList;
import org.ncgw.noron.common.AppSettings;
import org.ncgw.noron.utils.json.JSONObject;


/**
 * User: gaohan
 * Date: 2020/5/30
 * Time: 19:22
 *
 * 传入录音文件路径，调用run方法，得到语音结果
 */

public class SpeechToTextUtil {


  // 需要识别的文件
  private final String FILENAME = "/Users/gaohan/Downloads/speech-demo-master/rest-api-asr/java/16k.pcm";



  private String CUID = "noron_ncgw";

  // 采样率固定值
  private final int RATE = 16000;

  private String URL;

  private int DEV_PID;

  private String SCOPE;


  // 极速版 参数
  {
    URL =   "http://vop.baidu.com/pro_api"; // 可以改为https
    DEV_PID = 80001;
    //SCOPE = "brain_enhanced_asr";
  }


  public String run(String filePath) throws IOException {

    String token = AppSettings.baiduApiToken();
    String result = null;
    result = runJsonPostMethod(token, filePath);

    Map resultMap = JSON.parseObject(result, Map.class);

    String resultText = resultMap.get("result").toString().replace("[","").replace("]","");

    return resultText;
  }

  public String runJsonPostMethod(String token, String filePath) throws IOException {

    byte[] content = getFileContent(filePath);
    String speech = base64Encode(content);

    // 文件格式, 支持pcm/wav/amr 格式，极速版额外支持m4a 格式
    final String FORMAT = filePath.substring(filePath.length() - 3);

    JSONObject params = new JSONObject();


    params.put("dev_pid", DEV_PID);
    //params.put("lm_id",LM_ID);//测试自训练平台需要打开注释
    params.put("format", FORMAT);
    params.put("rate", RATE);
    params.put("token", token);
    params.put("cuid", CUID);
    params.put("channel", "1");
    params.put("len", content.length);
    params.put("speech", speech);

    // System.out.println(params.toString());
    HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();
    conn.setConnectTimeout(5000);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
    conn.setDoOutput(true);
    conn.getOutputStream().write(params.toString().getBytes());
    conn.getOutputStream().close();
    String result = SpeechToTextSupport.getResponseString(conn);


    params.put("speech", "base64Encode(getFileContent(FILENAME))");
    return result;
  }

  private byte[] getFileContent(String filename) throws IOException {
    File file = new File(filename);
    if (!file.canRead()) {
      System.err.println("文件不存在或者不可读: " + file.getAbsolutePath());
    }
    FileInputStream is = null;
    try {
      is = new FileInputStream(file);
      return SpeechToTextSupport.getInputStreamContent(is);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }

  private String base64Encode(byte[] content) {
    /**
     Base64.Encoder encoder = Base64.getEncoder(); // JDK 1.8  推荐方法
     String str = encoder.encodeToString(content);
     **/

    char[] chars = SpeechToTextSupport.encode(content); // 1.7 及以下，不推荐，请自行跟换相关库
    String str = new String(chars);

    return str;
  }

  public static void main(String[] args) throws IOException {
    // 填写下面信息
    SpeechToTextUtil demo = new SpeechToTextUtil();

    String result = demo.run("./audio/agenda1.pcm");
    System.out.println("识别结束：结果是：");
    System.out.println(result);

    // 如果显示乱码，请打开result.txt查看
    File file = new File("result.txt");
    FileWriter fo = new FileWriter(file);
    fo.write(result);
    fo.close();
    System.out.println("Result also wrote into " + file.getAbsolutePath());
  }




}
