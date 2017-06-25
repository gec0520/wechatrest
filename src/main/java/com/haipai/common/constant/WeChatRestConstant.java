package com.haipai.common.constant;

public enum WeChatRestConstant {
	QRCODE_SCAN_GROUP("QRCODE_SCAN_GROUP",1);
	
    private String name ;
    private int index ;
     
    private WeChatRestConstant( String name , int index ){
        this.name = name ;
        this.index = index ;
    }
     
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
