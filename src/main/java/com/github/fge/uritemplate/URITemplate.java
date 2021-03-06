/*
 * Copyright (c) 2013, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.fge.uritemplate;

import com.github.fge.uritemplate.expression.URITemplateExpression;
import com.github.fge.uritemplate.parse.URITemplateParser;
import com.github.fge.uritemplate.vars.VariableMap;
import com.github.fge.uritemplate.vars.VariableMapBuilder;
import com.github.fge.uritemplate.vars.values.VariableValue;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Main URI template class
 *
 * @see URITemplateParser
 */
public final class URITemplate
{
    /**
     * Ordered list of parsed URI template expressions
     */
    private final List<URITemplateExpression> expressions;

    /**
     * Constructor
     *
     * @param input the input string
     * @throws URITemplateParseException parse error
     */
    public URITemplate(final String input)
        throws URITemplateParseException
    {
        expressions = URITemplateParser.parse(input);
    }

    /**
     * Expand this template given a list of variables
     *
     * <p>Note that this only returns a string. It is up to the caller to verify
     * afterwards that the resulting string is actually a valid URI. The RFC
     * makes no guarantee about that!</p>
     *
     * @param vars the variable map (names as keys, contents as values)
     * @return expanded string
     * @throws URITemplateException expansion error (f.e. modifier mismatch)
     *
     * @deprecated use {@link #toString(VariableMap)} instead. Will be removed
     * in version 0.6.
     */
    @Deprecated
    public String expand(final Map<String, VariableValue> vars)
        throws URITemplateException
    {
        final VariableMapBuilder builder = VariableMap.newBuilder();
        for (final Map.Entry<String, VariableValue> entry: vars.entrySet())
            builder.addValue(entry.getKey(), entry.getValue());
        return toString(builder.freeze());
    }

    /**
     * Expand this template to a string given a list of variables
     *
     * @param vars the variable map (names as keys, contents as values)
     * @return expanded string
     * @throws URITemplateException expansion error (f.e. modifier mismatch)
     */
    public String toString(final VariableMap vars)
        throws URITemplateException
    {
        final StringBuilder sb = new StringBuilder();

        for (final URITemplateExpression expression: expressions)
            sb.append(expression.expand(vars));

        return sb.toString();
    }

    /**
     * Expand this template to a URI given a set of variables
     *
     * @param vars the variables
     * @return a URI
     * @throws URITemplateException see {@link #toString(VariableMap)}
     * @throws URISyntaxException expanded string is not a valid URI
     */
    public URI toURI(final VariableMap vars)
        throws URITemplateException, URISyntaxException
    {
        return new URI(toString(vars));
    }

    /**
     * Expand this template to a URL given a set of variables
     *
     * @param vars the variables
     * @return a URL
     * @throws URITemplateException see {@link #toString(VariableMap)}
     * @throws MalformedURLException expanded string is not a valid URL
     */
    public URL toURL(final VariableMap vars)
        throws URITemplateException, MalformedURLException
    {
        return new URL(toString(vars));
    }
}
