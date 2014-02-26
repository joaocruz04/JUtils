package pt.joaocruz.jutils;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by BEWARE S.A. on 26/02/14.
 */
public class JOnline {

    /**
     * If set TRUE, all logs will be displayed (if the global debug var in JLog is FALSE, then these logs won't be displayed)
     */
    public static boolean debug = true;

    private static final String TAG = JLog.prettyPrinting? "JOnline___" : "JOnline";
    public static enum FetchErrorType  {CONNECTION, PARSE, BAD_URL, OTHER};
    private static AsyncHttpClient client = new AsyncHttpClient();


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
        get(url, null, callback);
    }


    /**
     * Makes an HTTP GET to a given URL.
     * @param url the URL to connect
     * @param objClass class of the returned object (if json > object). If null, returns a string.
     * @param callback callback that will handle the response
     */

    public static void get(final String url, final Class objClass, final GetCallback callback) {
        print("Geting: " + url);
        client.get(url, null, new AsyncHttpResponseHandler() {
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
                        print("Error in parse geting " + url);
                        e.printStackTrace();
                        callback.onFailure(FetchErrorType.PARSE);
                    }
                    catch (Exception e) {
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
                }
                else {
                    print("General error geting " + url);
                    callback.onFailure(FetchErrorType.OTHER);
                }
            }
        });
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

    /**
     * Makes a request for a SOAP based web-service
     * @param namespace the web-service namespace
     * @param url the web-service url
     * @param methodName the web-service method name
     * @param parameters the web-service parameters.
     *                   Example with 2 parameters:
     *                   {@code
     *                      String[][] parameters = new String[2][2];
     *                      parameters[0] = new String[] { "key1", value1 };
     *                      parameters[1] = new String[] { "key2", value2 };
     *                    }
     *
     * @param objClass class of the returned object (if json > object). If null, returns the response string.
     * @param callback callback that will handle the response.
     */

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



    public interface GetCallback {
        public void onSuccess(Object object);
        public void onFailure(FetchErrorType error);
    }

}
