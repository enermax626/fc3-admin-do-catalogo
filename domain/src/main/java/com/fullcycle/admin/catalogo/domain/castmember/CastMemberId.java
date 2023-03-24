package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import java.util.Objects;

public class CastMemberId extends Identifier {

  private final String value;

  private CastMemberId(String value) {
    Objects.requireNonNull(value, "CastMemberId cannot be null");
    this.value = value;
  }

  public static CastMemberId unique(){
    return new CastMemberId(IdUtils.generate());
  }

  public static CastMemberId from(final String anId){
    return new CastMemberId(anId);
  }

  @Override
  public String getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CastMemberId that = (CastMemberId) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
