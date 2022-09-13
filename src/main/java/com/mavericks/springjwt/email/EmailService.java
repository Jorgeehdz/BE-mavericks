package com.mavericks.springjwt.email;

import com.mavericks.springjwt.email.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
}
