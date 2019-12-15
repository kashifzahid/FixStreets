package com.example.fixstreet.JSONData;

import android.content.Context;
import android.util.Log;

import com.example.fixstreet.Object.incident_type;
import com.example.fixstreet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class LoadJson {
    public Context context;
    public LoadJson(Context contexts) {
        this.context=contexts;

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("myfile.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public String CheckProduct(String id){


        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("data");


            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject objs=m_jArry.getJSONObject(i);
                JSONArray sub_m_jArry = objs.getJSONArray("SubCategory");
                for(int j=0;j<sub_m_jArry.length();j++){
                    JSONObject objs2=sub_m_jArry.getJSONObject(j);

                    String sub_id =objs2.getString("id");
                    if(id.equals(sub_id)){
                        JSONArray pro_m_jArry = objs2.getJSONArray("Product");
                        if(pro_m_jArry.length()>0){
                            return "yes";
                        }else{
                            return  "not";
                        }

                    }


                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "not";
    }
    public String GetProductId(String id,String type)
    {
        Log.e("AG", "GetProductId: "+type );


        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("data");


            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject objs=m_jArry.getJSONObject(i);
                String names =objs.getString("Category");
                JSONArray sub_m_jArry = objs.getJSONArray("SubCategory");
                for(int j=0;j<sub_m_jArry.length();j++){

                    JSONObject objs2=sub_m_jArry.getJSONObject(j);
                    String sub_names =objs2.getString("Sub_name");
                    String sub_id =objs2.getString("id");
//                    if(type.equals("two")){
//                        if(id.equals(sub_id)){
//                            String named=names+" / "+sub_names;
//                            return named;
//                        }
//                    else {
//                        return "none";
//
//                    }
                    JSONArray pro_m_jArry = objs2.getJSONArray("Product");
                        if(pro_m_jArry.length()>0 & type.equals("three")){
                            for(int k=0;k<pro_m_jArry.length();k++) {
                                JSONObject objs3=pro_m_jArry.getJSONObject(k);
                                String pro_id =objs3.getString("id");
                                String pro_name=objs3.getString("name");
                                if(id.equals(pro_id)){
                                    String named=names+" / "+sub_names+" / "+pro_name;
                                    return named;

                                }
                                else{


                                }


                            }

                        }else if(id.equals(sub_id) & type.equals("two")) {
                            String named=names+" / "+sub_names;
                            return named;
                        }
                    }




                }




        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "not";
    }
    public JSONArray GetProduct(String id)
    {



        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("data");


            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject objs=m_jArry.getJSONObject(i);
                String names =objs.getString("Category");
                JSONArray sub_m_jArry = objs.getJSONArray("SubCategory");
                for(int j=0;j<sub_m_jArry.length();j++){

                    JSONObject objs2=sub_m_jArry.getJSONObject(j);
                    String sub_names =objs2.getString("Sub_name");
                    String sub_id =objs2.getString("id");
//                    if(type.equals("two")){
//                        if(id.equals(sub_id)){
//                            String named=names+" / "+sub_names;
//                            return named;
//                        }
//                    else {
//                        return "none";
//
//                    }
                    if(id.equals(sub_id)){
                    JSONArray pro_m_jArry = objs2.getJSONArray("Product");
                    if(pro_m_jArry.length()>0 ){
                        Log.e("cjlmx", "GetProduct: subs  yecbyb "+sub_names );


                        return pro_m_jArry ;








                        }




                    }else {

                    }
                }




            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }
}
