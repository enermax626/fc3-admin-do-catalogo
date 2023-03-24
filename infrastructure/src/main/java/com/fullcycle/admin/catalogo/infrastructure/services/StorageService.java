package com.fullcycle.admin.catalogo.infrastructure.services;

import com.fullcycle.admin.catalogo.domain.resource.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StorageService {

  void store(String name, Resource resource);

  void deleteAll(Collection<String> names);

  Optional<Resource> get(String name);

  List<String> list(String prefix);

}
