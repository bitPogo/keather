/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.presentation.ui.store.command

interface RefreshCommandsContract {
    sealed interface CommandReceiver

    sealed interface Command<T : CommandReceiver> {
        fun execute(receiver: T)
    }

    interface CommandExecutor {
        fun runCommand(command: Command<out CommandReceiver>)
    }

    fun interface RefreshAll : CommandReceiver {
        fun refreshAll()
    }

    fun interface Refresh : CommandReceiver {
        fun refresh()
    }
}

data object RefreshAllCommand : RefreshCommandsContract.Command<RefreshCommandsContract.RefreshAll> {
    override fun execute(receiver: RefreshCommandsContract.RefreshAll) = receiver.refreshAll()
}

data object RefreshCommand : RefreshCommandsContract.Command<RefreshCommandsContract.Refresh> {
    override fun execute(receiver: RefreshCommandsContract.Refresh) = receiver.refresh()
}
