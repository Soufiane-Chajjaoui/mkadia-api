package fr.mkadia.mkadiaapi.enums;

public enum InteractionTopic {
    REVIEW("review"),
    CART("cart"),
    FAVORITE("favorite"),
    ORDER("order");

    private final String value;

    InteractionTopic(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
