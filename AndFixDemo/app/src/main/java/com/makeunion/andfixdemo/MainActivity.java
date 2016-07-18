package com.makeunion.andfixdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import volley.HttpRequest;

public class MainActivity extends Activity {

    private TextView txTestResult = null;

    public static final String REQUEST_TAG = "checkUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txTestResult = (TextView)findViewById(R.id.tx_test_result);
    }

    public void updatePatch(View view) {
        HttpRequest request = new HttpRequest("http://99.48.58.55:8080/VersionManager/versionController/checkUpdate.do", REQUEST_TAG);
        request.addParam("apkVersion", VersionUtil.getApkVersion(this));
        request.addParam("patchVersion", VersionManager.getInstance(this).getLastPatchVersion());
        request.setRequestListener(new HttpRequest.IRequestListener<String>() {
            @Override
            public void OnPreExecute() {

            }

            @Override
            public void OnResponse(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String newPatchUri = (String)jsonObject.opt("patchVersion");

                if(newPatchUri == null || newPatchUri.equals("null")) {
                    Toast.makeText(MainActivity.this, "没有更新补丁", Toast.LENGTH_LONG).show();
                    return;
                }

                VersionManager.getInstance(MainActivity.this).downloadPatch(newPatchUri);
            }

            @Override
            public void OnErrorResponse(VolleyError error) {

            }

            @Override
            public void OnPostExecute() {

            }
        });
        request.executeStringRequest();
    }

    public void testAction(View view) {
        txTestResult.setText("4444");
    }

}
