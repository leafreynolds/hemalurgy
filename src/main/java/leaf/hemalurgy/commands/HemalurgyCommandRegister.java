/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the New Tardis Mod team.
 * That mod taught me how to correctly add new commands, among other things!
 * https://tardis-mod.com/books/home/page/links#bkmrk-source
 */

package leaf.hemalurgy.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.commands.subcommands.HemalurgyCommand;
import leaf.hemalurgy.commands.subcommands.TestCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class HemalurgyCommandRegister
{

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal(Hemalurgy.MODID)
                .then(TestCommand.register(dispatcher))
                .then(HemalurgyCommand.register(dispatcher))
        );
    }
}
