package net.grewind.phroute;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PullEmoji {
    private static final String[] EMOJIS = new String[]{"keycap-digit-one", "keycap-digit-two", "keycap-digit-three",
            "keycap-digit-four", "keycap-digit-five", "fire", "large-red-circle", "large-red-square", "red-triangle-pointed-up",
            "up-arrow", "down-arrow", "right-arrow", "left-arrow", "cross-mark"};

    public static void main(String[] args) throws IOException {
        Path basePath = Path.of("img/emoji");
        for (String emoji : EMOJIS) {
            List<EmojiListing> emojiListings = new ArrayList<>();
            URL url = new URL("https://www.emojipedia.org/" + emoji + "/");
            Document document = Jsoup.parse(url, 10000);
            Elements vendorLists = document.getElementsByClass("vendor-list");
            if (vendorLists.size() != 1) throw new IllegalStateException();
            Element vendorList = vendorLists.first();
            Elements allScripts = vendorList.select("li div.vendor-container.vendor-rollout-target div.vendor-image script");
            for (Element script : allScripts) {
                emojiListings.add(new Gson().fromJson(script.html(), EmojiListing.class));
            }
            for (EmojiListing emojiListing : emojiListings) {
                if (!emojiListing.creator().type().equals("Organization")) {
                    System.out.print("");
                }
                Path savePath = basePath.resolve(emojiListing.creator().name()).resolve(emoji + ".png");
                if (Files.exists(savePath)) {
                    continue;
                }
                Files.createDirectories(savePath.getParent());
                BufferedImage image = ImageIO.read(new URL(emojiListing.contentUrl()));
                ImageIO.write(image, "PNG", savePath.toFile());
            }
        }
    }
}
