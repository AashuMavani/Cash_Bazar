#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_reward_cashbazar_value_POC_1Constants_getAppURL(JNIEnv *env, jclass clazz) {
    // TODO: implement getAppURL()
    std::string URL = "https://mycashbazar.com/New/App/api100/";
    return env->NewStringUTF(URL.c_str());

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_reward_cashbazar_value_POC_1Constants_getMIV(JNIEnv *env, jclass clazz) {
    // TODO: implement getMIV()
    std::string miv = "rd65vb4er984v1r6";
    return env->NewStringUTF(miv.c_str());

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_reward_cashbazar_value_POC_1Constants_getMKEY(JNIEnv *env, jclass clazz) {
    // TODO: implement getMKEY()
    std::string mkey = "efb4fer65b188t65";
    return env->NewStringUTF(mkey.c_str());
}



extern "C"
JNIEXPORT jstring JNICALL
Java_com_reward_cashbazar_value_POC_1Constants_getUSERTOKEN(JNIEnv *env, jclass clazz) {
    // TODO: implement getUSERTOKEN()
    std::string token = "4b36d6a5-e3e9-658t-8584-d8af62d21c92";
    return env->NewStringUTF(token.c_str());
}