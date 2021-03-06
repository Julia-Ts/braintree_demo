package com.yalantis.api.services

import com.yalantis.data.model.ClientToken
import com.yalantis.data.model.Transaction
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by jtsym on 11/3/2017.
 */
interface BraintreeService {

    @GET("/client_token")
    fun getClientToken(): Single<ClientToken>

    @GET("/client_token")
    fun getClientToken(@Query("customer_id") customerId: String,
                       @Query("merchant_account_id") merchantAccountId: String): Single<ClientToken>

    @FormUrlEncoded
    @POST("/nonce/transaction")
    fun createTransaction(@Field("nonce") nonce: String): Single<Transaction>

    /** merchant account ID is a unique identifier for a specific merchant account in your gateway,
     * and is used to specify which merchant account to use when creating a transaction.
     * You don't have to specify it if you have only one merchant account id
     * https://articles.braintreepayments.com/control-panel/important-gateway-credentials
     **/
    @FormUrlEncoded
    @POST("/nonce/transaction")
    fun createTransaction(@Field("nonce") nonce: String,
                          @Field("merchant_account_id") merchantAccountId: String): Single<Transaction>

    @FormUrlEncoded
    @POST("/nonce/transaction")
    fun createTransaction(@Field("nonce") nonce: String,
                          @Field("merchant_account_id") merchantAccountId: String,
                          @Field("three_d_secure_required") requireThreeDSecure: Boolean): Single<Transaction>

}