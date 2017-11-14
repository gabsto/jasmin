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

import net.oneandone.sushi.fs.Node;
import net.oneandone.sushi.metadata.annotation.Type;

/** Basic building block of a module. Created from a Resource by the resolver. */
@Type
public class File {
    private final Node normal;
    private final Node minimized;

    private final MimeType type;

    /** null if the file is in any variant */
    private final String variant;

    public File(Node normal, Node minimized, MimeType type, String variant) {
        this.normal = normal;
        this.minimized = minimized;
        this.type = type;
        this.variant = variant;
    }

    public Node getNormal() {
        return normal;
    }

    public Node getMinimized() {
        return minimized;
    }

    public Node get(boolean min) {
        return min && minimized != null ? minimized : normal;
    }

    public MimeType getType() {
        return type;
    }

    public String getVariant() {
        return variant;
    }

    public String toString() {
        return normal.getName();
    }
}
