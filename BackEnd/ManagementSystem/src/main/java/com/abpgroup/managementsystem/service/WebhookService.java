package com.abpgroup.managementsystem.service;

import com.abpgroup.managementsystem.model.dto.request.MidtransWebhookRequestDTO;

public interface WebhookService {
    void processWebhook(MidtransWebhookRequestDTO webhookRequest);
}
