package br.com.craftlife.eureka.database.converters;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter(autoApply = true)
public class LocationConverter implements AttributeConverter<Location, String> {

    @Override
    public String convertToDatabaseColumn(Location l) {
        if (l == null) {
            return null;
        }
        return "@w;" + l.getWorld().getName() +
                ":@x;" + l.getBlockX() +
                ":@y;" + l.getBlockY() +
                ":@z;" + l.getBlockZ() +
                ":@p;" + l.getPitch() +
                ":@ya;" + l.getYaw();
    }

    @Override
    public Location convertToEntityAttribute(String serialized) {
        Location location = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

        Arrays.stream(serialized.split(":")).forEach(attribute -> {
            try {
                String[] split = attribute.split(";");
                switch (split[0]) {
                    case "@w":
                        location.setWorld(Bukkit.getWorld(split[1]));
                        break;
                    case "@x":
                        location.setX(Double.parseDouble(split[1]));
                        break;
                    case "@y":
                        location.setY(Double.parseDouble(split[1]));
                        break;
                    case "@z":
                        location.setZ(Double.parseDouble(split[1]));
                        break;
                    case "@p":
                        location.setPitch(Float.parseFloat(split[1]));
                        break;
                    case "@ya":
                        location.setYaw(Float.parseFloat(split[1]));
                        break;
                }
            } catch (Exception ignored) {
            }
        });
        return location;
    }
}
