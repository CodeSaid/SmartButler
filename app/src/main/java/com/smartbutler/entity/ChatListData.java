package com.smartbutler.entity;

/**
 * 对话列表实体
 */

public class ChatListData {
    // 文本
    private String text;

    private int type;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
