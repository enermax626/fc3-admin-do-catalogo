package com.fullcycle.admin.catalogo.application.video.media.get;

import com.fullcycle.admin.catalogo.domain.resource.Resource;

public record GetMediaOutput(String name, String contentType, byte[] content) {

  public static GetMediaOutput with(Resource aResource) {
    return new GetMediaOutput(aResource.getName(), aResource.getContentType(),
        aResource.getContent());
  }

}
