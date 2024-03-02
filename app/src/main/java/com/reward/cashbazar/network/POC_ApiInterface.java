package com.reward.cashbazar.network;

import com.google.gson.JsonObject;
import com.reward.cashbazar.Async.Models.Api_Response;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface POC_ApiInterface {

    @FormUrlEncoded
    @POST("EROTRUCKDSF")
    Call<Api_Response> getHomeData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("DAILYMILK")
    Call<Api_Response> loginUser(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("RFGOKHDAS")
    Call<Api_Response> getUserProfile(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("TGHFKDVMB")
    Call<Api_Response> editUserProfile(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("GADITYRFM")
    Call<Api_Response> getNotificationData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("QEJKSFMXMCKSJDF")
    Call<Api_Response> getFAQ(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @Multipart
    @POST("DFDFDYHJLT")
    Call<Api_Response> submitFeedback(@Part("details") RequestBody details, @Part MultipartBody.Part Image);

    @FormUrlEncoded
    @POST("TGSPINRFDD")
    Call<Api_Response> getSpinData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("FDGDFGDFDFH")
    Call<Api_Response> getDiceData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("TGBNNNOOMM")
    Call<Api_Response> getCountData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("KERIYHNGJD")
    Call<Api_Response> getAlphabetData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("UHJKMGDF")
    Call<Api_Response> getCardsData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YHJMNBHKHJQQAA")
    Call<Api_Response> getColorData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("THGDASXCVBG")
    Call<Api_Response> saveSpinData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("THYUJNQWEDCDS")
    Call<Api_Response> saveDiceData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YHNFRQWEROOP")
    Call<Api_Response> saveCountData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("THNMDFLKJUOI")
    Call<Api_Response> saveAlphabetData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YHNQQQWEEDFDDSF")
    Call<Api_Response> saveCardsData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("GBVNGDFSDCV")
    Call<Api_Response> saveColorData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("DFFGJGHJKG")
    Call<Api_Response> saveMinesweeper(@Header("Token") String token, @Header("Secret") String random, @Field("details") String details);

    @FormUrlEncoded
    @POST("YHJLOPOOP")
    Call<Api_Response> getPointHistory(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("SAVEPURIBADATDA")
    Call<Api_Response> getInviteAsync(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("DSGDFGDFGDFH")
    Call<Api_Response> getTaskOfferList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);


    @FormUrlEncoded
    @POST("FGFNDHGH")
    Call<Api_Response> getTaskDetails(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @Multipart//VVV
    @POST("SDGDFDHUJ")
    Call<Api_Response> taskImageUpload(@Part("details") RequestBody details, @Part MultipartBody.Part Image);

    @FormUrlEncoded
    @POST("RVBNGHKKZX")
    Call<Api_Response> getWithdrawalType(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);
//    Call<Api_Response> getWithdrawalType();

    @FormUrlEncoded
    @POST("TGHNASEGBC")
    Call<Api_Response> getWithdrawTypeList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("TYERBHJBNVBJCFG")
    Call<Api_Response> redeemPoints(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("VBNBMADUU")
    Call<Api_Response> saveDailyBonus(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("UJDFMNDKSD")
    Call<Api_Response> saveTextTyping(@Header("Token") String token, @Header("Secret") String random, @Field("details") String details);

    @FormUrlEncoded
    @POST("FGDFGBDCFHDFH")
    Call<Api_Response> getTextTypingData(@Header("Token") String token, @Header("Secret") String random, @Field("details") String details);

    @FormUrlEncoded
    @POST("RTGBNMHJL")
    Call<Api_Response> getMinesweeper(@Header("Token") String token, @Header("Secret") String random, @Field("details") String details);

    @GET
    Call<JsonObject> callApi(@Url String Value,
                             @Query("pid") String pid,
                             @Query("offer_id") String o_id,
                             @Query("sub5") String installed_package_id,
                             @Query("sub3") String gaid,
                             @Query("sub2") String current_app_package_id,
                             @Query("sub1") String unique_click_id,//device_id
                             @Query("sub7") String ip_address);

    @FormUrlEncoded
    @POST("TGHNVBXZZZAS")
    Call<JsonObject> savePackageTracking(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("RGBNMMJKLIOOP")
    Call<Api_Response> getRewardScreenData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("THGBNMKLOPQA")
    Call<Api_Response> getWatchVideoList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("TREEDFVVVBHH")
    Call<Api_Response> saveWatchVideo(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("DFFJHJKJKTY")
    Call<Api_Response> getGiveAwayList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("GTYHJLMHDFFMG")
    Call<Api_Response> saveGiveAway(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YHJMKLOP")
    Call<Api_Response> getScratchcard(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("TYHHGKJHIOLK")
    Call<Api_Response> saveScratchcard(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YOPLKMJHGFG")
    Call<Api_Response> getLuckyNumber(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YJLOPMVAWREF")
    Call<Api_Response> saveLuckyNumberContest(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("DAVAPURI")
    Call<Api_Response> getLuckyNumberHistory(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("QWASQXZCQWA")
    Call<Api_Response> getImagePuzzleData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("SCAMEERS")
    Call<Api_Response> saveImagePuzzleData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("TGPOOPPLKOO")
    Call<Api_Response> getQuizData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("SACRFDSNH")
    Call<Api_Response> saveQuizData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("AKASHYKUMMAR")
    Call<Api_Response> getQuizHistory(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("RGBBVCFKH")
    Call<Api_Response> getDailyRewardList(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("GTBNMJKLOPPO")
    Call<Api_Response> saveDailyReward(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("TGOOUGGOSDD")
    Call<Api_Response> deleteAccount(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("GHFGJFGJGHKYH")
    Call<Api_Response> getAdjoeLeaderboardData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YHNVBXCVDFHH")
    Call<Api_Response> getAdjoeLeaderboardHistory(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("DYJHGNMKLO")
    Call<Api_Response> getWalletBalance(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YHNGKBMNMG")
    Call<Api_Response> saveQuickTask(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("DFGFMGHFEDH")
    Call<Api_Response> getMilestonesData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("YJKGMVAETYB")
    Call<Api_Response> saveMilestone(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("THNNDFMGHNBM")
    Call<Api_Response> getSingleMilestoneData(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("JSKSNJSFAJMASKS")
    Call<Api_Response> saveDailyTarget(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("IKLHJDPNDGQFDSJDFD")
    Call<Api_Response> shareTaskOffer(@Header("Token") String token, @Header("Secret") String secret, @Field("details") String details);

    @FormUrlEncoded
    @POST("GSDJDSJDGHDHCDJF")
    Call<Api_Response> getWordSorting(@Header("Token") String token, @Header("Secret") String random, @Field("details") String details);

    @FormUrlEncoded
    @POST("KKOKSHHHDTYGGSDHD")
    Call<Api_Response> saveWordSorting(@Header("Token") String token, @Header("Secret") String random, @Field("details") String details);

}
