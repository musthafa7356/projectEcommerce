package com.example.library.service;

import com.example.library.model.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails emailDetails);
}
