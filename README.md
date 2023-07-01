# Sound Categories

Fabric library that allows mods to add more sound categories, and modifies the Minecraft sound settings menu to fit as
many categories as required.

## Adding to your project

The build artifact is hosted on [my personal Maven server](https://repo.stashy.dev). Add the following to your
repositories block:

```groovy
maven {
    name = "stashymane's repo"
    url = "https://repo.stashy.dev"
}
```

And the following to your dependencies:

```groovy
modImplementation("dev.stashy:sound-categories:${project.soundcategories_version}")
```

**Note**: Starting with v2 it is no longer recommended to include SoundCategories in your mod due to multi-version
support, and an increased file size.

## Adding a category

Classes that contain sound categories must implement `CategoryLoader` so that they are picked up by the loader. Every
SoundCategory you define must have the `Register` annotation - they will have the category reference injected once it is
created.

Example class:

```java
import dev.stashy.soundcategories.CategoryLoader;
import net.minecraft.sound.SoundCategory;

public class CustomCategories implements CategoryLoader {
    @Register(master = true)
    public static SoundCategory MOD_MAIN;
    @Register
    public static SoundCategory SUBCATEGORY;
    @Register(name = "your_custom_name")
    public static SoundCategory CUSTOM_NAME;
    @Register(defaultLevel = 0.0)
    public static SoundCategory OFF_BY_DEFAULT;
}
```

The register annotation has a few attributes:

* `master` will group all categories defined underneath it under one, accessible with a button next to the master
  category's slider.
* `name` allows you to set a custom name the category will be created under - useful if you want to have a
  simple `MASTER` variable set and not conflict with Minecraft's own master category.
* `defaultLevel`, quite obviously, is used as the default level the game creates your category with.

After implementing the loader, make sure you add the class as an entrypoint for `soundcategories` in
your `fabric.mod.json`. You can check the test mod in the main source folder for an example.

## Localization

The newly added category needs language definitions to show the name. You can do so in your `lang`
folder. For an example, you can check the
[ExtraSounds](https://github.com/stashymane/extra-sounds/) language files.
