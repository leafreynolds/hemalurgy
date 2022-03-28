/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.compat.curios;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class CuriosCompat
{
    private static boolean curiosModDetected;


    public static boolean CuriosIsPresent()
    {
        return curiosModDetected;
    }

    public static void init()
    {
        curiosModDetected = ModList.get().isLoaded("curios");

        if (!curiosModDetected)
        {
            return;
        }

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(CuriosCompat::onEnqueueIMC);
    }

    private static void onEnqueueIMC(InterModEnqueueEvent event)
    {
        if (!curiosModDetected)
        {
            return;
        }

        for (SlotTypePreset slotType : SlotTypePreset.values())
        {
            int numberOfSlotsWanted = 0;
            switch (slotType)
            {
                case NECKLACE:
                    numberOfSlotsWanted = 1;
                    break;
                case HEAD:
                case BACK:
                case HANDS:
                case BRACELET:
                    numberOfSlotsWanted = 2;
                    break;
                case CURIO:
                    numberOfSlotsWanted = 4;
                    break;
            }

            if (numberOfSlotsWanted > 0)
            {
                int finalNumberOfSlotsWanted = numberOfSlotsWanted;
                InterModComms.sendTo(CuriosApi.MODID,
                        SlotTypeMessage.REGISTER_TYPE,
                        () -> slotType.getMessageBuilder().size(finalNumberOfSlotsWanted).build());
            }
        }

    }
}
