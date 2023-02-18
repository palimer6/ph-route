package net.grewind.phroute;

import com.google.gson.annotations.SerializedName;

public record EmojiListing(@SerializedName("@context") String context, @SerializedName("@type") String type,
                           String contentUrl, net.grewind.phroute.EmojiListing.EmojiCreator creator,
                           String copyrightNotice) {

    public record EmojiCreator(@SerializedName("@type") String type, String name) {
    }
}
