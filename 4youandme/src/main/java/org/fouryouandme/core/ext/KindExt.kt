package org.fouryouandme.core.ext

import arrow.Kind
import arrow.fx.ForIO
import arrow.fx.fix
import arrow.fx.typeclasses.Disposable
import timber.log.Timber

fun <A> Kind<ForIO, A>.unsafeRunAsync(): Unit =
    fix().unsafeRunAsync { either ->
        either.mapLeft { Timber.tag("4_YOU_AND_ME_ERROR").e(it) }
    }

fun <A> Kind<ForIO, A>.unsafeRunAsyncCancelable(): Disposable =
    fix().unsafeRunAsyncCancellable { either ->
        either.mapLeft { Timber.tag("4_YOU_AND_ME_ERROR").e(it) }
    }