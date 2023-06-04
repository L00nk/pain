package org.example;

import jakarta.jws.WebService;

import java.util.Arrays;

@WebService(endpointInterface = "org.example.LaptopsInterface")
public class LaptopsBean implements LaptopsInterface{
    Object[][] laptops = {};


    @Override
    public Object[][] laptopsByProducent(String producent) {
        return Arrays.stream(laptops)
                .filter(laptop -> String.valueOf(laptop[0]).equals(producent))
                .toArray(Object[][]::new);
    }

    @Override
    public Object[][] laptopsByPowierzchnia(String powierzchnia) {
        return Arrays.stream(laptops)
                .filter(laptop -> String.valueOf(laptop[3]).equals(powierzchnia))
                .toArray(Object[][]::new);
    }

    @Override
    public Object[][] laptopsByProporcje(String proporcje) {
        String[] proportionParts = proporcje.split(":");
        double proportionWidth = Double.parseDouble(proportionParts[0]);
        double proportionHeight = Double.parseDouble(proportionParts[1]);
        double proportionValue = Math.round((proportionWidth / proportionHeight) * 100.0) / 100.0;

        return Arrays.stream(laptops)
                .filter(laptop -> !"brak danych".equals(laptop[2]))
                .filter(laptop -> {
                    String[] partslaptop = ((String) laptop[2]).split("x");
                    double widthlaptop = Double.parseDouble(partslaptop[0]);
                    double heightlaptop = Double.parseDouble(partslaptop[1]);
                    double laptopProportionValue = Math.round((widthlaptop / heightlaptop) * 100.0) / 100.0;

                    return proportionValue == laptopProportionValue;
                })
                .toArray(Object[][]::new);
    }
}
