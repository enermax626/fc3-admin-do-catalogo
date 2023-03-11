package com.fullcycle.admin.catalogo.domain.exceptions;

import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;

public class NotificationException extends DomainException{

  public NotificationException(String message, Notification notification) {
    super(message, notification.getErrors());
  }
}
