package com.github.oliveiradev.rxphoto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.oliveiradev.lib.RxPhoto;
import com.github.oliveiradev.lib.shared.TypeRequest;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout thumbsContent = (LinearLayout) findViewById(R.id.thumbs);
        final ImageView image = (ImageView) findViewById(R.id.image);

        findViewById(R.id.get).setOnClickListener(v -> {
            thumbsContent.removeAllViews();
            RxPhoto.requestBitmap(v.getContext(), TypeRequest.GALLERY)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(image::setImageBitmap)
                    .subscribe();
        });

        findViewById(R.id.take).setOnClickListener(v -> {
            thumbsContent.removeAllViews();
            RxPhoto.requestBitmap(v.getContext(), TypeRequest.CAMERA)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(image::setImageBitmap)
                    .subscribe();
        });

        findViewById(R.id.get_thumb).setOnClickListener(v -> RxPhoto.requestThumbnails(v.getContext(), TypeRequest.GALLERY,
                new Pair(60, 60), new Pair(120, 120), new Pair(240, 240))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(bitmap -> {
                    final ImageView newImage = new ImageView(MainActivity.this);
                    newImage.setImageBitmap(bitmap);
                    newImage.setPadding(10,10,10,10);
                    thumbsContent.addView(newImage);
                })
                .subscribe());

        findViewById(R.id.transform).setOnClickListener(v -> RxPhoto.requestBitmap(v.getContext(), TypeRequest.GALLERY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(RxPhoto.transformToThumbnail(new Pair(240, 240), new Pair(120, 120), new Pair(60, 60)))
                .doOnNext(bitmap -> {
                    final ImageView newImage = new ImageView(MainActivity.this);
                    newImage.setImageBitmap(bitmap);
                    newImage.setPadding(10,10,10,10);
                    thumbsContent.addView(newImage);
                })
                .subscribe());
    }
}
