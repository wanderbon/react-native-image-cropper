package com.wanderbon.imagecropper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.facebook.react.uimanager.ThemedReactContext;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import java.io.File;

public class ImageCropperView extends RelativeLayout implements UCropFragmentCallback {
    private ThemedReactContext context;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private UCropFragment fragment;
    private Toolbar toolbar;

    private String mToolbarTitle;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;

    // Enables dynamic coloring
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mToolbarWidgetColor;

    private Uri uri;

    public ImageCropperView(ThemedReactContext context) {
        this(context, null);
    }

    public ImageCropperView(ThemedReactContext context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCropperView(ThemedReactContext context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflate(context, R.layout.cropper_fragment, this);
    }

    public void startCrop() {
        String extention = uri.toString().substring(uri.toString().lastIndexOf("."), uri.toString().length());
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + extention;

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(context.getCacheDir(), destinationFileName)));

        uCrop = basicConfig(uCrop);
        uCrop = advancedConfig(uCrop, extention);

        setupFragment(uCrop);
    }

    private UCrop basicConfig(@NonNull UCrop uCrop) {
        uCrop = uCrop.useSourceImageAspectRatio();

        return uCrop;
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop, String extention) {
        UCrop.Options options = new UCrop.Options();

        switch (extention) {
            case "png": {
                options.setCompressionFormat(Bitmap.CompressFormat.PNG);
                break;
            }
            case "jpg":
            default: {
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                break;
            }
        }
        options.setCompressionQuality(1);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        return uCrop.withOptions(options);
    }

    private void setupFragment(UCrop uCrop) {
        AppCompatActivity activity = getActivity();

        if(activity == null) throw new Error("Activity not found");

        fragment = uCrop.getFragment(uCrop.getIntent(context).getExtras());

        activity.getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, UCropFragment.TAG)
                .commitAllowingStateLoss();

        setupViews(uCrop.getIntent(context).getExtras());
    }

    private void setupViews(Bundle args) {
        mStatusBarColor = args.getInt(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(context, R.color.ucrop_color_statusbar));
        mToolbarColor = args.getInt(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(context, R.color.ucrop_color_toolbar));
        mToolbarCancelDrawable = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, R.drawable.ucrop_ic_cross);
        mToolbarCropDrawable = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_CROP_DRAWABLE, R.drawable.ucrop_ic_done);
        mToolbarWidgetColor = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(context, R.color.ucrop_color_toolbar_widget));
        mToolbarTitle = args.getString(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        mToolbarTitle = mToolbarTitle != null ? mToolbarTitle : getResources().getString(R.string.ucrop_label_edit_photo);

        setupAppBar();
    }

    private void setupAppBar() {
        toolbar = findViewById(R.id.toolbar);

        // Set all of the Toolbar coloring
        toolbar.setBackgroundColor(mToolbarColor);
        toolbar.setTitleTextColor(mToolbarWidgetColor);
        toolbar.setVisibility(View.VISIBLE);
        final TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(mToolbarWidgetColor);
        toolbarTitle.setText(mToolbarTitle);

        // Color buttons inside the Toolbar
        Drawable stateButtonDrawable = ContextCompat.getDrawable(context.getBaseContext(), mToolbarCancelDrawable);
        if (stateButtonDrawable != null) {
            stateButtonDrawable.mutate();
            stateButtonDrawable.setColorFilter(mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(stateButtonDrawable);
        }
    }

    private AppCompatActivity getActivity() {
        Context context = getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof AppCompatActivity) {
                return (AppCompatActivity) context;
            }

            context = ((ContextWrapper)context).getBaseContext();
        }

        return null;
    }

    public void setTitle(@NonNull String mToolbarTitle) {
        this.mToolbarTitle = mToolbarTitle;
    }

    public void setUri(@NonNull String uri) {
        this.uri = Uri.parse(uri);

        startCrop();
    }

    @Override
    public void loadingProgress(boolean showLoader) {
    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {
        Log.i("RESULT_CALLBACK", result + "");
    }
}
