# AddonAPI

🌐 他の言語: [English](README.md)

アドオンmodにおいて、Forgeに読み込まれているmodに応じて、動的にオブジェクトをロードするためのAPIを提供する前提mod。対象のmodが読み込まれていない場合でも、そのmodの拡張要素のみを取り除き、安全にminecraftを起動させることができる。

## 対応バージョン
- forge mc1.20.1

## 主な機能
- 登録したモジュールをModListに応じて動的にロードする。
- アドオンのconfigに、対象mod毎に設定可能なcompat項目を追加する。
- configのcompat項目に応じて、レシピを読み込むかどうかを設定できるconditionを追加する。
- mod読み込み状況を判定するMixinプラグインの雛形を提供する。

## モジュールの作成
1. クラス```AddonModule```を継承し、モジュールとなるクラスを作成する。
1. クラス```AddonModuleProvider```を継承し、メソッド```registerRawModules()```内で```addRawModules()```を呼び出すことで、モジュール プロバイダを作成する。
1. メソッド```AddonModuleRegistry.INSTANCE.LoadModule()```を**アドオンのコンストラクタ**で呼び出す。

## compat設定の利用
データパック内のレシピで、以下のように記述する。

```
{
  "type": YOUR_RECIPE_TYPE,
  "conditions": [
      {
        "type": "addonlib:mods_available",
        "required_raw_module": "YOUR_RAW_ADDON_MODULE_NAME"
      }
    ],
    ...
}
```
