package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;
import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberId> {

  private String name;
  private CastMemberType type;
  private Instant createdAt;
  private Instant updatedAt;

  private CastMember(CastMemberId identifier, String name, CastMemberType type, Instant createdAt,
      Instant updatedAt) {
    super(identifier);
    this.name = name;
    this.type = type;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;

    selfValidate();
  }

  public static CastMember newMember(String name, CastMemberType type){
    return new CastMember(CastMemberId.unique(), name, type, Instant.now(), Instant.now());
  }

  public static CastMember with(CastMemberId id, String name, CastMemberType type, Instant createdAt, Instant updatedAt){
    return new CastMember(id, name, type, createdAt, updatedAt);
  }

  public static CastMember with(CastMember castmember) {
    return new CastMember(castmember.getId(), castmember.getName(), castmember.getType(), castmember.getCreatedAt(), castmember.getUpdatedAt());
  }

  public CastMember update(String name, CastMemberType type){
    this.name = name;
    this.type = type;
    this.updatedAt = Instant.now();

    selfValidate();

    return this;
  }

  private void selfValidate() {
    Notification notification = Notification.create();
    this.validate(notification);

    if(notification.hasError()){
      throw new NotificationException("CastMember is not valid", notification);
    }
  }

  @Override
  public void validate(ValidationHandler validationHander) {
    new CastMemberValidator(validationHander, this).validate();

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CastMemberType getType() {
    return type;
  }

  public void setType(CastMemberType type) {
    this.type = type;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
