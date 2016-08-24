package com.mengshitech.colorrun.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


/**
 * @Project: colorrun
 * @Author: wschenyongyin
 * @Date: 2016年7月21日
 * @explain:用来生成二维码
 * @TestState:
 */
public class CreateQrCode {
    String contents;
    int QR_WIDTH = 300;// 定义二维码默认的显示宽度
    int QR_HEIGHT = 300;// 定义二维码默认的显示高度

    public CreateQrCode(String contents, int QR_WIDTH, int QR_HEIGHT) {
        this.contents = contents;
        this.QR_HEIGHT = QR_HEIGHT;
        this.QR_WIDTH = QR_WIDTH;
    }

    public CreateQrCode() {
    }

    // 生成二维码的方法
    public static String createImage(String contents, int QR_WIDTH,
                                     int QR_HEIGHT) {

        try {

            QRCodeWriter writer = new QRCodeWriter();

            if (contents == null || "".equals(contents)
                    || contents.length() < 1) {
                return null;
            }

            //
            BitMatrix martix = writer.encode(contents, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(contents,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            System.out.println(Environment.getExternalStorageDirectory());
            try {
                String imagepath = saveFile(bitmap);
                return imagepath;
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String saveFile(Bitmap bm) throws IOException {
        String imageFilePath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath()
                + "/"+ RandomUtils.getRandomInt()+".jpg";
        Log.i("imageFilePath",imageFilePath);
        File f = new File(imageFilePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return imageFilePath;

    }
}