/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.compat.jei;

import leaf.hemalurgy.registry.ItemsRegistry;
import leaf.hemalurgy.utils.ResourceLocationHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@JeiPlugin
public class JEICompat implements IModPlugin
{

    public static final ResourceLocation JEI = ResourceLocationHelper.prefix("jei");

    @Override
    public ResourceLocation getPluginUid()
    {
        return JEI;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        //the ability to right click on your tile entity to see what other items interact with it.
        //eg furnace burns fuel, so shows fuel, that kinda thing.
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        //for tile entities eg furnace furnace
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        //for (RegistryObject<Item> itemRO : ItemsRegistry.ITEMS.get())
        //reg.addRecipes(TestRecipe.getAllRecipes(world), TestRecipeCategory.NAME);

        addItemInfoPage(registration, ItemsRegistry.GUIDE.get());

    }

    private void addItemInfoPage(IRecipeRegistration reg, Item item)
    {
        //reg.addIngredientInfo(
        //        new ItemStack(item),
        //        VanillaTypes.ITEM,
        //        String.format(Constants.StringKeys.HEMALURGY_ITEM_TOOLTIP, item.getRegistryName().getPath()));
    }

}
