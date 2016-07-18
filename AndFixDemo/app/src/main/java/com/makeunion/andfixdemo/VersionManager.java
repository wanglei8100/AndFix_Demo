package com.makeunion.andfixdemo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import volley.HttpRequest;
import volley.HttpRequestAdapter;
import volley.RequestManager;

/**
 * Created by renjialiang on 2016/5/13.
 */
public class VersionManager {

    /**
     * Tag for log
     */
    private static final String LOG_TAG = "VersionManager";

    /**
     * Tag for http request
     */
    protected static final String REQUEST_DOWNLOAD = "request_download";

    /**
     * Sub directory of download patch
     */
    private static final String SUB_DIR = "MasPatch";

    /**
     * Single instance
     */
    public static VersionManager mInstance = null;

    /**
     * Context
     */
    private Context mContext = null;

    /**
     * Abs directory of download patch
     */
    private String mPatchDir = null;

    /**
     * PatchManager of AndFix
     */
    private PatchManager mPatchManager = null;

    /**
     * Constructor
     */
    private VersionManager(Context context) {
        mContext = context;
        mPatchManager = new PatchManager(context);
        mPatchDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), SUB_DIR).getAbsolutePath();
    }

    /**
     * Get single instance
     */
    public static VersionManager getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new VersionManager(context);
        }
        return mInstance;
    }

    /**
     * The first loading.Load the patches saved in the @mPatchDir
     */
    public void initLoad() {
        RequestManager.init(mContext);
        mPatchManager.init(VersionUtil.getApkVersion(mContext));
        mPatchManager.loadPatch();
    }

    /**
     * Download the patch by uri
     */
    public void downloadPatch(String url) {
        initDownloadDir();

        HttpRequest request = new HttpRequest(url, new File(mPatchDir, getSimpleNameByUri(url)).getAbsolutePath(), REQUEST_DOWNLOAD);
        request.setRequestListener(new HttpRequestAdapter<File>() {
            @Override
            public void OnResponse(File file) throws JSONException {
                if(file != null && file.exists()) {
                    boolean result = addPatch(file.getAbsolutePath());
                    if(result) {
                        Log.e(LOG_TAG, "Load patch success.");
                    } else {
                        Log.e(LOG_TAG, "Load patch failed.");
                    }
                    file.delete();
                } else {
                    Log.e(LOG_TAG, "Download patch failed.");
                }
            }
        });
        request.executeDownloadRequest();
    }

    /**
     * Get the last patch saved in @mPatchDir
     */
    public int getLastPatchVersion() {
        File file = new File(mContext.getFilesDir(), "apatch");
        if(file != null) {
            File[] files = file.listFiles();
            if(files == null || files.length == 0) {
                Log.e("TAG", "No patch file in the directory.");
                return 0;
            }

            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    return f1.compareTo(f2);
                }
            });

            return getPatchCode(files[files.length - 1].getName());
        } else {
            Log.e(LOG_TAG, "The patch directory is not exist.");
            return 0;
        }
    }

    /**
     * Add the patch downloaded
     */
    private boolean addPatch(String patchPath) {
        try {
            mPatchManager.addPatch(patchPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Init the directory of @mPatchDir
     */
    private void initDownloadDir() {
        File file = new File(mPatchDir);
        if(!file.exists()) {
            file.mkdirs();
        }
        if(!file.exists()) {
            throw new IllegalStateException("Can not create the patch directory:" + mPatchDir);
        }
    }

    /**
     * Get the simple name of patch file by the abs path
     */
    private String getSimpleNameByUri(String uri) {
        return uri.substring(uri.lastIndexOf('/') + 1);
    }

    /**
     * Get the version code of patch added
     */
    private int getPatchCode(String patchName) {
        Log.e("TAG", "patchName=" + patchName);
        if(patchName == null || patchName.length() == 0) {
            return 0;
        }
        String[] strArr = patchName.split("-p-");
        if(strArr == null || strArr.length < 2) {
            return 0;
        }
        return Integer.parseInt(strArr[1].substring(0, strArr[1].lastIndexOf('.')));
    }
}
