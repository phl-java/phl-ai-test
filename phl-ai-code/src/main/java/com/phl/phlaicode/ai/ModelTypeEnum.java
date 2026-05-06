package com.phl.phlaicode.ai;

public enum ModelTypeEnum {
    QWEN("qwen", "通义千问"),
    DEEPSEEK("deepseek", "DeepSeek");

    private final String value;
    private final String text;

    ModelTypeEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() { return value; }
    public String getText() { return text; }

    public static ModelTypeEnum getEnumByValue(String value) {
        if (value == null) return QWEN;
        for (ModelTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return QWEN;
    }
}
