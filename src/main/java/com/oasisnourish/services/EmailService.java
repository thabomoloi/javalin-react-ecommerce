package com.oasisnourish.services;

import org.thymeleaf.context.IContext;

public interface EmailService {
  void sendEmail(String to, String subject, String templateName, IContext context);
}
