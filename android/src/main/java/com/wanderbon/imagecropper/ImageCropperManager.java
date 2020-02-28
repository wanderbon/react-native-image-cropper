package com.wanderbon.imagecropper;

import android.net.Uri;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;
import javax.annotation.Nullable;

public class ImageCropperManager extends ViewGroupManager<ImageCropperView> {
    @Override
    public String getName() {
        return "ImageCropper";
    }

    @Override
    protected ImageCropperView createViewInstance(ThemedReactContext reactContext) {
        return new ImageCropperView(reactContext);
    }

    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put(
                        "customEvent",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "customEvent")))
                .build();
    }

    @Nullable @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("sendOnToggleStyleEvent",
                        MapBuilder.of("registrationName", "sendOnToggleStyleEvent"))
                .put("onChangeText",
                        MapBuilder.of("registrationName", "onChangeText"))
                .put("getRawText",
                        MapBuilder.of("registrationName", "getRawText"))
                .put("onSelectionIsLong",
                        MapBuilder.of("registrationName", "onSelectionIsLong"))
                .put("onCreateNewCard",
                        MapBuilder.of("registrationName", "onCreateNewCard"))
                .put("onFocusChanged",
                        MapBuilder.of("registrationName", "onFocusChanged"))
                .build();
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "customMethod",
                0);
    }


    @Override
    public void receiveCommand(
            ImageCropperView view,
            int commandType,
            ReadableArray args) {
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);

        switch (commandType) {
            case 0: {
                //Body custom method
                return;
            }
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandType,
                        getClass().getSimpleName()));
        }
    }

    @ReactProp(name="title")
    public void setTitle(ImageCropperView view, String title) {
        view.setTitle(title);
    }

    @ReactProp(name="uri")
    public void setUri(ImageCropperView view, Uri uri) {
        view.setUri(uri);
    }
}