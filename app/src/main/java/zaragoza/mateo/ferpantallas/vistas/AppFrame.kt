package zaragoza.mateo.ferpantallas.vistas

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import zaragoza.mateo.ferpantallas.modelos.Destination
import zaragoza.mateo.ferpantallas.modelos.AppTheme
import zaragoza.mateo.ferpantallas.modelos.UserProfile
import zaragoza.mateo.ferpantallas.viewmodels.SettingsViewModel
import zaragoza.mateo.ferpantallas.viewmodels.AboutYouViewModel

@Composable
fun AppFrame(
    settingsViewModel: SettingsViewModel = viewModel(),
    aboutYouViewModel: AboutYouViewModel = viewModel(),
) {
    var destination by rememberSaveable(
        stateSaver = Saver(
            save = { it.name },
            restore = { Destination.valueOf(it) },
        ),
    ) {
        mutableStateOf(
            if (aboutYouViewModel.welcomeCompleted) Destination.FEED else Destination.BIENVENIDA,
        )
    }
    var postId by rememberSaveable { mutableIntStateOf(-1) }
    var eventId by rememberSaveable { mutableIntStateOf(-1) }
    var detailOrigin by rememberSaveable { mutableStateOf(Destination.FEED.name) }
    LaunchedEffect(aboutYouViewModel.welcomeCompleted) {
        if (!aboutYouViewModel.welcomeCompleted) {
            destination = Destination.BIENVENIDA
        } else if (destination == Destination.BIENVENIDA) {
            destination = Destination.FEED
        }
    }
    SystemBarIcons(destination = destination, theme = settingsViewModel.theme)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(insetsFor(destination)),
    ) {
        if (destination.showsHeader) {
            FerHeader()
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            when (destination) {
                Destination.BIENVENIDA -> Welcome(
                    onContinue = { profile, level ->
                        aboutYouViewModel.saveWelcome(profile, level)
                        destination = Destination.FEED
                    },
                )
                Destination.FEED -> MainFeed(
                    profile = UserProfile(
                        audience = aboutYouViewModel.profile.slug,
                        level = aboutYouViewModel.level.slug,
                    ),
                    onPostClick = { id ->
                        detailOrigin = Destination.FEED.name
                        postId = id
                        destination = Destination.POST_DETAIL
                    },
                    onEventClick = { id ->
                        detailOrigin = Destination.FEED.name
                        eventId = id
                        destination = Destination.EVENT_DETAIL
                    },
                )
                Destination.LIKED -> LikesScreen(
                    onPostClick = { id ->
                        detailOrigin = Destination.LIKED.name
                        postId = id
                        destination = Destination.POST_DETAIL
                    },
                    onEventClick = { id ->
                        detailOrigin = Destination.LIKED.name
                        eventId = id
                        destination = Destination.EVENT_DETAIL
                    },
                )
                Destination.POST_DETAIL -> PostDetailScreen(
                    postId = postId,
                    onBack = { destination = Destination.valueOf(detailOrigin) },
                )
                Destination.EVENT_DETAIL -> EventDetailScreen(
                    eventId = eventId,
                    onBack = { destination = Destination.valueOf(detailOrigin) },
                )
                Destination.PREGUNTAS -> Faq()
                Destination.AJUSTES -> Settings(onAboutYou = { destination = Destination.SOBRE_TI })
                Destination.SOBRE_TI -> AboutYou(onBack = { destination = Destination.AJUSTES })
            }
        }
        if (destination.showsBottomBar) {
            BottomBar(
                destination = destination,
                onFeed = { destination = Destination.FEED },
                onLiked = { destination = Destination.LIKED },
                onFaq = { destination = Destination.PREGUNTAS },
                onSettings = { destination = Destination.AJUSTES },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun SystemBarIcons(destination: Destination, theme: AppTheme) {
    val dark = when (theme) {
        AppTheme.CLARO -> false
        AppTheme.OSCURO -> true
        AppTheme.SISTEMA -> isSystemInDarkTheme()
    }
    val view = LocalView.current
    if (view.isInEditMode) {
        return
    }
    val activity = view.context.findActivity() ?: return
    SideEffect {
        val darkIcons = destination != Destination.BIENVENIDA && !dark
        val controller = WindowCompat.getInsetsController(activity.window, view)
        controller.isAppearanceLightStatusBars = darkIcons
        controller.isAppearanceLightNavigationBars = darkIcons
    }
}

@Composable
private fun insetsFor(destination: Destination): Modifier {
    if (destination == Destination.BIENVENIDA ||
        destination == Destination.POST_DETAIL ||
        destination == Destination.EVENT_DETAIL
    ) {
        return Modifier
    }
    return Modifier
        .windowInsetsPadding(WindowInsets.safeDrawing)
        .imePadding()
}

private fun Context.findActivity(): Activity? {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is Activity) {
            return current
        }
        current = current.baseContext
    }
    return null
}
