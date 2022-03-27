/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.datagen.recipe;

import leaf.hemalurgy.registry.ItemsRegistry;
import leaf.hemalurgy.utils.ResourceLocationHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider implements IConditionBuilder
{
    public RecipeGen(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
    {
        //example recipes obtained from following website on 3rd April '21. Thank you ChampionAsh5357!
        //https://github.com/ChampionAsh5357/1.16.x-Minecraft-Tutorial/blob/1.16.1-32.0.61-web/src/main/java/io/github/championash5357/tutorial/data/TutorialRecipeProvider.java

        //addBasicArmorRecipes(consumer, ItemsRegistry.RUBY.get(), ItemsRegistry.RUBY_HELMET.get(), ItemsRegistry.RUBY_CHESTPLATE.get(), ItemsRegistry.RUBY_LEGGINGS.get(), ItemsRegistry.RUBY_BOOTS.get());
        //ShapedRecipeBuilder.shapedRecipe(BlocksRegistry.WASHER.get()).key('I', Items.IRON_INGOT).key('W', Items.WATER_BUCKET).patternLine("III").patternLine("IWI").patternLine("III").addCriterion("in_water", enteredBlock(Blocks.WATER)).build(consumer);


        //addOreSmeltingRecipes(consumer, BlocksRegistry.GEM_BLOCK.get(), ItemsRegistry.GUIDE.get(), 1.0f, 200);




    }


    protected static void addBasicArmorRecipes(Consumer<FinishedRecipe> consumer, Tag<Item> inputMaterial, @Nullable Item head, @Nullable Item chest, @Nullable Item legs, @Nullable Item feet)
    {
        if (head != null)
        {
            ShapedRecipeBuilder.shaped(head).define('X', inputMaterial).pattern("XXX").pattern("X X").group("helmets").unlockedBy("has_material", has(inputMaterial)).save(consumer);
        }
        if (chest != null)
        {
            ShapedRecipeBuilder.shaped(chest).define('X', inputMaterial).pattern("X X").pattern("XXX").pattern("XXX").group("chestplates").unlockedBy("has_material", has(inputMaterial)).save(consumer);
        }
        if (legs != null)
        {
            ShapedRecipeBuilder.shaped(legs).define('X', inputMaterial).pattern("XXX").pattern("X X").pattern("X X").group("leggings").unlockedBy("has_material", has(inputMaterial)).save(consumer);
        }
        if (feet != null)
        {
            ShapedRecipeBuilder.shaped(feet).define('X', inputMaterial).pattern("X X").pattern("X X").group("boots").unlockedBy("has_material", has(inputMaterial)).save(consumer);
        }
    }

    private void decompressRecipe(Consumer<FinishedRecipe> consumer, ItemLike output, Tag<Item> input, String name)
    {
        ShapelessRecipeBuilder.shapeless(output, 9)
                .unlockedBy("has_item", has(output))
                .requires(input)
                .save(consumer, ResourceLocationHelper.prefix("conversions/" + name));
    }

    private ShapedRecipeBuilder compressRecipe(ItemLike output, Tag<Item> input)
    {
        return ShapedRecipeBuilder.shaped(output)
                .define('I', input)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .unlockedBy("has_item", has(input));
    }

    protected static void addOreSmeltingRecipes(Consumer<FinishedRecipe> consumer, ItemLike ore, Item result, float experience, int time)
    {
        String name = result.getRegistryName().getPath();
        String path = ore.asItem().getRegistryName().getPath();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ore), result, experience, time).unlockedBy("has_ore", has(ore)).save(consumer, ResourceLocationHelper.prefix(name + "_from_smelting_" + path));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ore), result, experience, time / 2).unlockedBy("has_ore", has(ore)).save(consumer, ResourceLocationHelper.prefix(name + "_from_blasting_" + path));
    }

    protected static void addCookingRecipes(Consumer<FinishedRecipe> consumer, ItemLike inputItem, Item result, float experience, int time)
    {
        String name = result.getRegistryName().getPath();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(inputItem), result, experience, time).unlockedBy("has_item", has(inputItem)).save(consumer, ResourceLocationHelper.prefix(name + "_from_smelting"));
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(inputItem), result, experience, time / 2, RecipeSerializer.SMOKING_RECIPE).unlockedBy("has_item", has(inputItem)).save(consumer, ResourceLocationHelper.prefix(name + "_from_smoking"));
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(inputItem), result, experience, time, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(inputItem)).save(consumer, ResourceLocationHelper.prefix(name + "_from_campfire"));
    }
}
