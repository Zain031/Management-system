package com.abpgroup.managementsystem.service.impl;

import com.abpgroup.managementsystem.service.MidtransService;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class MidtransServiceImpl implements MidtransService {

    @Value("${midtrans.server-key}")
    private String serverKey;

    @Value("${midtrans.is-production}")
    private boolean isProduction;
    private static final OkHttpClient httpClient = new OkHttpClient();

    @Override

    public String createQrisTransaction(String orderId, long amount) {
        try {
            // Set global Midtrans configuration
            Midtrans.serverKey = serverKey;
            Midtrans.isProduction = isProduction;

            // Prepare transaction parameters
            Map<String, Object> chargeParams = new HashMap<>();
            Map<String, Object> transactionDetails = new HashMap<>();

            transactionDetails.put("order_id", "Order_" + orderId);
            transactionDetails.put("gross_amount", amount);

            chargeParams.put("transaction_details", transactionDetails);
            chargeParams.put("payment_type", "qris");

            // Call Snap API to create the transaction
            JSONObject response = SnapApi.createTransaction(chargeParams);
            return response.toString();

        } catch (MidtransError e) {
            throw new RuntimeException("Error while creating QRIS transaction: " + e.getMessage(), e);
        }
    }

    public String getTransactionStatus(String orderId) {
        System.out.println("Order ID: " + orderId);
        try {
            // Base64 encode the server key for authorization
            String encodedKey = Base64.getEncoder().encodeToString((serverKey + ":").getBytes());
            System.out.println("Encoded Key: " + encodedKey);

            // Set the environment-specific URL
            String baseUrl = "https://api.sandbox.midtrans.com/v2/";

            // Build the request URL
            String url = baseUrl + "Order_" + orderId + "/status";

            // Create request
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Basic " + encodedKey)
                    .build();

            // Execute request
            Response response = httpClient.newCall(request).execute();
            System.out.println("Response: " + response);

            if (response.isSuccessful() && response.body() != null) {
                // Parse the response body
                String responseBody = response.body().string();

                JSONObject jsonResponse = new JSONObject(responseBody);

                // Extract the 'transaction_status' from the JSON response
                String transactionStatus = jsonResponse.optString("transaction_status","pending");
                System.out.println("Transaction Status: " + transactionStatus);

                // Return the transaction status
                return transactionStatus;
            } else {
                throw new RuntimeException("Failed to fetch transaction status: " + response.message());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching transaction status: " + e.getMessage(), e);
        }
    }

}

