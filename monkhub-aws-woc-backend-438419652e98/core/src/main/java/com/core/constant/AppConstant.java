package com.core.constant;

import java.util.List;

public final class AppConstant {
    private AppConstant(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    public static final String CREATED_AT = "createdAt";
    public static final List<String> COMPRESSIBLE_IMAGE_TYPES = List.of("jpg", "jpeg", "png", "bmp", "gif" );
    public static final String COMPRESS_IMAGE_SUFFIX = "_compress.jpg";
    public static final Integer COMPRESS_IMAGE_WIDTH = 300;
    public static final Integer COMPRESS_IMAGE_HEIGHT = 300;
}
