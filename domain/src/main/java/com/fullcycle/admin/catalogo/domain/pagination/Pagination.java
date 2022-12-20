package com.fullcycle.admin.catalogo.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        int currentPage,
        int perPage,
        long total,
        List<T> items
) {

    public <R> Pagination<R> map(Function<T,R> mapper){
        List<R> mappedItems = this.items.stream().map(mapper).toList();
        return new Pagination<R>(this.currentPage, this.perPage, this.total, mappedItems);
    }
}
