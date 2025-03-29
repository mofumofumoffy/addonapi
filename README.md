# AddonAPI

🌐 Available languages: [日本語](README.jaJP.md)

**AddonAPI** is a required library mod that provides an API for dynamically loading objects in addon mods based on the mods loaded by Forge. If a target mod is not loaded, only the extension elements related to that mod are excluded, allowing Minecraft to launch safely.

## Supported Version
- Forge for Minecraft 1.20.1

## Main Features
- Dynamically loads registered modules according to the ModList.
- Adds a `compat` section to addon configs, which can be configured per target mod.
- Provides a recipe condition that determines whether to load a recipe based on the `compat` config.
- Includes a template Mixin plugin for detecting mod load status.

## Creating Modules
1. Create a module class by extending `AddonModule`.
2. Create a module provider by extending `AddonModuleProvider` and calling `addRawModules()` within the `registerRawModules()` method.
3. Call `AddonModuleRegistry.INSTANCE.LoadModule()` in the **constructor of your addon**.

## Using `compat` Settings
In data pack recipes, use the following format:

```json
{
  "type": "YOUR_RECIPE_TYPE",
  "conditions": [
      {
        "type": "addonlib:mods_available",
        "required_raw_module": "YOUR_RAW_ADDON_MODULE_NAME"
      }
    ],
    ...
}
```