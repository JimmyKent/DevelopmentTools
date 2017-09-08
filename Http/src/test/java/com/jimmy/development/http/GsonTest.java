package com.jimmy.development.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jimmy.development.http.data.ReturnData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by jinguochong on 17-9-5.
 */

public class GsonTest {

    @Test
    public void testNull() {
        String data = "{\"code\":200,\"message\":\"\",\"redirect\":\"\",\"value\":null}";
        String data2 = "{\"code\":200,\"message\":\"\",\"redirect\":\"\",\"value\":\"\"}";

        ReturnData<VCodeData> bean = new Gson().fromJson(data, new TypeToken<ReturnData<VCodeData>>() {
        }.getType());
        try {

            ReturnData<VCodeData> bean2 = new Gson().fromJson(data2, new TypeToken<ReturnData<VCodeData>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            try {
                JSONObject json = new JSONObject(data2);
                int code = json.optInt("code", -1);
                if (code == 200) {
                    String value = json.optString("value", null);
                    if (value == null || value.equals("")) {
                        System.out.println("success");
                    }
                    //VCodeData vCodeData = (VCodeData) json.opt("value");
                    //System.out.println("vCodeData:" + vCodeData);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}
