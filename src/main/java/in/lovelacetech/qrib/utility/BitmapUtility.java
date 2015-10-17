/*
 * Copyright 2015 Aditya Amirullah. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.lovelacetech.qrib.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.IntDef;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.lovelacetech.qrib.R;

/**
 * Created by tioammar
 * on 10/17/15.
 */
public class BitmapUtility {

    public static final int RAW_PHOTO = 0;
    public static final int COMPRESSED_PHOTO = 1;

    // type-safe here
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RAW_PHOTO, COMPRESSED_PHOTO})
    public @interface PhotoMode {}

    /**
     * helper method to get taken photo uri
     */
    public static Uri getOutputMediaFileUri(Context context, @PhotoMode int type){
        // getting photo saved location
        return Uri.fromFile(getOutputMediaFile(context, type));
    }

    /**
     * helper method for returning saved image file
     * we also creating our directory here!
     */
    public static File getOutputMediaFile(Context context, @PhotoMode int type){
        // creating our photo directory on storage
        File mediaStorageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                context.getString(R.string.app_name));

        // creating directory if no exists
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // our photo name format based on captured time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(new Date());

        if (type == RAW_PHOTO) {
            return new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        }
        else if (type == COMPRESSED_PHOTO) {
            return new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + "-small.jpg");
        } else return null;
    }

    /**
     * helper method to compress image file
     * we duplicate original photo with a smaller one
     */
    public static File getCompressedImage(Context context, String uri){
        // compressing
        Bitmap bitmap = BitmapFactory.decodeFile(uri);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, os);

        // creating new file
        File file = getOutputMediaFile(context, BitmapUtility.COMPRESSED_PHOTO);
        try {
            if (file == null) return null;
            FileOutputStream fos = new FileOutputStream(file);
            // write new file with compressed image
            fos.write(os.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fe){
            return null;
        } catch (IOException e){
            return null;
        }

        // get the new uri
        ImageProvider img = new ImageProvider();
        String path = img.getPath(context, Uri.fromFile(file));

        // return new file with new uri
        return new File(path);
    }

}
