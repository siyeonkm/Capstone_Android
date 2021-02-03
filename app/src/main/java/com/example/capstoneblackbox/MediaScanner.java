package com.example.capstoneblackbox;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection _msc;

    private File _file;



    public MediaScanner(Context context, File f) {

        _file = f;

        _msc = new MediaScannerConnection(context, this);

        _msc.connect();

    }



    @Override

    public void onMediaScannerConnected() {

        _msc.scanFile(_file.getAbsolutePath(), null);

    }



    @Override

    public void onScanCompleted(String path, Uri uri) {

        Log.d("SCAN", "스캔 완료");
        _msc.disconnect();

    }
}