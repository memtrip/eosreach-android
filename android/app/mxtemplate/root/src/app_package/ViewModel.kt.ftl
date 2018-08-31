package ${packageName}

import android.app.Application
import com.memtrip.mxandroid.MxViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ${className}ViewModel @Inject internal constructor(
    application: Application
) : MxViewModel<${className}Intent, ${className}RenderAction, ${className}ViewState>(
    ${className}ViewState(view = ${className}ViewState.View.Idle),
    application
) {

    override fun dispatcher(intent: ${className}Intent): Observable<${className}RenderAction> = when (intent) {
        is ${className}Intent.Init -> Observable.just(${className}RenderAction.OnProgress)
    }

    override fun reducer(previousState: ${className}ViewState, renderAction: ${className}RenderAction): ${className}ViewState = when (renderAction) {
        ${className}RenderAction.OnProgress -> previousState.copy(view = ${className}ViewState.View.OnProgress)
        ${className}RenderAction.OnError -> previousState.copy(view = ${className}ViewState.View.OnError)
    }

    override fun filterIntents(intents: Observable<${className}Intent>): Observable<${className}Intent> = Observable.merge(
        intents.ofType(${className}Intent.Init::class.java).take(1),
        intents.filter {
            !${className}Intent.Init::class.java.isInstance(it)
        }
    )
}