/**
 *
 */

package volley;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jun Gao(jun1.gao)
 */
public class HttpRequest {

    public static final int EXECUTE_METHOD_STRING = 0x1;
    public static final int EXECUTE_METHOD_JSON = 0x2;
    public static final int EXECUTE_METHOD_UPLOAD = 0x3;
    public static final int EXECUTE_METHOD_DOWNLOAD = 0x4;
    public static final int EXECUTE_METHOD_BYTE = 0x5;
    protected Map<String, String> mFilePaths;
    protected String mFilePath;
    protected int mMethod;
    protected HashMap<String, Object> mApiJsonParams = new HashMap<>();
    protected String mUrl;
    protected String mTag;
    protected int mExecuteStatus;

    @SuppressWarnings("rawtypes")
    protected IRequestListener mResquetListener;
    protected RetryPolicy mRetryPolicy = null;
    protected Response.LoadingListener mDownloadListener;

    public HttpRequest() {

    }

    /**
     * 上传文件
     *
     * @param url      网络上传路径
     * @param key      文件key值
     * @param filepath 文件路径
     * @param tag      访问标识
     */
    public HttpRequest(String url, String key, String filepath, String tag) {
        mFilePaths = new HashMap<>();
        mFilePaths.put(key, filepath);
        mUrl = url;
        mTag = tag;
    }

    /**
     * 下载文件
     *
     * @param url      网络下载路径
     * @param filepath 保存的文件路径
     * @param tag      请求标识
     */
    public HttpRequest(String url, String filepath, String tag) {
        mFilePath = filepath;
        mUrl = url;
        mTag = tag;
    }

    /**
     * post request
     *
     * @param url       网络地址
     * @param apiparams 参数
     * @param tag       网络标识
     */
    public HttpRequest(String url, HashMap<String, Object> apiparams, String tag) {
        mMethod = Method.POST;
        mApiJsonParams.putAll(apiparams);
        mUrl = url;
        mTag = tag;
    }

    /**
     * GET 请求
     *
     * @param url 网络地址
     * @param tag 网络标识
     */
    public HttpRequest(String url, String tag) {
        mMethod = Method.GET;
        mUrl = url;
        mTag = tag;
    }

    /**
     * 网络访问
     *
     * @param method 请求方式
     * @param url    网络地址
     * @param tag    网络标识
     */
    public HttpRequest(int method, String url, String tag) {
        mMethod = method;
        mUrl = url;
        mTag = tag;
    }

    public void addParam(String key, Object value) {
        mMethod = Method.POST;
        mApiJsonParams.put(key, value);
    }

    public void setMethod(int mMethod) {
        this.mMethod = mMethod;
    }


    /**
     * 设置网络监听
     */
    public void setRequestListener(IRequestListener<?> l) {
        mResquetListener = l;
    }

    /**
     * 设置下载监听
     */
    public void setDownloadListener(Response.LoadingListener l) {
        mDownloadListener = l;
    }

    /**
     * 执行string网络请求
     */
    public void executeStringRequest() {
        mExecuteStatus = EXECUTE_METHOD_STRING;
        if (mResquetListener != null) {
            mResquetListener.OnPreExecute();
        }
        StringRequest request;
        if (mMethod == Method.GET) {
            request = new StringRequest(mMethod, mUrl, responseStringListener(), errorListener());
            if (mRetryPolicy != null) {
                request.setRetryPolicy(mRetryPolicy);
            }
            RequestManager.addRequest(request, mTag);
        } else if (mMethod == Method.POST) {
            request = new StringRequest(mMethod, mUrl, responseStringListener(),
                    errorListener()) {
                public Map<String, Object> getParams() {
                    return mApiJsonParams;
                }
            };
            if (mRetryPolicy != null) {
                request.setRetryPolicy(mRetryPolicy);
            }
            RequestManager.addRequest(request, mTag);
        }
    }
    /**
     * 执行string网络请求
     */
    public void executeByteRequest() {
        mExecuteStatus = EXECUTE_METHOD_BYTE;
        if (mResquetListener != null) {
            mResquetListener.OnPreExecute();
        }
        ByteArrayRequest request;
        if (mMethod == Method.GET) {
            request = new ByteArrayRequest(mUrl, responseStringListener(), errorListener());
            request.setShouldCache(false);
            if (mRetryPolicy != null) {
                request.setRetryPolicy(mRetryPolicy);
            }
            RequestManager.addRequest(request, mTag);
        } else if (mMethod == Method.POST) {
            request = new ByteArrayRequest(Method.POST,mUrl, responseStringListener(),
                    errorListener()) {
            };
            request.setShouldCache(false);
            if (mRetryPolicy != null) {
                request.setRetryPolicy(mRetryPolicy);
            }
            request.addParams(mApiJsonParams);
            RequestManager.addRequest(request, mTag);
        }
    }
    /**
     * 执行上传文件请求
     */
    public void executeUploadRequest() {
        mExecuteStatus = EXECUTE_METHOD_UPLOAD;
        if (mResquetListener != null) {
            mResquetListener.OnPreExecute();
        }
        UploadFileRequest uplpoad = new UploadFileRequest(mUrl, responseUploadListener(), errorListener());
        if (mRetryPolicy != null) {
            uplpoad.setRetryPolicy(mRetryPolicy);
        }
        uplpoad.addParams(mApiJsonParams);
        uplpoad.addFiles(mFilePaths);
        RequestManager.addRequest(uplpoad, mTag);

    }

    public void executeDownloadRequest() {
        mExecuteStatus = EXECUTE_METHOD_DOWNLOAD;
        if (mResquetListener != null) {
            mResquetListener.OnPreExecute();
        }
        DownloadRequest downloadRequest = new DownloadRequest(mUrl, mFilePath, responseDownloadListener(), errorListener());
        if (mRetryPolicy != null) {
            downloadRequest.setRetryPolicy(mRetryPolicy);
        }
        downloadRequest.setShouldCache(false);
        downloadRequest.setLoadingListener(mDownloadListener);
        RequestManager.addRequest(downloadRequest, mTag);

    }

    /**
     * 执行Json请求
     */
    public void executeJsonObjectRequest() {
        mExecuteStatus = EXECUTE_METHOD_JSON;
        if (mResquetListener != null) {
            mResquetListener.OnPreExecute();
        }
        JsonObjectRequest request;
        if (mMethod == Method.GET) {
            request = new JsonObjectRequest(mMethod, mUrl, null, responseJSONObjectListener(), errorListener());
            if (mRetryPolicy != null) {
                request.setRetryPolicy(mRetryPolicy);
            }
            RequestManager.addRequest(request, mTag);
        } else if (mMethod == Method.POST) {
            request = new JsonObjectRequest(mMethod, mUrl, new JSONObject(mApiJsonParams), responseJSONObjectListener(), errorListener());
            if (mRetryPolicy != null) {
                request.setRetryPolicy(mRetryPolicy);
            }
            RequestManager.addRequest(request, mTag);
        }
    }

    protected Response.Listener<String> responseStringListener() {
        return new Response.Listener<String>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(String response) {
                if (mResquetListener != null) {
                    try {
                        mResquetListener.OnResponse(response);
                    } catch (JSONException e) {
                        mResquetListener.OnErrorResponse(null);
                    }
                    mResquetListener.OnPostExecute();
                }
            }
        };
    }


    protected Response.Listener<JSONObject> responseJSONObjectListener() {
        return new Response.Listener<JSONObject>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(JSONObject response) {
                if (mResquetListener != null) {
                    try {
                        mResquetListener.OnResponse(response);
                    } catch (JSONException e) {
                        mResquetListener.OnErrorResponse(null);
                    }
                    mResquetListener.OnPostExecute();
                }
            }
        };
    }

    protected Response.Listener<String> responseUploadListener() {
        return new Response.Listener<String>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(String response) {
                if (mResquetListener != null) {
                    try {
                        mResquetListener.OnResponse(response);
                    } catch (JSONException e) {
                        mResquetListener.OnErrorResponse(null);
                    }
                    mResquetListener.OnPostExecute();
                }
            }
        };
    }

    protected Response.Listener<File> responseDownloadListener() {
        return new Response.Listener<File>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponse(File response) {
                if (mResquetListener != null) {
                    try {
                        mResquetListener.OnResponse(response);
                    } catch (JSONException e) {
                        mResquetListener.OnErrorResponse(null);
                    }
                    mResquetListener.OnPostExecute();
                }
            }
        };
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mResquetListener != null) {
                    mResquetListener.OnErrorResponse(error);
                    mResquetListener.OnPostExecute();
                }
            }
        };
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.mRetryPolicy = retryPolicy;
    }

    public static void cancelAll(Object tag) {
        RequestManager.cancelAll(tag);
    }

    public interface IRequestListener<T> {
        void OnPreExecute();

        void OnResponse(T response) throws JSONException;

        void OnErrorResponse(VolleyError error);

        void OnPostExecute();
    }
}
