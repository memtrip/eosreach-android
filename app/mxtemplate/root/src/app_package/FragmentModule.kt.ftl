package ${packageName}

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ${className}FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributes${className}Fragment(): ${className}Fragment
}