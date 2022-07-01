/*
 * File created ~ 1 - 7 - 2022 ~Leaf
 */

package leaf.hemalurgy.capability.entity;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.utils.MetalHelper;

import java.awt.*;

public interface IHasMetalType extends IHasColour
{
    Metal getMetalType();

    default Color getColour()
    {
        return MetalHelper.getColor(getMetalType());
    }

    default int getColourValue()
    {
        return getColour().getRGB();
    }
}
