package fr.antoinebaudot.lab1mad;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Antoine on 12/05/2018.
 */

public class GetBookCoverService extends IntentService {

    private ResultReceiver result ;

    public  GetBookCoverService() {
        super("GetBookCoverService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        result = intent.getParcelableExtra("COVER_RECEIVER");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String imageLink = (String) intent.getExtras().get("COVER_LINK");
//        AddBook.CoverReceiver result = (AddBook.CoverReceiver) intent.getParcelableExtra("COVER_RECEIVER");
        System.out.println("SERVICE ACTION STARTED");

        try {
//            Log.i("imageLink",imageLink);
            URL coverURL = new URL (imageLink);
            HttpURLConnection connIv = (HttpURLConnection) coverURL.openConnection();
            connIv.setDoInput(true);
            connIv.connect();
            InputStream is = connIv.getInputStream();
            Bitmap cover = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            cover.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            Log.i("SERVICE","COVER GET IN SERVICE");
            Bundle resBitmap = new Bundle();
            resBitmap.putByteArray("COVER_BYTE_ARRAY",byteArray);
            resBitmap.putString("COVER_URL",imageLink);
            result.send(0,resBitmap);

        } catch (MalformedURLException e) {
            Log.i("imageLink","MALFORMED URL");
        } catch (IOException e) {
            Log.i("imageLink","OPEN CONNECTION ERROR");
        }

    }
}
