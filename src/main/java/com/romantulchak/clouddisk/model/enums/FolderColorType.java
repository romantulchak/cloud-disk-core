package com.romantulchak.clouddisk.model.enums;

public enum FolderColorType {
    CHOCOLATE_ICE_CREAM("rgb(172, 114, 94);"),
    OLD_RED_BRICK("rgb(208, 107, 100);"),
    FIRE_ENGINE("rgb(248, 58, 34);"),
    WILD_STRAWBERRIES("rgb(250, 87, 60);"),
    ORANGE_JUICE("rgb(255, 117, 55);"),
    AUTUMN_LEAVES("rgb(255, 173, 70);"),
    SPEARMINT("rgb(66, 214, 146);"),
    SPRING_MEADOW("rgb(22, 167, 101);"),
    ASPARAGUS("rgb(123, 209, 72);"),
    SLIME_GREEN("rgb(179, 220, 108);"),
    DESERT_SAND("rgb(251, 233, 131);"),
    CUSTARD("rgb(250, 209, 101);"),
    SEA_FOAM("rgb(146, 225, 192);"),
    POOL("rgb(159, 225, 231);"),
    DENIM("rgb(159, 198, 231);"),
    RAINY_SKY("rgb(73, 134, 231);"),
    BLUE_VELVET("rgb(154, 156, 255);"),
    ORCHID("rgb(185, 154, 255);"),
    MOUSE("rgb(95, 99, 104);"),
    MOUNTAIN_GREY("rgb(202, 189, 191);"),
    EARTHWORM("rgb(204, 166, 172);"),
    BUBBLE_GUM("rgb(246, 145, 178);"),
    PURPLE_RAIN("rgb(205, 116, 230);"),
    BAKED_AUBERGINE("rgb(164, 122, 226);");


    private String color;

    FolderColorType(String value){
        this.color = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
