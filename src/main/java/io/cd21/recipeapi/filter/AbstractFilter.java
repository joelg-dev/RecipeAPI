package io.cd21.recipeapi.filter;

public abstract class AbstractFilter {
    FilterType filterType;

    protected AbstractFilter(FilterType filterType) {
        this.filterType = filterType;
    }

    public FilterType getFilterType() {
        return this.filterType;
    }
}
