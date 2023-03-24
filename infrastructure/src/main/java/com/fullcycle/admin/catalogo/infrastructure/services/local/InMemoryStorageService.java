package com.fullcycle.admin.catalogo.infrastructure.services.local;

import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class InMemoryStorageService implements StorageService {

  private final Map<String,Resource> storage;

  public InMemoryStorageService() {
    this.storage = new ConcurrentHashMap<>();
  }

  public Map<String, Resource> storage() {
    return this.storage;
  }

  public void reset(){
    this.storage.clear();
  }

  @Override
  public void store(String name, Resource resource) {
    this.storage.put(name, resource);
  }

  @Override
  public void deleteAll(Collection<String> names) {
    names.forEach(storage::remove);
  }

  @Override
  public Optional<Resource> get(String name) {
    return Optional.ofNullable(this.storage.get(name));
  }

  @Override
  public List<String> list(String prefix) {
    if(prefix == null) {
      return Collections.emptyList();
    }
    return this.storage.keySet().stream()
        .filter(key -> key.startsWith(prefix))
        .toList();
  }
}
