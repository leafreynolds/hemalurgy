/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.hemalurgy.datagen.advancements;

import leaf.hemalurgy.registry.ItemsRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.commands.CommandFunction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class MainAdvancements implements Consumer<Consumer<Advancement>>
{
    public MainAdvancements()
    {
    }

    public void accept(Consumer<Advancement> advancementConsumer)
    {
        final String tabName = "main";

        final String titleFormat = "advancements.hemalurgy.%s.title";
        final String descriptionFormat = "advancements.hemalurgy.%s.description";
        final String achievementPathFormat = "hemalurgy:%s/%s";

        Advancement root = Advancement.Builder.advancement()
                .display(ItemsRegistry.GUIDE.get(),
                        new TranslatableComponent(String.format(titleFormat, tabName)),
                        new TranslatableComponent(String.format(descriptionFormat, tabName)),
                        new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
                        FrameType.TASK,
                        false,//showToast
                        false,//announceChat
                        false)//hidden
                .addCriterion("tick", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                .save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));



        final String obtainedGuide = "obtained_guide";
        Advancement advancement2 = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ItemsRegistry.GUIDE.get(),
                        new TranslatableComponent(String.format(titleFormat, obtainedGuide)),
                        new TranslatableComponent(String.format(descriptionFormat, obtainedGuide)),
                        (ResourceLocation)null,
                        FrameType.TASK,
                        true, //showToast
                        true, //announce
                        false)//hidden
                .addCriterion(
                        "has_item",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemsRegistry.GUIDE.get()))
                .rewards(new AdvancementRewards(5, new ResourceLocation[0], new ResourceLocation[0], CommandFunction.CacheableFunction.NONE))
                .save(advancementConsumer, String.format(achievementPathFormat, tabName, obtainedGuide));


        final String blank = "blank";
        Advancement advancement4 = Advancement.Builder.advancement()
                .parent(advancement2)
                .display(
                        ItemsRegistry.GUIDE.get(),
                        new TranslatableComponent(String.format(titleFormat, blank)),
                        new TranslatableComponent(String.format(descriptionFormat, blank)),
                        (ResourceLocation)null,
                        FrameType.TASK,
                        true, //showToast
                        true, //announce
                        true)//hidden
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .rewards(new AdvancementRewards(5, new ResourceLocation[0], new ResourceLocation[0], CommandFunction.CacheableFunction.NONE))
                .save(advancementConsumer, String.format(achievementPathFormat, tabName, blank));


    }
}