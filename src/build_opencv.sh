# mkdir opencv/build
# cd opencv/build
cmake -DCMAKE_BUILD_TYPE=Release \
      -DBUILD_TESTS=OFF \
      -DBUILD_PERF_TESTS=OFF \
      -DBUILD_opencv_cudev=OFF \
      -DBUILD_opencv_cudaarithm=OFF \
      -DBUILD_opencv_flann=OFF \
      -DBUILD_opencv_ml=OFF \
      -DBUILD_opencv_objdetect=OFF \
      -DBUILD_opencv_cudabgsegm=OFF \
      -DBUILD_opencv_cudafilters=OFF \
      -DBUILD_opencv_cudaimgproc=OFF \
      -DBUILD_opencv_cudawarping=OFF \
      -DBUILD_opencv_photo=OFF \
      -DBUILD_opencv_shape=OFF \
      -DBUILD_opencv_cudacodec=OFF \
      -DBUILD_opencv_ts=OFF \
      -DBUILD_opencv_features2d=OFF \
      -DBUILD_opencv_calib3d=OFF \
      -DBUILD_opencv_cudafeatures2d=OFF \
      -DBUILD_opencv_cudalegacy=OFF \
      -DBUILD_opencv_cudaobjdetect=OFF \
      -DBUILD_opencv_cudaoptflow=OFF \
      -DBUILD_opencv_cudastereo=OFF \
      -DBUILD_opencv_java=OFF \
      -DBUILD_opencv_stitching=OFF \
      -DBUILD_opencv_superres=OFF \
      -DBUILD_opencv_videostab=OFF ..

make -j4
