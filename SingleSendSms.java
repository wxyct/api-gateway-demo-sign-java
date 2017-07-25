package com.aliyun.api.gateway.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.api.gateway.demo.constant.Constants;
import com.aliyun.api.gateway.demo.constant.HttpSchema;
import com.aliyun.api.gateway.demo.enums.Method;
public class SingleSendSms {
    private final static String APP_KEY = "24558373"; //AppKey�ӿ���̨��ȡ
    private final static String APP_SECRET = "2cd653a18c7b2ac2b698cf8509d0e2cc"; //AppSecret�ӿ���̨��ȡ
    private final static String SIGN_NAME = "����԰"; // ǩ�����ƴӿ���̨��ȡ�����������ͨ����
    private final static String TEMPLATE_CODE = "SMS_78760220"; //ģ��CODE�ӿ���̨��ȡ�����������ͨ����
    private final static String HOST = "sms.market.alicloudapi.com"; //API�����ӿ���̨��ȡ

    private final static String ERRORKEY = "errorMessage";  //���ش����key

    // @phoneNum: Ŀ���ֻ��ţ�����ֻ��ſ��Զ��ŷָ�;
    // @params: ����ģ���еı��������ֱ���ת��Ϊ�ַ����������ģ���б���Ϊ${no}",�����params��ֵΪ{"no":"123456"}
    public void sendMsg(String phoneNum, String params){
        String path = "/singleSendSms";

        Request request =  new Request(Method.GET, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET, Constants.DEFAULT_TIMEOUT);

        //�����query
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("SignName", SIGN_NAME);
        querys.put("TemplateCode", TEMPLATE_CODE);
        querys.put("RecNum", phoneNum);
        querys.put("ParamString", params);
        request.setQuerys(querys);

        try {
            Map<String, String> bodymap = new HashMap<String, String>();
            Response response = Client.execute(request);
            //����ʵ��ҵ����Ҫ��������response�Ĵ���
            if (null == response) {
                System.out.println("no response");
            } else if (200 != response.getStatusCode()) {
                System.out.println("StatusCode:" + response.getStatusCode());
                System.out.println("ErrorMessage:"+response.getErrorMessage());
                System.out.println("RequestId:"+response.getRequestId());
            } else {
                bodymap = ReadResponseBodyContent(response.getBody());
                if (null != bodymap.get(ERRORKEY)) {
                    //������Ĳ������Ϸ�ʱ�������д���˵��
                    System.out.println(bodymap.get(ERRORKEY));
                } else {
                    //�ɹ�����map����Ӧ��key�ֱ�Ϊ��message��success��
                    System.out.println(JSON.toJSONString(bodymap));
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private Map<String, String> ReadResponseBodyContent(String body) {
        Map<String, String> map = new HashMap<String, String>();    
        try {
            JSONObject jsonObject = JSON.parseObject(body);
            if (null != jsonObject) {               
                for(Entry<String, Object> entry : jsonObject.entrySet()){
                    map.put(entry.getKey(), entry.getValue().toString());
                }               
            }
            if ("false".equals(map.get("success"))) {
                map.put(ERRORKEY, map.get("message"));
            }
        } catch (Exception e) {
            map.put(ERRORKEY, body);
        }
        return map;
    }


    public  static void main(String agrs[]){
        SingleSendSms app = new SingleSendSms();
        app.sendMsg("18600000000,13800000000","{'name':'David'}");
    }
}