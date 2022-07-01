/*
 * File created ~ 14 - 7 - 2021 ~ Leaf
 */

package leaf.hemalurgy.datagen.patchouli.categories;

import leaf.hemalurgy.datagen.patchouli.BookStuff;

import java.util.List;

public class PatchouliBasics
{
    public static void collect(List<BookStuff.Category> categories, List<BookStuff.Entry> entries)
    {
        BookStuff.Category basics = new BookStuff.Category(
                "basics",
                "An introduction to the mod, serving as a tutorial.",
                "hemalurgy:guide");

        basics.sortnum = 0;

        categories.add(basics);

        BookStuff.Entry welcomeEntry = new BookStuff.Entry("welcome", basics, basics.icon);
        welcomeEntry.pages = new BookStuff.Page[]
                {
                    new BookStuff.TextPage("Hey, thanks for checking out this mod! This current iteration of Hemalurgy is very basic, with a low barrier of entry on purpose. $(p)There's some exciting stuff coming in future versions though, so I hope you'll stick around!"),
                };
        welcomeEntry.priority = true;
        welcomeEntry.sortnum = -10;
        entries.add(welcomeEntry);

        BookStuff.Entry bookEntry = new BookStuff.Entry("guide", basics, basics.icon);
        bookEntry.sortnum = 2;
        bookEntry.pages = new BookStuff.Page[]
                {
                        new BookStuff.TextPage("Your guide to everything in the mod! There isn't much at the moment."),
                };
        entries.add(bookEntry);

    }
}
