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

package in.lovelacetech.qrib.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;
import in.lovelacetech.qrib.R;
import in.lovelacetech.qrib.model.MarkItem;

/**
 * Created by tioammar
 * on 10/23/15.
 */
public class DetailFragment extends DialogFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mUri;
    private CircleImageView mAvatarView;
    private TextView mNameView;
    private TextView mTimeView;
    private TextView mContentView;
    private ImageView mPhotoView;

    /**
     * this fragment will be show up like a pop up window
     * when a mark on map is clicked
     */
    public DetailFragment() {
        super();
    }

    public static DetailFragment newInstance(Uri uri){
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        // set uri as arguments
        args.putString("data_uri", uri.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        mAvatarView = (CircleImageView) v.findViewById(R.id.avatar_view);
        mNameView = (TextView) v.findViewById(R.id.name_view);
        mTimeView = (TextView) v.findViewById(R.id.time_view);
        mContentView = (TextView) v.findViewById(R.id.content_view);
        mPhotoView = (ImageView) v.findViewById(R.id.photo_view);
        mPhotoView.setVisibility(View.GONE); // not visible by default

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        mUri = Uri.parse(getArguments().getString("data_uri"));
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                mUri,
                MarkItem.COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) return;
        if (data.moveToFirst()){
            // data bound to view
            String avatar = data.getString(MarkItem.COLUMN_PERSON_AVATAR);
            String name = data.getString(MarkItem.COLUMN_PERSON_FULL_NAME);
            String time = data.getString(MarkItem.COLUMN_MARK_TIME);
            String content = data.getString(MarkItem.COLUMN_MARK_CONTENT);
            String photo = data.getString(MarkItem.COLUMN_MARK_PHOTO);

            // bind data to view
            if (!photo.equals("")){
                // only show when photo exists
                mPhotoView.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(photo)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(mPhotoView);
            }

            Glide.with(getActivity()).load(avatar)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mAvatarView);
            mNameView.setText(name);
            mTimeView.setText(time);
            mContentView.setText(content);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // nothing to do
    }
}
