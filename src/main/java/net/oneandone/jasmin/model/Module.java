/*
 * Copyright 1&1 Internet AG, https://github.com/1and1/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.oneandone.jasmin.model;

import net.oneandone.sushi.metadata.annotation.Type;
import net.oneandone.sushi.util.Separator;

import java.util.ArrayList;
import java.util.List;

/** A loaded modul. Has a name, a list of files, and a list of dependencies to other modules. */
@Type
public class Module {
    public static final Separator SEP = Separator.on('+');
    public static final char NOT = '!';

    private final String name;

    private final List<File> files;

    private final List<Module> dependencies;

    private final Source source;

    public Module(String name, Source source) {
        if (name.contains(SEP.getSeparator())) {
            throw new IllegalArgumentException(name);
        }
        if (name.indexOf(NOT) != -1) {
            throw new IllegalArgumentException(name);
        }
        this.name = name;
        this.files = new ArrayList<>();
        this.dependencies = new ArrayList<>();
        this.source = source;
    }

    public Source getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public List<File> files() {
        return files;
    }

    public List<Module> dependencies() {
        return dependencies;
    }

    //--

    public List<File> resolve(Request request) {
        List<File> result;
        String actual;
        boolean variantSeen;

        result = new ArrayList<File>();
        variantSeen = false;
        for (File file : files) {
            if (file.getType().equals(request.type)) {
                actual = file.getVariant();
                if (actual == null) {
                    result.add(file);
                } else {
                    variantSeen = true;
                }
            }
        }
        if (variantSeen) {
            addVariant(request.type, getBest(request.type, request.variant), result);
        }
        return result;
    }

    private static final String DELIM = ":";

    private void addVariant(MimeType type, String variant, List<File> result) {
        for (File file : files) {
            if (file.getType().equals(type) && variant.equals(file.getVariant())) {
                result.add(file);
            }
        }
    }

    /** @return a variant */
    public String getBest(MimeType type, String variant) {
        String requested;
        String actual;
        String best;

        requested = variant + DELIM;
        best = null;
        for (File file : files) {
            if (file.getType().equals(type)) {
                actual = file.getVariant();
                if (requested.startsWith(actual + DELIM)) {
                    if (best == null) {
                        best = actual;
                    } else if (actual.length() > best.length()) {
                        best = actual;
                    }
                }
            }
        }
        if (best == null) {
            return "lead";
        }
        return best;
    }

    @Override
    public String toString() {
        return getName();
    }
}
