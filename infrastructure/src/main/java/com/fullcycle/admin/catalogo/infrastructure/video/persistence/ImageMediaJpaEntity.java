package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "video_image_media")
public class ImageMediaJpaEntity {

  @Id
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "file_path", nullable = false)
  private String filePath;

  public ImageMediaJpaEntity() {
  }

  private ImageMediaJpaEntity(String id, String name, String filePath) {
    this.id = id;
    this.name = name;
    this.filePath = filePath;
  }

  public static ImageMediaJpaEntity with(String id, String name, String filePath) {
    return new ImageMediaJpaEntity(id, name, filePath);
  }

  public static ImageMediaJpaEntity from(ImageMedia imageMedia) {
    return new ImageMediaJpaEntity(imageMedia.checksum(), imageMedia.name(), imageMedia.location());
  }

  public ImageMedia toAggregate() {
    return ImageMedia.with(id, id, name, filePath);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageMediaJpaEntity that = (ImageMediaJpaEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name)
        && Objects.equals(filePath, that.filePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, filePath);
  }
}
