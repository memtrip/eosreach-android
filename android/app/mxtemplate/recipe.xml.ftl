<?xml version="1.0"?>
<recipe>
    <instantiate from="res/layout/fragment.xml.ftl"
        to="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(layoutName)}.xml" />

    <instantiate from="src/app_package/Activity.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}Activity.kt" />
    <instantiate from="src/app_package/ActivityModule.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}ActivityModule.kt" />
    <instantiate from="src/app_package/Intent.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}Intent.kt" />
    <instantiate from="src/app_package/Renderer.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}Renderer.kt" />
    <instantiate from="src/app_package/ViewModel.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}ViewModel.kt" />
    <instantiate from="src/app_package/ViewState.kt.ftl"
        to="${escapeXmlAttribute(srcOut)}/${className}ViewState.kt" />

    <instantiate from="src/app_package/RendererTest.kt.ftl"
        to="${escapeXmlAttribute(unitTestOut)}/${className}RendererTest.kt" />
    <instantiate from="src/app_package/ViewModelTest.kt.ftl"
        to="${escapeXmlAttribute(unitTestOut)}/${className}ViewModelTest.kt" />
</recipe>
