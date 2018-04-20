package fr.antoinebaudot.lab1mad;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class DownloadTask extends AsyncTask<String, Void, DownloadTask.Result> {
    private Result result = new Result("Something happened...");
    private AddBook ui;


    protected DownloadTask(AddBook ui) {
        this.ui = ui;
    }


    @Override
    protected Result doInBackground(String... urls) {
        HttpsURLConnection urlConnection = null;
        InputStream jis = null;
        try {
            URL url = new URL(urls[0]);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            urlConnection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            urlConnection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            urlConnection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            urlConnection.setDoInput(true);

            urlConnection.connect();
            Log.i("url", urls[0]);
            jis = new BufferedInputStream(urlConnection.getInputStream());
            readJson(jis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (jis != null) {
                try {
                    jis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    private void readJson(InputStream json) {
        try {
            int maxReadSize = 500;
            readStream(json, maxReadSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readStream(InputStream iJson, int maxReadSize) throws IOException {
        Reader reader;
        reader = new InputStreamReader(iJson);//ajouter un bufferinputstream et UTF-8
        char[] rawBuffer = new char[maxReadSize];
        StringBuffer buffer = new StringBuffer();
        String resultat;
        int readSize;

        while ((readSize = reader.read(rawBuffer)) != -1) {
                /*if (readSize > maxReadSize) {
                    readSize = maxReadSize;
                }*/
            buffer.append(rawBuffer, 0, readSize);
            //maxReadSize -= readSize;
        }
        resultat = buffer.toString();
        result.setResult(resultat);
        Log.i("Resultat", resultat);
    }


    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        this.ui.setParameters(result.getResult());
    }


    /**
     * Wrapper class that serves as a union of a result value and an exception. When the download
     * task has completed, either the result value or exception can be a non-null value.
     * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
     */
    static class Result {
        public String mResultValue;
        public Exception mException;

        public Result(String resultValue) {
            mResultValue = resultValue;
        }

        public Result(Exception exception) {
            mException = exception;
        }

        public String getResult() {
            return this.mResultValue;
        }

        public void setResult(String resultValue) {
            this.mResultValue = resultValue;
        }
    }
}
