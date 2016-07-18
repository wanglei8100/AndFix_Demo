package volley;

import com.android.volley.VolleyError;

/**
 * Created by gooljim on 2015/9/13.
 */

public abstract class HttpRequestAdapter<T> implements HttpRequest.IRequestListener<T> {
    @Override
    public void OnPreExecute() {

    }


    @Override
    public void OnErrorResponse(VolleyError error) {

    }

    @Override
    public void OnPostExecute() {

    }
}
