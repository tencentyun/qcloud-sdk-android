
# 指定需要CMAKE的最小版本
cmake_minimum_required( VERSION 3.4.1 )

## 指定头文件
#include_directories(src/main/cpp/include)

# 依赖其它第三方库,
add_library( tnetquic SHARED IMPORTED )
set_target_properties( tnetquic PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libtquic.so )

## 指定 so 库名称和C++源码文件位置
add_library( costquic
             SHARED

             src/main/cpp/main.cpp
             src/main/cpp/COSQuic.cpp
             )

# 依赖系统库
find_library( androidlog log)

target_link_libraries( costquic
                       ${androidlog}
                       tnetquic )