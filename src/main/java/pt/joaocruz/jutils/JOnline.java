package pt.joaocruz.jutils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by BEWARE S.A. on 26/02/14.
 */
public class JOnline {

    /**
     * If set TRUE, all logs will be displayed (if the global debug var in JLog is FALSE, then these logs won't be displayed)
     */
    public static boolean debug = true;

    private static final String TAG = JLog.prettyPrinting? "JOnline___" : "JOnline";



    public static enum FetchErrorType  {CONNECTION, PARSE, BAD_URL, OTHER, USER_EXISTS};
    private static AsyncHttpClient client = null;


    private static void print(String message) {
        if (debug)
            JLog.print(TAG, message);
    }

    /**
     * Makes an HTTP GET to a given URL.
     * @param url The url to connect
     * @param callback callback that will handle the response. The response will be in the String format
     */

    public static void get(String url, GetCallback callback) {
        get(url, null, null, null, callback);
    }

    /**
     * Makes an HTTP GET to a given URL with basic authentication
     * @param url The url to connect
     * @param username the username for basic auth
     * @param password the password for basic auth
     * @param callback callback that will handle the response
     */
    public static void get(String url, String username, String password, GetCallback callback) {
        get(url, username, password, null, callback);
    }


    private static void initClient() {
        if (client==null) {
            client = new AsyncHttpClient();
            client.setMaxConnections(3);
        }
    }


    /**
     * Makes an HTTP GET to a given URL.
     * @param url the URL to connect
     * @param username the username for basic auth
     * @param password the password for basic auth
     * @param objClass class of the returned object (if json > object). If null, returns a string.
     * @param callback callback that will handle the response
     */



    public static void get(final String url, String username, String password, final Class objClass, final GetCallback callback) {
        initClient();
        print("Geting: " + url + " for user/password: " + username + " / " + password);
        client.setTimeout(20000);
        if (username!=null) {
            client.addHeader("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP));
            client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        }
        else
            client.removeHeader("Authorization");
        client.get(url, new AsyncHttpResponseHandler() {

            /*
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JLog.print(" I'm here.....");
            }*/

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (objClass == null) {
                    callback.onSuccess(content);
                } else {
                    try {
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(content, objClass);
                        callback.onSuccess(obj);

                    } catch (JsonParseException e) {
                        print("Error in parse geting " + url);
                        e.printStackTrace();
                        callback.onFailure(FetchErrorType.PARSE);
                    } catch (Exception e) {
                        print("General error geting " + url);
                        e.printStackTrace();
                        callback.onFailure(FetchErrorType.OTHER);
                    }
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                error.printStackTrace();
                if (error instanceof IOException) {
                    print("Connection error geting " + url);
                    callback.onFailure(FetchErrorType.CONNECTION);
                } else {
                    print("General error geting " + url);
                    callback.onFailure(FetchErrorType.OTHER);
                }
            }
        });
    }

    public static void cancelRequests(Context c, boolean mayInterrupt) {
        client.cancelRequests(c, mayInterrupt);
    }

    /**
     * Makes an HTTP POST to a given URL with specified parameters.
     * @param url the URL to connect.
     * @param params the parameters of the post. Can be null.
     *               Example:
     *               {@code
     *                  RequestParams params = new RequestParams();
     *                  params.put("key", "value");
     *                  }
     * @param objClass class of the returned object (if json > object). If null, returns the response string.
     * @param callback callback that will handle the response.
     */

    public static void post(final String url, RequestParams params, final Class objClass, final GetCallback callback) {
        initClient();
        client.setMaxRetriesAndTimeout(0, 1000);
        client.setTimeout(10000);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (objClass == null) {
                    callback.onSuccess(content);
                }
                else {
                    try {
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(content, objClass);
                        callback.onSuccess(obj);
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        print("Parse error geting " + url);
                        callback.onFailure(FetchErrorType.PARSE);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        print("General error geting " + url);
                        callback.onFailure(FetchErrorType.OTHER);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                error.printStackTrace();
                if (error instanceof IOException) {
                    print("Connection error geting " + url);
                    callback.onFailure(FetchErrorType.CONNECTION);
                }
                else {
                    print("General error geting " + url);
                    callback.onFailure(FetchErrorType.OTHER);
                }
            }
        });
    }



    public static void post(Context c, final String url, String bodyContent, final Class objClass, final GetCallback callback) {
        initClient();
        StringEntity entity;
        try {
            entity = new StringEntity(bodyContent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            entity = null;
        }
        client.setMaxRetriesAndTimeout(0, 1000);
        client.setTimeout(10000);
        client.post(c, url, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (objClass == null) {
                    callback.onSuccess(content);
                }
                else {
                    try {
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(content, objClass);
                        callback.onSuccess(obj);
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        print("Parse error geting " + url);
                        callback.onFailure(FetchErrorType.PARSE);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        print("General error geting " + url);
                        callback.onFailure(FetchErrorType.OTHER);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                error.printStackTrace();
                if (error instanceof IOException) {
                    print("Connection error geting " + url);
                    callback.onFailure(FetchErrorType.CONNECTION);
                }
                else {
                    print("General error geting " + url);
                    callback.onFailure(FetchErrorType.OTHER);
                }
            }
        });
    }


    public static void post(final String url,
                            final File file,
                            final TextHttpResponseHandler responseHandler) {

        new AsyncTask<String, Void, Void>() {

            Exception error;
            String status;

            @Override
            protected Void doInBackground(String... params) {
                String absurl = url;

                HttpParams httpParameters = new BasicHttpParams();

                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(absurl);
                HttpResponse execute;

                try {
                    InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
                    reqEntity.setContentType("binary/octet-stream");
                    reqEntity.setChunked(false);
                    httpPost.setEntity(reqEntity);

                    Log.i(TAG, "Sending File " + file.getName());
                    execute = httpClient.execute(httpPost);
                    status = execute.toString();
                    Log.i(TAG, "File Sent");

                } catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (error == null) {
                    responseHandler.onSuccess(200, new Header[0], "");
                } else {
                    responseHandler.onFailure(500, new Header[0], "", new Exception(status + " / " + error.getMessage(), error));
                }
            }
        }.execute();
    }






    /*

    public static void getWS(String namespace, final String url, final String methodName, final String[][] parameters, final Class objClass, final GetCallback callback) {


        final String nmspace = (!namespace.endsWith("/")) ? (namespace+"/") : namespace;
        (new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String resultString = "";
                try {
                    SoapObject request = new SoapObject(nmspace, methodName);
                    if (parameters != null && parameters.length > 0) {
                        for (int i = 0; i < parameters.length; i++) {
                            request.addProperty(parameters[i][0], parameters[i][1]);
                        }
                    }
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
                    //androidHttpTransport.debug = Config.debug;
                    androidHttpTransport.call(nmspace + methodName, envelope);
                    Object result = (Object) envelope.getResponse();
                    //Utils.print("HTTP: " + androidHttpTransport.requestDump);
                    resultString = result.toString();
                    if (objClass == null)
                        callback.onSuccess(resultString);
                    else {
                        Gson gson = new Gson();
                        Object o = gson.fromJson(resultString, objClass);
                        callback.onSuccess(o);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    print("Connection error in " + nmspace + methodName);
                    callback.onFailure(FetchErrorType.CONNECTION);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    print("Parse error geting " + nmspace + methodName);
                    callback.onFailure(FetchErrorType.PARSE);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    print("Parse error geting " + nmspace + methodName);
                    callback.onFailure(FetchErrorType.PARSE);
                } catch (Exception e) {
                    e.printStackTrace();
                    print("General error geting " + nmspace + methodName);
                    callback.onFailure(FetchErrorType.OTHER);
                }

                return null;
            }
        }).execute();

    }

*/

    public interface GetCallback {
        public void onSuccess(Object object);
        public void onFailure(FetchErrorType error);
    }

}
