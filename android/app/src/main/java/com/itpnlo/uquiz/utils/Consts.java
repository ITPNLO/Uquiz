package com.itpnlo.uquiz.utils;

public final class Consts {
    private Consts(){}
    public static final String DB_NAME = "androidVoca.db";
    public static final String VOCA1 = "voca1";
    public static final String VOCA2 = "voca2";
    public static final String VOCA3 = "voca3";
    public static final String VOCA4 = "voca4";
    /**
     * 三つの暗記状態を確認方法
     */
    public static final String CHECK_BY_OX = "checkByOX";
    public static final String CHECK_BY_SPEECH = "checkBySpeech";
    public static final String CHECK_BY_WRITE = "checkByWrite";
    /**
     * vocaList random
     */
    public static final String RANDOM = "random";
    /**
     * Vocaの4つの暗記状態
     */
    public static final int MEMORY_STATUS_NOT = 0;
    public static final int MEMORY_STATUS_KNOWN = 1;
    public static final int MEMORY_STATUS_UNKNOWN = 2;
    public static final int MEMORY_STATUS_ALL = 3;
    /**
     * Vocaの4つの暗記状態String
     */
    public static final String MEMORY_STATUS_NOT_STRING = "NOT";
    public static final String MEMORY_STATUS_KNOWN_STRING = "O";
    public static final String MEMORY_STATUS_UNKNOWN_STRING = "X";
    public static final String MEMORY_STATUS_ALL_STRING = "ALL";
    /**
     * Locale
     */
    public static final String LOCALE_LANGUAGE = "language";
    public static final String LOCALE_COUNTRY = "country";

    public static final String QUATATION = "'";
    public static final String QUATATION_REPLACE  = "|";

    public static final String TITLE = "title";
    public static final String LANGUAGE = "language";
    /**
     * Convert Mode
     */
    public static final String CONVERT_MODE_SAVE = "save";
    public static final String CONVERT_MODE_COPY = "copy";
}
