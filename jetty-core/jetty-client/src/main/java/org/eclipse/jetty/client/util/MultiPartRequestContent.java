//
// ========================================================================
// Copyright (c) 1995-2022 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.client.util;

import java.util.Random;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.MultiPart;

/**
 * <p>A {@link Request.Content} for form uploads with the {@code "multipart/form-data"}
 * content type.</p>
 * <p>Example usage:</p>
 * <pre>
 * MultiPartRequestContent multiPart = new MultiPartRequestContent();
 * multiPart.addPart(new MultiPart.ContentSourcePart("field", null, HttpFields.EMPTY, new StringRequestContent("foo")));
 * multiPart.addPart(new MultiPart.PathPart("icon", "img.png", HttpFields.EMPTY, Path.of("/tmp/img.png")));
 * multiPart.close();
 * ContentResponse response = client.newRequest("localhost", connector.getLocalPort())
 *         .method(HttpMethod.POST)
 *         .content(multiPart)
 *         .send();
 * </pre>
 * <p>The above example would be the equivalent of submitting this form:</p>
 * <pre>
 * &lt;form method="POST" enctype="multipart/form-data"  accept-charset="UTF-8"&gt;
 *     &lt;input type="text" name="field" value="foo" /&gt;
 *     &lt;input type="file" name="icon" /&gt;
 * &lt;/form&gt;
 * </pre>
 */
public class MultiPartRequestContent extends MultiPart.ContentSource implements Request.Content
{
    private static String makeBoundary()
    {
        Random random = new Random();
        StringBuilder builder = new StringBuilder("JettyHttpClientBoundary");
        int length = builder.length();
        while (builder.length() < length + 16)
        {
            long rnd = random.nextLong();
            builder.append(Long.toString(rnd < 0 ? -rnd : rnd, 36));
        }
        builder.setLength(length + 16);
        return builder.toString();
    }

    private final String contentType;

    public MultiPartRequestContent()
    {
        this(makeBoundary());
    }

    public MultiPartRequestContent(String boundary)
    {
        super(boundary);
        this.contentType = "multipart/form-data; boundary=\"%s\"".formatted(boundary);
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }
}
