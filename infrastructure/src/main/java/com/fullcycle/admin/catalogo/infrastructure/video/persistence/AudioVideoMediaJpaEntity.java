package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "video_video_media")
@Entity
public class AudioVideoMediaJpaEntity {

  @Id
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "file_path", nullable = false)
  private String filePath;

  @Column(name = "encoded_path", nullable = false)
  private String encodedPath;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private MediaStatus status;

  private AudioVideoMediaJpaEntity() {
  }

  private AudioVideoMediaJpaEntity(String id, String name, String filePath,
      String encodedPath, MediaStatus status) {
    this.id = id;
    this.name = name;
    this.filePath = filePath;
    this.encodedPath = encodedPath;
    this.status = status;
  }

  public static AudioVideoMediaJpaEntity from(AudioVideoMedia audioVideoMedia) {
    return new AudioVideoMediaJpaEntity(audioVideoMedia.id(), audioVideoMedia.name(),
        audioVideoMedia.rawLocation(), audioVideoMedia.encodedLocation(), audioVideoMedia.status());
  }

  public AudioVideoMedia toAggregate(){
    return AudioVideoMedia.with(id, name, filePath, encodedPath, status);
  }
}
