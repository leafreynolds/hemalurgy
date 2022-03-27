/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.hemalurgy.utils;

import java.util.Collection;
import java.util.Map;

public class ListHelper
{
    public static boolean isNullOrEmpty( final Collection< ? > c ) {
        return c == null || c.isEmpty();
    }

    public static boolean isNullOrEmpty( final Map< ?, ? > m ) {
        return m == null || m.isEmpty();
    }
}
