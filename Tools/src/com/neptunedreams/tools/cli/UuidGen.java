package com.neptunedreams.tools.cli;

import java.util.UUID;

/**
 * Add this to your ~/.bash_profile
 * alias uuid="java -cp ~/Tools/out/production/Tools com.neptunedreams.tools.cli.UuidGen"
 */
public final class UuidGen {
    private UuidGen() { }

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }
}
