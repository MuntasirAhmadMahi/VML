package com.mam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class App {
    public static final BufferedImage PLACE_HOLDER_IMAGE;

    static {
        try {
            PLACE_HOLDER_IMAGE = ImageIO.read(Objects.requireNonNull(App.class.getResource("/place-holder.jpg")));
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load place-holder image.");
        }
    }

    public static Path getAppDataDirectory() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        Path path;
        if (os.contains("win")) {
            path = Paths.get(System.getenv("LOCALAPPDATA"), "vml");
        } else {
            path = Paths.get(
                    System.getProperty("user.home"),
                    ".local",
                    "share",
                    "vml"
            );
        }
        Files.createDirectories(path);

        return path;
    }

    public static Path getVehicleFolderFor(UUID vehicleId) throws IOException {
        return getAppDataDirectory().resolve(vehicleId.toString());
    }
}
