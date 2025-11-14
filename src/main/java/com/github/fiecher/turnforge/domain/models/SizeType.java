package com.github.fiecher.turnforge.domain.models;

public enum SizeType {
    TINY, SMALL, MEDIUM, LARGE, HUGE, GARGANTUAN;

    @Override
    public String toString() {
        String s = this.name();
        return s.charAt(0) + s.substring(1).toLowerCase();
    }
}
