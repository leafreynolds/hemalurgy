/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.items;


import leaf.hemalurgy.properties.PropTypes;
import net.minecraft.world.item.Item;

public class BaseItem extends Item
{

    public BaseItem()
    {
        super(PropTypes.Items.SIXTY_FOUR.get().tab(HemalurgyItemGroups.ITEMS));
    }

    public BaseItem(Properties prop)
    {
        super(prop);
    }


}
