package com.cjapps.whatsappmedia.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cjapps.whatsappmedia.R;
import com.cjapps.whatsappmedia.activities.MainActivity;
import com.cjapps.whatsappmedia.utils.Image;
import com.cjapps.whatsappmedia.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = ImageRecyclerAdapter.class.getSimpleName();
    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";

    private static final int ITEM_COUNT = 50;
    private List<Image> imageList;
    private Context context;
    private ImageLoader imageLoader;

    public ImageRecyclerAdapter(Context context) {
        super();

        this.context = context;

        imageLoader = new ImageLoader(context);
        ImageLoader.MODE = 1;

        populateImageList();
    }

    private void populateImageList() {
        // Create some items
        imageList = new ArrayList<>();

        // Set up an array of the Thumbnail Image ID column we want
        String[] projection = {MediaStore.Images.Thumbnails._ID};

        // Create the cursor pointing to the SDCard
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Thumbnails._ID
        );

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);

        /*
        while (cursor.moveToNext()) {
            int imageID = cursor.getInt(columnIndex);
            Image im = new Image(
                    Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                            String.valueOf(imageID))
            );
            Log.e("Thmb",im.getImageUri().toString());
            //imageList.add(im);
        }
        */

        cursor.close();


        // Set up an array of the Thumbnail Image ID column we want
        String[] projectionData = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };

        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[] {
                "WhatsApp Images"
        };

        // Create the cursor pointing to the SDCard
        cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projectionData,
                selection,
                selectionArgs,
                MediaStore.Images.Media._ID
        );

        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int columnIndex1 = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);


        while (cursor.moveToNext()) {
            int imageID = cursor.getInt(columnIndex);
            String dn = cursor.getString(columnIndex1);

            Image x = new Image(Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, String.valueOf(imageID)),
                    imageID);

            imageList.add(x);
        }


        cursor.close();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Image image = imageList.get(position);

        Bitmap b = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
                image.getId(), MediaStore.Images.Thumbnails.MINI_KIND, null);

        holder.image.setImageURI(image.getImageUri());
        //holder.image.setImageBitmap(b);
        //imageLoader.DisplayImage(image.getImageUri().toString(),R.drawable.ic_menu,holder.image);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
    
    //------------------------ ViewHolder -------------------------//

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        ImageView image;
        ImageView selectedOverlay;

        public ImageViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.imageView);
            selectedOverlay = (ImageView) itemView.findViewById(R.id.selectedimageView);

            int w = (MainActivity.width - 4*4)/4;

            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(w,w);

            image.setLayoutParams(rlp);
            selectedOverlay.setLayoutParams(rlp);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Item clicked at position " + getAdapterPosition());
            selectedOverlay.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onLongClick(View view) {
            Log.e(TAG, "Item long clicked at position " + getAdapterPosition());
            return true;
        }
    }
}
